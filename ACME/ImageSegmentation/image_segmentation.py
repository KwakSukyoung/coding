# image_segmentation.py
"""Volume 1: Image Segmentation.
<Name>Sukyoung Kwak
<Class>345-2
<Date>November 6th
"""

import numpy as np
from scipy import linalg as la
from imageio import imread
from matplotlib import pyplot as plt
import scipy.sparse
from scipy.sparse import load_npz
from scipy.sparse import linalg

# Problem 1
def laplacian(A):
    """Compute the Laplacian matrix of the graph G that has adjacency matrix A.

    Parameters:
        A ((N,N) ndarray): The adjacency matrix of an undirected graph G.

    Returns:
        L ((N,N) ndarray): The Laplacian matrix of G.
    """
    #Get the size 0f A
    n = len(A)
    #Initialize D and L
    D = np.zeros((n,n))
    L = np.zeros((n,n))
    #Set the diagonal elements
    for i in range(n):
        D[i][i] = np.sum(A[i])
    #The defintion of L
    L = D - A

    return L

# Problem 2
def connectivity(A, tol=1e-8):
    """Compute the number of connected components in the graph G and its
    algebraic connectivity, given the adjacency matrix A of G.

    Parameters:
        A ((N,N) ndarray): The adjacency matrix of an undirected graph G.
        tol (float): Eigenvalues that are less than this tolerance are
            considered zero.

    Returns:
        (int): The number of connected components in G.
        (float): the algebraic connectivity of G.
    """
    #call laplacian funtion to get L
    L = laplacian(A)
    #get the eigenvalues
    eig = la.eig(L)[0]
    #convert the eigenvalues into real number
    real_eig = np.real(eig)
    #convert the eigenvalues into 0 when it's smaller than tolerance
    real_eig[real_eig < tol] = 0
    #get the number of connected components
    num_con = len(real_eig[real_eig == 0])
    #sort the eigen values to get the second smallest number
    sorted_eig = np.sort(real_eig)
    alge_con = sorted_eig[1]

    return num_con, alge_con

# Helper function for problem 4.
def get_neighbors(index, radius, height, width):
    """Calculate the flattened indices of the pixels that are within the given
    distance of a central pixel, and their distances from the central pixel.

    Parameters:
        index (int): The index of a central pixel in a flattened image array
            with original shape (radius, height).
        radius (float): Radius of the neighborhood around the central pixel.
        height (int): The height of the original image in pixels.
        width (int): The width of the original image in pixels.

    Returns:
        (1-D ndarray): the indices of the pixels that are within the specified
            radius of the central pixel, with respect to the flattened image.
        (1-D ndarray): the euclidean distances from the neighborhood pixels to
            the central pixel.
    """
    # Calculate the original 2-D coordinates of the central pixel.
    row, col = index // width, index % width

    # Get a grid of possible candidates that are close to the central pixel.
    r = int(radius)
    x = np.arange(max(col - r, 0), min(col + r + 1, width))
    y = np.arange(max(row - r, 0), min(row + r + 1, height))
    X, Y = np.meshgrid(x, y)

    # Determine which candidates are within the given radius of the pixel.
    R = np.sqrt(((X - col)**2 + (Y - row)**2))
    mask = R < radius
    return (X[mask] + Y[mask]*width).astype(np.int), R[mask]


# Problems 3-6
class ImageSegmenter:
    """Class for storing and segmenting images."""

    # Problem 3
    def __init__(self, filename):
        """Read the image file. Store its brightness values as a flat array."""
        #Read
        self.filename = filename
        self.image = imread(filename)

        #scale the image
        self.scaled = self.image/255

        #When the image is in color
        if len(self.image.shape) == 3:
            self.gray = "False"
            self.color = "True"
            self.bright = self.scaled.mean(axis = 2)

        #When the image is in gray
        elif len(self.image.shape) == 2:
            self.gray = "True"
            self.color = "False"
            self.bright = self.scaled

        #When the inputs are wrong
        else:
            raise ValueError("It's not an image")

        #Get the brightness
        self.flat_brightness = np.ravel(self.bright)

        #Get and store the shape of filename
        #self.m, self.n = np.shape(self.image)
        self.m = np.shape(self.image)[0]
        self.n = np.shape(self.image)[1]

    # Problem 3
    def show_original(self):
        """Display the original image."""
        #When the image is in gray
        if self.gray == "True":
            plt.imshow(self.scaled, cmap="gray")
        #When the image is in color
        else:
            plt.imshow(self.scaled)

        plt.axis("off")
        plt.show()

    # Problem 4
    def adjacency(self, r=5., sigma_B2=.02, sigma_X2=3.):
        """Compute the Adjacency and Degree matrices for the image graph."""
        #Initialize A and D
        A = scipy.sparse.lil_matrix((self.m*self.n,self.m*self.n))
        D = np.zeros(self.m*self.n)
        #for loop 1 ~ mn -1
        for i in range(self.n*self.m):
            #use neighbor function to get the indices and the distance values.
            neigh_indices, neigh_dist = get_neighbors(i,r,self.n,self.m)
            #use the formula to get the weight
            neigh_weight = np.exp(-(abs(self.flat_brightness[i]-self.flat_brightness[neigh_indices])/sigma_B2)-(neigh_dist/sigma_X2))
            #save it in A
            A[i,neigh_indices] = neigh_weight
            #save it in D
            D[i] = np.sum(neigh_weight)
        #convert A
        A = scipy.sparse.csc_matrix(A)
        return A, D

    # Problem 5
    def cut(self, A, D):
        """Compute the boolean mask that segments the image."""
        #Obtain L
        L = scipy.sparse.csgraph.laplacian(A)
        #Change D
        constructed_D = scipy.sparse.diags(1/np.sqrt(D))
        computed_D = constructed_D@L@constructed_D
        #Get the second smallest eigen vectors
        small_eigs = scipy.sparse.linalg.eigsh(computed_D,which="SM",k=2)
        eig_vect = small_eigs[1][:,1]
        eig_vect = eig_vect.reshape((self.m,self.n))
        #Positive masks
        mask = eig_vect > 0
        return mask

    # Problem 6
    def segment(self, r=5., sigma_B=.02, sigma_X=3.):
        """Display the original image and its segments."""
        #Set A,D and mask
        A,D = self.adjacency(5.,.02,3. )
        seg_mask = self.cut(A,D)

        #Get the three parts of the graph
        axis_1 = plt.subplot(131)
        axis_2 = plt.subplot(132)
        axis_3 = plt.subplot(133)
        #Don't show the axis
        axis_1.axis("off")
        axis_2.axis("off")
        axis_3.axis("off")

        #When the image is in gray
        if self.gray == "True":
            #original
            axis_1.imshow(self.scaled,cmap="gray")
            #positive
            axis_2.imshow(np.multiply(self.scaled, seg_mask),cmap="gray")
            #negative
            axis_3.imshow(np.multiply(self.scaled, ~seg_mask),cmap="gray")

        #When the image is in color
        else:
            #Stack a mask into a 3-D array with np.dstack()
            new_mask = np.dstack((seg_mask,seg_mask,seg_mask))
            #original
            axis_1.imshow(self.scaled)
            #positive
            axis_2.imshow(np.multiply(self.scaled, new_mask))
            #negative
            axis_3.imshow(np.multiply(self.scaled, ~new_mask))

        plt.show()
