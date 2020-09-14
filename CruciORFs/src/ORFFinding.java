import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class ORFFinding created by John Wang 7/19/2020
 * ORFFinding contains functions needed to detect open reading frames and also contains struct ORF defined in Class ORF
 */
public class ORFFinding {
    /**
     * method readGenomeFASTA inputs a file path and returns an Object array containing information of all detected crucivirus sequences.
     * @param file specifies file to be read
     * @return Object[] containing an ArrayList of sequences, whether each sequence is a reverse complement, and the tags of each sequence
     * @throws IOException
     */
    public Object[] readGenomeFASTA(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(file)));

        ArrayList<String> sequences = new ArrayList<>();
        ArrayList<Boolean> reverseComplement = new ArrayList<>();
        ArrayList<String> virusTag = new ArrayList<>();

        String line = br.readLine();
        int counter = 0;
        boolean circSequence = false;
        StringBuilder sequence = new StringBuilder();

        while (line != null) {
            //if new sequence
            if (line.substring(0, 1).equals(">")) {
                //find identity of sequence
                String[] identity = line.split(" ");
                if (identity.length > 1? identity[1].equals("circ") : false) {
                    //if crucivirus then look for orfs
                    circSequence = true;
                    //check and record if crucivirus sequence is rc
                    if (identity.length > 2) {
                        reverseComplement.add(true);
                    } else {
                        reverseComplement.add(false);
                    }
                    //update last sequence to sequences array
                    if (counter != 0) {
                        sequences.add(sequence.toString());
                        sequence.setLength(0);
                    }
                    //add tag for sequence
                    virusTag.add(line);
                    //increase counter per circ sequence
                    counter++;
                } else {
                    //don't read sequence
                    circSequence = false;
                }
            } else if (circSequence) {
                sequence.append(line);
            }
            line = br.readLine();
        }
        //add last sequence read
        sequences.add(sequence.toString());

        Object[] returns = new Object[3];

        returns[0] = sequences;
        returns[1] = reverseComplement;
        returns[2] = virusTag;

        return returns;
    }

    /**
     * method dnaToRna returns rna sequence of given dna sequence
     * @param dna given sequence
     * @return rna sequence
     */
    public String dnaToRna(String dna) {
        StringBuilder rna = new StringBuilder();

        for (int i = 0; i < dna.length(); i++) {
            String base = dna.substring(i, i+1);

            if (base.equals("A") || base.equals("C") || base.equals("G")) {
                rna.append(base);
            } else if (base.equals("T")) {
                rna.append("U");
            }
        }

        return rna.toString();
    }

    /**
     * method translation returns amino acid sequence of rna if rna is translatable
     * @param rnaStrand complete sequence
     * @param startIndex start index of sequence
     * @return amino acid sequence
     */
    public String translation(String rnaStrand, int startIndex) {
        Map<String, String> codonsMap  = new HashMap<String, String>() {{
            put("UUU", "F"); put("UUC", "F"); put("UUA", "L"); put("UUG", "L");
            put("CUU", "L"); put("CUC", "L"); put("CUA", "L"); put("CUG", "L");
            put("AUU", "I"); put("AUC", "I"); put("AUA", "I"); put("AUG", "M");
            put("GUU", "V"); put("GUC", "V"); put("GUA", "V"); put("GUG", "V");
            put("UCU", "S"); put("UCC", "S"); put("UCA", "S"); put("UCG", "S");
            put("CCU", "P"); put("CCC", "P"); put("CCA", "P"); put("CCG", "P");
            put("ACU", "T"); put("ACC", "T"); put("ACA", "T"); put("ACG", "T");
            put("GCU", "A"); put("GCC", "A"); put("GCA", "A"); put("GCG", "A");
            put("UAU", "Y"); put("UAC", "Y"); put("UAA", "x"); put("UAG", "x");
            put("CAU", "H"); put("CAC", "H"); put("CAA", "Q"); put("CAG", "Q");
            put("AAU", "N"); put("AAC", "N"); put("AAA", "K"); put("AAG", "K");
            put("GAU", "D"); put("GAC", "D"); put("GAA", "E"); put("GAG", "E");
            put("UGU", "C"); put("UGC", "C"); put("UGA", "x"); put("UGG", "W");
            put("CGU", "R"); put("CGC", "R"); put("CGA", "R"); put("CGG", "R");
            put("AGU", "S"); put("AGC", "S"); put("AGA", "R"); put("AGG", "R");
            put("GGU", "G"); put("GGC", "G"); put("GGA", "G"); put("GGG", "G");
        }};

        StringBuilder aaSequence = new StringBuilder();

        String rna = rnaStrand.substring(startIndex);

        if (!rna.substring(0, 3).equals("AUG")) {
            return "";
        }

        for (int i = 0; i < rna.length() - 3; i += 3) {
            String codon = rna.substring(i, i+3);

            switch (codonsMap.get(codon)) {
                case "x":
                    return aaSequence.toString();
                default:
                    aaSequence.append(codonsMap.get(codon));
            }
        }

        return "";
    }

    /**
     * method findORFs finds orfs in a given rna sequence
     * @param rnaStrand given rna sequence
     * @param minORFLength specified minimum orf length
     * @param revComp boolean of whether sequence is a reverse complement
     * @return ArrayList of orfs
     */
    public ArrayList<ORF> findORFs(String rnaStrand, int minORFLength, boolean revComp) {
        ArrayList<ORF> orfs0 = new ArrayList<>();
        Map<Integer, Integer> stopCodons = new HashMap<>();

        for (int startIndex = 0; startIndex < rnaStrand.length() - (minORFLength + 3) + 1; startIndex++) {
            String aaSequence = translation(rnaStrand, startIndex);

            int orfLength = aaSequence.length()*3;

            if (orfLength >= minORFLength) {
                int stopIndex = startIndex + orfLength;

                if (!stopCodons.containsKey(stopIndex)) {
                    stopCodons.put(stopIndex, startIndex);

                    ORF o = new ORF(startIndex, stopIndex, orfLength, revComp, aaSequence);

                    orfs0.add(o);
                }
            }
        }

        return orfs0;
    }

    /**
     * method reverseComplement returns the reverse complement sequence of a dna strand
     * @param genome
     * @return
     */
    String reverseComplement(String genome) {
        return reverse(complement(genome));
    }

    public static String reverse(String original) {
        StringBuilder reverse = new StringBuilder();
        for (int i = original.length(); i > 0; i--) {
            reverse.append(original, i-1, i);
        }

        return  reverse.toString();
    }

    static String complement(String original) {
        StringBuilder complement = new StringBuilder();
        for (int i = 0; i < original.length(); i++) {
            switch (original.substring(i, i+1)) {
                case "A":
                    complement.append("T");
                    break;
                case "T":
                    complement.append("A");
                    break;
                case "G":
                    complement.append("C");
                    break;
                case "C":
                    complement.append("G");
                    break;
                default:
                    break;
            }
        }

        return complement.toString();
    }
}

/**
 * ORF struct contains 4 fields:
 * @startingPosition records starting position of orf
 * @endingPosition records ending position of orf
 * @length records ORF length
 * @rc records whether dna sequence was reverse complement
 */
class ORF {
    int startingPosition;
    int endingPosition;
    int length;
    boolean rc;
    String aaSequence;

    public ORF(int startingPosition, int endingPosition, int length, boolean rc, String aaSequence) {
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
        this.length = length;
        this.rc = rc;
        this.aaSequence = aaSequence;
    }
}