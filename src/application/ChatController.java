package application;

import Client.ChatClient;
import Client.ReadThread;
import Client.WriteThread;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    private ChatClient client;
    private ReadThread readThread;
    private WriteThread writeThread;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea areaDiscussion;

    @FXML
    private TextField entreeAdresseIP;

    @FXML
    private TextField entreeMessage;

    @FXML
    private TextField entreePort;

    @FXML
    private TextField entreePseudo;

    @FXML
    private Label labelEtatConnexion;

    @FXML
    void actionBoutonConnexion(ActionEvent event) {
        String hostname = entreeAdresseIP.getText();
        int port = Integer.parseInt(entreePort.getText());
        String userName = entreePseudo.getText();

        client = new ChatClient(hostname, port);
        client.setUserName(userName);
        if (client.connect()) {
            readThread = new ReadThread(client.getSocket(), client, areaDiscussion);
            writeThread = new WriteThread(client.getSocket(), client, entreeMessage);
            readThread.start();
            writeThread.start();
            labelEtatConnexion.setText(userName + "Connecté ");
        } else {
            labelEtatConnexion.setText("Impossible de se connecter au serveur.");
        }
    }

    @FXML
    void actionBoutonDeconnexion(ActionEvent event) {
        if (client != null) {
            client.disconnect();
            if (readThread != null) {
                readThread.interrupt();
                readThread = null;
            }
            if (writeThread != null) {
                writeThread.interrupt();
                writeThread = null;
            }
            labelEtatConnexion.setText("Déconnecté");
        }
    }

    @FXML
    void actionBoutonEnvoyer(ActionEvent event) {
        if (client != null && client.isConnected()) {
            writeThread.sendMessage(entreeMessage.getText());
            areaDiscussion.appendText(entreePseudo.getText() + ": " + entreeMessage.getText() + "\n");
            entreeMessage.clear();
        } else {
            labelEtatConnexion.setText("Vous n'êtes pas connecté.");
        }
    }

    @FXML
    void initialize() {
        assert areaDiscussion != null : "fx:id=\"areaDiscussion\" was not injected: check your FXML file 'chat.fxml'.";
        assert entreeAdresseIP != null : "fx:id=\"entreeAdresseIP\" was not injected: check your FXML file 'chat.fxml'.";
        assert entreeMessage != null : "fx:id=\"entreeMessage\" was not injected: check your FXML file 'chat.fxml'.";
        assert entreePort != null : "fx:id=\"entreePort\" was not injected: check your FXML file 'chat.fxml'.";
        assert entreePseudo != null : "fx:id=\"entreePseudo\" was not injected: check your FXML file 'chat.fxml'.";
        assert labelEtatConnexion != null : "fx:id=\"labelEtatConnexion\" was not injected: check your FXML file 'chat.fxml'.";
    }
}