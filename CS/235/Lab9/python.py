#!/bin/python3

import math
import os
import random
import re
import sys

def queensAttack(n, k, r_q, c_q, obstacles):
    if n == 1:
        return 0

    up = [[r_q+i,c_q] for i in range(1,n-r_q+1)]
    up_le = [[r_q+i,c_q-i] for i in range(1, min(n-r_q, c_q-1)+1)]
    left = [[r_q, c_q-i] for i in range(1,c_q)]
    do_le = [[r_q-i,c_q-i] for i in range(1, min(r_q-1, c_q-1)+1)]
    down = [[r_q-i,c_q] for i in range(1,r_q)]
    do_ri = [[r_q-i,c_q+i] for i in range(1, min(n-c_q, r_q-1)+1)]
    right = [[r_q,c_q+i] for i in range(1, n-c_q+1)]
    up_ri = [[r_q+i,c_q+i] for i in range(1, min(n-c_q, n-r_q)+1)]

    if len(obstacles) == 0:
        return len(up)+len(up_le)+len(do_le)+len(left)+len(down)+len(do_ri)+len(right)+len(up_ri)

    for obstacle in obstacles:
        if obstacle in up:
            up = up[:up.index(obstacle)]
        if obstacle in up_le:
            up_le = up_le[:up_le.index(obstacle)]
        if obstacle in left:
            left = left[:left.index(obstacle)]
        if obstacle in do_le:
            do_le = do_le[:do_le.index(obstacle)]
        if obstacle in down:
            down = down[:down.index(obstacle)]
        if obstacle in do_ri:
            do_ri = do_ri[:do_ri.index(obstacle)]
        if obstacle in right:
            right = right[:right.index(obstacle)]
        if obstacle in up_ri:
            up_ri = up_ri[:up_ri.index(obstacle)]

    return len(up)+len(up_le)+len(do_le)+len(left)+len(down)+len(do_ri)+len(right)+len(up_ri)

if __name__ == '__main__':
    n = 5
    k = 3
    r_q = 4
    c_q = 3
    obstacles = [[5,5],[4,2],[2,3]]

    print(queensAttack(n, k, r_q, c_q, obstacles))
