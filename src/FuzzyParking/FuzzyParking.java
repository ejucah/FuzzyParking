package FuzzyParking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Desenvolvido para o projeto da disciplina IA861, Sistemas Nebulosos
 * DAC, FEEC, Unicamp, segundo semestre de 2012
 * @author Eduardo F. Jucá de Castro
 */
public class FuzzyParking extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("SimpleParkingLot.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Fuzzy Parking");
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
