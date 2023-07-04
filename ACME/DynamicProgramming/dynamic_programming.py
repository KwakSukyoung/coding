# dynamic_programming.py
"""Volume 2: Dynamic Programming.
<Name>
<Class>
<Date>
"""

import numpy as np
import matplotlib.pyplot as plt


def calc_stopping(N):
    """Calculate the optimal stopping time and expected value for the
    marriage problem.

    Parameters:
        N (int): The number of candidates.

    Returns:
        (float): The maximum expected value of choosing the best candidate.
        (int): The index of the maximum expected value.
    """
    #initialization
    t_0 = 0
    all = []
    #loop through N - 1:
    for t in range(N, 0,-1):
        all.append(t_0)
        t_1 = max((t-1)*t_0/t +1/N, t_0)
        t_0 = t_1

    return max(all), N-all.index(max(all))

# if __name__ == "__main__":
#     print(calc_stopping(4))

# Problem 2
def graph_stopping_times(M):
    """Graph the optimal stopping percentage of candidates to interview and
    the maximum probability against M.

    Parameters:
        M (int): The maximum number of candidates.

    Returns:
        (float): The optimal stopping percent of candidates for M.
    """
    #initialization
    domain = np.linspace(3,M,M-3)
    stopping_perc = []
    max_probability = []
    #loop through 3,4,,,M
    for N in range(3,M+1):
        #get the values
        stopping_perc.append(calc_stopping(N)[1]/N)
        max_probability.append(calc_stopping(N)[0])
    #plotting
    plt.plot(stopping_perc, label = "stopping percentage of candidates (t0/N) to interview")
    plt.plot(max_probability, label = "the maximum probability V (t0 ) against N .")
    plt.xlabel("N values")
    plt.legend()
    plt.show()

    return calc_stopping(M)[1]/M


# if __name__ == "__main__":
#     print(graph_stopping_times(1000))


# Problem 3
def get_consumption(N, u=lambda x: np.sqrt(x)):
    """Create the consumption matrix for the given parameters.

    Parameters:
        N (int): Number of pieces given, where each piece of cake is the
            same size.
        u (function): Utility function.

    Returns:
        C ((N+1,N+1) ndarray): The consumption matrix.
    """
    #initialization
    W = 1
    w = np.array([i/N for i in range(N+1)]).T
    n = len(w)
    #returning the consum_matrix by list comprehension
    return np.array([[u(w[i-j]) if j <i else 0 for j in range(n)] for i in range(n)])

# if __name__ == "__main__":
#     print(get_consumption(4))


# Problems 4-6
def eat_cake(T, N, B, u=lambda x: np.sqrt(x)):
    """Create the value and policy matrices for the given parameters.

    Parameters:
        T (int): Time at which to end (T+1 intervals).
        N (int): Number of pieces given, where each piece of cake is the
            same size.
        B (float): Discount factor, where 0 < B < 1.
        u (function): Utility function.

    Returns:
        A ((N+1,T+1) ndarray): The matrix where the (ij)th entry is the
            value of having w_i cake at time j.
        P ((N+1,T+1) ndarray): The matrix where the (ij)th entry is the
            number of pieces to consume given i pieces at time j.
    """
    #prob3
    #initialization
    A = np.zeros((N+1,T+1))
    P = np.zeros((N+1,T+1))
    w = np.array([i/N for i in range(N+1)])
    #modify A and P
    A[:,-1] = u(w)
    P[:,-1] = w

    #prob5
    #loop through T-1...0:
    C = get_consumption(N,u)
    for t in range(T-1,-1,-1):
        #get CV matrix
        CV = [[C[i,j] + B*A[j,t+1] for j in range(N+1)] for i in range(N+1)]
        #get CV_t
        CV_t = np.tril(CV,0)
        #modify A based on CV_t
        for i in range(N+1):
            A[i,t] = max(CV_t[i])
    #prob6
            index = np.argmax([CV_t[i]]) #the index used to built P[i,t]
            P[i,t] = w[i] - w[index]

    return A, P

# if __name__=="__main__":
#     print(eat_cake(3,4,.9))


# Problem 7
def find_policy(T, N, B, u=np.sqrt):
    """Find the most optimal route to take assuming that we start with all of
    the pieces. Show a graph of the optimal policy using graph_policy().

    Parameters:
        T (int): Time at which to end (T+1 intervals).
        N (int): Number of pieces given, where each piece of cake is the
            same size.
        B (float): Discount factor, where 0 < B < 1.
        u (function): Utility function.

    Returns:
        ((T,) ndarray): The matrix describing the optimal percentage to
            consume at each time.
    """
    #get A and P from the previous function
    A,P = eat_cake(T,N,B,u)
    #get the first optimal policy
    opt = [P[:,0][-1]]

    #loop through 1,...T:
    for i in range(1,T+1):
        #get the left over
        N -= int(np.round(opt[-1]*N))
        opt.append(P[N,i])

    return opt

# if __name__ == "__main__":
#     T,N,B = 3,4,0.9
#     print(find_policy(T,N,B))
