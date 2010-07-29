###############################################################################
# File: Makefile
# Description: simple make file
# Copyright (c) 2009, 2010 Diego Maniloff
# W3: http://www.cs.uic.edu/~dmanilof
###############################################################################

# commands
JC=/usr/bin/javac -Xlint
RM=/bin/rm
ANTLR=org.antlr.Tool
JV=/usr/bin/java

# classpath
JCLASSPATH=../../../external/mtj-0.9.12/mtj-0.9.12.jar:../../../external/mtj-0.9.12/lib/netlib-java/arpack_combo-0.1.jar:../../../external/mtj-0.9.12/lib/netlib-java/netlib-java-0.9.1.jar:../../../external/antlr-3.2.jar:.

default: dotpomdpParserSparseMTJ.class

dotpomdpParserSparseMTJ.class: dotpomdpParserSparseMTJ.java dotpomdpMTJLexer.class dotpomdpMTJParser.class pomdpSpecSparseMTJ.class
	$(JC) -classpath $(JCLASSPATH) dotpomdpParserSparseMTJ.java

dotpomdpMTJParser.class: dotpomdpMTJParser.java pomdpSpecSparseMTJ.class
	$(JC) -classpath $(JCLASSPATH) dotpomdpMTJParser.java

dotpomdpMTJLexer.class: dotpomdpMTJLexer.java pomdpSpecSparseMTJ.class
	$(JC) -classpath $(JCLASSPATH) dotpomdpMTJLexer.java

pomdpSpecSparseMTJ.class: pomdpSpecSparseMTJ.java
	$(JC) -classpath $(JCLASSPATH) pomdpSpecSparseMTJ.java

dotpomdpMTJParser.java: dotpomdpMTJ.g
	$(JV) -classpath $(JCLASSPATH) $(ANTLR) dotpomdpMTJ.g

dotpomdpMTJLexer.java: dotpomdpMTJ.g
	$(JV) -classpath $(JCLASSPATH) $(ANTLR) dotpomdpMTJ.g

clean:
	$(RM) ./*.class
