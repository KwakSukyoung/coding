# cvxpy_intro.py
"""Volume 2: Intro to CVXPY.
<Name>
<Class>
<Date>
"""

import cvxpy as cp
import numpy as np


def prob1():
    """Solve the following convex optimization problem:

    minimize        2x + y + 3z
    subject to      x  + 2y         <= 3
                         y   - 4z   <= 1
                    2x + 10y + 3z   >= 12
                    x               >= 0
                          y         >= 0
                                z   >= 0

    Returns (in order):
        The optimizer x (ndarray)
        The optimal value (float)
    """
    #set the initial values
    x = cp.Variable(3, nonneg = True)
    c = np.array([2,1,3])
    objective = cp.Minimize(c.T @ x)
    #Then we'll write the constraints
    A = np.array([1, 2, 0])
    G = np.array([0, 1, -4])
    L = np.array([2, 10, 3])
    P = np.eye(3)
    constraints = [A @ x <= 3, G @ x <= 1, L @ x >= 12, P @ x >= 0] #This must be a list
    #Assemble the problem and then solve it
    problem = cp.Problem(objective, constraints)
    sol  = problem.solve()

    return x.value, sol

# if __name__=="__main__":
#     print(prob1())

# Problem 2
def l1Min(A, b):
    """Calculate the solution to the optimization problem

        minimize    ||x||_1
        subject to  Ax = b

    Parameters:
        A ((m,n) ndarray)
        b ((m, ) ndarray)

    Returns:
        The optimizer x (ndarray)
        The optimal value (float)
    """
    #set the initial values
    x = cp.Variable(A.shape[1])
    c = cp.norm(x,1)
    objective = cp.Minimize(c)
    constraints = [A[i] @x == b[i] for i in range(A.shape[0])] #This must be a list
    #Assemble the problem and then solve it
    problem = cp.Problem(objective, constraints)
    sol  = problem.solve()

    return x.value, sol

# if __name__=="__main__":
#     A = np.array([[1,2,1,1],[0,3,-2,-1]])
#     b = np.array([7,4])
#
#     print(l1Min(A, b))


# Problem 3
def prob3():
    """Solve the transportation problem by converting the last equality constraint
    into inequality constraints.

    Returns (in order):
        The optimizer x (ndarray)
        The optimal value (float)
    """
    #set the initial values
    p = cp.Variable(6, nonneg = True)
    c = np.array([4,7,6,8,8,9])
    objective = cp.Minimize(c.T @ p)
    #Then we'll write the constraints
    A = np.array([1, 1, 0, 0, 0, 0])
    B = np.array([0, 0, 1, 1, 0, 0])
    C = np.array([0, 0, 0, 0, 1, 1])
    D = np.array([1, 0, 1, 0, 1, 0])
    E = np.array([0, 1, 0, 1, 0, 1])

    constraints = [A @ p == 7, B @ p == 2, C @ p == 4, D @ p == 5, E @ p == 8] #This must be a list
    #Assemble the problem and then solve it
    problem = cp.Problem(objective, constraints)
    sol  = problem.solve()

    return p.value, sol

# if __name__=="__main__":
#     print(prob3())


# Problem 4
def prob4():
    """Find the minimizer and minimum of

    g(x,y,z) = (3/2)x^2 + 2xy + xz + 2y^2 + 2yz + (3/2)z^2 + 3x + z

    Returns (in order):
        The optimizer x (ndarray)
        The optimal value (float)
    """
    #get Q,r,x
    Q = np.array([[3, 2, 1], [2, 4, 2], [1, 2, 3]])
    r = np.array([3, 0, 1])
    x = cp.Variable(3)
    #solve for it
    prob = cp.Problem(cp.Minimize(.5 * cp.quad_form(x, Q) + r.T @ x))

    sol  = prob.solve()

    return x.value, sol

# if __name__=="__main__":
#     print(prob4())

# Problem 5
def prob5(A, b):
    """Calculate the solution to the optimization problem
        minimize    ||Ax - b||_2
        subject to  ||x||_1 == 1
                    x >= 0
    Parameters:
        A ((m,n), ndarray)
        b ((m,), ndarray)

    Returns (in order):
        The optimizer x (ndarray)
        The optimal value (float)
    """
    #set the initial values
    x = cp.Variable(A.shape[1], nonneg = True)
    c = cp.norm(A@x-b,2)
    objective = cp.Minimize(c)
    constraints = [cp.sum(x) == 1] #This must be a list
    #Assemble the problem and then solve it
    problem = cp.Problem(objective, constraints)
    sol  = problem.solve()

    return x.value, sol

# if __name__=="__main__":
#     A = np.array([[1,2,1,1],[0,3,-2,-1]])
#     b = np.array([7,4])
#
#     print(prob5(A, b))


# Problem 6
def prob6():
    """Solve the college student food problem. Read the data in the file
    food.npy to create a convex optimization problem. The first column is
    the price, second is the number of servings, and the rest contain
    nutritional information. Use cvxpy to find the minimizer and primal
    objective.

    Returns (in order):
        The optimizer x (ndarray)
        The optimal value (float)
    """
    data = np.load('food.npy', allow_pickle=True)

    #set the initial values
    x = cp.Variable(18, nonneg = True)
    c = data[:,0]
    objective = cp.Minimize(c.T @ x)

    #constraints settings
    s = data[:, 1]
    c = data[:, 2]*s
    f = data[:, 3]*s
    ss = data[:, 4]*s
    cc = data[:, 5]*s
    ff = data[:, 6]*s
    pp = data[:, 7]*s
    P = np.eye(18)

    constraints = [c@x<=2000,f@x<=65,ss@x<=50,cc@x>=1000,ff@x>=25,pp@x>=46] #This must be a list
    #Assemble the problem and then solve it
    problem = cp.Problem(objective, constraints)
    sol  = problem.solve()

    return x.value, sol

# if __name__=="__main__":
#     print(prob6())












    # raise NotImplementedError("Problem 6 Incomplete")
