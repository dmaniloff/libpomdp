'''
 libpomdp
 ========
 File: gen_catch_tag_10_5.py
 Description: simple jython script to generate a catch .sperseus file
              according to a specific configuration
              10 is the width of the grid
              7  is the height if the grid
 Copyright (c) 2009, 2010, 2011 Diego Maniloff 
 W3: http://www.cs.uic.edu/~dmanilof              
'''

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
from   libpomdp.problems.catchproblem.java import *
import java.io.PrintStream as PrintStream

# declarations
ol     = 0.8                            # omnisciency level of the wumpus
width  = 10
height = 7
gp     = CatchTagHGrid(width, height)   # wallAhead of an H-shaped grid
w      = OmniWumpus(gp, ol)
s      = CollocatedObs(gp)
out    = PrintStream("catch_tagH_taggingAction_10_7.sperseus")
gen    = CatchTagGenerator(width, height, gp, w, s, out) # defines a tagging action

# generate model
gen.generate();
