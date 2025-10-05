clean:
	@rm -rf build

run:
	@javac app/*.java Jaikin.java -d build
	@java -cp build Jaikin