# differentiation.py
"""Volume 1: Differentiation.
<Name> Sukyoung Kwak
<Class> vol-1
<Date> December 30th
"""
import sympy as sy
import numpy as np
from matplotlib import pyplot as plt
import math

from autograd import numpy as anp
from autograd import grad
from autograd import elementwise_grad

import time

# Problem 1
def prob1():
    """Return the derivative of (sin(x) + 1)^sin(cos(x)) using SymPy."""
    #set the symbol
    x = sy.symbols('x')
    #set the function
    f = (sy.sin(x)+1)**(sy.sin(sy.cos(x)))
    #get the derivative
    d = sy.diff(f,x)
    #lamdify the resulting function(variable, expression, library)
    lam = sy.lambdify(x, d, "numpy")

    return lam

# Problem 2
def fdq1(f, x, h=1e-5):
    """Calculate the first order forward difference quotient of f at x."""
    return (f(x+h)-f(x))/h

def fdq2(f, x, h=1e-5):
    """Calculate the second order forward difference quotient of f at x."""
    return (-3*f(x)+4*f(x+h)-f(x+2*h))/(2*h)

def bdq1(f, x, h=1e-5):
    """Calculate the first order backward difference quotient of f at x."""
    return (f(x)-f(x-h))/h

def bdq2(f, x, h=1e-5):
    """Calculate the second order backward difference quotient of f at x."""
    return (3*f(x)-4*f(x-h)+f(x-2*h))/(2*h)

def cdq2(f, x, h=1e-5):
    """Calculate the second order centered difference quotient of f at x."""
    return (f(x+h)-f(x-h))/(2*h)

def cdq4(f, x, h=1e-5):
    """Calculate the fourth order centered difference quotient of f at x."""
    return (f(x-2*h)-8*f(x-h)+8*f(x+h)-f(x+2*h))/(12*h)

# Problem 3
def prob3(x0):
    """Let f(x) = (sin(x) + 1)^(sin(cos(x))). Use prob1() to calculate the
    exact value of f'(x0). Then use fdq1(), fdq2(), bdq1(), bdq2(), cdq1(),
    and cdq2() to approximate f'(x0) for h=10^-8, 10^-7, ..., 10^-1, 1.
    Track the absolute error for each trial, then plot the absolute error
    against h on a log-log scale.

    Parameters:
        x0 (float): The point where the derivative is being approximated.
    """
    #set f function
    f = lambda x: (np.sin(x)+1)**(np.sin(np.cos(x)))
    #get the value of derivative at x0
    d = prob1()(x0)
    domain = np.logspace(-8,0,9)

    #plug
    plt.loglog(domain, abs(d-fdq1(f, x0, domain)),".-",label="Order 1 Forward")
    plt.loglog(domain, abs(d-fdq2(f, x0, domain)),".-",label="Order 2 Forward")
    plt.loglog(domain, abs(d-bdq1(f, x0, domain)),".-",label="Order 1 Backward")
    plt.loglog(domain, abs(d-bdq2(f, x0, domain)),".-",label="Order 2 Backward")
    plt.loglog(domain, abs(d-cdq2(f, x0, domain)),".-",label="Order 2 Centered")
    plt.loglog(domain, abs(d-cdq4(f, x0, domain)),".-",label="Order 4 Centered")

    #more plug info
    plt.xlabel("h")
    plt.ylabel("Absolute Error")
    plt.legend()
    plt.show()


# Problem 4
def prob4():
    """The radar stations A and B, separated by the distance 500m, track a
    plane C by recording the angles alpha and beta at one-second intervals.
    Your goal, back at air traffic control, is to determine the speed of the
    plane.

    Successive readings for alpha and beta at integer times t=7,8,...,14
    are stored in the file plane.npy. Each row in the array represents a
    different reading; the columns are the observation time t, the angle
    alpha (in degrees), and the angle beta (also in degrees), in that order.
    The Cartesian coordinates of the plane can be calculated from the angles
    alpha and beta as follows.

    x(alpha, beta) = a tan(beta) / (tan(beta) - tan(alpha))
    y(alpha, beta) = (a tan(beta) tan(alpha)) / (tan(beta) - tan(alpha))

    Load the data, convert alpha and beta to radians, then compute the
    coordinates x(t) and y(t) at each given t. Approximate x'(t) and y'(t)
    using a first order forward difference quotient for t=7, a first order
    backward difference quotient for t=14, and a second order centered
    difference quotient for t=8,9,...,13. Return the values of the speed at
    each t.
    """
    #get the data
    angles = np.load("plane.npy")
    new = np.deg2rad(angles[:,1:])
    #get the angles
    alpha = new[:,0]
    beta = new[:,1]
    #get the x(t) and y(t)
    x_cor = 500*(np.tan(beta)/(np.tan(beta)-np.tan(alpha)))
    y_cor = 500*((np.tan(beta)*np.tan(alpha))/(np.tan(beta)-np.tan(alpha)))
    #set varaialbes
    speed = [0] * 8
    h = 1
    #get the time
    for i in range(7):
        if i == 0:
            #fdq1
            speed[i] = math.sqrt((((x_cor[i+h]-x_cor[i])/h)**2) + (((y_cor[i+h]-y_cor[i])/h)**2))
        else:
            #cdq2
            speed[i] = math.sqrt((((x_cor[i+h]-x_cor[i-h])/(2*h))**2)+(((y_cor[i+h]-y_cor[i-h])/(2*h))**2))
    #bdq1
    speed[7] = math.sqrt((((x_cor[7]-x_cor[7-h])/h)**2)+(((y_cor[7]-y_cor[7-h])/h)**2))

    return speed

# Problem 5
def jacobian_cdq2(f, x, h=1e-5):
    """Approximate the Jacobian matrix of f:R^n->R^m at x using the second
    order centered difference quotient.

    Parameters:
        f (function): the multidimensional function to differentiate.
            Accepts a NumPy (n,) ndarray and returns an (m,) ndarray.
            For example, f(x,y) = [x+y, xy**2] could be implemented as follows.
            >>> f = lambda x: np.array([x[0] + x[1], x[0] * x[1]**2])
        x ((n,) ndarray): the point in R^n at which to compute the Jacobian.
        h (float): the step size in the finite difference quotient.

    Returns:
        ((m,n) ndarray) the Jacobian matrix of f at x.
    """
    #get the number of rows and columns
    i = np.size(f(x))
    j = np.size(x)
    #Initialize e and J
    e = np.eye(j)
    J = np.zeros((i,j))
    #assigning each element
    for b in range(j):
        J[:, b] = ((f(x+h*e[b])-f(x-h*e[b]))/(2*h))

    return J

# if __name__=="__main__":
#     f = lambda x: np.array([x[0]**2, x[0]**3 - x[1]])
#     a = np.array([1,1])
#     print(jacobian_cdq2(f, a))


# Problem 6
def cheb_poly(x, n):
    """Compute the nth Chebyshev polynomial at x.

    Parameters:
        x (autograd.ndarray): the points to evaluate T_n(x) at.
        n (int): The degree of the polynomial.
    """
    T_2 = anp.ones_like(x)
    if n == 0:
        return T_2

    elif n == 1:
        return x
    T_1 = x

    for i in range(n-1):
        T = 2*x*T_1-T_2
        T_2 = T_1
        T_1 = T
    return T


def prob6():
    """Use Autograd and cheb_poly() to create a function for the derivative
    of the Chebyshev polynomials, and use that function to plot the derivatives
    over the domain [-1,1] for n=0,1,2,3,4.
    """
    dg = elementwise_grad(cheb_poly)
    domain = np.linspace(-1,1,100)
    for i in range(5):
        plt.plot(domain, dg(domain,i),label=r"$T'_{{{}}}$".format(i))

    plt.legend()
    plt.show()


# Problem 7
def prob7(N=200):
    """Let f(x) = (sin(x) + 1)^sin(cos(x)). Perform the following experiment N
    times:

        1. Choose a random value x0.
        2. Use prob1() to calculate the “exact” value of f′(x0). Time how long
            the entire process takes, including calling prob1() (each
            iteration).
        3. Time how long it takes to get an approximation of f'(x0) using
            cdq4(). Record the absolute error of the approximation.
        4. Time how long it takes to get an approximation of f'(x0) using
            Autograd (calling grad() every time). Record the absolute error of
            the approximation.

    Plot the computation times versus the absolute errors on a log-log plot
    with different colors for SymPy, the difference quotient, and Autograd.
    For SymPy, assume an absolute error of 1e-18.
    """
    took = []
    f = lambda x: ((anp.sin(x)+1)**(anp.sin(anp.cos(x))))
    dg = grad(f)

    time_1 = []
    time_2 = []
    time_3 = []
    ab_err_1 = []
    ab_err_2 = []
    ab_err_3 = []

    for i in range(N):
        x0 = np.random.random()

        beg = time.time()
        exact_value = prob1()(x0)
        end = time.time()
        time_1.append(end - beg)

        beg = time.time()
        d = cdq4(f,x0)
        end = time.time()
        time_2.append(end - beg)

        beg = time.time()
        g = dg(x0)
        end = time.time()
        time_3.append(end - beg)

        #Where am I supposed to use SymPy????

        ab_err_1.append(abs(exact_value-d))
        ab_err_2.append(abs(exact_value-g))
        ab_err_3.append(abs(1e-18))

    #plug
    plt.loglog(time_2, ab_err_1,".",alpha = .4,color= "C1",label="Difference Quotients")
    plt.loglog(time_3, ab_err_2,".",alpha = .4,color= "C2",label="Autograd")
    plt.loglog(time_1, ab_err_3,".",alpha = .4,color= "C0",label="SymPy")

    #more plug info
    plt.xlabel("Computation Time (seconds)")
    plt.ylabel("Absolute Error")
    plt.legend()
    plt.show()
