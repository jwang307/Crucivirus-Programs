import java.util.ArrayList;

public class Sequence {
    public final String name;
    private final String description;
    private final int sequenceLength;
    private final String sequence;
    private final String gc;
    private ArrayList<Annotation> annotations;

    private String comments;

    public Sequence(String name, String description, int sequenceLength, String sequence, String gc) {
        this.name = name;
        this.description = description;
        this.sequenceLength = sequenceLength;
        this.sequence = sequence;
        this.gc = gc;

        annotations = new ArrayList<>();
    }

    public void addAnnotation(Annotation annt, boolean intron) {
        if (intron) {
            Annotation.Type annotationType = annt.type;
            boolean annotationFound = false;
            for (int i = 0; i < annotations.size(); i++) {
                if (annotationType == annotations.get(i).type) {
                    annotations.get(i).addInterval(annt.getInterval(0)[0], annt.getInterval(0)[1]);

                    annotationFound = true;
                    break;
                }
            }

            if (!annotationFound) {
                annotations.add(annt);
            }
        } else {
            annotations.add(annt);
        }
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
        String[] annotationInfo =  new String[]{"None", "None", "", ""};
        for (int i = 0; i < annotations.size(); i++) {
            Annotation annotation = annotations.get(i);

            if (annotation.type == Annotation.Type.CAPSID) {
                annotationInfo[0] = annotation.toString();
            } else if (annotation.type == Annotation.Type.REP) {
                annotationInfo[1] = annotation.toString();
            } else {
                annotationInfo[2] += annotation.toString();
            }
        }
        return annotationInfo;
    }

    public String toCSV() {
        String[] annotationInfo = annotationInfo();

        StringBuilder sequenceCSV = new StringBuilder();
        sequenceCSV.append(name).append(",");
        sequenceCSV.append(description).append(",");
        sequenceCSV.append(sequence).append(",");
        sequenceCSV.append(sequenceLength).append(",");
        sequenceCSV.append(gc).append(",");
        sequenceCSV.append(annotationInfo[0]).append(",");
        sequenceCSV.append(annotationInfo[1]).append(",");
        sequenceCSV.append(getOrganization()).append(",");
        sequenceCSV.append(annotationInfo[2]).append(",");
        sequenceCSV.append(annotationInfo[3]);

        return sequenceCSV.toString();
    }
}
