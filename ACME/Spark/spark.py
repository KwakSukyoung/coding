  # solutions.py

import pyspark
from pyspark.sql import SparkSession
import numpy as np
import numpy.linalg as la
import matplotlib.pyplot as plt
from pyspark.ml import Pipeline
from pyspark.ml.feature import VectorAssembler, StringIndexer, OneHotEncoder
from pyspark.ml.tuning import ParamGridBuilder, TrainValidationSplit
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml.evaluation import MulticlassClassificationEvaluator as MCE



# --------------------- Resilient Distributed Datasets --------------------- #

### Problem 1
def word_count(filename='huck_finn.txt'):
    """
    A function that counts the number of occurrences unique occurrences of each
    word. Sorts the words by count in descending order.
    Parameters:
        filename (str): filename or path to a text file
    Returns:
        word_counts (list): list of (word, count) pairs for the 20 most used words
    """
    # initialize your SparkSession object
    spark = SparkSession\
            .builder\
            .appName("app_name")\
            .getOrCreate()

    #Load the file as a PySpark RDD
    huck_finn = spark.sparkContext.textFile(filename)
    # print(huck_finn.take(10))
    #flat the words and split them
    words = huck_finn.flatMap(lambda row: row.split())
    # print(words.take(10))
    #make each words be a form of (word, 1)
    words = words.map(lambda row: (row,1))
    # print(words.take(10))
    #now we add all them together
    words = words.reduceByKey(lambda x, y: x+y)
    # print(words.take(10))
    #sort them in descending order
    words = words.sortBy(lambda row: -1*row[1])
    #format it to return value
    word_counts = list(words.collect())[:20]
    # print(word_counts)
    #end SparkSession
    spark.stop()

    return word_counts




### Problem 2
def monte_carlo(n=10**5, parts=6):
    """
    Runs a Monte Carlo simulation to estimate the value of pi.
    Parameters:
        n (int): number of sample points per partition
        parts (int): number of partitions
    Returns:
        pi_est (float): estimated value of pi
    """
    # initialize your SparkSession object
    spark = SparkSession\
            .builder\
            .appName("app_name")\
            .getOrCreate()

    #get the random (n*parts,2) array range through [-1, 1]
    range = 2*np.random.random((n*parts,2))-1

    # create an RDD
    ints = spark.sparkContext.parallelize(range,parts)

    #filter the points that are in the unit 1: x^2 + y^2 = r^2
    ints = ints.filter(lambda ints: (ints[0]**2)+(ints[1]**2) <= 1)

    #get the percentage and multiply by 4
    monte_carlos = (4*ints.count())/(n*parts)

    spark.stop()

    return monte_carlos


# ------------------------------- DataFrames ------------------------------- #

### Problem 3
def titanic_df(filename='titanic.csv'):
    """
    Calculates some statistics from the titanic data.

    Returns: the number of women on-board, the number of men on-board,
             the survival rate of women,
             and the survival rate of men in that order.
    """
    # initialize your SparkSession object
    spark = SparkSession\
            .builder\
            .appName("app_name")\
            .getOrCreate()

    #setting the headers
    schema = ('survived INT, pclass INT, name STRING, sex STRING, '
              'age FLOAT, sibsp INT, parch INT, fare FLOAT')

    #load file into a PySpark DataFrame
    titanic = spark.read.csv(filename, schema=schema)

    #Q1.the number of women on-board
    answer1 = titanic.filter(titanic.sex == 'female').count()

    #Q2.the number of women on-board
    answer2 = titanic.filter(titanic.sex == 'male').count()

    #Q3. the survival rate of women
    # (survived women)/answer1
    #1 - survived
    female = titanic.filter(titanic.sex == 'female')
    survived_female = female.filter(female.survived == '1').count()
    answer3 = survived_female/answer1

    #Q4. the survival rate of men
    # (survived men)/answer2
    #1 - survived
    male = titanic.filter(titanic.sex == 'male')
    survived_male = male.filter(male.survived == '1').count()
    answer4 = survived_male/answer2

    spark.stop()

    return ((answer1, answer2, answer3, answer4))


### Problem 4
def crime_and_income(crimefile='london_crime_by_lsoa.csv',
                     incomefile='london_income_by_borough.csv', major_cat='Robbery'):
    """
    Explores crime by borough and income for the specified major_cat
    Parameters:
        crimefile (str): path to csv file containing crime dataset
        incomefile (str): path to csv file containing income dataset
        major_cat (str): major or general crime category to analyze
    returns:
        numpy array: borough names sorted by percent months with crime, descending
    """
    # initialize your SparkSession object
    spark = SparkSession\
            .builder\
            .appName("app_name")\
            .getOrCreate()

    #creating the new DataFrame
    crime = spark.read.csv(crimefile, header = True, inferSchema = True)
    income = spark.read.csv(incomefile, header = True, inferSchema = True)

    #filter by major-cat on the major category column
    crime = crime.filter(crime.major_category == major_cat)

    #group by the borough column and sum up the values column(# of mjajor_cat crimes for that borough)
    crime = crime.groupBy("borough")\
         .sum('value')

    #join the groupby dataframe
    combined = crime.join(income, on='borough')

    #OrderBy to order by major cat to take crimes
    combined = combined.orderBy('sum(value)', ascending = False)

    #Keep only three columns
    combined = combined.drop('mean-08-16')

    #change into an array
    combined_array = np.array(combined.collect())

    #get the x and y values
    x = [float(combined_array[i][1]) for i in range(len(combined_array))]
    y = [float(combined_array[i][2]) for i in range(len(combined_array))]

    #plotting
    plt.scatter(x,y)
    plt.xlabel("sum(value)")
    plt.ylabel("median-08-16")
    plt.title("the number of major_cat = crimes by the median income for each borough")
    plt.show()

    spark.stop()

    return combined_array



### Problem 5
def titanic_classifier(filename='titanic.csv'):
    """
    Implements a classifier model to predict who survived the Titanic.
    Parameters:
        filename (str): path to the dataset
    Returns:
        metrics (tuple): a tuple of metrics gauging the performance of the model
            ('accuracy', 'weightedRecall', 'weightedPrecision')
    """
    # initialize your SparkSession object
    spark = SparkSession\
            .builder\
            .appName("app_name")\
            .getOrCreate()

    #setting the headers
    schema = ('survived INT, pclass INT, name STRING, sex STRING, '
              'age FLOAT, sibsp INT, parch INT, fare FLOAT')

    #load file into a PySpark DataFrame
    titanic = spark.read.csv(filename, schema=schema)

    # prepare data
    # convert the 'sex' column to binary categorical variable
    from pyspark.ml.feature import StringIndexer, OneHotEncoder
    sex_binary = StringIndexer(inputCol='sex', outputCol='sex_binary')

    # one-hot-encode pclass (Spark automatically drops a column)
    onehot = OneHotEncoder(inputCols=['pclass'],
                            outputCols=['pclass_onehot'])

    # create single features column
    from pyspark.ml.feature import VectorAssembler
    features = ['sex_binary', 'pclass_onehot', 'age', 'sibsp', 'parch', 'fare']
    features_col = VectorAssembler(inputCols=features, outputCol='features')

    # now we create a transformation pipeline to apply the operations above
    # this is very similar to the pipeline ecosystem in sklearn
    from pyspark.ml import Pipeline
    pipeline = Pipeline(stages=[sex_binary, onehot, features_col])

    titanic = pipeline.fit(titanic).transform(titanic)

    # drop unnecessary columns for cleaner display (note the new columns)
    titanic = titanic.drop('pclass', 'name', 'sex')

    # split into train/test sets (75/25)
    train, test = titanic.randomSplit([0.75, 0.25], seed=11)

    # initialize logistic regression
    from pyspark.ml.regression import RandomForestRegressor
    rf = RandomForestClassifier(labelCol='survived', featuresCol='features')

    # run a train-validation-split to fit best elastic net param
    # ParamGridBuilder constructs a grid of parameters to search over.
    from pyspark.ml.tuning import ParamGridBuilder, TrainValidationSplit
    from pyspark.ml.evaluation import MulticlassClassificationEvaluator as MCE
    paramGrid = ParamGridBuilder()\
                     .addGrid(rf.maxBins, [5, 3, 12]).build()

    # TrainValidationSplit will try all combinations and determine best model using
    # the evaluator (see also CrossValidator)
    tvs = TrainValidationSplit(estimator=rf,
                                estimatorParamMaps=paramGrid,
                                evaluator=MCE(labelCol='survived'),
                                trainRatio=0.75,
    seed=11)

    # we train the classifier by fitting our tvs object to the training data
    clf = tvs.fit(train)
    # use the best fit model to evaluate the test data
    results = clf.bestModel.evaluate(test)
    # results.predictions.select(['survived', 'prediction']).show(5)
    # +--------+----------+
    # |survived|prediction|
    # +--------+----------+
    # | 0| 1.0|
    # | 0| 1.0|
    # | 0| 1.0|
    # | 0| 1.0|
    # |       0|       0.0|
    # +--------+----------+
    # performance information is stored in various attributes of "results"
    accuracy = results.accuracy
    # 0.7527272727272727
    weightedRecall = results.weightedRecall
    # 0.7527272727272727
    weightedPrecision = results.weightedPrecision
    # 0.751035147726004

    spark.stop()

    return ((accuracy, weightedRecall, weightedPrecision))

if __name__=="__main__":
    print("first question,",word_count())
    print("second question,",monte_carlo())
    print("third question,",titanic_df())
    print("fourth question,",crime_and_income())
    print("fifth question,",titanic_classifier())
