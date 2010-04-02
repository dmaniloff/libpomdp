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
import CatchGen
import CatchGridProperties
import CatchTagGrid
import java.io.PrintStream as PrintStream

# declarations
gp   = CatchTagGrid(10, 5)
out  = PrintStream("catch_tag_10_5.SPUDD")
gen  = CatchGen(10, 5, gp, out)

# generate model
gen.generate();
