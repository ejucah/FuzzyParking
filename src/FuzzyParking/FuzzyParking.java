package FuzzyParking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Desenvolvido para o projeto da disciplina IA861, Sistemas Nebulosos
 * DAC, FEEC, Unicamp, segundo semestre de 2012.
 * @author Eduardo F. Jucá de Castro
 * 
 * Copyright © 2012 Eduardo Ferreira Jucá de Castro (e.jucah@ieee.og)
 * 
 * Este arquivo é parte do programa FuzzyParking.
 * 
 * FuzzyParking é um software livre; você pode redistribui-lo e/ou 
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como 
 * publicada pela Fundação do Software Livre (FSF); na versão 2 da 
 * Licença, ou qualquer versão.
 * 
 * Este programa é distribuido na esperança que possa ser  útil, 
 * mas SEM NENHUMA GARANTIA; sem uma garantia implícita de 
 * ADEQUAÇÃO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. 
 * Veja a Licença Pública Geral GNU para maiores detalhes.
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral 
 * GNU junto com este programa, se não, escreva para a Fundação 
 * do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 * 
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
