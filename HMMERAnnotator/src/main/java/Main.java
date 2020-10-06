import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.sequence.io.FastaSequenceParser;
import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureI;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;


public class Main {

    private static final double MAX_E_VALUE = 1E-5;

    public static void main(String[] args) throws  IOException {
        if (args.length < 3)
            throw new IllegalArgumentException("Specify 3 parameters: gff input file name, hmmer result, and gff output file name");

        FeatureList gffInFeatures = GFF3Reader.read(args[0]);
        List<String> sense = getSenses(args[0]);

        List<HMMHit> hits = readHMMER(args[1]);

        writeGFF(gffInFeatures, sense, hits, args[2]);

        System.out.println("Results written to " + args[2] + ". Drag output file into Geneious folder to merge changes");
    }

    public static List<HMMHit> readHMMER(String hmmer) throws IOException {
        BufferedReader hmmerIn = new BufferedReader(new FileReader(hmmer));

        List<HMMHit> hits = new ArrayList<>();
        List<String> annotatedSequences = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            hmmerIn.readLine();
        }

        String hit;
        HMMHit prevHit = null;

        while (!(hit = hmmerIn.readLine()).equals("")) {
            hit = hit.trim();
            String[] descriptions = hit.split(" +");
            double eVal = Double.parseDouble(descriptions[0]);

            if (eVal > MAX_E_VALUE) {
                System.out.println("E Value too high: " + hit);
                continue;
            }

            String seqName = HMMHit.getSeqName(descriptions[8]);
            int orfNum = HMMHit.getORFNum(descriptions[8]);

            HMMHit tempHit = new HMMHit(eVal, Float.parseFloat(descriptions[1]), Float.parseFloat(descriptions[2]), seqName, orfNum);

            if (prevHit == null) {
                hits.add(tempHit);
                prevHit = tempHit;
                annotatedSequences.add(tempHit.seqName);
            } else {
                if (!prevHit.equals(tempHit) && !annotatedSequences.contains(tempHit.seqName)) {
                    hits.add(tempHit);
                    prevHit = tempHit;
                    annotatedSequences.add(tempHit.seqName);
                }
            }
        }

        hmmerIn.close();
        return hits;
    }

    public static void writeGFF(FeatureList gff, List<String> senses, List<HMMHit> hits, String outFile) throws IOException {
        PrintWriter gffOut = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));

        for (int i = 0; i < gff.size(); i++) {
            Feature currentFeature = (Feature) gff.get(i);
            int orfNum = 0;
            String attribute = currentFeature.getAttribute("Name");
            if (attribute != null && attribute.toLowerCase().contains("orf")) {
                orfNum = Integer.parseInt(attribute.split(" ")[1]);
            }

            for (int j = 0; j < hits.size(); j++) {
                HMMHit currentHit = hits.get(j);
                if (currentFeature.seqname().equals(currentHit.seqName) && orfNum == currentHit.orfNum) {
                    int start = Math.abs(currentFeature.location().start());
                    int end = Math.abs(currentFeature.location().end());

                    if (start > end) {
                        int temp = start;
                        start = end + 1;
                        end = temp;
                    } else {
                        start += 1;
                    }

                    gffOut.println(currentFeature.seqname() + "\t"
                            + "HMMAnnotator" + "\t"
                            + "CDS" + "\t"
                            + start + "\t"
                            + end + "\t"
                            + "." + "\t"
                            + senses.get(i) + "\t"
                            + (currentFeature.frame() == -1? "." : currentFeature.frame()) + "\t"
                            + "Name=Putative HMMER Rep CDS");
                    break;
                }
            }
        }

        gffOut.close();
    }

    public static ArrayList<String> getSenses(String inFile) throws IOException {
        ArrayList<String> senses = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(inFile));

        String line;

        while ((line = br.readLine()) != null && !line.toLowerCase().equals("##fasta")) {
            if (line.charAt(0) == '#') {
                continue;
            }
            String[] annotation = line.split("\t");
            senses.add(annotation[6]);
        }

        br.close();
        return senses;
    }

}
