###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac
RM=/bin/rm

# classpath
JCLASSPATH=../../external/jmatharray.jar:../../general/java:.

# source files
SOURCES=AndOrTree.java orNode.java andNode.java heuristic.java aems2.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
