# python_intro.py
"""Python Essentials: Introduction to Python.
<Name> Sukyoung Kwak
<Class> 321 - Section 2
<Date> September 7th, 2021
"""
import numpy as np

#Problem 1
def isolate(a, b, c, d, e):
    """Print the first threeseparated by 5 spaces,
    the rest with a single space between each output."""
    print(a, b, c, sep= '     ',end=' ')
    print(d, e, sep= ' ')

#Problem 2
def first_half(string):
    """Accept a parameter and return the first half of it,
    excluding themiddle character if there is an odd number of characters."""
    lengthy = int(len(string)/2)
    return string[:lengthy]

def backward(first_string):
    """Accept a parameter and reverse the order of its characters using slicing,
     then return the reversed string."""
    return first_string[::-1]

#Problem 3
def list_ops():
    """Define a list with the entries"bear","ant","cat", and"dog"""
    my_list = ["bear","ant","cat", "dog"]
    """Append"eagle"""
    my_list.append("eagle")
    """Replace the entry at index 2 with"fox"""
    my_list[2] = "fox"
    """Remove (or pop) the entry at index 1."""
    my_list.pop(1)
    """Sort the list in reverse alphabetical order."""
    my_list = list(reversed(sorted(my_list)))
    """Replace"eagle"with"hawk"""
    my_list[1] = "hawk"
    """Add the string"hunter"to the last entry in the list."""
    my_list[-1] = my_list[-1] + "hunter"
    return my_list

#Problem 4
def alt_harmonic(n):
    """Return the partial sum of the first n terms of the alternating
    harmonic series. Use this function to approximate ln(2)."""
    i = 1
    harmonic_sum = 0

    harmonic_series = [(((-1)**(i+1))/i) for i in range(i,n+1)]

    return sum(harmonic_series)

def prob5(A):
    """Make a copy of 'A' and set all negative entries of the copy to 0.
    Return the copy."""


    y = np.array(A)
    mask = y < 0
    y[mask] = 0

    return y

def prob6():
    """Define the matrices A, B, and C as arrays. Return the block matrix
                                | 0 A^T I |
                                | A  0  0 |,
                                | B  0  C |
    where I is the 3x3 identity matrix and each 0 is a matrix of all zeros
    of the appropriate size.
    """

    A_trans = np.arange(6).reshape((3,2))
    A = A_trans.T

    B = np.full(9,3).reshape((3,3))
    B = np.tril(B)

    C = np.full(9,-2).reshape((3,3))
    C = np.diag(C)
    C = np.diag(C)

    first_stack = np.hstack((np.zeros((3,3)), A_trans, np.identity(3)))
    second_stack = np.hstack((A, np.zeros((2,2)), np.zeros((2,3))))
    third_stack = np.hstack((B, np.zeros((3,2)), C))

    final_stack = np.vstack((first_stack,second_stack,third_stack))
    return final_stack

def prob7(A):
    """Divide each row of 'A' by the row sum and return the resulting array."""

    Sum = np.sum(A,axis = 1)
    A_Trans = np.transpose(A)
    new_A = np.divide(A_Trans, Sum)
    A = np.transpose(new_A)

    return A

def prob8():
    """Given the array stored in grid.npy, return the greatest product of four
    adjacent numbers in the same direction (up, down, left, right, or
    diagonally) in the grid.

    The greatest product of four adjacent numbers in the same direction
    (up, down, left,right, or diagonally)
    """

    grid = np.load("grid.npy")

    col_max = np.max(grid[:,:-3] * grid[:,1:-2] * grid[:,2:-1] * grid[:,3:])
    row_max = np.max(grid[:-3,:] * grid[1:-2,:] * grid[2:-1,:] * grid[3:,:])
    ldig_max = np.max(grid[:-3,:-3] * grid[1:-2,1:-2] * grid[2:-1,2:-1] * grid[3:,3:])
    rdig_max = np.max(grid[3:,:-3] * grid[2:-1,1:-2] * grid[1:-2,2:-1] * grid[:-3,3:])
    maX = max(col_max,row_max,ldig_max,rdig_max)

    return maX























    return len(new_grid)
