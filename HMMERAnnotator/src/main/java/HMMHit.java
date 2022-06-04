public class HMMHit {

    public final double eVal;
    public final float score;
    public final float bias;
    public final String seqName;
    public final int orfNum;

    public HMMHit(double eVal, float score, float bias, String name, int orfNum) {
        this.eVal = eVal;
        this.bias = bias;
        this.score = score;
        this.seqName = name;
        this.orfNum = orfNum;
    }

    public boolean equals(HMMHit that) {
        if (/*(this.eVal == that.eVal) && (this.score == that.score) &&
                (this.bias == that.bias) &&*/ this.seqName.equals(that.seqName)) {
            return true;
        }
        return false;
    }

    public static String getSeqName(String tag) {
        return tag.split("_-_")[0];
    }

    public static int getORFNum(String tag) {
        String orfTag = tag.split("_-_")[1];

        return Integer.parseInt(orfTag.split("_")[1]);
    }
}
