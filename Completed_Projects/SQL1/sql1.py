# sql1.py
"""Volume 1: SQL 1 (Introduction).
<Name>
<Class>
<Date>
"""
import sqlite3 as sql
import csv
import numpy as np
import matplotlib.pyplot as plt

# Problems 1, 2, and 4
def student_db(db_file="students.db", student_info="student_info.csv",
                                      student_grades="student_grades.csv"):
    """Connect to the database db_file (or create it if it doesn’t exist).
    Drop the tables MajorInfo, CourseInfo, StudentInfo, and StudentGrades from
    the database (if they exist). Recreate the following (empty) tables in the
    database with the specified columns.

        - MajorInfo: MajorID (integers) and MajorName (strings).
        - CourseInfo: CourseID (integers) and CourseName (strings).
        - StudentInfo: StudentID (integers), StudentName (strings), and
            MajorID (integers).
        - StudentGrades: StudentID (integers), CourseID (integers), and
            Grade (strings).

    Next, populate the new tables with the following data and the data in
    the specified 'student_info' 'student_grades' files.

                MajorInfo                         CourseInfo
            MajorID | MajorName               CourseID | CourseName
            -------------------               ---------------------
                1   | Math                        1    | Calculus
                2   | Science                     2    | English
                3   | Writing                     3    | Pottery
                4   | Art                         4    | History

    Finally, in the StudentInfo table, replace values of −1 in the MajorID
    column with NULL values.

    Parameters:
        db_file (str): The name of the database file.
        student_info (str): The name of a csv file containing data for the
            StudentInfo table.
        student_grades (str): The name of a csv file containing data for the
            StudentGrades table.
    """
    try:
        #connect to the database
        with sql.connect(db_file) as conn:
            cur = conn.cursor()
            #prob1 -------------------------------------------------------------
            #drop the talbes if they exist
            cur.execute("DROP TABLE IF EXISTS MajorInfo")
            cur.execute("DROP TABLE IF EXISTS CourseInfo")
            cur.execute("DROP TABLE IF EXISTS StudentInfo")
            cur.execute("DROP TABLE IF EXISTS StudentGrades")
            #add to the database
            cur.execute("CREATE TABLE MajorInfo (MajorID INTEGER, MajorName TEXT)")
            cur.execute("CREATE TABLE CourseInfo (CourseID INTEGER, CourseName TEXT)")
            cur.execute("CREATE TABLE StudentInfo (StudentID INTEGER, StudentName TEXT, MajorID INTEGER)")
            cur.execute("CREATE TABLE StudentGrades (StudentID INTEGER, CourseID INTEGER, Grade TEXT)")

            # cur.execute("SELECT * FROM StudentInfo;")
            # print([d[0] for d in cur.description])

            #prob2--------------------------------------------------------------
            #Adding to MajorInfo
            rows = [(1, 'Math'), (2, 'Science'),(3, 'Writing'),(4, 'Art')]
            cur.executemany("INSERT INTO MajorInfo Values(?, ?);", rows)
            #Adding to CourseInfo
            rows = [(1, 'Calculus'), (2, 'English'),(3, 'Pottery'),(4, 'History')]
            cur.executemany("INSERT INTO CourseInfo Values(?, ?);", rows)
            #Adding to StudentInfo after getting the info
            with open("student_info.csv", 'r') as infile:
                rows = list(csv.reader(infile))
            cur.executemany("INSERT INTO StudentInfo Values(?, ?, ?);", rows)
            #Adding to StudentGrades after getting the info
            with open("student_grades.csv", 'r') as infile:
                rows = list(csv.reader(infile))
            cur.executemany("INSERT INTO StudentGrades Values(?, ?, ?);", rows)

            # for row in cur.execute("SELECT * FROM StudentGrades;"):
            #     print(row)

            #prob4--------------------------------------------------------------
            #modify StudentInfo table, values of −1 in the MajorID column to NULL values.
            cur.execute("UPDATE StudentInfo SET MajorID=NULL WHERE MajorID==-1;")
            # for row in cur.execute("SELECT * FROM StudentInfo;"):
            #     print(row)

    finally:
        conn.close()

# if __name__ =="__main__":
#     student_db()


# Problems 3 and 4
def earthquakes_db(db_file="earthquakes.db", data_file="us_earthquakes.csv"):
    """Connect to the database db_file (or create it if it doesn’t exist).
    Drop the USEarthquakes table if it already exists, then create a new
    USEarthquakes table with schema
    (Year, Month, Day, Hour, Minute, Second, Latitude, Longitude, Magnitude).
    Populate the table with the data from 'data_file'.

    For the Minute, Hour, Second, and Day columns in the USEarthquakes table,
    change all zero values to NULL. These are values where the data originally
    was not provided.

    Parameters:
        db_file (str): The name of the database file.
        data_file (str): The name of a csv file containing data for the
            USEarthquakes table.
    """
    try:
        #connect to the database
        with sql.connect(db_file) as conn:
            cur = conn.cursor()
            #prob3 -------------------------------------------------------------
            #drop the talbe if they exist
            cur.execute("DROP TABLE IF EXISTS USEarthquakes")
            #Adding Schema
            cur.execute("CREATE TABLE USEarthquakes (Year INTEGER, Month INTEGER, Day INTEGER, Hour INTEGER, Minute INTEGER, Second INTEGER, Latitude REAL, Longitude REAL, Magnitude REAL)")
            #populating Schema after getting the info
            with open("us_earthquakes.csv", 'r') as infile:
                rows = list(csv.reader(infile))
            cur.executemany("INSERT INTO USEarthquakes Values(?, ?, ?, ?, ?, ?, ?, ?, ?);", rows)

            # for row in cur.execute("SELECT * FROM USEarthquakes;"):
            #     print(row)

            #prob4--------------------------------------------------------------
            #Remove rows from USEarthquakes that hxwxave a value of 0 for the Magnitude.
            cur.execute("DELETE FROM USEarthquakes WHERE Magnitude==0;")
            #Replace 0 values in the Day, Hour, Minute, and Second columns with NULL values.
            cur.execute("UPDATE USEarthquakes SET Day=NULL WHERE Day==0;")
            cur.execute("UPDATE USEarthquakes SET Hour=NULL WHERE Hour==0;")
            cur.execute("UPDATE USEarthquakes SET Minute=NULL WHERE Minute==0;")
            cur.execute("UPDATE USEarthquakes SET Day=NULL WHERE Day==0;")
            cur.execute("UPDATE USEarthquakes SET Second=NULL WHERE Second==0;")

            # for row in cur.execute("SELECT * FROM USEarthquakes;"):
            #     print(row)
    finally:
        conn.close()

# if __name__ =="__main__":
#     earthquakes_db()



# Problem 5
def prob5(db_file="students.db"):
    """Query the database for all tuples of the form (StudentName, CourseName)
    where that student has an 'A' or 'A+'' grade in that course. Return the
    list of tuples.

    Parameters:
        db_file (str): the name of the database to connect to.

    Returns:
        (list): the complete result set for the query.
    """
    #change the data
    student_db()
    try:
        #connect to the database
        with sql.connect(db_file) as conn:
            cur = conn.cursor()
            #query the database for all tuples of the form (StudentName, CourseName) where that student has an “A” or “A+” grade in that course.
            val = cur.execute("SELECT SI.StudentName, CN.CourseName "
                                "FROM StudentInfo AS SI, CourseInfo AS CN, StudentGrades AS SG "
                                "WHERE SI.StudentID == SG.StudentID AND (SG.Grade == 'A' OR SG.Grade == 'A+') AND SG.CourseID == CN.CourseID; ").fetchall()

    finally:
        conn.close()

    return val

# if __name__ =="__main__":
#     print(prob5())

# Problem 6
def prob6(db_file="earthquakes.db"):
    """Create a single figure with two subplots: a histogram of the magnitudes
    of the earthquakes from 1800-1900, and a histogram of the magnitudes of the
    earthquakes from 1900-2000. Also calculate and return the average magnitude
    of all of the earthquakes in the database.

    Parameters:
        db_file (str): the name of the database to connect to.

    Returns:
        (float): The average magnitude of all earthquakes in the database.
    """
    earthquakes_db()
    try:
        #connect to the database
        with sql.connect(db_file) as conn:
            cur = conn.cursor()
            #get the info of 19th centry
            mag_19 = conn.execute("SELECT Magnitude, Year "
                                  "From USEarthquakes "
                                  "WHERE Year < 1900 AND Year > 1799; ").fetchall()

            hist_19_values = list([row[0] for row in mag_19])

            #get the info of 20th centry
            mag_20 = conn.execute("SELECT Magnitude, Year "
                                    "From USEarthquakes "
                                    "WHERE Year < 2000 AND Year > 1899; ").fetchall()

            hist_20_values = list([row[0] for row in mag_20])

            print(len(hist_19_values))
            print(len(hist_20_values))

            #get the info of avg
            mag_avg = conn.execute("SELECT AVG(Magnitude) FROM USEarthquakes")

            #plottin
            fig, (ax1, ax2) = plt.subplots(1, 2)

            #The relation between population and gdp
            ax1.hist(hist_19_values)
            ax1.set_title("The relation between population and gdp",size = 8)
            ax1.set_xlabel("population")
            ax1.set_ylabel("gdp")

            #The relation between female and male
            ax2.hist(hist_20_values)
            ax2.set_title("The relation between female and male",size = 8)
            ax2.set_xlabel("female")
            ax2.set_ylabel("male")
            #plt.axis("equal")

            plt.tight_layout()
            plt.show()

            return np.ravel(mag_avg.fetchall())[0]
    finally:
        conn.close()

# if __name__=="__main__":
#     prob6()
