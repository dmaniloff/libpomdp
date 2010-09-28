'''
 libpomdp
 ========
 File: genscript.py
 Description: jython script to generate a catch SPUDD file
              for a 5x5 rectangular grid catchw world with
              an omnicient wumpus and an agent with adjacent-cell
              sensing capabilities.
'''

# imports
import CatchGen
import Wumpus
import OmniWumpus
import Sensor
import AdjacentObs
import CatchGridProperties
import CatchRectangularGrid
import java.io.PrintStream as PrintStream

# declarations
OL   = 0.5                      # omniscience level 
gp   = CatchRectangularGrid(5, 5)
w    = OmniWumpus(gp, OL)
s    = AdjacentObs(gp)
out  = PrintStream("catch_rect_5_5.SPUDD")
gen  = CatchGen(5, 5, gp, w, s, out)

# generate model
gen.generate();
