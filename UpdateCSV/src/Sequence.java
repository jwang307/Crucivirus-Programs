import java.util.ArrayList;
import java.util.List;

public class Sequence {
    public final String name;
    private final String description;
    private final int sequenceLength;
    private final String sequence;
    private final String gc;
    private ArrayList<Annotation> annotations = null;

    private String comments;

    public Sequence(String name, String description, int sequenceLength, String sequence, String gc) {
        this.name = name;
        this.description = description;
        this.sequenceLength = sequenceLength;
        this.sequence = sequence;
        this.gc = gc;
    }

    public void addAnnotation(Annotation annt) {
        annotations.add(annt);
    }

    private String getOrganization() {
        boolean repFound = false; boolean capsidFound = false;
        int repIndex = -1, capsidIndex = -1;
        for (int i = 0; i < annotations.size(); i++) {
            if (annotations.get(i).type == Annotation.Type.CAPSID) {
                capsidFound = true;
                capsidIndex = i;
            } else if (annotations.get(i).type == Annotation.Type.REP) {
                repFound = true;
                repIndex = i;
            }
        }

        if (capsidFound && repFound) {
            return annotations.get(capsidIndex).positiveSense() == annotations.get(repIndex).positiveSense()? "Unisense" : "Ambisense";
        } else if (capsidFound) {
            return "No Rep";
        } else if (repFound) {
            return "No Capsid";
        } else {
            return "No Rep. No Capsid";
        }
    }

    private String[] annotationInfo() {
        return null;
    }

    public String toCSV() {
        StringBuilder sequenceCSV = new StringBuilder();
        sequenceCSV.append(name).append(",");
        sequenceCSV.append(description).append(",");
        sequenceCSV.append(sequence).append(",");
        sequenceCSV.append(sequenceLength).append(",");
        sequenceCSV.append(gc).append(",");

        sequenceCSV.append(getOrganization()).append(",");

        return "";
    }
}
