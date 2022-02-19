import java.util.Comparator;

public class SearchScore implements SearchResult {

    private String link;
    private double score;

    //SearchScore constructor
    public SearchScore(double sIn, String lIn) {
        link = lIn;
        score = sIn;
    }

    //getTitle method, returns SearchScore title
    @Override
    public String getTitle() {
        return link;
    }

    //getScore method, returns SearchScore score
    @Override
    public double getScore() {
        return score;
    }
}

//comparatorHelper helper class, allows for SearchScore to be sorted using Collections.sort
class comparatorHelper implements Comparator<SearchResult> {

    @Override
    public int compare(SearchResult o1, SearchResult o2) {

        int returnFlag;

        if (o1.getScore() > o2.getScore()) { returnFlag = -1; }
        else if (o2.getScore() < o2.getScore()) { returnFlag = 1; }
        else { returnFlag = 0; }

        return returnFlag;

    }
}
