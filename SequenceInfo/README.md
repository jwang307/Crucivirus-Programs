# Program: Extract sequences based on presence of Capsid or Rep

This program can be used to extract sequences within a dataset based on the presence (or lack of) annotated Capsid or Rep gene. 

The program requires the following inputs:
- Input 1: The main GenBank file. This is a .gb or GenBank file that has all the sequences in the dataset.
- Input 2: Output file name. The output is a line-separated list of sequences names based on the criteria of (Input 3).
- Input 3: The criteria for extracting sequences. This can either be "nocp" or "norep" The program will then output the sequence list with the chosen criteria.

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```SequenceInfo.jar```

Run the following command: 
```
java -jar SequenceInfo.jar [Input 1] [Input 2] [Input 3]
```
Example: 
```
java -jar SequenceInfo.jar Dataset.gb NoRepSequences.txt norep
```
The output should in the file specified. Open in Notepad or any other way to check.