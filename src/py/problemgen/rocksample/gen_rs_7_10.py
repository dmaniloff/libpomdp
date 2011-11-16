'''
 libpomdp
 ========
 File: gen_rs_7_10.py
 Description: jython script to generate RockSample[7,10]
 Copyright (c) 2009, 2010, 2011 Diego Maniloff 
'''

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
import java.io.PrintStream as PrintStream
from libpomdp.problems.rocksample import rocksampleGen

# declarations
out = PrintStream("7-10/RockSample_7_10.SPUDD")
n=7
k= [ [2,0],
     [0,1],
     [3,1],
     [6,3],
     [2,4],
     [3,4],
     [5,5],
     [1,6],
     [6,0],
     [6,6]]
apos=[0,3]

# generate
gen = rocksampleGen(n, k, apos, out)
