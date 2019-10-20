echo "Starting The Build"
echo "Starting the Build" >> test/SudokuSolver.log

echo "Compile"
javac code/*.java -d bytecode/ >> test/SudokuSolver.log

echo "Build Archive"
jar -cfe0 SudokuSolver.jar GUI -C assets/ . -C bytecode/ . >> test/SudokuSolver.log

echo "Documentation"
javadoc code/*.java -d docs/ >> test/SudokuSolver.log
