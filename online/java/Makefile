###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac
JVM=/usr/bin/java
RM=/bin/rm


# classpath
JCLASSPATH=../../external/jmatharray.jar:../../external/symPerseusJava:../../general/java:../../general/problems/rocksample:../../offline/java:.

# source files
SOURCES=AndOrTree.java orNode.java andNode.java heuristic.java aems2.java onlineRockSampleSimulatorAdd.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

run:
	$(JVM) -classpath $(JCLASSPATH) onlineRockSampleSimulatorAdd

clean:
	$(RM) ./*.class
