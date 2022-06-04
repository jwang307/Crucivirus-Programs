# Program: GenBank File to Sequence Name List

This program can be used to retrieve a list of sequences names corresponding to the sequences in a GenBank file. Given a GenBank file as input, the program will output the list of sequence names in the GenBank file. It is essentially the inverse of the program ```SeqListToGB```.

The program requires several inputs to be passed along when the program is run:

- Input 1: The main GenBank file. This is a .gb or GenBank file that has all the sequences in the dataset.
- Input 2: Output location specification. This is the output file to print results to. It should be a Text file (.txt).

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```ParseSeqNames.jar```

Run the following command: 
```
java -jar ParseSeqNames.jar [Input 1] [Input 2]
```
Example: 
```
java -jar ParseSeqNames.jar Dataset.gb SeqNamesList.txt 
```
The output should in the file specified. Open in Notepad or any other way to check.