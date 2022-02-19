public class Maths {

    //log base 2
    public float log2(float val) {
        return (float) ((Math.log(val)) / (Math.log(2)));
    }

    //euclidDist method
    public double euclidDist(float[] aVect, float[] bVect) {
        if (aVect.length != bVect.length) { return 0; }
        else {
            double sum = 0;
            for (int i = 0; i < aVect.length; i++) {
                double dist = aVect[i] - bVect[i];
                sum += (dist * dist);
            }
            return Math.sqrt(sum);
        }
    }

    //cossineSim method
    public float cossineSim(float[] qVect, float[] lVect) {

        float cossim = 0;
        double num = 0;
        double denLeft = 0;
        double denRight = 0;

        for (int i = 0; i < qVect.length; i++) {
            num += (qVect[i] * lVect[i]);
            denLeft += (qVect[i] * qVect[i]);
            denRight += (lVect[i] * lVect[i]);
        }
        if ((denLeft == 0) || (denRight == 0)) { cossim = 0; }
        else { cossim = ((float)num / ((float)(Math.sqrt(denLeft)) * ((float)Math.sqrt(denRight))));}

        return cossim;
    }
}
