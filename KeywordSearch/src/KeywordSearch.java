import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class KeywordSearch {

    //input a list of sequences with keywords and formal sequence listsuspect.txt, will parse through and return the list of sequence names
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        Scanner in = new Scanner(new FileInputStream(args[1]));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[2])));

        ArrayList<String> tags = new ArrayList<>();
        String tag = br.readLine();
        while (tag != null) {
            tags.add(tag);
            tag = br.readLine();
        }

        List<String[]> keywords = new ArrayList<>();
        while(in.hasNextLine()) {
            String[] keywordsplit = in.nextLine().toLowerCase().split("_");
            keywords.add(keywordsplit);
        }
        ArrayList<String> finalNames = new ArrayList<>();
        //go through each input
        for (int i = 0; i < keywords.size(); i++) {
            //get input array
            String[] query = keywords.get(i);
            //is string a space?
            if (query[0].equals("")) {
                finalNames.add("");
                continue;
            }

            //break when tag found
            boolean possible = true;
            //go through each tag
            for (int j = 0; j < tags.size(); j++) {
                String name = tags.get(j).toLowerCase();
                //go through each key word and match with tag. If all of them match, then record tag as that key wor
                //otherwise, continue
                for (int k = 0; k < query.length; k++) {
                    if (!name.contains(query[k])) {
                        possible = false;
                        break;
                    }
                }

                if (possible) {
                    finalNames.add(tags.get(j));
                    break;
                } else {
                    possible = true;
                }
            }
        }
        for (String name : finalNames) {
            pw.println(name);
        }
        pw.close();
    }
}
