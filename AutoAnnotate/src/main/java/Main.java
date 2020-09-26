import java.io.*;

public class Main {
    public static void main(String[] args) throws  IOException {
        BufferedReader br = new BufferedReader(new FileReader("in.in"));
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("out.out")));
        String line;
        for (int i = 0; i < 14; i++) {
            if (i<9) {
                System.out.println(br.readLine());
            } else {
                br.readLine();
            }
        }
        while (!(line= br.readLine()).equals("")) {
            pw.println(line);
        }
        pw.close();
    }
}
