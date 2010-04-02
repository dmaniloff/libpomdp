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
SOURCES=rocksampleGen.java rocksampleGraph.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
