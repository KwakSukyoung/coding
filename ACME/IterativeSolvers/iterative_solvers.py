# iterative_solvers.py
"""Volume 1: Iterative Solvers.
<Name>
<Class>
<Date>
"""

import numpy as np
from numpy import linalg as LA
import matplotlib.pyplot as plt
from scipy import sparse


# Helper function
def diag_dom(n, num_entries=None, as_sparse=False):
    """Generate a strictly diagonally dominant (n, n) matrix.
    Parameters:
        n (int): The dimension of the system.
        num_entries (int): The number of nonzero values.
            Defaults to n^(3/2)-n.
        as_sparse: If True, an equivalent sparse CSR matrix is returned.
    Returns:
        A ((n,n) ndarray): A (n, n) strictly diagonally dominant matrix.
"""
    if num_entries is None:
        num_entries = int(n**1.5) - n
    A = sparse.dok_matrix((n,n))
    rows = np.random.choice(n, size=num_entries)
    cols = np.random.choice(n, size=num_entries)
    data = np.random.randint(-4, 4, size=num_entries)
    for i in range(num_entries):
        A[rows[i], cols[i]] = data[i]
    B = A.tocsr()          # convert to row format for the next step
    for i in range(n):
        A[i,i] = abs(B[i]).sum() + 1
    return A.tocsr() if as_sparse else A.toarray()

# Problems 1 and 2
def jacobi(A, b, tol=1e-8, maxiter=100, plot = False):
    """Calculate the solution to the system Ax = b via the Jacobi Method.

    Parameters:
        A ((n,n) ndarray): A square matrix.
        b ((n ,) ndarray): A vector of length n.
        tol (float): The convergence tolerance.
        maxiter (int): The maximum number of iterations to perform.

    Returns:
        ((n,) ndarray): The solution to system Ax = b.
    """
    #get the shape
    n = A.shape[0]
    #initialization
    x_0 = np.zeros((n,))
    D_1 = np.zeros((n,n))
    lis = []
    #set D
    for i in range(n):
        D_1[i][i] = 1/A[i][i]
    #loop thourgh 0 - maxiter - 1
    for i in range(maxiter):
        x_1 = x_0 + D_1@(b - A@x_0)
        lis.append(LA.norm(A@x_1-b,ord=np.inf))
        #if it converges
        if (LA.norm(x_0 - x_1, ord=np.inf) < tol):
            break
        #renew x0
        x_0 = x_1
    #plotting
    if plot == True:
        plt.semilogy(np.arange(len(lis)),lis)
        plt.show()
    return x_1

# if __name__=="__main__":
#     # A = diag_dom(5)
#     # b = np.random.random((5,))
#
#     A = np.array([[2, 0, 1],
#                   [-1, 3, 2],
#                   [0, 1, 3]])
#
#     b = np.array([3, 3, -1])
#
#     print(jacobi(A,b,plot=True))



# Problem 3
def gauss_seidel(A, b, tol=1e-8, maxiter=100, plot=False):
    """Calculate the solution to the system Ax = b via the Gauss-Seidel Method.

    Parameters:
        A ((n, n) ndarray): A square matrix.
        b ((n, ) ndarray): A vector of length n.
        tol (float): The convergence tolerance.
        maxiter (int): The maximum number of iterations to perform.
        plot (bool): If true, plot the convergence rate of the algorithm.

    Returns:
        x ((n,) ndarray): The solution to system Ax = b.
    """
    #set variables
    n = A.shape[0]
    x_0 = np.zeros((n,))
    x_1 = np.zeros((n,))
    lis = []
    #loop through 0 - maxiter - 1
    for k in range(maxiter):
        x_1 = x_0.copy()
        for i in range(n):
            x_1[i] = x_1[i] + (b[i] - A[i].T@x_1)/A[i][i]
        #save the values
        lis.append(LA.norm(A@x_1-b,ord=np.inf))
        #if it converges
        if (LA.norm(x_0 - x_1, ord=np.inf) < tol):
            break
        x_0 = x_1
    #plotting
    if plot == True:
        plt.semilogy(np.arange(len(lis)),lis)
        plt.show()

    return x_1

# if __name__=="__main__":
#     A = diag_dom(99)
#     b = np.random.random((99,))
#
#     print(jacobi(A,b,plot=True))
#     print(gauss_seidel(A,b,plot=True))
#     print(np.allclose(jacobi(A,b,plot=True),gauss_seidel(A,b,plot=True)))

# Problem 4
def gauss_seidel_sparse(A, b, tol=1e-8, maxiter=100):
    """Calculate the solution to the sparse system Ax = b via the Gauss-Seidel
    Method.

    Parameters:
        A ((n, n) csr_matrix): A (n, n) sparse CSR matrix.
        b ((n, ) ndarray): A vector of length n.
        tol (float): The convergence tolerance.
        maxiter (int): the maximum number of iterations to perform.

    Returns:
        x ((n,) ndarray): The solution to system Ax = b.
    """
    if not isinstance(A, sparse.csr_matrix):
        A = sparse.csr_matrix(A)

    #set variables
    n = A.shape[0]
    D_1 = A.diagonal()
    x_0 = np.zeros(n)
    lis = []

    #loop through 0 - maxiter - 1
    for k in range(maxiter):
        x_1 = x_0.copy()
        for i in range(n):
            #lab spec
            rowstart = A.indptr[i]
            rowend = A.indptr[i+1]
            Aix = A.data[rowstart:rowend] @ x_1[A.indices[rowstart:rowend]]
            x_1[i] += (b[i] - Aix)/ D_1[i]
            #save the values
            lis.append(LA.norm(A@x_1-b,ord=np.inf))
            #if it converges
        if (LA.norm(x_0 - x_1, ord=np.inf) < tol):
            return x_1
        x_0 = x_1
    #plotting
    if plot == True:
        plt.semilogy(np.arange(len(lis)),lis)
        plt.show()

    return x_1

# if __name__=="__main__":
#     A = diag_dom(500, as_sparse=True)
#     b = np.random.random(500)
#     print(gauss_seidel_sparse(A, b))
#     print(np.allclose(A@gauss_seidel_sparse(A, b),b))

# Problem 5
def sor(A, b, omega, tol=1e-8, maxiter=100):
    """Calculate the solution to the system Ax = b via Successive Over-
    Relaxation.

    Parameters:
        A ((n, n) csr_matrix): A (n, n) sparse matrix.
        b ((n, ) Numpy Array): A vector of length n.
        omega (float in [0,1]): The relaxation factor.
        tol (float): The convergence tolerance.
        maxiter (int): The maximum number of iterations to perform.

    Returns:
        ((n,) ndarray): The solution to system Ax = b.
        (bool): Whether or not Newton's method converged.
        (int): The number of iterations computed.
    """

    if not isinstance(A, sparse.csr_matrix):
        A = sparse.csr_matrix(A)

    #set variables
    n = A.shape[0]
    D_1 = A.diagonal()
    x_0 = np.zeros(n)

    #loop through 0 - maxiter - 1
    for k in range(maxiter):
        x_1 = x_0.copy()
        for i in range(n):
            #lab spec
            rowstart = A.indptr[i]
            rowend = A.indptr[i+1]
            Aix = A.data[rowstart:rowend] @ x_1[A.indices[rowstart:rowend]]
            x_1[i] += omega*(b[i] - Aix)/ D_1[i]
            #if it converges
        if (LA.norm(x_0 - x_1, ord=np.inf) < tol):
            return x_1, True,k
        x_0 = x_1

    return x_1, False, maxiter

# if __name__=="__main__":
#     A = diag_dom(500, as_sparse=True)
#     b = np.random.random(500)
#     print(sor(A, b, 1))
#     print(np.allclose(A@sor(A, b, 1)[0],b))

# Problem 6
def hot_plate(n, omega, tol=1e-8, maxiter=100, plot=False):
    """Generate the system Au = b and then solve it using sor().
    If show is True, visualize the solution with a heatmap.

    Parameters:
        n (int): Determines the size of A and b.
            A is (n^2, n^2) and b is one-dimensional with n^2 entries.
        omega (float in [0,1]): The relaxation factor.
        tol (float): The iteration tolerance.
        maxiter (int): The maximum number of iterations.
        plot (bool): Whether or not to visualize the solution.

    Returns:
        ((n^2,) ndarray): The 1-D solution vector u of the system Au = b.
        (bool): Whether or not Newton's method converged.
        (int): The number of computed iterations in SOR.
    """
    #seeting A
    offsets = [-1,0,1]
    B = sparse.diags([1,-4,1], offsets, shape=(n,n))
    A = sparse.block_diag([B] * n)
    A.setdiag(1, -n)
    A.setdiag(1, n)

    #setting b
    x = [-100 if i == 0 or i == n-1 else 0 for i in range(n)]
    b = np.tile(x, n)

    #finding u using sor
    u = sor(A, b, omega, tol, maxiter)

    #plotting
    if plot == True:
        plt.pcolormesh(u[0].reshape(n, n), cmap='coolwarm')
        plt.title("the visualziation of the solution")
        plt.show()

    return u

# if __name__ == "__main__":
#     print(hot_plate(50,1,plot = True))


# Problem 7
def prob7():
    """Run hot_plate() with omega = 1, 1.05, 1.1, ..., 1.9, 1.95, tol=1e-2,
    and maxiter = 1000 with A and b generated with n=20. Plot the iterations
    computed as a function of omega.
    """
    #initialization
    n = 20
    w = [1 + (.05)*i for i in range(n)]
    lis = []
    #loop through 0 -- n-1
    for i in w:
        lis.append(hot_plate(20,i,tol=1e-2, maxiter=1000, plot = True)[2])
    #plotting
    plt.plot(w, lis)
    plt.title("omega vs number of iterations")
    plt.show()

    return w[lis.index(min(lis))]

# if __name__=="__main__":
#     print(prob7())
