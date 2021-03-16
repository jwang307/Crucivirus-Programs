package SpliceSiteTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.biojava.nbio.core.sequence.io.FastaReader;

/**
 * SSFinder is meant to find all the splice site codes in a region of DNA (GT/AG).
 * Test for the viability of brute force intron search of DNA length < 1000 bp.
 * Created 11/20/2020 by John Wang
 *
 * Adding search for branch point motif (YNYYRAY)
 */
public class SSFinder {
    /**
     * main inputs a fasta file and max intron length and returns all the potential intervals of introns
     * based purely on splice site codes (GT/AG)
     * @param args includes a fasta file path and an integer for max intron length and an integer for sequence count.
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("results.txt")));

        int maxLength = Integer.parseInt(args[1]);

        String[] sequenceNames = new String[Integer.parseInt(args[2])];

        String[] sequences = readFasta(br, sequenceNames);

        for (int h = 0; h < sequenceNames.length; h++) {

            String DNASeq = sequences[h];
            ArrayList<Integer> GTSites = new ArrayList<Integer>();
            ArrayList<Integer> AGSites = new ArrayList<Integer>();
            ArrayList<Integer> branchPoints = new ArrayList<>();


            for (int i = 0; i < DNASeq.length() - 1; i++) {
                String doublet = DNASeq.substring(i, i + 2);
                if (doublet.equals("GT")) {
                    GTSites.add(i);
                } else if (doublet.equals("AG")) {
                    AGSites.add(i);
                }

                if (i < DNASeq.length() - 6 && checkBranchPoint(DNASeq.substring(i, i + 7))) {
                    branchPoints.add(i);
                }
            }

            //output
            pw.println(sequenceNames[h]);

            pw.println(GTSites.toString());
            pw.println(AGSites.toString());
            pw.println(branchPoints.toString());

            for (int i = 0; i < GTSites.size(); i++) {
                int start = GTSites.get(i);
                for (int j = 0; j < AGSites.size() && AGSites.get(j) < start + maxLength; j++) {
                    int end = AGSites.get(j);
                    if (end > start) {
                        pw.println(start + " -> " + end);
                    }
                }
            }
        }

        pw.close();
    }

    private static boolean checkBranchPoint(String seq) {
        String branchPoint = "YNYYRAY";

        for (int i = 0; i < 7; i++) {
            char correct = branchPoint.charAt(i);
            char seqChar = seq.charAt(i);
            switch (correct) {
                case 'Y':
                    if (seqChar != 'C' && seqChar != 'G') {
                        return false;
                    }
                    break;
                case 'R':
                    if (seqChar != 'A' && seqChar != 'T') {
                        return false;
                    }
                    break;
                case 'A':
                    if (seqChar != 'A') {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
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
}
