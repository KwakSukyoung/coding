# qr_decomposition.py
"""Volume 1: The QR Decomposition.
<Name> Sukyoung Kwak
<Class> 345 Section 2
<Date> October 24th
"""
import numpy as np
import scipy as sp
from scipy import linalg as la

# Problem 1
def qr_gram_schmidt(A):
    """Compute the reduced QR decomposition of A via Modified Gram-Schmidt.

    Parameters:
        A ((m,n) ndarray): A matrix of rank n.

    Returns:
        Q ((m,n) ndarray): An orthonormal matrix.
        R ((n,n) ndarray): An upper triangular matrix.
    """
    #Modified Gram-Schmidt(A)
    m = np.shape(A)[0] #store the row dimension of A
    n = np.shape(A)[1] #store the column dimension of A
    Q = np.copy(A) #make a copy of A with np.copy()
    R = np.zeros((n,n)) #An nxn array of all zeros

    for i in range(n):
        R[i,i] = la.norm(Q[:,i])
        Q[:,i] = np.divide(Q[:,i],R[i,i]) #Normalize the ith column of Q
        for j in range(i+1, n):
            R[i,j] = (np.transpose(Q[:,j]))@Q[:,i]
            Q[:,j] = Q[:,j] - np.multiply((R[i,j]),(Q[:,i]))#Orthogonalize the jth column of Q

    return Q,R


# Problem 2
def abs_det(A):
    """Use the QR decomposition to efficiently compute the absolute value of
    the determinant of A.

    Parameters:
        A ((n,n) ndarray): A square matrix.

    Returns:
        (float) the absolute value of the determinant of A.
    """
    #multiple of diag rows of R
    return np.abs(np.prod(np.diag(qr_gram_schmidt(A)[1])))

# Problem 3
def solve(A, b):
    """Use the QR decomposition to efficiently solve the system Ax = b.

    Parameters:
        A ((n,n) ndarray): An invertible matrix.
        b ((n, ) ndarray): A vector of length n.

    Returns:
        x ((n, ) ndarray): The solution to the system Ax = b.
    """
    #get Q, R
    Q, R = qr_gram_schmidt(A)
    #get y
    y = np.dot(Q.T,b)

    #get x
    #Rx = y
    x = np.zeros(len(b))
    row_sum = 0
    #x[0] - devided by the diagonal number
    x[len(b)-1] = y[len(b)-1]/R[len(b)-1][len(b)-1]
    #loops the top triangle

    for i in range(len(b)-1, -1, -1):
        for j in range(i+1, len(b)):
            #sums of top triangle excluding the diagonal
            row_sum += R[i][j]*x[j]
        x[i] = (y[i] -row_sum)/R[i][i]
        row_sum = 0

    return x

# Problem 4
def qr_householder(A):
    """Compute the full QR decomposition of A via Householder reflections.

    Parameters:
        A ((m,n) ndarray): A matrix of rank n.

    Returns:
        Q ((m,m) ndarray): An orthonormal matrix.
        R ((m,n) ndarray): An upper triangular matrix.
    """
    #Householder(A) procedure
    #store the shape of A
    m = np.shape(A)[0]
    n = np.shape(A)[1]
    R = np.copy(A)
    Q = np.identity(m) #The mxm identity matrix

    for k in range (n):
        u = np.copy(R[k:,k])

        sign = lambda x: 1 if x >= 0 else -1
        u[0] += (sign(u[0]))*(la.norm(u)) #u0 is the first entry of u
        u = u/(la.norm(u)) #normalize u

        R[k:,k:] -= 2*(np.outer(u,(np.dot((np.transpose(u)),R[k:,k:])))) #apply the reflection to R
        Q[k:,:] -= 2*(np.outer(u,(np.dot((np.transpose(u)),Q[k:,:])))) #apply the reflection to Q

    return Q.T, R

if __name__=="__main__":
    A = np.random.random((5, 3))
    Q,R = la.qr(A)
    print(np.allclose(Q @ R, A))
    Q,R = qr_householder(A)
    print(np.allclose(Q @ R, A))

# Problem 5
def hessenberg(A):
    """Compute the Hessenberg form H of A, along with the orthonormal matrix Q
    such that A = QHQ^T.

    Parameters:
        A ((n,n) ndarray): An invertible matrix.

    Returns:
        H ((n,n) ndarray): The upper Hessenberg form of A.
        Q ((n,n) ndarray): An orthonormal matrix.
    """
    #procedure Hessenberg(A)
    m = np.shape(A)[0]#row number of A
    n = np.shape(A)[1]#column number of A
    H = np.copy(A)#copy(A)
    Q = np.identity(m)#identity m dimension

    for k in range(n-2):#loop for 0 ~ n-3
        u = np.copy(H[k+1:,k])
        sign = lambda x: 1 if x >= 0 else -1
        u[0] += sign(u[0])*la.norm(u)
        u = u/la.norm(u)
        H[k+1:,k:] -= 2*(np.outer(u, np.dot(u.T, H[k+1:,k:]))) #Apply Q_k to H
        H[:,k+1:] -= 2*(np.outer(np.dot(H[:,k+1:],u),u.T)) #Apply Q_k^T to H
        Q[k+1:,:] -= 2*(np.outer(u, np.dot(u.T, Q[k+1:,:]))) #Apply Q_k to Q
    return H, Q.T
