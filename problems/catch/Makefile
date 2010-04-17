###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, 2010 Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac
RM=/bin/rm

# classpath
JCLASSPATH=.

# source files
SOURCES=CatchGen.java CatchGraph.java CatchGridProperties.java CatchRectangularGrid.java CatchTagGrid.java Wumpus.java OmniWumpus.java RandomWumpus.java Sensor.java AdjacentObs.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

gen:
	jython genscript.py

clean:
	$(RM) ./*.class
