import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void main(String[] args) throws IOException {
        //check inputs
        checkArgs(args);
        //create file readers/writers
        BufferedReader csvreader = new BufferedReader(new FileReader(new File(args[0])));
        BufferedReader gffIn = new BufferedReader(new FileReader(new File(args[1])));
        PrintWriter csvout = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));

        List<Sequence> sequences = new ArrayList<>();
        //read in csv
        readCSV(csvreader, sequences);
        //read in gff
        readGFF(gffIn, sequences);
        //output
        printCSV(csvout, sequences);

        System.out.println("CSV successfully written to " + args[2]);
    }

    static void checkArgs(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Specify 1. Input csv file, 2. Input gff file, 3. Output csv file \n csv should contain name, gc content, sequence, sequence length, and description");
        }
    }

    static void readCSV(BufferedReader csvReader, List<Sequence> sequences) throws IOException {
        String[] columns = csvReader.readLine().split(",");

        int name = -1, description = -1, seq = -1, seqLength = -1, gc = -1;
        for (int i = 0; i < columns.length; i++) {
            switch(columns[i]) {
                case "Name":
                    name = i;
                    break;
                case "%GC":
                    gc = i;
                    break;
                case "Description":
                    description = i;
                    break;
                case "Sequence":
                    seq = i;
                    break;
                case "Sequence Length":
                    seqLength = i;
                    break;
            }
        }

        if (name == -1 || description == -1 || seq == -1 || seqLength == -1 || gc == -1) {
           throw new IllegalArgumentException("Columns not found in csv");
        }

        String csvEntry;

        while ((csvEntry = csvReader.readLine()) != null) {
            String[] entryInfo = csvEntry.split(",");
            Sequence sequence = new Sequence(entryInfo[name], entryInfo[description],
                    Integer.parseInt(entryInfo[seqLength]), entryInfo[seq], entryInfo[gc]);

            sequences.add(sequence);
        }

        csvReader.close();
    }

    static void readGFF(BufferedReader gffIn, List<Sequence> sequences) {

    }

    static void printCSV(PrintWriter csvOut, List<Sequence> sequences) {
        String columns = "Name,Description,Sequence,Sequence Length,%GC,Capsid,Rep,Organization,Other Annotations,Comments";
        csvOut.println(columns);

        for (Sequence sequence : sequences) {
            csvOut.println(sequence.toCSV());
        }

        csvOut.close();
    }
}
