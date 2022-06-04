import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ParseSequences {
    public static void main(String[] args) throws IOException {
        if (args.length != 3)
            throw new IllegalArgumentException
                    ("Need 3 parameters: base gb file and a list of sequences names you wish to parse, along with output file");

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
        Scanner in = new Scanner(new FileInputStream(args[1]));
        ArrayList<String> tags = new ArrayList<>();
        while(in.hasNextLine()) {
            tags.add(in.nextLine());
        }
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("LOCUS")) {
                String[] info = line.split(" +");
                String tag = info[1];
                if (tags.contains(tag)) {
                    pw.println(line);
                    while ((line = br.readLine()) != null) {
                        pw.println(line);
                        if (line.contains("//")) {
                            break;
                        }
                    }
                }
            }
        }
        pw.close();
    }
}
