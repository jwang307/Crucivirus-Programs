import java.util.ArrayList;

public class Annotation {

    private class Interval {
        private final int start;
        private final int end;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getLength() {
            return Math.abs(start - end);
        }

        public String toString() {
            return start + "->" + end;
        }
    }

    public enum Type {
        CAPSID,
        REP,
        ITERON,
        STEM_LOOP,
        OTHER
    }

    public Type type;
    public String name;
    private ArrayList<Interval> intervals;
    private int length;

    public Annotation(int start, int end, String name, Type type) {
        this.type = type;
        this.name = name;
        intervals.add(new Interval(start, end));
    }

    public int getLength() {
        int sum = 0;
        for (Interval interval : intervals) {
            sum += interval.getLength();
        }
        return 1;
    }

    public boolean positiveSense() {
        return intervals.get(0).start - intervals.get(0).end < 0;
    }

    public String toString() {
        StringBuilder annotationString = new StringBuilder();
        annotationString.append(type.toString()).append("; ");
        annotationString.append(name).append("; ");
        annotationString.append(length).append("; ");
        annotationString.append("Intervals: ").append(intervals.size()).append("; ");
        for (Interval interval : intervals) {
            annotationString.append(interval.toString()).append("; ");
        }
        annotationString.delete(annotationString.length() - 2, annotationString.length());

        return annotationString.toString();
    }

    public void addInterval(int start, int end) {
        intervals.add(new Interval(start, end));
    }
}