#Program: Format a directory of results from Adam Jones' Iteron Finder to a single GFF File

This program can be used to condense the results from Adam's Iteron Finder to a single .gff file that can be imported to Geneious. Supposedly, there is a way to easily add the annotations as a folder, which makes this program redundant. The reason to condense the results to a single .gff is so that you can drag and drop the .gff file and add annotations in Geneious.

The program requires the following inputs:

- Input 1: The name of the directory (folder) containing the results of the Iteron Finder
- Input 2: The name of the output file (.gff)

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```IteronParser.jar```

Run the following command: 
```
java -jar IteronParser.jar [Input 1] [Input 2]
```
Example: 
```
java -jar IteronParser.jar IteronSearchResults IteronAnnotations.gff
```
The output should in the file specified. Open in Notepad or any other way to check.