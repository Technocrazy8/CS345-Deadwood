default:
	rm -f *.class
	javac *.java

run:
	rm -f *.class
	javac *.java
	java Deadwood.java
	rm -f *.class

clean:
	rm -f *.class
