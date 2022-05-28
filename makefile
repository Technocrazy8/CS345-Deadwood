default:
	rm -f *.class
	javac *.java

run:
	rm -f *.class
	javac *.java
	java Main.java
	rm -f *.class

clean:
	rm -f *.class
