import java.util.HashMap;

public class PageRankCalculator {

    URLMapping map;
    int n;
    AdjacencyMatrix pageRankMat;

    //PageRankCalculator constructor
    public PageRankCalculator(URLMapping m, int size) {
        map = m;
        n = size;
        pageRankMat = new AdjacencyMatrix(n);
    }

    //get methods
    public URLMapping getMap() { return map; }

    //pageRank method, calculates the page rank vector using its URLMapping and AdjacencyMatrix
    public float[] pageRank() {
        AdjacencyMatrix AdjMat = new AdjacencyMatrix(n);
        HashMap<Integer, Integer> oneFreq = new HashMap<>();

        for (int x = 0; x < AdjMat.getN(); x++) {
            int count = 0;
            for (int y = 0; y < AdjMat.getN(); y++) {
                if (map.getIDtoURL().get(x).getLinksTo().contains(map.getIDtoURL().get(y).getURLIn())) {
                    AdjMat.addNode(x, y, 1);
                    count++;
                    oneFreq.put(x, count);
                } else {
                    AdjMat.addNode(x, y, 0);
                }
            }
        }
        //transProbMat
        AdjacencyMatrix transProbMat = new AdjacencyMatrix(n);
        for (int x = 0; x < transProbMat.getN(); x++) {
            for (int y = 0; y < transProbMat.getN(); y++) {
                if (oneFreq.get(x) == 0) {
                    float maths = ((float)1 / (float)transProbMat.getN());
                    transProbMat.addNode(x, y, (int)maths);
                }
                else if (AdjMat.getMatrix()[x][y] == 1) {
                    float maths = ((float)1 / (float)oneFreq.get(x));
                    transProbMat.addNode(x, y, maths);
                } else {
                    transProbMat.addNode(x, y, 0);
                }
            }
        }
        //scaleMat (transProbMat * (1 - a)
        AdjacencyMatrix scaleMat = transProbMat.scalarMult(((float)1 - (float)0.1), n);
        //matAdd (scaleMat + (a / n))
        AdjacencyMatrix matAdd = new AdjacencyMatrix(n);
        for (int x = 0; x < matAdd.getN(); x++) {
            for (int y = 0; y < matAdd.getN(); y++) {
                matAdd.addNode(x, y, scaleMat.getMatrix()[x][y] + ((float)0.1 / (float)matAdd.getN()));
            }
        }
        //power iteration
        float[] lastVect = new float[n];
        for (int i = 0; i < n; i++) { lastVect[i] = ((float)1 / (float)n); }
        double euclidD = 1;
        Maths calculator = new Maths();

        while (euclidD > 0.0001) {
            float[] currVect = matAdd.matMult(lastVect);
            euclidD = calculator.euclidDist(lastVect, currVect);
            lastVect = currVect;
        }
        return lastVect;
    }

}
