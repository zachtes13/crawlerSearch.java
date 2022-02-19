public class AdjacencyMatrix {

    int n;
    float matrix[][];

    //AdjacencyMatrix constructor
    public AdjacencyMatrix(int sizeLinkList) {
        n = sizeLinkList;
        matrix = new float[n][n];
    }

    //get methods
    public int getN() { return n; }
    public float[][] getMatrix() { return matrix; }

    //addNode method, adds a given node to the given coordinate in the matrix
    public void addNode(int currLink, int linkOut, float val) { matrix[currLink][linkOut] = val; }

    //scalarMult method, given a scalar, performs scalar multiplication on a given matrix
    public AdjacencyMatrix scalarMult(float scalar, int size) {
        AdjacencyMatrix scaleMat = new AdjacencyMatrix(size);

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                scaleMat.matrix[x][y] = this.matrix[x][y] * scalar;
            }
        }
        return scaleMat;
    }

    //matMult method, performs matrix multiplication on two given vectors
    public float[] matMult(float[] aVect) {
        float[] prodVect = new float[n];

        if (aVect.length != this.getN()) { return null; }
        else {
            for (int x = 0; x < n; x++) {
                float sum = 0;
                for (int y = 0; y < n; y++) {
                    sum += this.matrix[y][x] * aVect[y];
                }
                prodVect[x] = sum;
            }
        }
        return prodVect;
    }
}

