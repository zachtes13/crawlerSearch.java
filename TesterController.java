import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.*;


public class TesterController extends Application {

    Tester model;
    TesterView view;

    //start method, sets the stage
    @Override
    public void start(Stage primaryStage) {

        model = new Tester();
        view = new TesterView(model);

        view.getSearch().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { handleSearch(); }
        });

        view.getBoost().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {  }
        });

        primaryStage.setTitle("FROOTGLE");
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(view, 550, 350));
        primaryStage.show();
        view.update();
    }

    //main method, launches view
    public static void main(String[] args) { launch(args); }

    //handleSearch method, performs correct processing given a user's search
    public void handleSearch() {
        model.initialize();
        String inQuery = view.getQueryField().getText();
        boolean boostVal = view.getBoost().isSelected();
        view.setSearchResultsList(model.search(inQuery, boostVal, 10));
        view.update();
    }
}
