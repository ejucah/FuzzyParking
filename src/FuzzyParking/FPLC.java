package FuzzyParking;

/**
 * Desenvolvido para o projeto da disciplina IA861, Sistemas Nebulosos
 * DAC, FEEC, Unicamp, segundo semestre de 2012.
 * @author Eduardo F. Jucá de Castro
 */
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.sourceforge.jFuzzyLogic.FIS;
//import net.sourceforge.jFuzzyLogic.rule.Rule;

public class FPLC
        implements Initializable {

    @FXML //  fx:id="button1"
    private Button button1; // Value injected by FXMLLoader
    @FXML //  fx:id="button2"
    private Button button2; // Value injected by FXMLLoader
    @FXML //  fx:id="car"
    private Rectangle car; // Value injected by FXMLLoader
    @FXML //  fx:id="car1"
    private Rectangle car1; // Value injected by FXMLLoader
    @FXML //  fx:id="car2"
    private Rectangle car2; // Value injected by FXMLLoader
    @FXML //  fx:id="comboBox1"
    private ComboBox<?> comboBox1; // Value injected by FXMLLoader
    @FXML //  fx:id="comboBox2"
    private ComboBox<?> comboBox2; // Value injected by FXMLLoader
    @FXML //  fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader
    @FXML //  fx:id="slot1"
    private Rectangle slot1; // Value injected by FXMLLoader
    @FXML //  fx:id="slot2"
    private Rectangle slot2; // Value injected by FXMLLoader
    private double x0, xF, xR, dX;
    private double y0, yF, yR, dY;
    private double v, w, l;
    private double theta, phi, dTheta;
    private double[] xyR = {0.0, 0.0};
    private double[] xyF = {0.0, 0.0};
    private double xMax = 500.0;
    private double xLim1 = 167.0;
    private double xLim2 = 222.0;
    private double xLim3 = 330.0;
    private double yLim1 = 105.0;
    private double yLim2 = 151.0;
    private double yLim3 = 162.0;
    private double yMax = 272.0;
    private double sP = 495.0;
    private double p = 55.0;
    private double d = 0.0;
    private double a = 0.0;
    private boolean diagonalParking = false;
    private boolean garageParking = false;
    private enum Behavior {WallFollowing, GarageParking, DiagonalParking};
    private Behavior behavior;
    private String emptySlot = "";
    private volatile boolean stop = false;
    private Timer update = null;
    // Load from 'FCL' file
    private String fileName = "fwfc.fcl";
    private InputStream in = FPLC.class.getResourceAsStream(fileName);
    private FIS fis = FIS.load(in, true);
    Timeline movement = new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (!stop) {
                // Avalia posição e seleciona comportamento
                if (xF >= sP) {
                    if (diagonalParking) {
                        behavior = Behavior.DiagonalParking;
                    } else if (garageParking) {
                        behavior = Behavior.GarageParking;
                    } else {
                        behavior = Behavior.WallFollowing;
                    }
                }
                // Excuta ação correspondente ao comportamento selecionado
                switch (behavior) {
                    case WallFollowing:
                        wallFollowing(true);
                        if (xF >= (xMax - 5.0)) {
                            stop = true;
                        }
                        break;
                    case GarageParking:
                        garageParking();
                        break;
                    case DiagonalParking:
                        diagonalParking();
                        break;
                    default:
                        wallFollowing(true);
                        break;
                }
                if (yF >= yMax || xF >= xMax) {
                    stop = true;
                }
                // Se ainda não chegou ao final
                if (!stop) {
                    // Atualiza posição
                    update = new Timer();
                    update.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            car.setRotate(Math.floor(theta));
                            car.setTranslateX(car.getTranslateX() + dX);
                            car.setTranslateY(car.getTranslateY() + dY);
                            update.cancel();
                        }
                    }, 50);
                } else {
                    // senão, para
                    movement.stop();
                }
            }
        }
    }));
    
    // Handler for Button[fx:id="button1"] onAction
    public void handleResetButton(ActionEvent event) {
        fillAll();
        car.setRotate(0.0);
        car.setTranslateX(0.0);
        car.setTranslateY(0.0);
        car.setLayoutX(0.0);
        car.setLayoutY(198.0);
        car.getTransforms().clear();
        x0 = car.getLayoutX();
        xR = x0;
        xF = x0 + l;
        y0 = car.getLayoutY();
        yR = y0 + w / 2;
        yF = yR;
        theta = car.getRotate();
        phi = 0;
        behavior = Behavior.WallFollowing;
        comboBox1.getSelectionModel().select(0);
        comboBox2.getSelectionModel().select(1);
        garageParking = false;
        diagonalParking = false;
        sP = 495.0;
        stop=false;
    }

    // Handler for ComboBox[fx:id="comboBox1"] onAction
    public void handleSlotNumber(ActionEvent event) {
        emptySlot = (String) comboBox1.getValue();
        switch (emptySlot) {
            case "Lotado":
                fillAll();
                break;
            case "Vaga 1":
                fillAll();
                diagonalParking = false;
                garageParking = true;
                sP = 262.0;
                car1.setFill(Color.TRANSPARENT);
                break;
            case "Vaga 2":
                fillAll();
                diagonalParking = true;
                garageParking = false;
                sP = 300.0;
                car2.setFill(Color.TRANSPARENT);
                break;
            default:
                fillAll();
                break;
        }
    }

    // Handler for Button[fx:id="button2"] onAction
    public void handleStartButton(ActionEvent event) {
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
        } else {
            // Show 
            //fis.chart();
            movement.setCycleCount(Timeline.INDEFINITE);
            movement.play();
        }
    }

    // Handler for ComboBox[fx:id="comboBox2"] onAction
    public void handleStartPosition(ActionEvent event) {
        switch ((String) comboBox2.getValue()) {
            case "Esquerda":
                car.setLayoutY(179.0);
                yR = car.getLayoutY() + w / 2;
                break;
            case "Centro":
                car.setLayoutY(198.0);
                break;
            case "Direita":
                car.setLayoutY(217.0);
                yR = car.getLayoutY() + w / 2;
                break;
            default:
                car.setLayoutY(198.0);
                break;
        }
        yF = yR;
    }

    public void fillAll() {
        car1.setFill(Color.DODGERBLUE);
        car2.setFill(Color.DODGERBLUE);
    }

    public void wallFollowing(boolean forward) {
        if (forward) {
            p = getRelativePosition(xF, yF);
        } else {
            p = getRelativePosition(xR, yR);
        }
        if (behavior == Behavior.DiagonalParking) {
            a = theta + 45.0;
        } else if (behavior == Behavior.GarageParking) {
            a = theta - 90.0;
        } else {
            a = theta;
        }
        // Set inputs
        fis.setVariable("relative_position", p);
        fis.setVariable("orientation", a);
        // Evaluate
        fis.evaluate();
        // Get result defuzzified
        phi = (fis.getVariable("steering_angle").getLatestDefuzzifiedValue());
        // Plot 
        //fis.getVariable("steering_angle").chartDefuzzifier(true);
        // Print ruleSet
        //System.out.println(fis);
        // Show each rule (and degree of support)
        //for (Rule r : fis.getFunctionBlock("fwfc").getFuzzyRuleBlock("No1").getRules()) {
        //     System.out.println(r);
        //}
        calculatePosition(forward);
        // Atualiza variáveis de posição
        theta = theta + dTheta;
        if (forward) {
            xF = xF + dX;
            yF = yF + dY;
            getR(xF, yF, theta);
            xR = xyR[0];
            yR = xyR[1];            
        } else {
            xR = xR + dX;
            yR = yR + dY;
            getF(xR, yR, theta);
            xF = xyF[0];
            yF = xyF[1];            
        }
    }
    
    public void garageParking() {
        // na posição de estacionar, vira o volante para a direita
        // Se ainda não entrou na vaga, retrocede
        if (yR >= yLim3) {
            phi = -30.0;
            calculatePosition(false);
            if (theta < 90.0) {
                theta = theta + 5.0;                
            }
            xR = xR + dX;
            yR = yR + dY;
            getF(xR, yR, theta);
            xF = xyR[0];
            yF = xyR[1];            
        } else {
            // Se está aproximadamente no centro da vaga, para
            if (xR <= xLim1 && yR <= yLim1) {
                stop = true;
            // Se ainda não está próximo ao fundo da vaga, retrocede    
            } else if (yR > yLim1) {
                wallFollowing(false);
            // Se ainda não está próximo a frente da vaga, avança
            } else if (yF < yLim2) {
                wallFollowing(true);
            }
        }
    }

    public void diagonalParking() {
        if (xF < xLim3 ) {
            theta = theta - 6.2;
            phi = 0.0;
            calculatePosition(true);
            // Atualiza variáveis de posição
            xF = xF + dX;
            yF = yF + dY;
            getR(xF, yF, theta);
            xR = xyR[0];
            yR = xyR[1];
        } else {
            wallFollowing(true);
        }
        if (xF > 435.0) {
            stop = true;
        }
    }
    
    public void calculatePosition(boolean forward) {
        if (forward) {
            dX = (v * Math.cos(Math.toRadians(theta + phi)));
            dY = (v * Math.sin(Math.toRadians(theta + phi)));
            dTheta = (v * (Math.sin(Math.toRadians(phi)) / l));
        } else {
            dX = -(v * Math.cos(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi)));
            dY = -(v * Math.sin(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi)));
            dTheta = (v * (Math.sin(Math.toRadians(phi)) / l));
        }
    }

    public double getRelativePosition(double x, double y) {
        d = yMax - y;
        if (behavior == Behavior.GarageParking) {
            d = x - xLim2;
        } else if (behavior == Behavior.DiagonalParking) {
            d = (Math.abs(x - y - 226.0) / Math.sqrt(2.0));
        }
        return d;
        
    }

    public void getR(double x, double y, double theta) {
        if (theta > -270 && theta < -180) {
            xyR[0] = x + Math.floor(l * Math.sin(Math.toRadians(270 - Math.abs(theta))));
            xyR[1] = y - Math.floor(l * Math.cos(Math.toRadians(270 - Math.abs(theta))));
        } else if (theta == -180) {
            xyR[0] = x + l;
            xyR[1] = y;
        } else if (theta > -180 && theta < -90) {
            xyR[0] = Math.floor(l * Math.sin(Math.toRadians(90 - Math.abs(theta)))) - x;
            xyR[1] = Math.floor(l * Math.cos(Math.toRadians(90 - Math.abs(theta)))) - y;
        } else if (theta == -90) {
            xyR[0] = x;
            xyR[1] = y + l;
        } else if (theta > -90 && theta < 0) {
            xyR[0] = x - Math.floor(l * Math.cos(Math.toRadians(Math.abs(theta))));
            xyR[1] = y + Math.floor(l * Math.sin(Math.toRadians(Math.abs(theta))));
        } else if (theta == 0) {
            xyR[0] = x - l;
            xyR[1] = y;
        } else if (theta > 0 && theta < 90) {
            xyR[0] = x - Math.floor(l * Math.cos(Math.toRadians(Math.abs(theta))));
            xyR[1] = y - Math.floor(l * Math.sin(Math.toRadians(Math.abs(theta))));
        }
    }

    public void getF(double x, double y, double theta) {
        if (theta > -270 && theta < -180) {
            xyF[0] = x - Math.floor(l * Math.sin(Math.toRadians(270 - Math.abs(theta))));
            xyF[1] = y + Math.floor(l * Math.cos(Math.toRadians(270 - Math.abs(theta))));
        } else if (theta == -180) {
            xyF[0] = x - l;
            xyF[1] = y;
        } else if (theta > -180 && theta < -90) {
            xyF[0] = x - Math.floor(l * Math.sin(Math.toRadians(90 - Math.abs(theta))));
            xyF[1] = y - Math.floor(l * Math.cos(Math.toRadians(90 - Math.abs(theta))));
        } else if (theta == -90) {
            xyF[0] = x;
            xyF[1] = y - l;
        } else if (theta > -90 && theta < 0) {
            xyF[0] = x - Math.floor(l * Math.sin(Math.toRadians(Math.abs(theta))));
            xyF[1] = y + Math.floor(l * Math.cos(Math.toRadians(Math.abs(theta))));
        } else if (theta == 0) {
            xyF[0] = x + l;
            xyF[1] = y;
        } else if (theta > 0 && theta < 90) {
            xyF[0] = x - Math.floor(l * Math.cos(Math.toRadians(Math.abs(theta))));
            xyF[1] = y - Math.floor(l * Math.sin(Math.toRadians(Math.abs(theta))));
        }
    }

    /**
     *
     * @param fxmlFileLocation
     * @param resources
     */
    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert button1 != null : "fx:id=\"button1\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert button2 != null : "fx:id=\"button2\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert car != null : "fx:id=\"car\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert car1 != null : "fx:id=\"car1\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert car2 != null : "fx:id=\"car2\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert comboBox1 != null : "fx:id=\"comboBox1\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert comboBox2 != null : "fx:id=\"comboBox2\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert slot1 != null : "fx:id=\"slot1\" was not injected: check your FXML file 'ParkingLot.fxml'.";
        assert slot2 != null : "fx:id=\"slot2\" was not injected: check your FXML file 'ParkingLot.fxml'.";

        comboBox1.setVisibleRowCount(4);
        comboBox1.setEditable(false);
        comboBox1.getSelectionModel().select(0);
        comboBox2.setVisibleRowCount(3);
        comboBox2.setEditable(false);
        comboBox2.getSelectionModel().select(1);
        v = 5.0;
        w = car.getHeight();
        l = car.getWidth();
        x0 = car.getLayoutX();
        xR = x0;
        xF = x0 + l;
        y0 = car.getLayoutY();
        yR = y0 + w / 2;
        yF = yR;
        theta = car.getRotate();
        phi = 0;
        behavior = Behavior.WallFollowing;
    }
}