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
SOURCES=catchGen.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

gen:
	jython genscript.py

clean:
	$(RM) ./*.class
