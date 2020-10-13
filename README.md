# Crucivirus-Programs
A collection of programs used to help annotate cruciviruses, a family of circular ssDNA viruses with characteristics of RNA and DNA viruses. Part of ongoing NSF sponsered research at the Extreme Virus Lab, Portland State University. Focusing on the origin of viruses through the unique viral RNA/DNA characteristics of cruciviruses.

# Programs
Most of the programs are simply annotation scripts meant to help make manual work more efficient. Seom of them are not scalable, and I'm currently in the process of cleaning up code and making sure it works on any dataset. Below you can find instructions on how to run each program (still updating).

# Legit
HMMERAnnotator is meant to provide support for hits detected by HMMER (Hidden Markov Model Biosequence analysis: [hmmer.org](http://hmmer.org)) for automatic annotation in Geneious sequences. <br />
IntronFinder is an upcoming project hoping to discover potential introns and splice sites in cruciviruses.

# Instructions
These programs are all written in Java, some with the help of external libraries. As such, all programs run can be done through an executable jar file. This is so that the program can be run on any OS as long as the device has the Java Runtime Environment (any version 11 and onward) installed. To install Java, go to this page: [JAVA Download](https://www.oracle.com/java/technologies/javase-downloads.html). <br />

Programs are run in the basic format "java -jar [JAR_NAME.jar] [system arguments]". Argument options are specified based on each program and are listed below.

### Working ###
- KeywordSearch
- HMMERAnnotator
- ParseSeqNames
- Iteron Parser
- SeqListToGB
- SequenceInfo
- UpdateCSV [NOT TO USE RIGHT NOW]

### In development ###
- IntronFinder
- AutoAnnotate

### Deprecated ###
- CPFinding
- CruciORFs
- CheckFiles
