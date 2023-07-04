# condition_stability.py
"""Volume 1: Conditioning and Stability.
<Name>
<Class>
<Date>
"""

import numpy as np
import sympy as sy
from scipy import linalg
from numpy import linalg as la
from matplotlib import pyplot as plt
from sympy import *


# Problem 1
def matrix_cond(A):
    """Calculate the condition number of A with respect to the 2-norm."""
    #computing singular values
    sing_val = linalg.svd(A)[1]
    #get the min and max singular values
    top = max(sing_val)
    bottom = min(sing_val)
    #if the min == 0, returns infinity
    if bottom == 0:
        return np.inf

    return top/bottom

# if __name__=="__main__":
#     #general array
#     q = np.random.rand(3,3)
#     #orthonormal matrix
#     q = linalg.qr(q)[0]
#     #singular
#     q = np.array([[3,12],[2,8]])
#     # print(np.linalg.svd(q)[2])
#     #q = np.array([[1,0],[0,0]])
#     print(la.cond(q))
#     print(matrix_cond(q))

# Problem 2
def prob2():
    """Randomly perturb the coefficients of the Wilkinson polynomial by
    replacing each coefficient c_i with c_i*r_i, where r_i is drawn from a
    normal distribution centered at 1 with standard deviation 1e-10.
    Plot the roots of 100 such experiments in a single figure, along with the
    roots of the unperturbed polynomial w(x).

    Returns:
        (float) The average absolute condition number.
        (float) The average relative condition number.
    """
    w_roots = np.arange(1, 21)

    # Get the exact Wilkinson polynomial coefficients using SymPy.
    x, i = sy.symbols('x i')
    w = sy.poly_from_expr(sy.product(x-i, (i, 1, 20)))[0]
    w_coeffs = np.array(w.all_coeffs())

    ab = []
    re = []
    for i in range(99):
        r = np.random.normal(1, 10**-10, 21)
        new_co = w_coeffs *r

        new_r = np.sort(np.roots(np.poly1d(new_co)))
        w_roots = np.sort(w_roots)

        real_pts = (np.real(new_r))
        imag_pts = (np.imag(new_r))

        plt.scatter(real_pts, imag_pts, marker=",",color="k",s=.25)

        #absolute condition
        ab.append(la.norm(new_r -w_roots, np.inf)/la.norm(r, np.inf))
        #relative condition
        re.append(ab[-1] * la.norm(w_coeffs, np.inf) / la.norm(w_roots, np.inf))

    r = np.random.normal(1, 10**-10,21)
    new_co = w_coeffs *r
    new_r = np.sort(np.roots(np.poly1d(new_co)))
    w_roots = np.sort(w_roots)

    real_pts = (np.real(new_r))
    imag_pts = (np.imag(new_r))

    #absolute condition
    ab.append(la.norm(new_r -w_roots, np.inf)/la.norm(r, np.inf))
    #relative condition
    re.append(ab[-1] * la.norm(w_coeffs, np.inf) / la.norm(w_roots, np.inf))

    plt.scatter(real_pts, imag_pts, marker=',', color="k", label="Perturbed",s=.25)
    plt.scatter(w_roots.real, w_roots.imag, label= "original")

    plt.xlabel("Real Axis")
    plt.ylabel("Imaginary Axis")
    plt.legend()
    plt.show()
    return np.mean(ab), np.mean(re)

# if __name__=="__main__":
#     print(prob2())


# Helper function
def reorder_eigvals(orig_eigvals, pert_eigvals):
    """Reorder the perturbed eigenvalues to be as close to the original eigenvalues as possible.

    Parameters:
        orig_eigvals ((n,) ndarray) - The eigenvalues of the unperturbed matrix A
        pert_eigvals ((n,) ndarray) - The eigenvalues of the perturbed matrix A+H

    Returns:
        ((n,) ndarray) - the reordered eigenvalues of the perturbed matrix
    """
    n = len(pert_eigvals)
    sort_order = np.zeros(n).astype(int)
    dists = np.abs(orig_eigvals - pert_eigvals.reshape(-1,1))
    for _ in range(n):
        index = np.unravel_index(np.argmin(dists), dists.shape)
        sort_order[index[0]] = index[1]
        dists[index[0],:] = np.inf
        dists[:,index[1]] = np.inf
    return pert_eigvals[sort_order]

# Problem 3
def eig_cond(A):
    """Approximate the condition numbers of the eigenvalue problem at A.

    Parameters:
        A ((n,n) ndarray): A square matrix.

    Returns:
        (float) The absolute condition number of the eigenvalue problem at A.
        (float) The relative condition number of the eigenvalue problem at A.
    """

    reals = np.random.normal(0, 1e-10, A.shape)
    imags = np.random.normal(0, 1e-10, A.shape)
    H = reals + 1j*imags
    #Maybe that's why I got it wrong

    lamb = linalg.eigvals(A)
    lamb_til = linalg.eig(A+H)[0]
    #if it doens't work, don't use index

    lamb_til = reorder_eigvals(lamb, lamb_til)

    #absolute condition
    abs = la.norm(lamb-lamb_til)/la.norm(H,2)
    #relative condition
    rel = abs*(la.norm(A,2))/la.norm(lamb,2)

    return abs, rel

# if __name__ == "__main__":
#      A = np.arange(25).reshape((5,5))
#      print(eig_cond(A))

# Problem 4
def prob4(domain=[-100, 100, -100, 100], res=200):
    """Create a grid [x_min, x_max] x [y_min, y_max] with the given resolution. For each
    entry (x,y) in the grid, find the relative condition number of the
    eigenvalue problem, using the matrix   [[1, x], [y, 1]]  as the input.
    Use plt.pcolormesh() to plot the condition number over the entire grid.

    Parameters:
        domain ([x_min, x_max, y_min, y_max]):
        res (int): number of points along each edge of the grid.
    """
    #getting A and initialization
    A = lambda x,y : np.array([[1,x],[y,1]])
    rel_cond = []
    #get X and Y linspace
    X = np.linspace(-100,100,200)
    Y = np.linspace(-100,100,200)
    #loops to get rel_cond
    for i in X:
        for j in Y:
            rel_cond.append(eig_cond(A(i,j))[1])
    #reshaping rel_cond
    rel_cond = np.array(rel_cond).reshape((200,200))
    #get the meshgrid
    domain, codo = np.meshgrid(X,Y)
    #plotting
    plt.suptitle('rel cond and eigenvalues')
    plt.pcolormesh(domain,codo, rel_cond,cmap='gray_r',shading="nearest")
    plt.colorbar(ticks=np.linspace(0,100,9))
    plt.tight_layout()
    plt.show()



# Problem 5
def prob5(n):
    """Approximate the data from "stability_data.npy" on the interval [0,1]
    with a least squares polynomial of degree n. Solve the least squares
    problem using the normal equation and the QR decomposition, then compare
    the two solutions by plotting them together with the data. Return
    the mean squared error of both solutions, ||Ax-b||_2.

    Parameters:
        n (int): The degree of the polynomial to be used in the approximation.

    Returns:
        (float): The forward error using the normal equations.
        (float): The forward error using the QR decomposition.
    """

    #get the basic info from the file
    xk, yk = np.load("stability_data.npy").T
    A = np.vander(xk, n+1)
    b = yk
    #get the first result from the first approach
    y1 = la.inv(A.T@A)@A.T@b
    range1 = np.polyval(y1,xk)
    #get the result from othe second approach
    Q, R = linalg.qr(A, mode='economic')
    y2 = linalg.solve_triangular(R, Q.T@b)
    range2 = np.polyval(y2,xk)
    #plotting
    plt.scatter(xk, yk)
    plt.plot(xk, range1, label="first approach")
    plt.plot(xk, range2, label="second approach")
    plt.legend()
    plt.show()

    return la.norm((A@y1)-b), la.norm((A@y2)-b)
    #forward_error = (abs(inte1 -inte2))/(abs(inte1))

# if __name__ == "__main__":
#     # for i in range(20):
#     #     print(prob5(i))
#     prob5(10)

# Problem 6
def prob6():
    """For n = 5, 10, ..., 50, compute the integral I(n) using SymPy (the
    true values) and the subfactorial formula (may or may not be correct).
    Plot the relative forward error of the subfactorial formula for each
    value of n. Use a log scale for the y-axis.
    """
    #get the domain symbols
    x = sy.symbols('x')
    domain = [5*n for n in range(1,11)]
    #get the first integrate
    I1 = lambda n: (x**int(n))*(sy.exp(x-1))
    #I2 = lambda n: ((-1)**n)*(sy.subfactorial(n)-(sy.factorial(n)/np.e))
    inte1 = np.array([float(sy.integrate(I1(i),(x,0,1))) for i in domain])
    #get the second integrate
    I2 = lambda n: ((-1)**int(n))*(sy.subfactorial(int(n))-(sy.factorial(int(n))/np.e))
    inte2 = np.array([I2(n) for n in domain])
    #get the forward_error
    forward_error = (abs(inte1 -inte2))/(abs(inte1))
    #forward_error.append((abs(f_x - f_x_t))/abs(f_x))
    #-10,46
    #plotting
    plt.plot(domain, forward_error, label="forward_error")
    plt.yscale("log")
    plt.legend()
    plt.show()


    #pau
