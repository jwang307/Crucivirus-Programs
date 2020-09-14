import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class UpdateCSV {
    public static void main(String[] args) throws IOException {
        if (args.length >= 3) {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));

            PrintWriter pw;
            String filetype;
            if (args.length == 3) {
                pw = new PrintWriter(new BufferedWriter(new FileWriter(args[1])));
                filetype = args[2];
            } else {

                pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
                filetype = args[3];
            }

            String columns = "Name,Description,Sequence,Sequence Length,%GC,Capsid,Rep,Organization,Comments";
            List<SequenceData> sequenceData;
            switch (filetype) {
                case "csv":
                    sequenceData = readFromCSV(br);
                    break;
                case "gff":
                    BufferedReader csvReader = new BufferedReader(new FileReader(args[1]));
                    sequenceData = readFromGFF(br, csvReader);
                    break;
                default:
                    throw new IllegalArgumentException("Not a file choice");
            }
            writeToCSV(pw, sequenceData, columns);
            pw.close();
        } else {
            throw new IllegalArgumentException("Not the correct number of arguments: specify input file, output file, and input type");
        }
    }

    static List<SequenceData> readFromCSV(BufferedReader br) throws IOException {
        List<SequenceData> sequenceList = new LinkedList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] info = line.split(",");
            SequenceData sd = new SequenceData(info[0], info[4], info[10], info[11], info[1], "N", "N", "N", "N");
            sequenceList.add(sd);
        }

        return sequenceList;
    }

    static List<SequenceData> readFromGFF(BufferedReader br, BufferedReader csvReader) throws IOException {
        List<SequenceData> sdList = new LinkedList<>();
        String line;
        csvReader.readLine();
        csvReader.readLine();
        while ((line = csvReader.readLine()) != null) {
            System.out.println(line);
            String[] params = line.split(",");
            SequenceData sd = new SequenceData(params[0], params[1], params[2], params[3], params[4], params[5],
                    params[6], params[7], params[8]);

            sdList.add(sd);
        }
        System.out.println(line);
        String fileGFF = br.readLine();

        if (fileGFF.contains("gff")) {
            br.readLine();
            br.readLine();

            for (int i = 0; i < sdList.size(); i++) {
                SequenceData sd = sdList.get(i);

                List<Annotation> capsids = new LinkedList<>();
                List<Annotation> reps = new LinkedList<>();
                StringBuilder capsid = new StringBuilder(), rep = new StringBuilder(), organization = new StringBuilder(), comments = new StringBuilder();
                line = br.readLine();
                while (!line.contains("sequence-region") && !line.contains("FASTA")) {
                    String[] annotation = line.split("\t");
                    Annotation annt = new Annotation();
                    if (annotation[8].contains("Rep") || annotation[8].contains("CP")) {
                        annt.start = Integer.parseInt(annotation[3]);
                        annt.end = Integer.parseInt(annotation[4]);
                        annt.sense = annotation[6];
                        if (annotation[8].contains("CP")) {
                            annt.cp = true;
                            capsids.add(annt);
                        } else {
                            annt.cp = false;
                            reps.add(annt);
                        }
                    }
                    line = br.readLine();
                }

                if (reps.size() == 0) {
                    organization.append("No rep. ");
                } else {
                    rep.append(reps.get(0).sense).append(".")
                            .append(reps.get(0).start).append(".")
                            .append(reps.get(0).end).append(".")
                            .append(reps.get(0).getLength()).append(".");
                    if (reps.size() > 1) {
                        comments.append("Multiple reps. ");
                    }
                }
                if (capsids.size() == 0) {
                    organization.append("No capsid.");
                } else {
                    capsid.append(capsids.get(0).sense).append(".")
                            .append(capsids.get(0).start).append(".")
                            .append(capsids.get(0).end).append(".")
                            .append(capsids.get(0).getLength()).append(".");

                    if (capsids.size() > 1) {
                        comments.append("Multiple capsids.");
                    }
                }

                if (capsids.size() > 0 && reps.size() > 0) {
                    if (uniSense(capsids.get(0), reps.get(0))) {
                        organization.append("unisense");
                    } else {
                        organization.append("ambisense");
                    }
                }

                sd.capsid = capsid.toString();
                sd.rep = rep.toString();
                sd.organization = organization.toString();
                sd.comments = comments.toString();
            }
        } else{
            throw new IllegalArgumentException("Wrong file type");
        }

        return sdList;
    }

    static void writeToCSV (PrintWriter pw, List < SequenceData > sequenceData, String columns){
        pw.println(columns);

        for (SequenceData sd : sequenceData) {
            pw.println(sd.toString());
        }
    }

    static boolean uniSense (Annotation a, Annotation b){
        return a.sense.equals(b.sense);
    }
}


class SequenceData {
    public String name;
    public String description;
    public String sequence;
    public String sequenceLength;
    public String GC;
    public String capsid;
    public String rep;
    public String organization;
    public String comments;
    List<String> sequenceData;


    public SequenceData(String name, String description, String sequence, String sequenceLength,
                        String GC, String capsid, String rep, String organization, String comments) {
        this.name = name;
        this.description = description;
        this.sequence = sequence;
        this.sequenceLength = sequenceLength;
        this.GC = GC;
        this.capsid = capsid;
        this.rep = rep;
        this.organization = organization;
        this.comments = comments;

    }

    public String toString() {
        String[] info = {name, description, sequence, sequenceLength, GC, capsid, rep, organization, comments};
        sequenceData = Arrays.asList(info);

        StringBuilder csvString = new StringBuilder();

        for (String str : sequenceData) {
            csvString.append(str);
            csvString.append(",");
        }
        csvString.deleteCharAt(csvString.length() - 1);

        return csvString.toString();
    }

}

class Annotation {
    boolean cp;
    String sense;
    int start;
    int end;

    public int getLength() {
        return end - start + 1;
    }

}



