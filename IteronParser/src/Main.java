import java.io.*;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) throw new IllegalArgumentException(" Input in directory and out gff");

        File folder = new File(args[0]);

        if (folder.isDirectory()) {
            PrintWriter pw = new PrintWriter(new FileWriter(args[1]));

            for (File gffIn : Objects.requireNonNull(folder.listFiles())) {
                BufferedReader br = new BufferedReader(new FileReader(gffIn));

                String line;
                while((line = br.readLine()) != null) {
                    if (line.toLowerCase().contains("iteron")) {
                        pw.println(line);
                    }
                }

                br.close();
            }

            pw.close();
        } else {
            throw new IllegalArgumentException("in arg must be folder");
        }
    }
}
