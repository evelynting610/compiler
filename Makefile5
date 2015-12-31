all:
	make minijava/parser
	make rest

minijava/parser: grammar
	rm -rf minijava/parser minijava/node minijava/lexer minijava/analysis
	java -cp ../sablecc-3.2amh/src org.sablecc.sablecc.SableCC --amh-dump grammar

rest:
	make classclean
	javac -Xlint:unchecked minijava/Main5.java
	javac -Xlint:unchecked minijava/Interp.java
	javac -Xlint:unchecked minijava/Canonical.java
	javac -Xlint:unchecked minijava/Interp2.java
	javac -Xlint:unchecked minijava/CodeGen.java

clean:
	make classclean

distclean:
	rm -rf minijava/parser minijava/node minijava/lexer minijava/analysis parser.dump
	make classclean

classclean:
	find minijava -name '*.class' -exec rm {} \;

backupfileclean:
	find . -name '*~' -exec rm {} \;
