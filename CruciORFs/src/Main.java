import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * Class Main created by John Wang 7/19/2020
 * Program takes in fasta file and outputs ORFs that are detected
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //create orfFinding object
        ORFFinding orfFinding = new ORFFinding();
        //specify input/output paths
        String file = "C:\\Users\\ykwan\\IdeaProjects\\Crucivirus\\src\\ORFs\\Data\\contigs_circLin_withMCP_filtered99.fasta";
        String outputFile = "C:\\Users\\ykwan\\IdeaProjects\\Crucivirus\\src\\ORFs\\Output\\cruciORFs.txt";
        //create PrintWriter to write to output file
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
        //modifiable min orf length
        final int minORFLength = 300;

        //read in sequence info from fasta file
        Object[] fileInfo = orfFinding.readGenomeFASTA(file);
        ArrayList<String> sequences = (ArrayList<String>) fileInfo[0];
        ArrayList<String> tag = (ArrayList<String>) fileInfo[2];
        ArrayList<Boolean> revComp = (ArrayList<Boolean>) fileInfo[1];
        //record orfs for each sequence
        for (int i = 0; i < sequences.size(); i++) {
            //record if sequence is reverse complement
            boolean reverseComplement = revComp.get(i);
            //record sequence tag
            String virusTag = tag.get(i);
            //convert dna sequence to rna
            String rnaSequence = orfFinding.dnaToRna(sequences.get(i));
            //record length of sequence. If the length is over max crucivirus genome length (set at 10000 for now) print msg to console
            int length = rnaSequence.length();
            if (length > 8000) {
                System.out.println("too big: " + virusTag + " Length:" + rnaSequence.length());
            }
            //find orfs in the dna
            ArrayList<ORF> orfs0 = orfFinding.findORFs(rnaSequence, minORFLength, reverseComplement);
            //if virus is ambisense, check reverse complement for orfs
            String dnaRC = orfFinding.reverseComplement(sequences.get(i));
            //convert rcDNA to rcRNA
            String rnaRC = orfFinding.dnaToRna(dnaRC);
            //generate array of orfs on rc sequence
            ArrayList<ORF> orfs1 = orfFinding.findORFs(rnaRC, minORFLength, !reverseComplement);
            //add rc orfs to orfs0
            for (int j = 0; j < orfs1.size(); j++) {
                orfs0.add(orfs1.get(j));
            }
            //print sequence tag
            pw.println(virusTag);
            //print orfs, if the sequences is reverse complement, orf add "-", else "+"
            for (int j = 0; j < orfs0.size(); j++) {
                ORF orf = orfs0.get(j);
                pw.println(orf.startingPosition + " " + orf.endingPosition + " " + (orf.rc? "-" : "+"));
                pw.println("Amino Acid Sequence: " + orf.aaSequence);
                pw.println("\n");
            }
        }
        pw.close();
    }
}
