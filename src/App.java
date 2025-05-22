import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("CMathMath IDE");
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
