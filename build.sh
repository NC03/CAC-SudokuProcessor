echo "Starting The Build"
echo "Starting the Build" >> SudokuSolver.log

echo "Compile"
javac code/*.java -d bytecode/ >> SudokuSolver.log

echo "Build Archive"
jar -cfe0 SudokuSolver.jar GUI -C assets/ . -C bytecode/ . >> SudokuSolver.log

echo "Documentation"
javadoc code/*.java -d docs/ >> SudokuSolver.log
