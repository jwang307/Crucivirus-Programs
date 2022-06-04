import java.io.*;
import java.util.ArrayList;

public class CheckFiles {
    public static void main(String[] args) throws IOException {
        BufferedReader db = new BufferedReader(new FileReader("suspect.txt"));
        BufferedReader q = new BufferedReader(new FileReader("suspectreps.txt"));

        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("missed.txt")));

        ArrayList<String> test = new ArrayList<>();

        String tested;
        while ((tested = db.readLine()) != null)
            test.add(tested);

        String query;
        while ((query = q.readLine()) != null) {
            if (!test.contains(query)) {
                pw.println(query);
            }
        }

        pw.close();
    }
}
