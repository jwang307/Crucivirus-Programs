# Program: Add Annotations from Hits using HMMSearch from HMMER program (Note: Still Testing, Bugs/Errors Expected)

This program can be used to add annotations to a .gff file based on the results of running hmmsearch. This mirrors the function in Geneious annotating certain regions based on hits in BLAST or some other sequence recognition program. 

The program requires the following inputs:
- Input 1: The main GFF file. This GFF file contains all the sequences that were inputted into HMMER. Fasta sequences are not required.
- Input 2: HMMSearch output file (.o or .out). This file is the result of hmmsearch on the sequences in (Input 1). Currently, the program cannot run HMMER internally, so it must be done externally and the results inputted.
- Input 3: The output GFF file. This GFF file will contain the updated annotations contained in hits from HMMSearch.

To run the program, ensure Java is downloaded and the terminal is in the location of the .jar file ```HMMERAnnotator.jar```

Run the following command: 
```
java -jar SequenceInfo.jar [Input 1] [Input 2] [Input 3]
```
Example: 
```
java -jar SequenceInfo.jar Dataset.gb NoRepSequences.txt norep
```
The output should in the file specified. Open in Notepad or any other way to check.