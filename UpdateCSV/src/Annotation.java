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
    private String sense;

    public Annotation(int start, int end, String name, Type type, String sense) {
        this.type = type;
        this.name = name;
        this.sense = sense;

        intervals = new ArrayList<>();
        intervals.add(new Interval(start, end));
    }

    public int getLength() {
        int sum = 0;
        for (Interval interval : intervals) {
            sum += interval.getLength() + 1;
        }
        return sum;
    }

    public boolean positiveSense() {
        return sense.equals("+");
    }

    public String toString() {
        StringBuilder annotationString = new StringBuilder();
        annotationString.append(type.toString()).append("; ");
        annotationString.append(name).append("; ");
        annotationString.append("LENGTH: ").append(getLength()).append("; ");
        annotationString.append("Intervals: ").append(intervals.size()).append("; ");
        for (Interval interval : intervals) {
            annotationString.append(interval.toString()).append("; ");
        }

        return annotationString.toString();
    }

    public void addInterval(int start, int end) {
        intervals.add(new Interval(start, end));
    }

    public int[] getInterval(int index) {
        if (index >= intervals.size()) throw new IndexOutOfBoundsException("index is greater than number of intervals");

        return new int[]{intervals.get(index).start, intervals.get(index).end};
    }

}