'''
 libpomdp
 ========
 File: genscript.py
 Description: jython script to generate a catch .sperseus file
              for a 5x5 rectangular grid catch world with
              a random wumpus and an agent with adjacent-cell
              sensing capabilities.
 Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 W3: http://www.cs.uic.edu/~dmanilof              
'''

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
import java.io.PrintStream as PrintStream
from libpomdp.problems.catchproblem.java import *

# declarations
gp   = CatchRectangularGrid(5, 5)       # defines a square-shaped grid
w    = RandomWumpus(gp)                 # defines a random-moving wumpus
s    = AdjacentObs(gp)
out  = PrintStream("catch_rect_5_5_rand_adjobs.sperseus")
gen  = CatchGen(5, 5, gp, w, s, out)    # does not define a tagging action, collocate only

# generate model
gen.generate();
