'''
 libpomdp
 ========
 File: genscript.py
 Description: jython script to generate a catch .sperseus file
              for a 5x5 rectangular grid catchw world with
              an omnicient wumpus and an agent with adjacent-cell
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
OL   = 0.5                              # omniscience level - rand 
gp   = CatchRectangularGrid(5, 5)
w    = OmniWumpus(gp, OL)
s    = AdjacentObs(gp)
out  = PrintStream("catch_rect_5_5.sperseus")
gen  = CatchGen(5, 5, gp, w, s, out)    # no tagging action, just collocate

# generate model
gen.generate();
