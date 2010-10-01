# /** ------------------------------------------------------------------------- *
#  * libpomdp
#  * ========
#  * File: gen_rs_15_12.py
#  * Description: jython script to generate RockSample[15,12]
#  * Copyright (c) 2009, 2010 Diego Maniloff 
#  * W3: http://www.cs.uic.edu/~dmanilof
#  --------------------------------------------------------------------------- */

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
import java.io.PrintStream as PrintStream
from libpomdp.problems.rocksample import *

out = PrintStream("15-12/RockSample_15_12.SPUDD")
n=15
k= [ [0, 9],
     [0, 13],
     [1, 14],
     [3, 9],
     [3, 14],
     [4, 9],
     [5, 14],
     [6, 9],
     [9, 14],
     [9, 9], 
     [4, 0],
     [14,0] ]
apos=[0,8]
gen = rocksampleGen(n, k, apos, out)
