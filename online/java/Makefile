# simple make file
# author: Diego Maniloff
# Copyright (c) 2009, Diego Maniloff.

# commands
JC=/usr/bin/javac
RM=/bin/rm

# classpath
JCLASSPATH=../../external/jmatharray.jar:.

# source files
SOURCES=AndOrTree.java orNode.java andNode.java pomdp.java heuristic.java valueFunction.java

AndOrTree.class: AndOrTree.java orNode.java andNode.java pomdp.java heuristic.java valueFunction.java
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
