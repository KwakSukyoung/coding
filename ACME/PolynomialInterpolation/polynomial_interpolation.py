# polynomial_interpolation.py
"""Volume 2: Polynomial Interpolation.
<Name>
<Class>
<Date>
"""
import sympy as sy
import numpy as np
from matplotlib import pyplot as plt
from scipy.interpolate import BarycentricInterpolator
from numpy import linalg as la
from numpy.fft import fft

# Problems 1 and 2
def lagrange(xint, yint, points):
    """Find an interpolating polynomial of lowest degree through the points
    (xint, yint) using the Lagrange method and evaluate that polynomial at
    the specified points.

    Parameters:
        xint ((n,) ndarray): x values to be interpolated.
        yint ((n,) ndarray): y values to be interpolated.
        points((m,) ndarray): x values at which to evaluate the polynomial.

    Returns:
        ((m,) ndarray): The value of the polynomial at the specified points.
    """
    #initializing
    n = len(xint)
    L = []

    #for loop 0 - n-1
    for j in range(n):
        #get numerator
        num = np.product(points - np.delete(xint,j).reshape(n-1, 1), axis = 0)
        #get the denominator
        den = np.product(xint[j]-np.delete(xint,j))
        #change L
        L.append(num/den)
    #reshape yint
    yint = yint.reshape(n,1)
    #return p
    return np.sum(yint*L, axis = 0)

# if __name__=="__main__":
#
#     n = 5
#     runge = lambda x: 1 / ( 1 + 25 * x ** 2)
#     x = np.linspace(-1, 1, n)
#     y = runge(x)
#     domain = np.linspace(-1, 1, 100)
#     output = lagrange(x, y, domain)
#     print(output)
#     plt.plot(domain, runge(domain), 'c-', label='Original')
#     plt.plot(domain, output, 'r-', label='Interpolation')
#     plt.legend(loc='best')
#     plt.show()

# Problems 3 and 4
class Barycentric:
    """Class for performing Barycentric Lagrange interpolation.

    Attributes:
        w ((n,) ndarray): Array of Barycentric weights.
        n (int): Number of interpolation points.
        x ((n,) ndarray): x values of interpolating points.
        y ((n,) ndarray): y values of interpolating points.
    """

    def __init__(self, xint, yint):
        """Calculate the Barycentric weights using initial interpolating points.

        Parameters:
            xint ((n,) ndarray): x values of interpolating points.
            yint ((n,) ndarray): y values of interpolating points.
        """
        #save the arguments
        self.xint = xint
        self.yint = yint
        #save n
        n  = len(xint)
        #initializing w
        w = np.ones(n)
        #get C
        C = (np.max(xint) - np.min(xint)) / 4
        shuffle = np.random.permutation(n-1)
        #calculating w
        for j in range(n):
            temp = (xint[j] - np.delete(xint, j)) / C
            temp = temp[shuffle]
            w[j] /= np.product(temp)
        #setting w
        self.W = w

# if __name__=="__main__":
#
#     n = 5
#     runge = lambda x: 1 / ( 1 + 25 * x ** 2)
#     x = np.linspace(-1, 1, n)
#     y = runge(x)
#     domain = np.linspace(-1, 1, 100)
#     # output = lagrange(x, y, domain)
#     Christian = Barycentric(x, y)
#     print(Christian.W)
    def __call__(self, points):
        """Using the calcuated Barycentric weights, evaluate the interpolating polynomial
        at points.

        Parameters:
            points ((m,) ndarray): Array of points at which to evaluate the polynomial.

        Returns:
            ((m,) ndarray): Array of values where the polynomial has been computed.
        """

        #initializing
        n = len(self.xint)
        p = []

        #get numerator
        num = np.sum([self.W[j]*self.yint[j]/(points-self.xint[j]) for j in range(n)], axis = 0)
        #get the denominator
        den = np.sum([self.W[j]/(points-self.xint[j]) for j in range(n)],axis =0)
        #change L
        p = num/den
        #make 0 and the last one not being 0
        p[0] = self.yint[0]
        p[-1] = self.yint[-1]

        return p


# if __name__=="__main__":
#
#     n = 5
#     runge = lambda x: 1 / ( 1 + 25 * x ** 2)
#     x = np.linspace(-1, 1, n)
#     y = runge(x)
#     domain = np.linspace(-1, 1, 100)
#     output = lagrange(x, y, domain)
#     Christian = Barycentric(x,y)
#
#     plt.plot(domain, runge(domain), 'c-', label='Original')
#     #plt.plot(domain, output, 'r-', label='Interpolation')
#     plt.plot(domain, Christian(domain), 'r-', label='n-Original')
#
#     plt.show()
# Problem 4
    def add_weights(self, xint, yint):
        """Update the existing Barycentric weights using newly given interpolating points
        and create new weights equal to the number of new points.

        Parameters:
            xint ((m,) ndarray): x values of new interpolating points.
            yint ((m,) ndarray): y values of new interpolating points.
        """
        #renew xint and yint
        self.xint = np.concatenate((self.xint, xint))
        self.yint = np.concatenate((self.yint, yint))
        #save n
        n  = len(self.xint)
        #initializing wxs
        w = np.ones(n)
        #get C
        C = (np.max(self.xint) - np.min(self.xint)) / 4
        shuffle = np.random.permutation(n-1)
        #calculating w
        for j in range(n):
            temp = (self.xint[j] - np.delete(self.xint, j)) / C
            temp = temp[shuffle]
            w[j] /= np.product(temp)
        #setting w
        self.W = w

#
# #Super wrong I think
# if __name__=="__main__":
#     n = 11
#     runge = lambda x: 1 / (1 + 25 * x**2)
#     xvals_original = np.linspace(-1, 1, n)
#     xvals_1 = xvals_original[1::2]
#     xvals_2 = xvals_original[::2]
#     domain = np.linspace(-1, 1, 1000)
#     bary = Barycentric(xvals_1, runge(xvals_1))
#
#     bary_2 = Barycentric(xvals_original, runge(xvals_original))
#     plt.plot(domain, bary_2(domain),linewidth=6, label='Not added')
#     plt.plot(domain, runge(domain), label='Original')
#     plt.plot(domain, bary(domain), label='Odd Points, n = ' + str(n))
#     bary.add_weights(xvals_2, runge(xvals_2))
#     plt.plot(domain, bary(domain),'k', label='All points, n = ' + str(n))
#     plt.legend(loc='best')
#     plt.show()


# Problem 5
def prob5():
    """For n = 2^2, 2^3, ..., 2^8, calculate the error of intepolating Runge's
    function on [-1,1] with n points using SciPy's BarycentricInterpolator
    class, once with equally spaced points and once with the Chebyshev
    extremal points. Plot the absolute error of the interpolation with each
    method on a log-log plot.
    """
    #set x, n, f
    x = np.linspace(-1,1, 400)
    n = [2**k for k in range(2,9)]
    f = lambda x: 1/(1+25*x**2)

    #initialization
    f_error = []
    c_error = []
    a = 1
    b = -1

    #loops through n
    for i in n:
        #f function
        spac = np.linspace(-1,1,i)
        f_t = BarycentricInterpolator(spac,f(spac))
        f_error.append(la.norm(f(x)-f_t(x),np.inf))
        #Chebyshefv
        y= [a+b+(b-a)*np.cos(j*np.pi/i)/2 for j in range(i+1)]
        y = np.array(y)
        c_t = BarycentricInterpolator(y, f(y))
        c_error.append(la.norm(f(x)-c_t(x),np.inf))
    #plotting
    plt.loglog(n, f_error, label=r"$\frac{1}{1+25x^2}$")
    plt.loglog(n, c_error, label="Chebyshev")
    plt.legend()
    plt.show()



# Problem 6
def chebyshev_coeffs(f, n):
    """Obtain the Chebyshev coefficients of a polynomial that interpolates
    the function f at n points.

    Parameters:
        f (function): Function to be interpolated.
        n (int): Number of points at which to interpolate.

    Returns:
        coeffs ((n+1,) ndarray): Chebyshev coefficients for the interpolating polynomial.
    """
    #setting y
    y = np.cos((np.pi*np.arange(2*n))/n)
    #get the samples
    samples = f(y)
    #modify the coefficients
    coeffs = np.real(fft(samples))[:n+1] /n
    coeffs[0] = coeffs[0]/2
    coeffs[n] = coeffs[n]/2

    return coeffs

# if __name__=="__main__":
#     f = lambda x: -3 + 2*x**2 - x**3 + x**4
#     pcoeffs = [-3, 0, 2, -1, 1]
#     ccoeffs = np.polynomial.chebyshev.poly2cheb(pcoeffs)
#     print(ccoeffs)
#     print(chebyshev_coeffs(f, 4))
#     fpoly = np.polynomial.Polynomial(pcoeffs)
#     fcheb = np.polynomial.Chebyshev(ccoeffs)

# Problem 7
def prob7(n):
    """Interpolate the air quality data found in airdata.npy using
    Barycentric Lagrange interpolation. Plot the original data and the
    interpolating polynomial.

    Parameters:
        n (int): Number of interpolating points to use.
    """
    #save the data
    data = np.load('airdata.npy')
    #get the f function
    f = lambda a, b, n: .5*(a+b + (b-a) * np.cos(np.arange(n+1) * np.pi / n))
    #get a, b
    a, b = 0, 366 - 1/24
    #set domain
    domain = np.linspace(0, b, 8784)
    #get the points
    points = f(a, b, n)
    #set temps
    temp = np.abs(points - domain.reshape(8784, 1))
    temp2 = np.argmin(temp, axis=0)
    #get barycetnric numbers
    poly = Barycentric(domain[temp2], data[temp2])
    #plot
    plt.subplot(211)
    plt.plot(domain,data, label="airdata.npy")
    plt.legend()
    plt.subplot(212)
    plt.plot(domain, poly(domain), label="barycentric polynomial")
    plt.legend()
    plt.show()

# if __name__=="__main__":
#     prob7(50)
