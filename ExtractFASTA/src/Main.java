import java.awt.image.BufferedImageFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader fastaIn = new BufferedReader(new FileReader(args[0]));
        BufferedReader seqListIn = new BufferedReader(new FileReader(args[1]));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));

        ArrayList<String> tags = new ArrayList<>();

        String tag = seqListIn.readLine();
        while (tag != null) {
            tags.add(tag);
            tag = seqListIn.readLine();
        }

        Map<String, String> fastaList = new HashMap<>();

        String seqName = fastaIn.readLine();
        String seq = fastaIn.readLine();

        while (seqName != null) {
            fastaList.put(seqName, seq);
            seqName = fastaIn.readLine();
            seq = fastaIn.readLine();
        }

        for (String seqKey :fastaList.keySet()) {
            boolean rdrpHit = false;
            String seqKeyFormatted = seqKey.substring(1);
            for (String keyword : tags) {
                if (seqKeyFormatted.toLowerCase().equals(keyword.toLowerCase())) {
                    rdrpHit = true;
                    break;
                }
            }

            if (rdrpHit) {
                pw.println(seqKey);
                pw.println(fastaList.get(seqKey));
            }
        }

        pw.close();
    }
}
