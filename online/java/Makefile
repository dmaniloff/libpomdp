###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, 2010 Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac
JVM=/usr/bin/java
RM=/bin/rm

# compiler options
JCOPTS=-Xlint

# classpath
JCLASSPATH=../../external/jmatharray.jar:../../external/symPerseusJava:../../general/java:../../problems/rocksample:../../offline/java:.

# source files
SOURCES=AndOrTree.java AndOrTreeUpdateAdd.java orNode.java andNode.java expandHeuristic.java aems2.java backupHeuristic.java DOSI.java onlineRockSampleSimulatorAdd.java

default:
	$(JC) $(JCOPTS) -classpath $(JCLASSPATH) $(SOURCES)

run:
	$(JVM) -classpath $(JCLASSPATH) onlineRockSampleSimulatorAdd

# profile:
# 	$(JVM) -javaagent:shiftone-jrat/shiftone-jrat.jar -classpath $(JCLASSPATH) onlineRockSampleSimulatorAdd

profile:
	$(JVM) -javaagent:shiftone-jrat/shiftone-jrat.jar -classpath $(JCLASSPATH) TreeProfiler

clean:
	$(RM) ./*.class
