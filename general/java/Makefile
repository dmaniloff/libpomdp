###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2010, Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac
RM=/bin/rm

# classpath
JCLASSPATH=../../external/jmatharray.jar:../../external/symPerseusJava:.

# source files
SOURCES=pomdp.java pomdpFlat.java pomdpAdd.java valueFunction.java valueFunctionAdd.java belState.java belStateAdd.java belStateFlat.java Common.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
