import java.io.*;

public class Methods {
    private final String infile, outfile;

    public Methods(String infile, String outfile) {
        this.infile = infile;
        this.outfile = outfile;
    }

    public void findNoCP() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));

            String line = br.readLine();

            while (line != null) {
                if (line.contains("LOCUS")) {

                    String fill = br.readLine();
                    while (!fill.contains("FEATURES")) {
                        fill = br.readLine();
                    }
                    boolean cpFound = false;
                    String feature = br.readLine().toLowerCase();
                    while (feature.contains("cds")) {
                        String label;
                        while (!(label = br.readLine().toLowerCase()).contains("label")) {}
                        if (label.contains("cp")) {
                            cpFound = true;
                            break;
                        }
                        feature = br.readLine().toLowerCase();
                    }
                    if (!cpFound) {
                        String[] info = line.split(" +");
                        pw.println(info[1]);
                        System.out.println(info[1]);
                    }
                }
                line = br.readLine();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: IOException");
            System.exit(1);
        }
    }

    public void findMultipleCP() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));

            String line = br.readLine();

            while (line != null) {
                if (line.contains("LOCUS")) {

                    String fill = br.readLine();
                    while (!fill.contains("FEATURES")) {
                        fill = br.readLine();
                    }
                    String feature = br.readLine().toLowerCase();
                    int CPAnnotations = 0;
                    boolean multipleCPs = false;
                    while (feature.contains("cds")) {
                        String label;
                        while (!(label = br.readLine().toLowerCase()).contains("label")) {}
                        if (label.contains("cp")) {
                            CPAnnotations++;
                            if (CPAnnotations > 1) {
                                multipleCPs = true;
                                break;
                            }
                        }
                        feature = br.readLine().toLowerCase();
                    }
                    if (multipleCPs) {
                        String[] info = line.split(" +");
                        pw.println(info[1]);
                        System.out.println(info[1]);
                    }
                }
                line = br.readLine();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: IOException");
            System.exit(1);
        }
    }

    public void findNoRep() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));

            String line = br.readLine();

            while (line != null) {
                if (line.contains("LOCUS")) {

                    String fill = br.readLine();
                    while (!fill.contains("FEATURES")) {
                        fill = br.readLine();
                    }
                    boolean repFound = false;
                    String feature = br.readLine().toLowerCase();

                    while (feature.contains("cds")) {
                        String label;
                        while (!(label = br.readLine().toLowerCase()).contains("label")) {}
                        if (label.contains("rep")) {
                            repFound = true;
                            break;
                        }
                        feature = br.readLine().toLowerCase();
                    }
                    if (!repFound) {
                        String[] info = line.split(" +");
                        pw.println(info[1]);
                        System.out.println(info[1]);
                    }
                }
                line = br.readLine();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: IOException");
            System.exit(1);
        }
    }

    public void findMultipleRep() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));

            String line = br.readLine();

            while (line != null) {
                if (line.contains("LOCUS")) {

                    String fill = br.readLine();
                    while (!fill.contains("FEATURES")) {
                        fill = br.readLine();
                    }
                    String feature = br.readLine().toLowerCase();
                    int repAnnotations = 0;
                    boolean multipleReps = false;
                    while (feature.contains("cds")) {
                        String label;
                        while (!(label = br.readLine().toLowerCase()).contains("label")) {}
                        if (label.contains("rep")) {
                            repAnnotations++;
                            if (repAnnotations > 1) {
                                multipleReps = true;
                                break;
                            }
                        }
                        feature = br.readLine().toLowerCase();
                    }
                    if (multipleReps) {
                        String[] info = line.split(" +");
                        pw.println(info[1]);
                        System.out.println(info[1]);
                    }
                }
                line = br.readLine();
            }
            pw.close();
        } catch (IOException e) {
            System.out.println("Error: IOException");
            System.exit(1);
        }
    }
}
