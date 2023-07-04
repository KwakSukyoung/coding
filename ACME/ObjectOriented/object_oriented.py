# object_oriented.py
"""Python Essentials: Object Oriented Programming.
<Name> Sukyoung Kwak
<Class> Section 2
<Date> September 24th, 2021
"""
import numpy as np
import math

class Backpack:
    """A Backpack object class. Has a name and a list of contents.

    Attributes:
        name (str): the name of the backpack's owner.
        color (str): the color of the backpack.
        max_size (int): the maximum number of items that can fit inside.
        contents (list): the contents of the backpack.
    """

    # Problem 1: Modify __init__() and put(), and write dump().
    def __init__(self, name, color, max_size = 5):
        """Set the name and initialize an empty list of contents.

        Parameters:
            name (str): the name of the backpack's owner.
            color (str): the color of the backpack.
            max_size (str): the maximum size of the backpack.
            contents (list): the contents of the backpack.
        """
        #attributes
        self.name = name#the name of the backpack's owner
        self.color = color#the color of the backpack
        self.max_size = max_size#the maximum size of the backpack
        self.contents = []#the contents of the backpack

    def put(self, item):
        """Add an item to the backpack's list of contents.

        Parameters:
            item (str): a content that will be added to the backpack.
            max_size (str): the maximum size of the backpack.
        """
        #if there are too many
        if (len(self.contents) >= self.max_size):
            print("No Room!")
        #Add an item till it reaches the limit
        else:
            self.contents.append(item)

    def dump(self):
        """Empty the backpack."""
        self.contents = []#remove all the contents

    def take(self, item):
        """Remove an item from the backpack's list of contents.\

        Parameters:
            item (str): a content that will be added to the backpack.
        """
        self.contents.remove(item)#remove items one by one

    # Magic Methods -----------------------------------------------------------

    # Problem 3: Write __eq__() and __str__().
    def __add__(self, other):
        """Add the number of contents of each Backpack."""
        return len(self.contents) + len(other.contents)#return the sum of the length

    def __lt__(self, other):
        """Compare two backpacks. If 'self' has fewer contents
        than 'other', return True. Otherwise, return False.
        """
        return len(self.contents) < len(other.contents)#compare the sizes of those two bags
    def __eq__(self, other):
        """Compare the name, color, and number of contents of each Backpack."""
        if (self.name == other.name):
            if (self.color == other.color):
                if (self.contents == other.contents):
                    return True#if the three conditions meet, it's true
                #otherwise, it's wrong
                else:
                    return False
            else:
                return False
        else:
            return False


    def __str__(self):
        """output the backpack contents info"""
        #putting all the info together
        string = "Owner: \t\t" + self.name +  "\n"+ "Color: \t\t"+  self.color+  "\n"+  "Size:  \t\t"+  str(len(self.contents))+  "\n"+ "Max Size: \t"+  str(self.max_size)+  "\n"+ "Contents: \t"+  str(self.contents)+  "\n"
        return string

# An example of inheritance. You are not required to modify this class.
class Knapsack(Backpack):
    """A Knapsack object class. Inherits from the Backpack class.
    A knapsack is smaller than a backpack and can be tied closed.

    Attributes:
        name (str): the name of the knapsack's owner.
        color (str): the color of the knapsack.
        max_size (int): the maximum number of items that can fit inside.
        contents (list): the contents of the backpack.
        closed (bool): whether or not the knapsack is tied shut.
    """
    def __init__(self, name, color):
        """Use the Backpack constructor to initialize the name, color,
        and max_size attributes. A knapsack only holds 3 item by default.

        Parameters:
            name (str): the name of the knapsack's owner.
            color (str): the color of the knapsack.
            max_size (int): the maximum number of items that can fit inside.
        """
        Backpack.__init__(self, name, color, max_size=3)
        self.closed = True

    def put(self, item):
        """If the knapsack is untied, use the Backpack.put() method."""
        if self.closed:
            print("I'm closed!")
        else:
            Backpack.put(self, item)

    def take(self, item):
        """If the knapsack is untied, use the Backpack.take() method."""
        if self.closed:
            print("I'm closed!")
        else:
            Backpack.take(self, item)

    def weight(self):
        """Calculate the weight of the knapsack by counting the length of the
        string representations of each item in the contents list.
        """
        return sum(len(str(item)) for item in self.contents)

# Problem 2: Write a 'Jetpack' class that inherits from the 'Backpack' class.
class Jetpack(Backpack):
    """A Jetpack object class. Inherits from the Backpack class.
    A Jetpack is smaller that contains fuel.

    Attributes:
        name (str): the name of the knapsack's owner.
        color (str): the color of the knapsack.
        max_size (int): the maximum number of items that can fit inside.
        contents (list): the contents of the backpack.
        fuel (int): the amount of fuel.
    """
    def __init__(self, name, color, max_size=2, fuel=10):
        """Use the Backpack constructor to initialize the name, color,
        and max_size attributes. A Jetpack only holds 3 item by default.

        Parameters:
            name (str): the name of the knapsack's owner.
            color (str): the color of the knapsack.
            max_size (int): the maximum number of items that can fit inside.
        """
        #Recall the backpack class
        Backpack.__init__(self, name, color, max_size=max_size)
        #Setting the fuel as an attribute
        self.fuel = fuel

    def fly(self,fuel):
        """counting how much fuel is used.
        If the Jetpack used under the amount of remained fuel.
        Then reduce the amount of fuel.
        """
        #if we are trying to use too much oil than what we have
        if (fuel > self.fuel):
            print("Not enough fuel!")
        #reduce the amount of the fuel as much as we have used
        else:
            self.fuel -= fuel

    def dump(self):
        """Created based on Backpack.dump().
        Used the Backpack.dump() method.
        """
        #dump the back contents
        Backpack.dump(self)
        #fuel is empty
        self.fuel =0


# Problem 4: Write a 'ComplexNumber' class.
class ComplexNumber:
    """Complex numbers are denoted a+bi where a,b ∈ R and i=√−1.

    Attributes:
        real (int): the number on the integer position.
        imag (int): the number on the imagery position.
    """

    # Problem 1: Modify __init__() and put(), and write dump().
    def __init__(self, real, imag):
        """Set the name and initialize an empty list of contents.

        Parameters:
            real (int): the number on the integer position.
            imag (int): the number on the imagery position.
        """
        #Setting the attributes
        self.real = real
        self.imag = imag

    def conjugate(self):
        """The complex conjugate of a+bi is defined as a+bi=a−bi.

        Returns:
            returns the object’s complex conjugate as a new ComplexNumberobject.
        """
        #make the imaginary number negative
        return ComplexNumber(self.real, (-1)*self.imag)

    # Magic Methods -----------------------------------------------------------
    def __str__(self):
        """output the backpack contents info"""
        #express the numbers into a string
        if (self.imag >= 0):
            return ("("+str(self.real)+ "+"+str(self.imag)+"j)")
        else:
            return ("("+str(self.real) +str(self.imag)+"j)")

    def __abs__(self):
        """
        determines the output of the built-in abs()function (absolute value).
        returns the magnitude of the complex number
        """
        #sqrt the squared of both words
        return math.sqrt((self.real**2)+(self.imag**2))

    def __eq__(self, other):
        """Two ComplexNumber objects are equal
        if and only if
        they have the same real and imaginary parts.
        """
        if (self.real == other.real):
            if (self.imag == other.imag):
                #True only when the numbers are the same
                return True
            else: return False
        else: return False

    def __add__(self, other):
        """Add two ComplextNumber Objects"""
        #add the complex number
        return ComplexNumber((self.real+other.real),(self.imag+other.imag))

    def __sub__(self, other):
        """Subtract two ComplextNumber Objects"""
        #subtract the complex number
        return ComplexNumber((self.real-other.real),(self.imag-other.imag))

    def __mul__(self, other):
        """Multiply two ComplextNumber Objects"""
        #multipy the complex number
        return ComplexNumber(((self.real*other.real) - (self.imag*other.imag)),((self.real*other.imag)+(other.real*self.imag)))

    def __truediv__(self, other):
        """Divide two ComplextNumber Objects"""
        #get the coefficients separately
        denominator = (other.real**2) + (other.imag**2)
        real_nominator = (self.real*other.real)+(self.imag*other.imag)
        imag_nominator = (self.imag*other.real)-(self.real*other.imag)
        #put all the info together
        return ComplexNumber(real_nominator/denominator,imag_nominator/denominator)
