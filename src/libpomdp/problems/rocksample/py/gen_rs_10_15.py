'''
 libpomdp
 ========
 File: gen_rs_10_15.py
 Description: jython script to generate RockSample[10,15]
 Copyright (c) 2009, 2010, 2011 Diego Maniloff 
'''

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
import java.io.PrintStream as PrintStream
from libpomdp.problems.rocksample import rocksampleGen

# declarations
out = PrintStream("10-15/RockSample_10_15.SPUDD")
n=10
k= [ [0, 3],
     [0, 7],
     [1, 8],
     [3, 3],
     [3, 8],
     [4, 3],
     [5, 8],
     [6, 1],
     [9, 3],
     [9, 9],
     [0, 0],
     [0, 9],
     [3, 0],
     [3, 9],
     [9, 0] ]
apos=[0,5]

# generate
gen = rocksampleGen(n, k, apos, out)
