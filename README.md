# Crucivirus-Programs
A collection of programs used to help annotate cruciviruses, a family of circular ssDNA viruses with characteristics of RNA and DNA viruses. Part of ongoing NSF sponsered research at the Extreme Virus Lab, Portland State University. Focusing on the origin of viruses through the unique viral RNA/DNA characteristics of cruciviruses.

# Programs
Most of the programs are simply annotation scripts meant to help make manual work more efficient. Some of them are not scalable, and I'm currently in the process of cleaning up code and making sure it works on any dataset. Below you can find instructions on how to run each program (still updating, I will have a jar folder with all the executable jars ASAP so it's usable. Some of the src code programs still don't take arguments from args so I will have to change that.).

# Annotators and Algorithms
HMMERAnnotator is meant to provide support for hits detected by HMMER (Hidden Markov Model Biosequence analysis: [hmmer.org](http://hmmer.org)) for automatic annotation in Geneious sequences. <br />
IntronFinder is an upcoming project hoping to discover potential introns and splice sites in cruciviruses.

# Instructions
These programs are all written in Java, some with the help of external libraries. As such, all programs run can be done through an executable jar file. This is so that the program can be run on any OS as long as the device has the Java Runtime Environment (any version 11 and onward) installed. To install Java, go to this page: [JAVA Download](https://www.oracle.com/java/technologies/javase-downloads.html). <br />

Programs are run in the basic format "java -jar [JAR_NAME.jar] [system arguments]". Argument options are specified based on each program and are listed below.

### Working ###
- KeywordSearch: Returns the full name of sequences given an input of keywords (ie. jilin_1026 -> ETS_JilinP_c1026) 
  ```
  java -jar KeywordSearch.jar [keyword list] [full name list] [output name file]
  ```
- HMMERAnnotator: Returns a gff file of annotations detected by an HMMER run when a HMMER result file and sequence gff file is passed in
  ```
  java -jar SeqListToGB.jar [main gff file] [HMMER result file] [output gff file]
  ```
- ParseSeqNames: Returns a list of sequence names from a GenBank file
  ```
  java -jar SeqListToGB.jar [main gb file] [sequence name output file]
  ```
- Iteron Parser - Not really useful, just extracting iterons from Adam's output folder :)
- SeqListToGB: Converts a sequence list to it's GenBank file if the sequences are present in a larger GB file
  ```
  java -jar SeqListToGB.jar [main gb file] [line separated sequence list] [output gb file]
  ```
- SequenceInfo: Return list of sequences matching the given choice
  ```
  java -jar SequenceInfo.jar [main gb file] [output file] [choice (pick one, will add more later): norep, multrep, nocp, multcp]
  ```
- UpdateCSV: Input a csv and gff file of a dataset of crucis.
   ```
   java -jar UpdateCSV.jar [csv file] [gff file] [output csv file]
   ```

### In development ###
- IntronFinder
- AutoAnnotate

### Deprecated ###
- CPFinding
- CruciORFs
- CheckFiles

## TO-DO ##
- TEST jars for working scripts/programs
- Add annotation columns for stemp loop/iterons
- Intron Finder
