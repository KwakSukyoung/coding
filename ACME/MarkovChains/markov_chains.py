# markov_chains.py
"""Volume 2: Markov Chains.
<Name> Sukyoung Kwak
<Class> 320 - 2
<Date> November 9th
"""

import numpy as np
from scipy import linalg as la


class MarkovChain:
    """A Markov chain with finitely many states.

    Attributes:
        (fill this out)
    """
    # Problem 1 #value error
    def __init__(self, A, states=None):
        """Check that A is column stochastic and construct a dictionary
        mapping a state's label to its index (the row / column of A that the
        state corresponds to). Save the transition matrix, the list of state
        labels, and the label-to-index dictionary as attributes.

        Parameters:
        A ((n,n) ndarray): the column-stochastic transition matrix for a
            Markov chain with n states.
        states (list(str)): a list of n labels corresponding to the n states.
            If not provided, the labels are the indices 0, 1, ..., n-1.

        Raises:
            ValueError: if A is not square or is not column stochastic.

        Example:
            >>> MarkovChain(np.array([[.5, .8], [.5, .2]], states=["A", "B"])
        corresponds to the Markov Chain with transition matrix
                                   from A  from B
                            to A [   .5      .8   ]
                            to B [   .5      .2   ]
        and the label-to-index dictionary is {"A":0, "B":1}.
        """
        #Initialize A and the size
        self.A = A
        self.size = np.shape(self.A)[0]

        #Find out if it's column_stoc or not.
        if np.allclose(A.sum(axis = 0), np.ones(self.size))  == False:
            self.col_stoc = "False"
            raise ValueError("A is not column stochastic")
        else:
            self.col_stoc = "True"

        #Set states either by the choice or 0 ~ n-1
        self.states = states
        if self.states is None:
            self.states = list(range(0,self.size))

        #Set label_to_index
        self.lab_to_ind = {}
        for i in range(self.size):
            self.lab_to_ind[self.states[i]] = i

    # Problem 2
    def transition(self, state):
        """Transition to a new state by making a random draw from the outgoing
        probabilities of the state with the specified label.

        Parameters:
            state (str): the label for the current state.

        Returns:
            (str): the label of the state to transitioned to.
        """
        #Get the index according to the state
        index = self.lab_to_ind[state]

        #Get the column elements accroding to the column
        column = self.A[:,index]

        #Choose the bigger element
        new = np.argmax(np.random.multinomial(1, column))

        #Return the label of the state to transitioned to
        return self.states[new]

    # Problem 3 #bad not right.......cry
    def walk(self, start, N):
        """Starting at the specified state, use the transition() method to
        transition from state to state N-1 times, recording the state label at
        each step.

        Parameters:
            start (str): The starting state label.

        Returns:
            (list(str)): A list of N state labels, including start.
        """
        #Initializing the trans_state list
        trans_state_w = []
        trans_state_w.append(start)

        #N-1 times iteration
        for i in range(N-1):
            trans_state_w.append(self.transition(trans_state_w[i]))

        return trans_state_w



    # Problem 3
    def path(self, start, stop):
        """Beginning at the start state, transition from state to state until
        arriving at the stop state, recording the state label at each step.

        Parameters:
            start (str): The starting state label.
            stop (str): The stopping state label.

        Returns:
            (list(str)): A list of state labels from start to stop.
        """
        #Initializing the trans_state list
        trans_state_p = []
        trans_state_p.append(start)

        #N-1 times iteration
        while trans_state_p[-1] != stop:
            i = 0
            trans_state_p.append(self.transition(trans_state_p[i-1]))
            i += 1
        return trans_state_p


    # Problem 4
    def steady_state(self, tol=1e-12, maxiter=40):
        """Compute the steady state of the transition matrix A.

        Parameters:
            tol (float): The convergence tolerance.
            maxiter (int): The maximum number of iterations to compute.

        Returns:
            ((n,) ndarray): The steady state distribution vector of A.

        Raises:
            ValueError: if there is no convergence within maxiter iterations.
        """
        #initialize x_cur
        x_cur = np.random.random(self.size)
        #normalize x_cur
        x_cur = x_cur/np.sum(x_cur)
        #get the next x_cur
        x_next = np.dot(self.A, x_cur)
        i = 0
        while (la.norm(x_cur - x_next ,1) >=tol):
            i += 1
            #get warning if maxiter > 40
            if i == 40:
                raise ValueError("A^k does not converge.")
            #keep getting the x
            else:
                x_cur = x_next
                x_next = np.dot(self.A, x_cur)

        return x_next

class SentenceGenerator(MarkovChain):
    """A Markov-based simulator for natural language.

    Attributes:
        (fill this out)
    """
    # Problem 5
    def __init__(self, filename):
        """Read the specified file and build a transition matrix from its
        contents. You may assume that the file has one complete sentence
        written on each line.
        """
        self.states = []
        #initialize sentences and words
        self.sentences = open(filename).read().split("\n")
        self.words = open(filename).read().split()
        for i in range(np.size(self.words)):
            if self.words[i] not in self.states:
                self.states.append(self.words[i])
                
        #insert the start and stop thing
        self.states.insert(0, "$tart")
        self.states.append("$top")
        self.lab_to_ind = {self.states[i]: i for i in range(len(self.states))}
        
        #initialize B
        B = np.zeros((np.size(self.states),np.size(self.states)))
        
        #loop through to modify B
        for each in range(np.size(self.sentences)):
            sentence_list = self.sentences[each].split()
            sentence_list.insert(0,"$tart")
            sentence_list.append("$top")
            for cons in range(np.size(sentence_list)-1):
                cur_word = sentence_list[cons]
                next_word = sentence_list[cons+1]
                fir_ind = self.lab_to_ind[cur_word]
                sec_ind = self.lab_to_ind[next_word]
                B[sec_ind, fir_ind] += 1
        B[-1,-1] = 1
        sum_col = np.sum(B,axis = 0)
        self.A = B/sum_col

    # Problem 6
    def babble(self):
        """Create a random sentence using MarkovChain.path().

        Returns:
            (str): A sentence generated with the transition matrix, not
                including the labels for the $tart and $top states.

        Example:
            >>> yoda = SentenceGenerator("yoda.txt")
            >>> print(yoda.babble())
            The dark side of loss is a path as one with you.
        """
        #modify the path 
        list = self.path("$tart", "$top")
        #remove start and stop
        list.remove("$tart")
        list.remove("$top")
        #join the list
        sentence = ' '.join(list)

        return sentence

#if __name__=="__main__":
#    A = SentenceGenerator("yoda.txt")
#    for _ in range(3):
#        print(A.babble())
