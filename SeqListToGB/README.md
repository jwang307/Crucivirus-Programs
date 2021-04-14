# Program: Sequence List to GenBank Converter

This program can be used to extract selected GenBank sequences from a larger GenBank file. For example, to extract all sequences containing a Rep protein in a given dataset.

The program requires several inputs to be passed along when the program is run:

- Input 1: The main GenBank file. This ia a .gb or GenBank file that has all the sequences in the dataset.
- Input 2: Select sequences to be extracted. This is a .txt file containing the names of the sequences, each on a line.
- Input 3: Output location specification. This is the output file to print results to. It should be a GenBank file (.gb) as the results are in GenBank format.

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```SeqListToGB.jar```

Run the following command: 
```
java -jar SeqListToGB.jar [Input 1] [Input 2] [Input 3]
```