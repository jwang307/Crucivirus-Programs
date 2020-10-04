import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.FastaSequenceParser;
import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;


public class Main {

    public static void main(String[] args) throws  IOException {
        if (args.length < 3) throw new IllegalArgumentException("Specify 3 parameters: gff input file name, fasta input file name, and gff output file name");
        FeatureList gffInFeatures = GFF3Reader.read(args[0]);
        String[] cmd = {"hmmsearch", "repprofile.hmm", args[1], "> test.out"};
        Process hmmRun = Runtime.getRuntime().exec(cmd);

        try{
            Thread.sleep(5000);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        List<String> hits = readHMMER();


        //remove all the features that aren't ORFs and removed duplicate ORFs
        //run exec command hmmsearch on fasta file
        //read in results and remove all gff annotations that don't fit
        //return gff file
        //TODO: INSTALL JAVA ON UBUNTU IN ORDER TO TEST
    }

    public static void cleanGFF(FeatureList gffFeatures) {

    }

    public static List<String> readHMMER() {
      return null;
    }

}
