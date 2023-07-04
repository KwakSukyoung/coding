# lstsq_eigs.py
"""Volume 1: Least Squares and Computing Eigenvalues.
<Name> Sukyoung Kwak
<Class> 345 - section 2
<Date> October 31st, Happy Halloweeen
"""

# (Optional) Import functions from your QR Decomposition lab.
import numpy as np
from matplotlib import pyplot as plt
from scipy import linalg as la
import cmath


# Problem 1
def least_squares(A, b):
    """Calculate the least squares solutions to Ax = b by using the QR
    decomposition.

    Parameters:
        A ((m,n) ndarray): A matrix of rank n <= m.
        b ((m, ) ndarray): A vector of length m.

    Returns:
        x ((n, ) ndarray): The solution to the normal equations.
    """
    #QR decomposition
    Q, R = la.qr(A, mode="economic")
    #solve the x
    x = la.solve_triangular(R, np.dot(Q.T,b),lower=False)
    return x

# Problem 2
def line_fit():
    """Find the least squares line that relates the year to the housing price
    index for the data in housing.npy. Plot both the data points and the least
    squares line.
    """

    #get and save the info
    housing_data = np.load("housing.npy")

    #Get A data
    A = np.ones((len(housing_data),2))
    for i in range(len(A)):
        A[i][0] = housing_data[i][0]

    #Get b data
    b = []
    for i in range(len(A)):
        b.append(housing_data[i][1])

    #Get solution x
    x = least_squares(A,b)

    #plot the data
    plt.plot(housing_data[:,0], b, 'r.', label = "housing data")
    plt.plot(housing_data[:,0], x[1] + x[0]*housing_data[:,0], 'g', label = "solution")

    plt.xlabel("year")
    plt.ylabel("Housing Price")
    plt.title("Housing Data")
    plt.legend(loc="upper left")

    plt.show()

# Problem 3
def polynomial_fit():
    """Find the least squares polynomials of degree 3, 6, 9, and 12 that relate
    the year to the housing price index for the data in housing.npy. Plot both
    the data points and the least squares polynomials in individual subplots.
    """
    #get and save the info
    housing_data = np.load("housing.npy")

    #Get b data
    b = np.copy(housing_data[:,1])

    #Set the domain of the graph
    domain = np.linspace(min(housing_data[:,0]),max(housing_data[:,0]),100)

    #degree 3
    A_3 = np.vander(housing_data[:,0],4)
    x_3 = la.lstsq(A_3, b)[0]
    y3_vals = np.polyval(x_3,domain)
    plt.subplot(2,2,1)
    plt.plot(domain, y3_vals, 'b-', lw=2)
    plt.plot(housing_data[:,0], b, 'r.')
    plt.title("degree 3",fontsize = 6)

    #degree 6
    A_6 = np.vander(housing_data[:,0],7)
    x_6 = la.lstsq(A_6, b)[0]
    y6_vals = np.polyval(x_6,domain)
    plt.subplot(2,2,2)
    plt.plot(domain, y6_vals, 'y-', lw=2)
    plt.plot(housing_data[:,0], b, 'r.')
    plt.title("degree 6",fontsize = 6)

    #degree 9
    A_9 = np.vander(housing_data[:,0],10)
    x_9 = la.lstsq(A_9, b)[0]
    y9_vals = np.polyval(x_9,domain)
    plt.subplot(2,2,3)
    plt.plot(domain, y9_vals, 'g-', lw=2)
    plt.plot(housing_data[:,0], b, 'r.')
    plt.title("degree 9",fontsize = 6)

    #degree 12
    A_12 = np.vander(housing_data[:,0],13)
    x_12 = la.lstsq(A_12, b)[0]
    y12_vals = np.polyval(x_12,domain)
    plt.subplot(2,2,4)
    plt.plot(domain, y12_vals, 'c-', lw=2)
    plt.plot(housing_data[:,0], b, 'r.')
    plt.title("degree 12",fontsize = 6)

    plt.xlabel("x-axis is Year\ny-axis is Housing Price")
    plt.show()

# Problem 4
def plot_ellipse(a, b, c, d, e):
    """Plot an ellipse of the form ax^2 + bx + cxy + dy + ey^2 = 1."""
    #get theta
    theta = np.linspace(0, 2*np.pi, 200)
    #get cos and sin vals
    cos_t, sin_t = np.cos(theta), np.sin(theta)

    #Get A, B, r
    A = a*(cos_t**2) + c*cos_t*sin_t + e*(sin_t**2)
    B = b*cos_t + d*sin_t
    r = (-B + np.sqrt(B**2 + 4*A)) / (2*A)

    #Plot
    plt.plot(r*cos_t, r*sin_t,lw = 2)
    plt.gca().set_aspect("equal", "datalim")


def ellipse_fit():
    """Calculate the parameters for the ellipse that best fits the data in
    ellipse.npy. Plot the original data points and the ellipse together, using
    plot_ellipse() to plot the ellipse.
    """
    #Get and store the data
    x, y = np.load("ellipse.npy").T

    #Set A = x^2,x,xy,y,y^2 and B =1
    A = np.column_stack(((x*x),x,(x*y),y,(y*y)))
    B = np.ones(len(A))

    #Find the coefficients
    a,b,c,d,e = la.lstsq(A,B)[0]

    #plot
    plot_ellipse(a,b,c,d,e)
    plt.plot(x,y,'k*')
    plt.show()

# Problem 5
def power_method(A, N=20, tol=1e-12):
    """Compute the dominant eigenvalue of A and a corresponding eigenvector
    via the power method.

    Parameters:
        A ((n,n) ndarray): A square matrix.
        N (int): The maximum number of iterations.
        tol (float): The stopping tolerance.

    Returns:
        (float): The dominant eigenvalue of A.
        ((n,) ndarray): An eigenvector corresponding to the dominant
            eigenvalue of A.
    """
    #Get the shape of A
    n = np.shape(A)[0]
    #set x
    x = np.random.rand(n)
    #noramlize x
    x = x/la.norm(x)

    #iterate through 0 - N-1
    for k in range(N):
        x = np.dot(A, x)
        x = x/la.norm(x)

    #returns the dominant eigenvalue, eigenvector
    return np.dot(x.T, np.dot(A, x)),x

# Problem 6
def qr_algorithm(A, N=50, tol=1e-12):
    """Compute the eigenvalues of A via the QR algorithm.

    Parameters:
        A ((n,n) ndarray): A square matrix.
        N (int): The number of iterations to run the QR algorithm.
        tol (float): The threshold value for determining if a diagonal S_i
            block is 1x1 or 2x2.

    Returns:
        ((n,) ndarray): The eigenvalues of A.
    """
    #get n and S
    n = np.shape(A)[0]
    S = la.hessenberg(A)

    #get the Qr and S
    for k in range(N):
        Q,R = la.qr(S)
        S = np.dot(R,Q)

    #Initialization
    eigs = []
    i = 0

    #While 0, 1, ...n-1
    while i <n:
        #The last element
        if (i == n-1):
            eigs.append(S[i,i])
        #tolerance is bigger
        elif abs(S[i+1,i]) <tol:
            eigs.append(S[i,i])
        else:
            a = S[i,i]
            b = S[i,i+1]
            c = S[i+1,i]
            d = S[i+1,i+1]

            #odd and even
            eigs.append((a+d+cmath.sqrt(((a+d)**2)-4*(a*d -b*c)))/2)
            eigs.append((a+d-cmath.sqrt(((a+d)**2)-4*(a*d -b*c)))/2)

            i += 1
        i+= 1
    return eigs
