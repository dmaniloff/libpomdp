###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, 2010 Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac -Xlint
RM=/bin/rm

# classpath
JCLASSPATH=../../external/mtj-0.9.12/mtj-0.9.12.jar:../../external/mtj-0.9.12/lib/netlib-java/arpack_combo-0.1.jar:../../external/mtj-0.9.12/lib/netlib-java/netlib-java-0.9.1.jar:../../external/jmatharray.jar:../../external/ujmp-complete-0.2.4.jar:../../external/symPerseusJava:.

# source files
SOURCES=pomdp.java \
	pomdpFlat.java \
	pomdpSparseUJMP.java \
	pomdpSparseMTJ.java \
	pomdpAdd.java \
	valueFunction.java \
	valueFunctionFlat.java \
	valueFunctionSparseUJMP.java \
	valueFunctionSparseMTJ.java \
	valueFunctionAdd.java \
	belState.java \
	belStateAdd.java \
	BelStateFactoredADD.java \
	belStateFlat.java \
	belStateSparseUJMP.java \
	belStateSparseMTJ.java \
	Common.java

default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
