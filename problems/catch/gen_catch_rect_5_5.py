'''
 libpomdp
 ========
 File: genscript.py
 Description: simple jython script to generate a catch SPUDD file
              according to a specific configuration
'''

# imports
import CatchGen
import CatchGridProperties
import CatchRectangularGrid
import java.io.PrintStream as PrintStream

# declarations
gp   = CatchRectangularGrid(5, 5)
out  = PrintStream("catch.SPUDD")
gen  = CatchGen(5, 5, gp, out)

# generate model
gen.generate();
