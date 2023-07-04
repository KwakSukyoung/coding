# gradient_methods.py
"""Volume 2: Gradient Descent Methods.
<Name>
<Class>
<Date>
"""
from scipy import optimize as opt
import numpy as np
from numpy import linalg as la
import matplotlib.pyplot as plt


# Problem 1
def steepest_descent(f, Df, x0, tol=1e-5, maxiter=100):
    """Compute the minimizer of f using the exact method of steepest descent.

    Parameters:
        f (function): The objective function. Accepts a NumPy array of shape
            (n,) and returns a float.
        Df (function): The first derivative of f. Accepts and returns a NumPy
            array of shape (n,).
        x0 ((n,) ndarray): The initial guess.
        tol (float): The stopping tolerance.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        ((n,) ndarray): The approximate minimum of f.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    #loop through maxiter
    for k in range(maxiter):
        #getting alpha function
        alpha = lambda al: f(x0 -al*Df(x0).T)
        a_k = opt.minimize_scalar(alpha).x
        #get the new x
        x1 = x0 - a_k*Df(x0).T
        #when it converges
        if la.norm(Df(x0), ord=np.inf) < tol:
            return x1, True, k
        #renew the values
        x0 = x1
        a = a_k

    return x1, False, maxiter

# if __name__ =="__main__":
#     # Should get: (array([9.24407773e-10, 9.24407773e-10, 9.24407773e-10]), True, 1)
#     f = lambda x: x[0]**4 + x[1]**4 + x[2]**4
#     Df = lambda x: np.array([4*x[0]**3,  4*x[1]**3, 4*x[2]**3])
#     x0 = np.array([1, 1, 1])
#     print(r"$x^4 + y^4 + z^4$: ", steepest_descent(f, Df, x0))
#
#     #Should get: (array([1.69787179, 2.8849864 ]), False, 100)
#     r = lambda x: (1 - x[0])**2 + 100*(x[1] - x[0]**2)**2
#     dr = lambda x: np.array([400*x[0]**3 - 400*x[0]*x[1] + 2*x[0] - 2, 200*(x[1] - x[0]**2)])
#     x0 = np.array([-2, 2])
#     print('Rosenbrock: ', steepest_descent(r, dr, x0))
#
#     # Should get: (array([0.50000475, 2.00000057]), True, 7)
#     p2 = lambda x: x[0]**2 + 2*x[1]**2 - x[0] - 8*x[1]
#     p2f = lambda x: np.array([2*x[0] - 1, 4*x[1] - 8])
#     x0 = np.array([1, 1])
#     print(r"$x^2 + 2y^2 - x - 8y$: ",steepest_descent(p2, p2f, x0))

# Problem 2
def conjugate_gradient(Q, b, x0, tol=1e-4):
    """Solve the linear system Qx = b with the conjugate gradient algorithm.

    Parameters:
        Q ((n,n) ndarray): A positive-definite square matrix.
        b ((n, ) ndarray): The right-hand side of the linear system.
        x0 ((n,) ndarray): An initial guess for the solution to Qx = b.
        tol (float): The convergence tolerance.

    Returns:
        ((n,) ndarray): The solution to the linear system Qx = b.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    # the dimension
    n = np.shape(Q)[0]
    #initialization
    r0 = Q@x0-b
    d0 = -r0
    k = 0
    #while it's not meeting the condition
    while (la.norm(r0, ord=np.inf) >= tol and k <n):
        #get the new values
        a_k = (r0.T@r0)/(d0.T@Q@d0)
        x1 = x0 + a_k*d0
        r1 = r0 + a_k*Q@d0
        B1 = (r1.T@r1)/(r0.T@r0)
        d1 = -r1 + B1*d0
        k += 1
        #if it converges
        if la.norm(r0) < tol:
            return x1, True, k
        #renew the values
        x0 = x1
        r0 = r1
        d0 = d1
    return x1, False, k

# if __name__=="__main__":
#     # Should get: (array([0.5, 2. ]), False, 2)
#     Q = np.array([[2, 0],[0, 4]])
#     b = np.array([1, 8])
#     x0 = np.array([1, 1])
#     print(conjugate_gradient(Q, b, x0))
#
#     n = 4
#     A = np.random.random((n,n))
#     Q = A.T @ A
#     b, x0 = np.random.random((2,n))
#     x = conjugate_gradient(Q, b, x0)[0]
#     print(np.allclose(Q @ x, b))

# Problem 3
def nonlinear_conjugate_gradient(f, df, x0, tol=1e-5, maxiter=100):
    """Compute the minimizer of f using the nonlinear conjugate gradient
    algorithm.

    Parameters:
        f (function): The objective function. Accepts a NumPy array of shape
            (n,) and returns a float.
        Df (function): The first derivative of f. Accepts and returns a NumPy
            array of shape (n,).
        x0 ((n,) ndarray): The initial guess.
        tol (float): The stopping tolerance.
        maxiter (int): The maximum number of iterations to compute.

    Returns:
        ((n,) ndarray): The approximate minimum of f.
        (bool): Whether or not the algorithm converged.
        (int): The number of iterations computed.
    """
    #initialization
    r0 = -df(x0).T
    d0 = r0
    alpha = lambda al: f(x0 +al*d0)
    a0 = opt.minimize_scalar(alpha).x
    x1 = x0+a0*d0
    k = 1
    #loop while it's meeting the condition
    while (la.norm(r0, ord=np.inf) >= tol and k <maxiter):
        #renew the values
        r1 = -df(x1).T
        B0 = (r1.T@r1)/(r0.T@r0)
        d1 = r1 +B0*d0
        alpha = lambda al: f(x1 + al*d1)
        a_k = opt.minimize_scalar(alpha).x
        x1 += a_k*d1
        k += 1
        #if it converes
        if la.norm(r1) < tol:
            return x1, True, k
        #change the values
        d0 = d1
        r0 = r1
    return x1, False, maxiter

# if __name__=="__main__":
#     opt.fmin_cg(opt.rosen, np.array([10, 10]), fprime=opt.rosen_der)
#     # Should get: (array([0.99999757, 0.99999513]), True, 307)
#     print(nonlinear_conjugate_gradient(f=opt.rosen, df=opt.rosen_der, x0=np.array([10, 10]), maxiter=500))

# Problem 4
def prob4(filename="linregression.txt",
          x0=np.array([-3482258, 15, 0, -2, -1, 0, 1829])):
    """Use conjugate_gradient() to solve the linear regression problem with
    the data from the given file, the given initial guess, and the default
    tolerance. Return the solution to the corresponding Normal Equations.
    """
    #get the data
    data = np.loadtxt(filename)
    #get all the elements using the data
    b = data[:,0]
    A = np.append(np.ones(len(b))[:,np.newaxis],data[:,1:],axis =1)
    #find the solution
    x = A.T@b

    return conjugate_gradient(A.T@A, x, x0)[0]

# if __name__=="__main__":
#     print(prob4())

# Problem 5
class LogisticRegression1D:
    """Binary logistic regression classifier for one-dimensional data."""

    def fit(self, x, y, guess):
        """Choose the optimal beta values by minimizing the negative log
        likelihood function, given data and outcome labels.

        Parameters:
            x ((n,) ndarray): An array of n predictor variables.
            y ((n,) ndarray): An array of n outcome variables.
            guess (array): Initial guess for beta.
        """
        #get the nagative log likelihood function
        negative_log = lambda b: np.sum(np.log(1+np.exp(-(b[0]+b[1]*x))) + (1-y)*(b[0]+b[1]*x))
        #minize it with respect to B
        minize_b = opt.fmin_cg(negative_log, guess)
        #saving the values
        self.b0 = minize_b[0]
        self.b1 = minize_b[1]

    def predict(self, x):
        """Calculate the probability of an unlabeled predictor variable
        having an outcome of 1.

        Parameters:
            x (float): a predictor variable with an unknown label.
        """
        #calculating the predict delta
        return 1/(1+np.exp(-(self.b0+self.b1*x)))



# Problem 6
def prob6(filename="challenger.npy", guess=np.array([20., -1.])):
    """Return the probability of O-ring damage at 31 degrees Farenheit.
    Additionally, plot the logistic curve through the challenger data
    on the interval [30, 100].

    Parameters:
        filename (str): The file to perform logistic regression on.
                        Defaults to "challenger.npy"
        guess (array): The initial guess for beta.
                        Defaults to [20., -1.]
    """
    #get the data
    data = np.load(filename)
    #Analyze the data
    x = data[:,0]
    y = data[:,1]
    B0 = np.array([20,-1]).T
    #Instantiate logistic regression
    regression = LogisticRegression1D()
    regression.fit(x,y, B0)
    #getting delta value
    domain = np.linspace(30,100,np.shape(x)[0])
    delta = regression.predict(domain)
    #plotting
    plt.plot(domain,delta)
    plt.plot(x,y,'.',label="Damage")
    plt.plot(31,regression.predict(31), '.',label="Damage at Launch")
    plt.xlabel("Temperature")
    plt.ylabel("O-Ring Damage")
    plt.legend()
    plt.show()

    return regression.predict(31)

# if __name__=="__main__":
#     print(prob6())
