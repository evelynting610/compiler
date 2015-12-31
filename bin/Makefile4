all:
	make minijava/parser
	make rest

minijava/parser: grammar
	rm -rf minijava/parser minijava/node minijava/lexer minijava/analysis
	java -cp ../sablecc-3.2amh/src org.sablecc.sablecc.SableCC grammar

rest:
	make classclean
	javac -Xlint:unchecked minijava/Main4.java
	javac -Xlint:unchecked minijava/Interp.java

clean:
	make classclean

distclean:
	rm -rf minijava/parser minijava/node minijava/lexer minijava/analysis parser.dump
	make classclean

classclean:
	find minijava -name '*.class' -exec rm {} \;

backupfileclean:
	find . -name '*~' -exec rm {} \;
