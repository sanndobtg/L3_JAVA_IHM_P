package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Classe principale pour l'application de chat JavaFX.
 */
public class Main extends Application {

    /**
     * Démarre l'application en chargeant l'interface utilisateur à partir du fichier FXML
     * et en l'affichant dans une fenêtre.
     *
     * @param primaryStage Le stage principal pour cette application, sur lequel
     *                     la scène de l'application est définie.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charge l'interface utilisateur à partir du fichier FXML
            Parent root = FXMLLoader.load(getClass().getResource("chat.fxml"));
            Scene scene = new Scene(root);

            // Définit la scène pour le stage principal et l'affiche
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Point d'entrée principal de l'application JavaFX.
     *
     * @param args Les arguments de la ligne de commande.
     */
    public static void main(String[] args) {
        // Lance l'application JavaFX
        launch(args);
    }
}