import java.io.*;

public class TrimNamesForDendrogram {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(args[0])));

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.charAt(0) == '>') {
                if (line.startsWith("CruV", 1) || line.startsWith("CruCGE", 1)) {
                    pw.println(line.split("-")[0] + "-" + line.split("-")[1]);
                } else {
                    pw.println(line.split("-")[0]);
                }
            } else {
                pw.println(line);
            }
        }

        pw.close();
    }
}
