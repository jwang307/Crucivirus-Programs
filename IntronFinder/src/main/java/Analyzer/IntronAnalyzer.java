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
    public static void main(String[] args) throws IOException {
        BufferedReader fastaReader = new BufferedReader(new FileReader(new File(args[1])));

        FeatureList gffFeatures = GFF3Reader.read(args[0]);

        int[][] intervals = readGFF(gffFeatures);
        String[] sequences = readFasta(fastaReader);

        String[] introns = extract(intervals, sequences);

        double[][] fivePrime = new double[8][10];
        double[][] branchPoint = new double[8][20];
        double[][] threePrime = new double[8][10];

        analyzeIntrons(introns, fivePrime, branchPoint, threePrime);

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
        printResults(pw, fivePrime, branchPoint, threePrime);
    }

    /**
     * Function to extract the intron start and endpoints of each intron within the sequences
     * @param fl list of gff features
     * @return matrix of the intervals of each intron
     */
    static int[][] readGFF(FeatureList fl) {
        ArrayList<int[]> intervals = new ArrayList<>();

        String currentName = fl.get(0).seqname();
        String currentID = "";

        boolean repFound = false;
        boolean intronFound = false;

        int[] lastRepIntervals = new int[2];

        for (int i = 0; i < fl.size(); i++) {
            Feature currentFeature = (Feature) fl.get(i);
            if (currentFeature.seqname().equals(currentName)) {
                if (repFound) {
                    if (currentFeature.attributes().equals(currentID)) {
                        intronFound = true;
                    }
                }

                else if (currentFeature.attributes().contains("Replication")) {
                    repFound = true;
                    currentID = currentFeature.attributes();
                    lastRepIntervals[0] = currentFeature.location().bioStart();
                    lastRepIntervals[1] = currentFeature.location().bioEnd();
                }
            } else {
                currentName = fl.get(i).seqname();
                repFound = false;
                currentID = "";
            }

            if (intronFound) {
                if (lastRepIntervals[0] > currentFeature.location().bioStart()) {
                    intervals.add(new int[]{lastRepIntervals[0], currentFeature.location().bioEnd()});
                } else {
                    intervals.add(new int[]{lastRepIntervals[1], currentFeature.location().bioStart()});
                }

                intronFound = false;
                repFound = false;
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
    static String[] readFasta(BufferedReader br) throws IOException {
        ArrayList<String> sequences = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            if (line.charAt(0) == '>') {
                sequences.add(br.readLine());
            }
        }

        return sequences.toArray(new String[0]);
    }

    /**
     * Extract the introns from the sequences and store in an array
     * @param intervals start and enpoints of each intron
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
                    introns[i] = sequences[i].substring(end, start - 1);
                    //reverse
                    StringBuilder reverse = new StringBuilder();
                    reverse.append(introns[i]);
                    reverse.reverse();
                    for (int j = 0; j < reverse.length(); j++) {
                        introns[i] = IntronAnalyzer.makeComplement(reverse.toString());
                    }
                } else {
                    introns[i] = sequences[i].substring(start, end - 1);
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

    static void analyzeIntrons(String[] introns, double[][] five, double[][] branch, double[][] three) {
        int total = 0;
        int branchL = 20;
        int threeL = 10;
        int fiveL = 10;

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

                for (int j = intron.length() - branchL; j < intron.length(); j++) {
                    char c = intron.charAt(j);
                    switch (c) {
                        case 'A':
                            if (j >= intron.length() - threeL) {
                                three[0][j - (intron.length() - threeL)]++;
                            }
                            branch[0][j - (intron.length() - branchL)]++;
                            break;
                        case 'T':
                            if (j >= intron.length() - threeL) {
                                three[1][j - (intron.length() - threeL)]++;
                            }
                            branch[1][j - (intron.length() - branchL)]++;
                            break;
                        case 'C':
                            if (j >= intron.length() - threeL) {
                                three[2][j - (intron.length() - threeL)]++;
                            }
                            branch[2][j - (intron.length() - branchL)]++;
                            break;
                        case 'G':
                            if (j >= intron.length() - threeL) {
                                three[3][j - (intron.length() - threeL)]++;
                            }
                            branch[3][j - (intron.length() - branchL)]++;
                            break;
                    }
                }
            }

        }

        for (int j = 0; j < 20; j++) {
            for (int k = 0; k < 8; k++) {
                if (k < 4) {
                    if (j < 10) {
                        five[k][j] /= total;
                        three[k][j] /= total;
                    }
                    branch[k][j] /= total;
                } else {
                    switch (k) {
                        case 4:
                            //purine
                            if (j < 10) {
                                five[k][j] = (five[0][j]+five[3][j]);
                                three[k][j] = (three[0][j]+three[3][j]);
                            }
                            branch[k][j] = (branch[0][j]+branch[3][j]);
                            break;
                        case 5:
                            //pyrimidine
                            if (j < 10) {
                                five[k][j] = (five[1][j]+five[2][j]);
                                three[k][j] = (three[1][j]+three[2][j]);
                            }
                            branch[k][j] = (branch[1][j]+branch[2][j]);
                            break;
                        case 6:
                            //AT
                            if (j < 10) {
                                five[k][j] = (five[0][j]+five[1][j]);
                                three[k][j] = (three[0][j]+three[1][j]);
                            }
                            branch[k][j] = (branch[0][j]+branch[1][j]);
                            break;
                        case 7:
                            //CG
                            if (j < 10) {
                                five[k][j] = (five[2][j]+five[3][j]);
                                three[k][j] = (three[2][j]+three[3][j]);
                            }
                            branch[k][j] = (branch[2][j]+branch[3][j]);
                            break;
                    }
                }

            }
        }
    }

    static void printResults(PrintWriter pw, double[][] five, double[][] branch, double[][] three) {
        pw.println(", , 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20");

        printMatrix(pw, five, "five");

        printMatrix(pw, branch, "branch");

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

    static void roundMatrix(double[][] matrix) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = Double.parseDouble(df.format(matrix[i][j]));
            }
        }
    }
}
