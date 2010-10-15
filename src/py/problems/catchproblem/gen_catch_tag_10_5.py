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
import CatchTagGenerator
import CatchGridProperties
import CatchTagGrid
import OmniWumpus
import CollocatedObs
import java.io.PrintStream as PrintStream

# declarations
ol     = 0.8
width  = 10
height = 5
gp     = CatchTagGrid(width, height)
w      = OmniWumpus(gp, ol)
s      = CollocatedObs(gp)
out    = PrintStream("catch_tag_taggingAction_10_5.SPUDD")
gen    = CatchTagGenerator(width, height, gp, w, s, out)

# generate model
gen.generate();
