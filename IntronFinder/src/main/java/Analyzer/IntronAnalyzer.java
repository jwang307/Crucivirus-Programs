package Analyzer;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import org.biojava.nbio.genome.parsers.gff.Feature;
import org.biojava.nbio.genome.parsers.gff.FeatureList;
import org.biojava.nbio.genome.parsers.gff.GFF3Reader;

/**
 * Program to extract sequence data from the introns detected in the previous crucivirus dataset.
 * Created 11/28 @John Wang
 * Takes in a GenBank file (to find intron interval), and a fasta (for sequence)
 * Prints out a report of the nucleotides around the 5', branch point, and 3' sites
 */
public class IntronAnalyzer {

    static int exonLength = 10;

    public static void main(String[] args) throws IOException {

        BufferedReader fastaReader = new BufferedReader(new FileReader(new File(args[1])));

        FeatureList gffFeatures = GFF3Reader.read(args[0]);

        int[][] intervals = readGFF(gffFeatures);
        String[] sequenceNames = new String[intervals.length];
        String[] sequences = readFasta(fastaReader, sequenceNames);

        String[] introns = extract(intervals, sequences);

        double[][] fivePrime = new double[8][20];
        double[][] threePrime = new double[8][20];

        extractFastas(introns, sequenceNames);
        extractIntrons(introns, sequenceNames);
        analyzeIntrons(introns, fivePrime, threePrime);

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
        printResults(pw, fivePrime, threePrime);
    }

    /**
     * Function to extract the intron start and endpoints of each intron within the sequences
     * @param fl list of gff features
     * @return matrix of the intervals of each intron
     */
    static int[][] readGFF(FeatureList fl) {
        ArrayList<int[]> intervals = new ArrayList<>();

        String currentName = fl.get(0).seqname();
        String seqWithIntronDetected = "";
        String currentID = "";

        boolean repFound = false;
        boolean capsidFound = false;
        boolean intronFound = false;

        int[] lastIntervals = new int[2];

        for (int i = 0; i < fl.size(); i++) {
            Feature currentFeature = (Feature) fl.get(i);
            if (currentFeature.seqname().equals(currentName) && !currentFeature.seqname().equals(seqWithIntronDetected)) {
                if (repFound) {
                    if (currentFeature.attributes().equals(currentID)) {
                        intronFound = true;
                    }
                } else if (currentFeature.attributes().contains("Rep")) {
                    repFound = true;
                    currentID = currentFeature.attributes();
                    lastIntervals[0] = currentFeature.location().bioStart();
                    lastIntervals[1] = currentFeature.location().bioEnd();
                    capsidFound = false;
                }

                if (capsidFound) {
                    if (currentFeature.attributes().equals(currentID)) {
                        intronFound = true;
                    }
                } else if (currentFeature.attributes().contains("CP")) {
                    capsidFound = true;
                    currentID = currentFeature.attributes();
                    lastIntervals[0] = currentFeature.location().bioStart();
                    lastIntervals[1] = currentFeature.location().bioEnd();
                    repFound = false;
                }
            } else {
                currentName = fl.get(i).seqname();
                repFound = false;
                capsidFound = false;
                currentID = "";
            }

            if (intronFound) {
                int start, end;

                if (lastIntervals[0] > currentFeature.location().bioStart()) {
                    start = lastIntervals[0] + exonLength;
                    end = currentFeature.location().bioEnd() - exonLength;

                    if (Math.abs(end - start) > 1000) {
                        start = lastIntervals[1] - exonLength;
                        end = currentFeature.location().bioStart() + exonLength;
                    }
                } else {
                    start = lastIntervals[1] - exonLength;
                    end = currentFeature.location().bioStart() + exonLength;
                    if (Math.abs(end - start) > 1000) {
                        start = lastIntervals[0] + exonLength;
                        end = currentFeature.location().bioEnd() - exonLength;
                    }
                }

                intervals.add(new int[]{start, end});

                intronFound = false;
                repFound = false;
                capsidFound = false;
                seqWithIntronDetected = currentFeature.seqname();
                currentID = "";
            }
        }

        return intervals.toArray(new int[0][0]);
    }

    /**
     * Convert a fasta file to an array of sequences
     * @param br bufferedreader input of file
     * @return a string array of the sequences
     * @throws IOException bufferedreader exception
     */
    static String[] readFasta(BufferedReader br, String[] names) throws IOException {
        ArrayList<String> sequences = new ArrayList<>();
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            if (line.charAt(0) == '>') {
                names[count] = line;
                sequences.add(br.readLine());
                count++;
            }
        }

        return sequences.toArray(new String[0]);
    }

    /**
     * Extract the introns from the sequences and store in an array
     * @param intervals start and endpoints of each intron
     * @param sequences array of complete sequences
     * @return array of intron sequences
     */
    static String[] extract(int[][] intervals, String[] sequences) {
        int length = intervals.length;
        String[] introns = new String[length];


        if (length != sequences.length) {
            throw new IllegalArgumentException("Lengths of arrays are not equal");
        } else {
            for (int i = 0; i < length; i++) {
                int start = intervals[i][0];
                int end = intervals[i][1];

                if (end < start) {
                    String prelimIntron = sequences[i].substring(end, start - 1);
                    //reverse and complement
                    introns[i] = reverseAndComplement(prelimIntron);
                } else {
                    if (end > sequences[i].length()) {
                        int overflow = end - sequences[i].length();
                        introns[i] = sequences[i].substring(start) + sequences[i].substring(0, overflow - 1);
                    } else {
                        introns[i] = sequences[i].substring(start, end - 1);
                    }
                }

                //if the intron length is greater than 50% of the sequence length, we can say that the intron contains
                // the point where the genome wraps around
                if (introns[i].length() > 0.5*sequences[i].length()) {
                    if (end < start) {
                        introns[i] = sequences[i].substring(start) + sequences[i].substring(0, end - 1);
                    } else {
                        String prelimIntron = sequences[i].substring(end) + sequences[i].substring(0, start - 1);

                        //reverse and complement
                        introns[i] = reverseAndComplement(prelimIntron);
                    }
                }
            }
        }

        return introns;
    }

    static String makeComplement(String sequence) {
        StringBuilder complement = new StringBuilder();

        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            switch (c) {
                case 'A':
                    complement.append("T");
                    break;
                case 'T':
                    complement.append("A");
                    break;
                case 'C':
                    complement.append("G");
                    break;
                case 'G':
                    complement.append("C");
                    break;
            }
        }

        return complement.toString();
    }

    static void analyzeIntrons(String[] introns, double[][] five, double[][] three) {
        int total = 0;
        int threeL = three[0].length;
        int fiveL = five[0].length;

        for (int i = 0; i < introns.length; i++) {
            String intron = introns[i];
            if (intron.length() < 30) {
                continue;
            } else {
                total++;
                //5' site

                for (int j = 0; j < fiveL; j++) {
                    char c = intron.charAt(j);
                    switch (c) {
                        case 'A':
                            five[0][j]++;
                            break;
                        case 'T':
                            five[1][j]++;
                            break;
                        case 'C':
                            five[2][j]++;
                            break;
                        case 'G':
                            five[3][j]++;
                            break;
                    }
                }

                for (int j = intron.length() - threeL; j < intron.length(); j++) {
                    char c = intron.charAt(j);
                    switch (c) {
                        case 'A':
                            three[0][j - (intron.length() - threeL)]++;
                            break;
                        case 'T':
                            three[1][j - (intron.length() - threeL)]++;
                            break;
                        case 'C':
                            three[2][j - (intron.length() - threeL)]++;
                            break;
                        case 'G':
                            three[3][j - (intron.length() - threeL)]++;
                            break;
                    }
                }
            }

        }

        for (int j = 0; j < threeL; j++) {
            for (int k = 0; k < 8; k++) {
                if (k < 4) {
                    five[k][j] /= total;
                    three[k][j] /= total;
                } else {
                    switch (k) {
                        case 4:
                            //purine
                            five[k][j] = (five[0][j]+five[3][j]);
                            three[k][j] = (three[0][j]+three[3][j]);
                            break;
                        case 5:
                            //pyrimidine
                            five[k][j] = (five[1][j]+five[2][j]);
                            three[k][j] = (three[1][j]+three[2][j]);
                            break;
                        case 6:
                            //AT
                            five[k][j] = (five[0][j]+five[1][j]);
                            three[k][j] = (three[0][j]+three[1][j]);
                            break;
                        case 7:
                            //CG
                            five[k][j] = (five[2][j]+five[3][j]);
                            three[k][j] = (three[2][j]+three[3][j]);
                            break;
                    }
                }

            }
        }
    }

    /**
     * Function extractFastas meant to extract sequences around the splice sites for alignment in Geneious
     * @param introns array of introns
     * @param names corresponding array of sequence names
     * @throws IOException File Writer
     */
    static void extractFastas(String[] introns, String[] names) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Data\\extracted.fasta")));

        for (int i = 0; i < introns.length; i++) {
            if (introns[i].length() >= 40) {
                pw.println(names[i]);
                pw.println(introns[i].substring(0, 20) + introns[i].substring(introns[i].length() - 20));
            }
        }

        pw.close();
    }

    static void extractIntrons(String[] introns, String[] names) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Data\\extractedIntrons.fasta")));

        for (int i = 0; i < introns.length; i++) {
            if (introns[i].length() >= 20) {
                pw.println(names[i]);
                pw.println(introns[i].substring(10, introns[i].length() - 10));
            }
        }

        pw.close();
    }

    static void printResults(PrintWriter pw, double[][] five, double[][] three) {
        pw.println(", , -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10");

        printMatrix(pw, five, "five");

        printMatrix(pw, three, "three");

        pw.close();
    }

    static void printMatrix(PrintWriter pw, double[][] matrix, String name) {
        String[] secondColumn = {"A", "T", "C", "G", "Purine", "Pyrimidine", "AT", "CG"};

        roundMatrix(matrix);

        for (int i = 0; i < matrix.length; i++) {
            StringBuilder line = new StringBuilder();

            if (i == 0) {
                line.append(name).append(", ");
            } else {
                line.append(", ");
            }

            line.append(secondColumn[i]).append(", ");

            for(int j = 0; j < matrix[0].length; j++) {
                line.append(matrix[i][j]).append(", ");
            }
            line.deleteCharAt(line.length() - 2);
            pw.println(line.toString());
        }

    }

    /**
     * roundMatrix meant to format matrix for csv print
     * @param matrix
     */
    static void roundMatrix(double[][] matrix) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = Double.parseDouble(df.format(matrix[i][j]));
            }
        }
    }

    /**
     * functio used to obtain correct sequence for introns of annotations on the complementary dna sequence
     * @param sequence
     */
    static String reverseAndComplement(String sequence) {
        StringBuilder reverse = new StringBuilder();
        reverse.append(sequence);
        reverse.reverse();
        return IntronAnalyzer.makeComplement(reverse.toString());
    }
}
