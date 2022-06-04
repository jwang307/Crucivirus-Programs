import javax.security.auth.login.CredentialNotFoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static HashMap<String, Integer> referenceList = new HashMap<>();

    public static void main(String[] args) throws IOException {
        if (args.length < 3) throw new IllegalArgumentException("need at least 3 params");
        BufferedReader br1 = new BufferedReader(new FileReader(new File(args[0])));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(args[1])));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));
        ArrayList<String> commonTags = new ArrayList<>();

        String line;
        while((line = br1.readLine()) != null && line.length() > 1) {
            referenceList.put(line, 1);
        }

        while ((line = br2.readLine()) != null && line.length() > 1) {
            if (referenceList.containsKey(line)) {
                commonTags.add(line);
            }
        }

        for (String commonTag : commonTags) {
            pw.println(commonTag);
        }

        br1.close(); br2.close(); pw.close();
    }
}
