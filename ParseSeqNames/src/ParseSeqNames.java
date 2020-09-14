import java.io.*;
import java.nio.Buffer;

public class ParseSeqNames {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("suspectreps.gb"));

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("suspect.txt")));

        String line;

        while ((line = br.readLine()) != null) {
            if (line.contains("LOCUS")) {
                String[] info = line.split(" +");
                pw.println(info[1]);
            }
        }

        pw.close();
    }
}
