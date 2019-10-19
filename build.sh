echo "Starting The Build"
echo "Starting the Build" >> SudokuSolver.log

echo "Compile"
javac code/*.java -d bytecode/ >> SudokuSolver.log

echo "Build Archive"
jar -cfe SudokuSolver.jar GUI bytecode/*.class assets/* >> SudokuSolver.log

echo "Documentation"
javadoc code/*.java -d docs/ >> SudokuSolver.log
