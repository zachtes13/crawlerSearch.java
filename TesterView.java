import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

public class TesterView extends Pane {

    Tester model;
    ListView<String> linksList;
    Button search;
    ToggleButton boost;
    TextField queryField;
    Label queryLabel;
    Label resultsLabel;
    List<SearchResult> searchResultsList;

    //TesterView constructor
    public TesterView(Tester modelIn) {

        searchResultsList = new ArrayList<SearchResult>();
        model = modelIn;

        //add links list
        linksList = new ListView<String>();
        linksList.relocate(10, 100);
        linksList.setPrefSize(430, 236);

        //add results label
        resultsLabel = new Label("Search Results");
        resultsLabel.relocate(10, 67);
        resultsLabel.setPrefSize(80, 30);

        //add query field
        queryField = new TextField("");
        queryField.relocate(10, 38);
        queryField.setPrefSize(430, 15);

        //add query label
        queryLabel = new Label("Search Query");
        queryLabel.relocate(10, 5);
        queryLabel.setPrefSize(80, 30);

        //add search button
        search = new Button("Search");
        search.relocate(460, 18);
        search.setPrefSize(70,25);

        //add boost button
        boost = new ToggleButton("Boost");
        boost.relocate(460, 58);
        boost.setPrefSize(70,25);

        //add all to view
        getChildren().addAll(linksList, search, boost, queryField, queryLabel, resultsLabel);

        update();
    }

    //get methods
    public Button getSearch() { return search; }
    public ToggleButton getBoost() { return boost; }
    public TextField getQueryField() { return queryField; }
    //set methods
    public void setSearchResultsList(List<SearchResult> listIn) { searchResultsList = listIn; }

    //update method, updates view
    public void update() {
        ArrayList<String> linksResults = new ArrayList<>();

        if (!searchResultsList.isEmpty() && !(searchResultsList.get(0).getScore() == 0)) {
            for (int i = 0; i < 10; i++) {
                linksResults.add("Page: " + searchResultsList.get(i).getTitle() + " with a score of " + searchResultsList.get(i).getScore());
            }
        } else { linksResults.add("Search did not match any documents"); }

        linksList.setItems(FXCollections.observableArrayList(linksResults));
    }
}
