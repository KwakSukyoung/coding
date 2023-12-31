# montecarlo_integration.py
"""Volume 1: Monte Carlo Integration.
<Name>
<Class>
<Date>
"""
import numpy as np
from scipy import linalg as la
import matplotlib.pyplot as plt
from scipy import stats


# Problem 1
def ball_volume(n, N=10000):
    """Estimate the volume of the n-dimensional unit ball.

    Parameters:
        n (int): The dimension of the ball. n=2 corresponds to the unit circle,
            n=3 corresponds to the unit sphere, and so on.
        N (int): The number of random points to sample.

    Returns:
        (float): An estimate for the volume of the n-dimensional unit ball.
    """

    # Get N random points in the 2-D domain [-1,1]x[-1,1].
    points = np.random.uniform(-1, 1, (n,N))
    # Determine how many points are within the circle.
    lengths = la.norm(points, axis=0)
    num_within = np.count_nonzero(lengths < 1)
    # Estimate the circle's area.

    return (2**n) * (num_within / N)

# if __name__ =="__main__":
#     print(ball_volume(3))
#     print(ball_volume(5))
#     print(ball_volume(7))
#     print(ball_volume(9))
#     print(ball_volume(2,500))
#     print(ball_volume(2,2000))
#     print(ball_volume(2,8000))


# Problem 2
def mc_integrate1d(f, a, b, N=10000):
    """Approximate the integral of f on the interval [a,b].

    Parameters:
        f (function): the function to integrate. Accepts and returns scalars.
        a (float): the lower bound of interval of integration.
        b (float): the lower bound of interval of integration.
        N (int): The number of random points to sample.

    Returns:
        (float): An approximation of the integral of f over [a,b].

    Example:
        >>> f = lambda x: x**2
        >>> mc_integrate1d(f, -4, 2)    # Integrate from -4 to 2.
        23.734810301138324              # The true value is 24.
    """
    #sample N points over a b
    points = np.random.uniform(a, b, N)
    #plug the random points into the function
    y_val = f(points)
    #get the average out of it
    avera = np.sum(y_val)
    #The monte-function
    return (b-a)*(avera)/N

# if __name__ =="__main__":
#     f1 = lambda x: x**2
#     f2 = lambda x: np.sin(x)
#     f3 = lambda x: 1/x
#     f4 = lambda x: abs(np.sin(10*x)*np.cos(10*x) + np.sqrt(x)*np.sin(3*x))
#
#     print(mc_integrate1d(f1,-4,2))
#     print(mc_integrate1d(f2,-2*np.pi,2*np.pi))
#     print(mc_integrate1d(f3,1,10))
#     print(mc_integrate1d(f4,1,5))


# Problem 3
def mc_integrate(f, mins, maxs, N=10000):
    """Approximate the integral of f over the box defined by mins and maxs.

    Parameters:
        f (function): The function to integrate. Accepts and returns
            1-D NumPy arrays of length n.
        mins (list): the lower bounds of integration.
        maxs (list): the upper bounds of integration.
        N (int): The number of random points to sample.

    Returns:
        (float): An approximation of the integral of f over the domain.

    Example:
        # Define f(x,y) = 3x - 4y + y^2. Inputs are grouped into an array.
        >>> f = lambda x: 3*x[0] - 4*x[1] + x[1]**2

        # Integrate over the box [1,3]x[-2,1].
        >>> mc_integrate(f, [1, -2], [3, 1])
        53.562651072181225              # The true value is 54.
    """
    #get V
    V = np.product(np.array(maxs)-np.array(mins))
    #get the dimension
    dim = len(maxs)
    #
    scaled = np.random.random((N, dim)) * (np.array(maxs)-np.array(mins)) + mins
    f_x = np.sum([f(s) for s in scaled])

    return V*f_x/N

# if __name__ == "__main__":
#     f = lambda x: 3*x[0] - 4*x[1] + x[1]**2
#     mins = [1,-2]
#     maxs = [3,1]
#     print(mc_integrate(f,mins,maxs))
# Problem 4
def prob4():
    """Let n=4 and Omega = [-3/2,3/4]x[0,1]x[0,1/2]x[0,1].
    - Define the joint distribution f of n standard normal random variables.
    - Use SciPy to integrate f over Omega.
    - Get 20 integer values of N that are roughly logarithmically spaced from
        10**1 to 10**5. For each value of N, use mc_integrate() to compute
        estimates of the integral of f over Omega with N samples. Compute the
        relative error of estimate.
    - Plot the relative error against the sample size N on a log-log scale.
        Also plot the line 1 / sqrt(N) for comparison.
    """
    #get N numbers
    N = np.logspace(1,5,20).astype(int)

    #get n
    n = 4
    #set the boundary
    mins = np.array([-3/2,0,0,0])
    maxs = np.array([3/4,1,1/2,1])
    #get f
    f = lambda x: np.exp(-(x@x)/2)/(2*np.pi)**(n/2)
    #get F-tilda number
    F_til = [mc_integrate(f, mins, maxs, k) for k in N]
    #get F
    means, cov = np.zeros(n), np.eye(n)
    F = stats.mvn.mvnun(mins, maxs, means, cov)[0]
    #relative condition
    rel = [abs(F - F_)/abs(F) for F_ in F_til]
    #plot
    plt.plot(N, rel, label="relative error")
    plt.plot(N, 1/np.sqrt(N), label="1/sqrt(N)")
    plt.yscale('log')
    plt.xscale('log')
    plt.legend()
    plt.show()
