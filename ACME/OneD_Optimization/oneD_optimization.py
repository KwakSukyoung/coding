# oneD_optimization.py
"""Volume 2: One-Dimensional Optimization.
<Name>
<Class>
<Date>
"""
import numpy as np
from scipy import optimize as opt
from matplotlib import pyplot as plt
from sympy.solvers import solve
import sympy as sy
from scipy.optimize import linesearch
from autograd import numpy as anp
from autograd import grad

# Problem 1
def golden_section(f, a, b, tol=1e-5, maxiter=15):
    """Use the golden section search to minimize the unimodal function f.

    Parameters:
        f (function): A unimodal, scalar-valued function on [a,b].
        a (float): Left bound of the domain.
        b (float): Right bound of the domain.
        tol (float): The stopping tolerance.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        (float): The approximate minimizer of f.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    #setting x0 and the xi...I think
    x0 = (a+b)/2
    bot= (1+np.sqrt(5))/2
    #for loop
    for i in range(maxiter):
        #getting the at,bt, and c
        c = (b-a)/bot
        a_t = b-c
        b_t = a+c
        #condition
        if f(a_t) <= f(b_t):
            b = b_t
        else:
            a = a_t
        #getting the new sequence
        x1 = (a+b)/2
        #break if the condition meets
        if abs(x0-x1) < tol:
            return x1, True, k+1
        x0 = x1
    return x1, False, maxiter


# if __name__=="__main__":
#     f = lambda x : np.exp(x) - 4*x
#     x = np.linspace(0,3,100)
#     print(golden_section(f,0,3))
#     x1 = golden_section(f,0,3)[0]
#     plt.plot(x,f(x), label="original")
#     #print(x1,f(x1))
#     plt.plot(x1, f(x1), label="dot")
#     plt.legend()
#     plt.show()

# Problem 2
def newton1d(df, d2f, x0, tol=1e-5, maxiter=15):
    """Use Newton's method to minimize a function f:R->R.

    Parameters:
        df (function): The first derivative of f.
        d2f (function): The second derivative of f.
        x0 (float): An initial guess for the minimizer of f.
        tol (float): The stopping tolerance.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        (float): The approximate minimizer of f.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    for i in range(maxiter):
        #getting the new x1
        x1 = x0 - df(x0)/d2f(x0)
        #break if it meets the condition
        if abs(x0-x1) < tol:
            return x1, True, i+1
        x0 = x1
    return x1, False, maxiter

# if __name__=="__main__":
#     #f = lambda x: x**2 +np.sin(5*x)
#     df = lambda x : 2*x + 5*np.cos(5*x)
#     d2f = lambda x : 2 - 25*np.sin(5*x)
#
#     x = newton1d(df, d2f, 0)
#     print(x)

# Problem 3
def secant1d(df, x0, x1, tol=1e-5, maxiter=15):
    """Use the secant method to minimize a function f:R->R.

    Parameters:
        df (function): The first derivative of f.
        x0 (float): An initial guess for the minimizer of f.
        x1 (float): Another guess for the minimizer of f.
        tol (float): The stopping tolerance.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        (float): The approximate minimizer of f.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    for i in range(maxiter):
        #the new equation
        x2 = (x0*df(x1)-x1*df(x0))/(df(x1)-df(x0))
        #stops if it's very small gap
        if abs(x2-x1) < tol:
            return x2, True, i+1
        #changes the values of the variables
        x0 = x1
        x1 = x2
    return x2, False, maxiter

# if __name__=="__main__":
#     df = lambda x: 2*x + np.cos(x) + 10*np.cos(10*x)
#     print(secant1d(df, 0,-1))

# Problem 4
def backtracking(f, Df, x, p, alpha=1, rho=.9, c=1e-4):
    """Implement the backtracking line search to find a step size that
    satisfies the Armijo condition.

    Parameters:
        f (function): A function f:R^n->R.
        Df (function): The first derivative (gradient) of f.
        x (float): The current approximation to the minimizer.
        p (float): The current search direction.
        alpha (float): A large initial step length.
        rho (float): Parameter in (0, 1).
        c (float): Parameter in (0, 1).

    Returns:
        alpha (float): Optimal step size.
    """
    #compute values only once
    dfp = np.dot(Df(x).T,p)
    fx = f(x)
    #loop till it meets the condition
    while (f(x+alpha*p)> fx +c*alpha*dfp):
        #changes alpha
        alpha *= rho
    return alpha

# if __name__=="__main__":
#     f = lambda x: x[0]**2 + x[1]**2 + x[2]**2
#     Df = lambda x: np.array([2*x[0], 2*x[1], 2*x[2]])
#     x = anp.array([150., .03, 40.])
#     p = anp.array([-.5, -100., -4.5])
#     print(backtracking(f,Df, x, p))
#
#
#     phi = lambda alpha: f(x + alpha*p)
#     dphi = grad(phi)
#     alpha, _ = linesearch.scalar_search_armijo(phi, phi(0.), dphi(0.))
#     print(alpha)


    #For some time
