echo "Compile"
javac code/*.java -d bytecode/

echo "Build Archive"
jar -cfe SudokuSolver.jar GUI bytecode/*.class assets/*

echo "Documentation"
javadoc code/*.java -d docs/
