'''
 libpomdp
 ========
 File: genscript.py
 Description: simple jython script to generate a catch SPUDD file
              according to a specific configuration
              10 is the width of the grid
              5  is the height if the grid
'''

# imports
import sys
sys.path.append('../../../../dist/libpomdp.jar')
from   libpomdp.problems.catchproblem.java import *
import java.io.PrintStream as PrintStream

# declarations
ol     = 0.8
width  = 10
height = 7
gp     = CatchTagHGrid(width, height)
w      = OmniWumpus(gp, ol)
s      = CollocatedObs(gp)
out    = PrintStream("catch_tagh_taggingAction_10_7.SPUDD")
gen    = CatchTagGenerator(width, height, gp, w, s, out)

# generate model
gen.generate();
