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
JCLASSPATH=../../external/ujmp-complete-0.2.4.jar:../../external/mtj-0.9.12/mtj-0.9.12.jar:../../external/mtj-0.9.12/lib/netlib-java/arpack_combo-0.1.jar:../../external/mtj-0.9.12/lib/netlib-java/netlib-java-0.9.1.jar:../../external/jmatharray.jar:../../external/symPerseusJava:../../general/java:.

# source files
SOURCES=blindAdd.java qmdpAdd.java mdpSparseMTJ.java mdpFlatFromSparseMTJ.java mdpFlatFromSparseUJMP.java
default:
	$(JC) -classpath $(JCLASSPATH) $(SOURCES)

clean:
	$(RM) ./*.class
