# newtons_method.py
"""Volume 1: Newton's Method.
<Name>
<Class>
<Date>
"""

import numpy as np
from matplotlib import pyplot as plt
import sympy as sy
from scipy import optimize
from autograd import grad
from numpy import linalg as la
from sympy import symbols, Eq, solve

# Problems 1, 3, and 5
def newton(f, x0, Df, alpha=1., tol=1e-5, maxiter=15):
    """Use Newton's method to approximate a zero of the function f.

    Parameters:
        f (function): a function from R^n to R^n (assume n=1 until Problem 5).
        x0 (float or ndarray): The initial guess for the zero of f.
        Df (function): The derivative of f, a function from R^n to R^(nxn).
        tol (float): Convergence tolerance. The function should returns when
            the difference between successive approximations is less than tol.
        maxiter (int): The maximum number of iterations to compute.
        alpha (float): Backtracking scalar (Problem 3).

    Returns:
        (float or ndarray): The approximation for a zero of f.
        (bool): Whether or not Newton's method converged.
        (int): The number of iterations computed.
    """
    #solve, yk
    if np.isscalar(x0) != True:
        for k in range(maxiter):
            x1 = x0 - alpha*(la.solve(Df(x0), f(x0)))
            if la.norm(x1-x0) < tol:
                return x1, True, k-1
            x0 = x1
        return x1, False, maxiter

    else:
        for k in range(maxiter):
            #The equation
            x1 = x0 - alpha*(f(x0)/Df(x0))
            if abs(x1-x0) < tol:
                return x1, True, k+1
            #change the values
            x0 = x1
        return x1, False, maxiter

#
# if __name__=="__main__":
#     F = lambda x: np.exp(x)-2
#     #f = lambda x: x**4 -3
#     f = lambda x: np.exp(x)
#     print(newton(F, 2., f))
# if __name__=="__main__":
#     F = lambda x: np.sign(x) * np.power(np.abs(x), 1./3)
#     #f = lambda x: x**4 -3
#     f = grad(F)
#     print(newton(F, .01, f, .4))


# Problem 2
def prob2(N1, N2, P1, P2):
    """Use Newton's method to solve for the constant r that satisfies

                P1[(1+r)**N1 - 1] = P2[1 - (1+r)**(-N2)].

    Use r_0 = 0.1 for the initial guess.

    Parameters:
        P1 (float): Amount of money deposited into account at the beginning of
            years 1, 2, ..., N1.
        P2 (float): Amount of money withdrawn at the beginning of years N1+1,
            N1+2, ..., N1+N2.
        N1 (int): Number of years money is deposited.
        N2 (int): Number of years money is withdrawn.

    Returns:
        (float): the value of r that satisfies the equation.
    """
    #setting lambda funtion
    F = lambda r: P2*(1-(1+r)**(-N2))-P1*(((1+r)**N1)-1)
    #df
    f = grad(F)

    return newton(F, .1, f)[0]

# if __name__=="__main__":
#     N1, N2, P1, P2 = 30, 20, 2000, 8000
#     print(prob2(N1, N2, P1, P2))

# Problem 4
def optimal_alpha(f, x0, Df, tol=1e-5, maxiter=15):
    """Run Newton's method for various values of alpha in (0,1].
    Plot the alpha value against the number of iterations until convergence.

    Parameters:
        f (function): a function from R^n to R^n (assume n=1 until Problem 5).
        x0 (float or ndarray): The initial guess for the zero of f.
        Df (function): The derivative of f, a function from R^n to R^(nxn).
        tol (float): Convergence tolerance. The function should returns when
            the difference between successive approximations is less than tol.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        (float): a value for alpha that results in the lowest number of
            iterations.
    """
    #iterate till maiter or till x1 and x0 are close
    alpha = np.linspace(0,1,10)
    alpha = np.delete(alpha, 0)

    iter_num = []

    for i in range(len(alpha)):
        #print("when alpha =", alpha[i])
        for k in range(maxiter):
            #print("when k =", k)
            #The equation
            x1 = x0 - alpha[i]*(f(x0)/Df(x0))
            #print("x1 = ", x1)
            if abs(x1-x0) < tol:
                iter_num.append(k-1)
                break
            #change the values
            x0 = x1
            if k == maxiter-1:
                iter_num.append(k)
    #print(iter_num)
    # plt.plot(alpha, iter_num)
    # plt.show()
    return alpha[np.argmin(iter_num)]

#need a good test

# if __name__=="__main__":
#     F = lambda x: np.sign(x) * np.power(np.abs(x), 1./3)
#     f = grad(F)
#
#     #print(newton(F, .01, f))
#     print(optimal_alpha(F, .01, f))


# Problem 6
def prob6():
    """Consider the following Bioremediation system.

                              5xy − x(1 + y) = 0
                        −xy + (1 − y)(1 + y) = 0

    Find an initial point such that Newton’s method converges to either
    (0,1) or (0,−1) with alpha = 1, and to (3.75, .25) with alpha = 0.55.
    Return the intial point as a 1-D NumPy array with 2 entries.
    """

    fir_eq = lambda x,y: 5*x*y - x*(1+y)
    sec_eq = lambda x,y: -x*y + (1-y)*(1+y)

    f = lambda x: np.array([(5*x[0]*x[1] - x[0]*(1+x[1])),
                            (-x[0]*x[1] + (1-x[1])*(1+x[1]))])
    df = lambda x: np.array([[(5*x[1]-(1+x[1])),(5*x[0]-x[0])],
                             [(-x[1]),(-x[0]-2*x[1])]])

    x = np.linspace(-1/4,0,50)
    y = np.linspace(0,1/4,50)

    for i in x:
        for j in y:
            x0 = np.array([i,j])
            x1 = newton(f, x0, df, alpha= 1., tol=1e-5, maxiter=15)
            if np.allclose(x1[0], np.array([0,1])) or np.allclose(x1[0], np.array([0,-1])):
                x2 = newton(f, x0, df, alpha= .55, tol=1e-5, maxiter=15)
                if np.allclose(x2[0], np.array([3.75,.25])):
                    return x0

#
# if __name__=="__main__":
#     prob6()


# Problem 7
def plot_basins(f, Df, zeros, domain, res=1000, iters=15):
    """Plot the basins of attraction of f on the complex plane.

    Parameters:
        f (function): A function from C to C.
        Df (function): The derivative of f, a function from C to C.
        zeros (ndarray): A 1-D array of the zeros of f.
        domain ([r_min, r_max, i_min, i_max]): A list of scalars that define
            the window limits and grid domain for the plot.
        res (int): A scalar that determines the resolution of the plot.
            The visualized grid has shape (res, res).
        iters (int): The exact number of times to iterate Newton's method.
    """
    #get X
    x_real = np.linspace(domain[0], domain[1], 500)    # Real parts.
    x_imag = np.linspace(domain[2], domain[3], 500)    # Imaginary parts.
    X_real, X_imag = np.meshgrid(x_real, x_imag)
    X_0 = X_real + 1j*X_imag

    #iterate to run the code
    for k in range(iters):
        X_1 = X_0 - f(X_0)/Df(X_0)
        X_0 = X_1

    #get Y
    y_real = np.linspace(domain[0], domain[1], 500)    # Real parts.
    y_imag = np.linspace(domain[2], domain[3], 500)    # Imaginary parts.
    Y_real, Y_imag = np.meshgrid(x_real, x_imag)
    Y = Y_real + Y_imag

    #modifiy Y
    for i in range(500):
        for j in range(500):
            Y[i][j] = np.argmin(abs(X_1[i][j]-zeros))

    #plug the function
    plt.pcolormesh(X_real, X_imag,Y,cmap = "brg",label="basins")
    plt.legend()
    plt.show()

# if __name__=="__main__":
#     f = lambda x: x**3 - x
#     Df = lambda x: 3*x**2 -1
#     #zeros = np.array([1,(-1+1j*np.sqrt(3))/2, (-1-1j*np.sqrt(3))/2])
#     zeros = np.array([-1,0,1])
#     domain = [-3/2,3/2,-3/2,3/2]
#     plot_basins(f, Df, zeros, domain)













    #What the....
