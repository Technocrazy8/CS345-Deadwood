default:
	javac Deadwood.java

run:
	rm -f *.class
	javac Deadwood.java
	java Deadwood
	rm -f *.class

clean:
	rm -f *.class