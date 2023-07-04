"""Volume 3: Web Scraping.
<Name>
<Class>
<Date>
"""
import requests
from bs4 import BeautifulSoup
import re
import numpy as np
import matplotlib.pyplot as plt


# Problem 1
def prob1():
    """Use the requests library to get the HTML source for the website
    http://www.example.com.
    Save the source as a file called example.html.
    If the file already exists, do not scrape the website or overwrite the file.
    """
    #get HTML source for the webiste
    response = requests.get("http://www.example.com")

    #If the file already exists, make surer not to scrape the website or overwrite the file
    data = open('example.html', 'w')
    data.write(response.text)
    data.close()

# Problem 2
def prob2(code):
    """Return a list of the names of the tags in the given HTML code.
    Parameters:
        code (str): A string of html code
    Returns:
        (list): Names of all tags in the given code"""
    #Trying to use Beaufiul soup for our happiness
    small_soup = BeautifulSoup(code, 'html.parser')

    #list of all the tags
    all_tags = small_soup.find_all(True)

    #getting all the names
    names = [all_tags[i].name for i in range(len(all_tags))]

    return names

# Problem 3
def prob3(filename="example.html"):
    """Read the specified file and load it into BeautifulSoup. Return the
    text of the first <a> tag and whether or not it has an href
    attribute.
    Parameters:
        filename (str): Filename to open
    Returns:
        (str): text of first <a> tag
        (bool): whether or not the tag has an 'href' attribute
    """
    #loading the file
    file = open(filename, 'r')
    data = file.read()
    file.close()

    #loades it into BeautifulSoup
    small_soup = BeautifulSoup(data, 'html.parser')

    #Find the first <a> tag
    a_tag = small_soup.a

    return a_tag.text, hasattr(a_tag, 'href')

# if __name__=="__main__":
#     print(prob3())


# Problem 4
def prob4(filename="san_diego_weather.html"):
    """Read the specified file and load it into BeautifulSoup. Return a list
    of the following tags:

    1. The tag containing the date 'Thursday, January 1, 2015'.
    2. The tags which contain the links 'Previous Day' and 'Next Day'.
    3. The tag which contains the number associated with the Actual Max
        Temperature.

    Returns:
        (list) A list of bs4.element.Tag objects (NOT text).
    """
    #loading the file
    file = open(filename, 'r')
    data = file.read()
    file.close()

    #loades it into BeautifulSoup
    small_soup = BeautifulSoup(data, 'html.parser')

    #Teh tag containing the date "Thursda, january 1, 2015"
    answer1 = small_soup.find(string = 'Thursday, January 1, 2015').parent

    #The tags which contain the links "Prvious Day" and "Next Day"
    answer2_1 = small_soup.find(string = re.compile('Previous Day')).parent
    answer2_2 = small_soup.find(string = re.compile('Next Day')).parent

    #The tag which contains the number associated with the Acutal Max Temperature
    answer3 = small_soup.find(string = 'Max Temperature').parent.parent
    answer3 = answer3.next_sibling.next_sibling
    answer3 = answer3.span.span

    answer = [answer1, answer2_1,answer2_2, answer3]

    return answer
# if __name__=="__main__":
#     print(prob4())


# Problem 5
def prob5(filename="large_banks_index.html"):
    """Read the specified file and load it into BeautifulSoup. Return a list
    of the tags containing the links to bank data from September 30, 2003 to
    December 31, 2014, where the dates are in reverse chronological order.
    Returns:
        (list): A list of bs4.element.Tag objects (NOT text).
    """
    #loading the file
    file = open(filename, 'r')
    data = file.read()
    file.close()

    #loades it into BeautifulSoup
    small_soup = BeautifulSoup(data, 'html.parser')

    #reverse chronological order

    #find the list of all related dates
    all_dates = small_soup.find_all(string = re.compile("20(0[3-9]|1[0-4])"))
    #find the tags that are related those dates
    tags = [date.parent for date in all_dates]

    return tags[:-2]
# if __name__=="__main__":
#     print(prob5())

# Problem 6
def prob6(filename="large_banks_data.html"):
    """Read the specified file and load it into BeautifulSoup. Create a single
    figure with two subplots:

    1. A sorted bar chart of the seven banks with the most domestic branches.
    2. A sorted bar chart of the seven banks with the most foreign branches.

    In the case of a tie, sort the banks alphabetically by name.
    """
    #loading the file
    file = open(filename, 'r')
    data = file.read()
    file.close()

    #loades it into BeautifulSoup
    small_soup = BeautifulSoup(data, 'html.parser')

    #find the tags that has "tr"
    tags = small_soup.find_all("tr")[5:-10]

    #get the names
    names = np.array([tag.contents[1].string.split("/")[0] for tag in tags])

    #get the domestic and for arrays
    domest = []
    fore = []

    for tag in tags:
        do = tag.contents[19].string
        fo = tag.contents[21].string

        if "," in do:
            do = do.replace(",", '')
        if "." in do:
            do = do.replace(".", "0")
        domest.append(int(do))

        if "," in fo:
            fo = fo.replace(",", '')
        if "." in fo:
            fo = fo.replace(".", "0")
        fore.append(int(fo))

    #getting the arrays
    domest = np.array(domest,dtype = int)
    fore = np.array(fore, dtype = int)

    #getting the masks
    mask = np.flip(np.argsort(domest))
    mask2 = np.flip(np.argsort(fore))

    #plotting
    plt.subplot(1,2,1)
    plt.barh(names[mask][:7], domest[mask][:7])
    plt.title("A sorted bar chart of the seven banks with the most domestic branches.",fontsize = 5)
    plt.subplot(1,2,2)
    plt.barh(names[mask2][:7], fore[mask2][:7])
    plt.title("A sorted bar chart of the seven banks with the most foreign branches.",fontsize = 5)

    plt.tight_layout()
    plt.show()

# if __name__=="__main__":
#     print(prob6())
