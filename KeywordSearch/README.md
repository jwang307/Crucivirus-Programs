# Program: Short-Hand Search For Sequences (ie. jilin_1026 -> ETS_JilinP_c1026) (Note: Still Testing, Bugs/Errors Expected)

This program can be used to retrieve a list of sequences names corresponding to the keywords of a list of sequences in an input file. For example, if you input jilin_1026, the full sequence name ETS_JilinP_c1026 will be returned. This program was primarily used to quickly jot down sequence names and create a full sequence name list, usually to feed into a program like ```SeqListToGB``` or another program that required full sequence names.

The program requires several inputs to be passed along when the program is run:

- Input 1: The text file (.txt) of short-hand sequence names
- Input 2: A list of full sequence names to search from (.txt). This can be generated from a particular dataset using the program ```ParseSeqNames```.
- Input 3: The name of the output file (.txt) that will list the full sequence names of (Input 1).

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```KeywordSearch.jar```

Run the following command: 
```
java -jar KeywordSearch.jar [Input 1] [Input 2] [Input 3]
```
Example: 
```
java -jar KeywordSearch.jar ShorthandList.gb SeqNamesList.txt SearchedSequences.txt
```
The output should in the file specified. Open in Notepad or any other way to check.