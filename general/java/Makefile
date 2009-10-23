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
JCLASSPATH=../../external/jmatharray.jar:.

# source files
SOURCES=pomdp.java valueFunction.java

pomdp.class: pomdp.java valueFunction.java
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
