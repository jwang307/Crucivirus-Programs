

public class Main {
    private static String infile, outfile, choice;

    public static void main(String[] args) {
        if (args.length > 0) {
            infile = args[0];

            if (!infile.equals("choices")) {
                try {
                    outfile = args[1]; choice = args[2];

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: Not enough arguments given");
                    System.exit(1);
                }

                Methods methods = new Methods(infile, outfile);

                switch (choice) {
                    case "nocp":
                        methods.findNoCP();
                        break;
                    case "multcp":
                        methods.findMultipleCP();
                        break;
                    case "norep":
                        methods.findNoRep();
                        break;
                    case "multrep":
                        methods.findMultipleRep();
                        break;
                    default:
                        System.out.println("Error: Not a valid choice");
                        System.exit(1);
                }
                System.out.println("Task finished: Results written to " + outfile);
            } else {
                System.out.println("NOTE: Only works for GenBank files as of now");
                System.out.println("To run the jar executable, specify the input file, output file, and choice");
                System.out.println("Example: 'java -jar SequenceInfo.jar infile.gb outfile.txt nocp'");
                System.out.println("To find sequences with no capsid annotation, type 'nocp' as the third parameter");
                System.out.println("To find sequences with multiple capsid annotations, type 'multcp' as the third parameter");
                System.out.println("To find sequences with no rep annotation, type 'norep' as the third parameter");
                System.out.println("To find sequences with multiple rep annotations, type 'multrep' as the third parameter");
            }
        } else {
            System.out.println("Error: No arguments given");
        }
    }
}
