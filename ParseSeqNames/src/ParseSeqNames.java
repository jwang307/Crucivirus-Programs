import java.io.*;
import java.nio.Buffer;

public class ParseSeqNames {
    public static void main(String[] args) throws IOException {

        checkArgs(args);

        BufferedReader br = new BufferedReader(new FileReader(args[0]));

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));

        String line;

        while ((line = br.readLine()) != null) {
            if (line.contains("LOCUS")) {
                String[] info = line.split(" +");
                pw.println(info[1]);
            }
        }

        pw.close();
    }

    static void checkArgs(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Specificy 1. GenBank reference file, and 2. output file to record names of sequences.");
        }
    }
}
