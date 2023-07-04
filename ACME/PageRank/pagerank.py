# solutions.py
"""Volume 1: The Page Rank Algorithm.
<Name>
<Class>
<Date>
"""

import numpy as np
from scipy import linalg as la
from numpy import linalg as LA
import networkx as nx
from itertools import combinations

# Problems 1-2
class DiGraph:
    """A class for representing directed graphs via their adjacency matrices.

    Attributes:
        (fill this out after completing DiGraph.__init__().)
    """
    # Problem 1
    def __init__(self, A, labels=None):
        """Modify A so that there are no sinks in the corresponding graph,
        then calculate Ahat. Save Ahat and the labels as attributes.

        Parameters:
            A ((n,n) ndarray): the adjacency matrix of a directed graph.
                A[i,j] is the weight of the edge from node j to node i.
            labels (list(str)): labels for the n nodes in the graph.
                If None, defaults to [0, 1, ..., n-1].
        """
        #setting the attributes
        self.A = A
        self.til = A
        self.labels = labels
        self.n = len(self.A)
        #if labels == None
        if labels is None:
            self.labels = [str(i) for i in range(self.n)]
        #if we don't have the same amount of labels
        if (self.n != len(self.labels)):
            raise ValueError("the number of labels is not equal to the number of nodes in the graph.")
        #making A-tilda
        for i in range(self.n):
            if sum(self.til[:,i]) == 0:
                self.til[:,i] += 1
        # print(self.til)
        #making A-hat
        self.hat = self.til / np.sum(self.til,axis=0)

# if __name__ == "__main__":
#     A = np.array([[0,0,0,0],[1,0,1,0],[1,0,0,1],[1,0,1,0]])
#     Dig = DiGraph(A)


    # Problem 2
    def linsolve(self, epsilon=0.85):
        """Compute the PageRank vector using the linear system method.

        Parameters:
            epsilon (float): the damping factor, between 0 and 1.

        Returns:
            dict(str -> float): A dictionary mapping labels to PageRank values.
        """
        #A = I -eA, B = 2-e*1/n
        p = la.solve((np.identity(self.n)-epsilon*(self.hat)),((1-epsilon)/self.n)*np.ones((self.n)))
        #returning in the dictionary form
        Dict = {}
        for i in range(self.n):
            Dict[self.labels[i]] = p[i]
        return Dict

# if __name__ == "__main__":
#     A = np.array([[0,0,0,0],[1,0,1,0],[1,0,0,1],[1,0,1,0]])
#     Dig = DiGraph(A)
#     print(Dig.linsolve())

    # Problem 2
    def eigensolve(self, epsilon=0.85):
        """Compute the PageRank vector using the eigenvalue method.
        Normalize the resulting eigenvector so its entries sum to 1.

        Parameters:
            epsilon (float): the damping factor, between 0 and 1.

        Return:
            dict(str -> float): A dictionary mapping labels to PageRank values.
        """
        #setting E and V and eigen values
        E = np.ones((self.n))
        B = epsilon*self.hat + ((1-epsilon)/self.n)*E
        val = LA.eig(B)

        #getting the eigenvectors when the eigenvalue is 1
        for i in range(len(val[0])):
            if np.allclose(val[0][i], 1):
                p = (val[1][:,i])
                p_n = LA.norm(p,1)
                break
        #returning the value with the normalization
        Dict = {}
        for i in range(self.n):
            Dict[self.labels[i]] = (p/p_n)[i]

        return Dict

# if __name__ == "__main__":
#     A = np.array([[0,0,0,0],[1,0,1,0],[1,0,0,1],[1,0,1,0]])
#     Dig = DiGraph(A, ['a','b','c','d'])
#     print(Dig.eigensolve())

    # Problem 2
    def itersolve(self, epsilon=0.85, maxiter=100, tol=1e-12):
        """Compute the PageRank vector using the iterative method.

        Parameters:
            epsilon (float): the damping factor, between 0 and 1.
            maxiter (int): the maximum number of iterations to compute.
            tol (float): the convergence tolerance.

        Return:
            dict(str -> float): A dictionary mapping labels to PageRank values.
        """
        #setting the dictionary
        Dict = {}
        #the initilization
        p_0 = np.array([1/self.n for i in range(self.n)]).T
        #loop through 0 - maxiter -1
        for i in range(maxiter):
            #get the new value
            p_1 = epsilon*self.hat@p_0 + ((1-epsilon)/self.n)*np.ones((self.n))
            #if it's good enough
            if (LA.norm(p_1-p_0,1) < tol):
                for i in range(self.n):
                    Dict[self.labels[i]] = p_1[i]
                return Dict
            #changing the values
            p_0 = p_1
        #if it went through the maxiter loops
        for i in range(self.n):
            Dict[self.labels[i]] = p_1[i]
        return Dict

# if __name__ == "__main__":
#     A = np.array([[0,0,0,0],[1,0,1,0],[1,0,0,1],[1,0,1,0]])
#     Dig = DiGraph(A, ['a','b','c','d'])
#     print(Dig.itersolve())


# Problem 3
def get_ranks(d):
    """Construct a sorted list of labels based on the PageRank vector.

    Parameters:
        d (dict(str -> float)): a dictionary mapping labels to PageRank values.

    Returns:
        (list) the keys of d, sorted by PageRank value from greatest to least.
    """
    lis = [(-val,key) for key,val in zip(d.keys(), d.values())]
    lis.sort()
    return [lis[i][1] for i in range(len(lis))]

# if __name__ == "__main__":
#     A = np.array([[0,0,0,0],[1,0,1,0],[1,0,0,1],[1,0,1,0]])
#     Dig = DiGraph(A, ['a','b','c','d'])
#     d = Dig.itersolve()
#     print(get_ranks(d))

# Problem 4
def rank_websites(filename="web_stanford.txt", epsilon=0.85):
    """Read the specified file and construct a graph where node j points to
    node i if webpage j has a hyperlink to webpage i. Use the DiGraph class
    and its itersolve() method to compute the PageRank values of the webpages,
    then rank them with get_ranks(). If two webpages have the same rank,
    resolve ties by listing the webpage with the larger ID number first.

    Each line of the file has the format
        a/b/c/d/e/f...
    meaning the webpage with ID 'a' has hyperlinks to the webpages with IDs
    'b', 'c', 'd', and so on.

    Parameters:
        filename (str): the file to read from.
        epsilon (float): the damping factor, between 0 and 1.

    Returns:
        (list(str)): The ranked list of webpage IDs.
    """
    #get the data sets
    with open(filename, 'r') as infile:
        data = [n for line in infile.readlines() for n in line.strip().split("/")]
    #get the lines
    with open(filename, 'r') as infile:
        lines = [[n for n in line.strip().split("/")] for line in infile.readlines()]
    #get the unique labels
    labels = sorted(set(data))
    n = len(labels)
    #initialize A
    A = np.zeros(shape=(n,n))

    #making the adjacency matrix
    Jaque_label = {labels[i]: i for i in range(n)}
    for line in lines:
        source = line[0]
        for target in line[1:]:
            A[Jaque_label[target]][Jaque_label[source]] += 1

    #using the class function
    Jaque = DiGraph(A, labels)
    rank_val = Jaque.itersolve(epsilon=epsilon)
    rank_3 = get_ranks(rank_val)

    return rank_3

# if __name__ == "__main__":
#     print(rank_websites(epsilon=.22)[0:20])# '106064', '332' .47,    20283  .64

# Problem 5
def rank_ncaa_teams(filename, epsilon=0.85):
    """Read the specified file and construct a graph where node j points to
    node i with weight w if team j was defeated by team i in w games. Use the
    DiGraph class and its itersolve() method to compute the PageRank values of
    the teams, then rank them with get_ranks().

    Each line of the file has the format
        A,B
    meaning team A defeated team B.

    Parameters:
        filename (str): the name of the data file to read.
        epsilon (float): the damping factor, between 0 and 1.

    Returns:
        (list(str)): The ranked list of team names.
    """
    #get the data sets
    with open(filename, 'r') as infile:
        data = [n for line in infile.readlines()
                        for n in line.strip().split(",")]
    #get the lines
    with open(filename, 'r') as infile:
        lines = [[n for n in line.strip().split(",")]
                        for line in infile.readlines()]
    #cut unnecesarily part
    data = data[2:]
    lines = lines[1:]

    #get the unique labels
    labels = sorted(set(data))
    n = len(labels)
    #initialize A
    A = np.zeros(shape=(n,n))

    #making the adjacency matrix
    Jaque_label = {labels[i]: i for i in range(n)}
    for line in lines:
        source = line[1]
        target = line[0]
        A[Jaque_label[target]][Jaque_label[source]] += 1

    #using the class function
    Jaque = DiGraph(A,labels)
    rank_val = Jaque.itersolve(epsilon)
    rank_3 = get_ranks(rank_val)

    return rank_3

# if __name__=="__main__":
#     print(rank_ncaa_teams("ncaa2010.csv",epsilon = .31)[0:20])

# Problem 6
def rank_actors(filename="top250movies.txt", epsilon=0.85):
    """Read the specified file and construct a graph where node a points to
    node b with weight w if actor a and actor b were in w movies together but
    actor b was listed first. Use NetworkX to compute the PageRank values of
    the actors, then rank them with get_ranks().

    Each line of the file has the format
        title/actor1/actor2/actor3/...
    meaning actor2 and actor3 should each have an edge pointing to actor1,
    and actor3 should have an edge pointing to actor2.
    """

    #get the data sets
    with open(filename, 'r',encoding="utf-8") as infile:
        movie_data = [n for line in infile.readlines()
                        for n in line.strip().split("\n")]
    #create the Matrix
    DG = nx.DiGraph()
    #loop through each movie
    for movie in movie_data:
        #get only each actors
        movie_actors = movie.strip().split("/")
        movie_actors = movie_actors[1:]
        #check if there is a link between two actors
        for actor_1,actor_2 in combinations(movie_actors,2):
            if DG.has_edge(actor_2, actor_1):
                DG[actor_2][actor_1]["weight"] += 1
            else:
                DG.add_edge(actor_2, actor_1, weight=1)

    # using the class function
    rank_val = nx.pagerank(DG,alpha = epsilon)
    rank_3 = get_ranks(rank_val)

    return rank_3

    # with open(filename, 'r') as file:
    #     data = file.readlines()
    #
    # Actors = nx.DiGraph()
    #
    # for d in data:
    #     movie_actors = d.strip('\n')
    #     actors = movie_actors.split('/')[1:]
    #     for a1,a2 in combinations(actors,2):
    #         if Actors.has_edge(a2,a1):
    #             Actors[a2][a1]['weight'] += 1
    #         else:
    #             Actors.add_edge(a2,a1,weight=1)
    # rank_actors = nx.pagerank(Actors, alpha = epsilon)
    # rank = get_ranks(rank_actors)
    #
    # return rank
# if __name__=="__main__":
#     print(rank_actors()[0:20])
