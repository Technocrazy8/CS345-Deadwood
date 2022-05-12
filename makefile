default:
	javac Deadwood.java

run:
	rm -f *.class
	javac Deadwood.java
	java Deadwood 2
	rm -f *.class

clean:
	rm -f *.class