package IntronParser;

import java.io.BufferedReader;
import java.io.IOException;

public class SequenceManipulation {
    static String sequenceFromGB(BufferedReader br) throws IOException {
        String firstLine = br.readLine();
        if (Integer.parseInt(firstLine.toLowerCase().split(" ")[0]) != 1) {
            throw new IllegalArgumentException("First line doesn't include sequence keyword ORIGIN");
        }

        StringBuilder sequenceBuilder = new StringBuilder();

        String seqLine;
        while ((seqLine = br.readLine()).equals("//")) {
            String[] sequenceSeqments = seqLine.split(" +");


        }



        return sequenceBuilder.toString();
    }
}
