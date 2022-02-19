import java.util.HashMap;
import java.util.HashSet;

public class URLMapping {

    private HashMap<Link, Integer> URLtoID;
    private HashMap<Integer, Link> IDtoURL;

    //URLMapping constructor
    public URLMapping(HashSet<Link> l) {
        URLtoID = URLtoID(l);
        IDtoURL = IDtoURL(l);
    }

    //get methods
    public HashMap<Link, Integer> getURLtoID() { return URLtoID; }
    public HashMap<Integer, Link> getIDtoURL() { return IDtoURL; }

    //URLtoID method, creates a mapping from URL to index that coincides with the mapping of IDtoURL
    public HashMap<Link, Integer> URLtoID(HashSet<Link> linkList) {
        HashMap<Link, Integer> map = new HashMap<>();
        int index = -1;

        for(Link link : linkList) {
            index++;
            map.put(link, index);
        }
        return map;
    }

    //IDtoURL method, creates a mapping from index to URL that coincides with the mapping of URLtoID
    public HashMap<Integer, Link> IDtoURL(HashSet<Link> linkList) {
        HashMap<Integer, Link> map = new HashMap<>();

        for (HashMap.Entry<Link, Integer> keyVal : this.URLtoID.entrySet()) { map.put(keyVal.getValue(), keyVal.getKey()); }
        return map;
    }
}
