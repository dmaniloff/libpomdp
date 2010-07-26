'''
 libpomdp
 ========
 File: genscript.py
 Description: jython script to generate a catch SPUDD file
              for a 5x5 rectangular grid catchw world with
              a random wumpus and an agent with adjacent-cell
              sensing capabilities.
'''

# imports
import CatchGen
import Wumpus
import RandomWumpus
import Sensor
import AdjacentObs
import CatchGridProperties
import CatchRectangularGrid
import java.io.PrintStream as PrintStream

# declarations
gp   = CatchRectangularGrid(5, 5)
w    = RandomWumpus(gp)
s    = AdjacentObs(gp)
out  = PrintStream("catch_rect_5_5_rand_adjobs.SPUDD")
gen  = CatchGen(5, 5, gp, w, s, out)

# generate model
gen.generate();
