import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String dir = "finalCruciCPAnnotations.gb";
        BufferedReader br = new BufferedReader(new FileReader(dir));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("NoCPHits.txt")));

        String line = br.readLine();

        while(line != null) {
            if (line.contains("LOCUS")) {

                String fill = br.readLine();
                while (!fill.contains("FEATURES")) {
                    fill = br.readLine();
                }
                boolean cpFound = false;
                String feature = br.readLine().toLowerCase();
                //int CPAnnotations = 0;
                //boolean multipleCPs = false;
                while (feature.contains("cds")) {
                    String label;
                    while (!(label = br.readLine().toLowerCase()).contains("label")) {}
                    if (label.contains("cp")) {
                        cpFound = true;
                        //CPAnnotations++;
                        /*if (CPAnnotations > 1) {
                            multipleCPs = true;
                            break;
                        }*/
                        break;
                    }
                    feature = br.readLine().toLowerCase();
                }
                if (!cpFound/*multipleCPs*/) {
                    String[] info = line.split(" +");
                    pw.println(info[1]);
                }
            }
            line = br.readLine();
        }

        pw.close();
    }
}
