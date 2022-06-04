package IntronParser;


import java.io.*;

public class ThymineComp {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) throw new IllegalArgumentException("need two file names for i/o");

        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));

        //learn how to read genbank using biojava
        //parse sequence and extract intron
        //call function to get thymine composition
        //call function to get general statistics of intron
        //get average statistics for a set of introns
        //print to stdout
    }
}
