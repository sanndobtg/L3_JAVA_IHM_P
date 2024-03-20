package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Thread de lecture pour le client de chat. Il lit les messages du serveur et
 * les affiche dans la zone de discussion.
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private ChatClient client;
    private TextArea areaDiscussion;

    /**
     * Construit un ReadThread pour lire les messages du serveur.
     *
     * @param socket Le socket connecté au serveur.
     * @param client Le client de chat.
     * @param areaDiscussion La zone de texte où les messages seront affichés.
     */
    public ReadThread(Socket socket, ChatClient client, TextArea areaDiscussion) {
        this.client = client;
        this.areaDiscussion = areaDiscussion;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            System.out.println("Error getting input stream: " + e.getMessage());
            interrupt();
        }
    }

    /**
     * Exécute le thread. Lit les messages du serveur et les affiche dans la zone
     * de discussion.
     */
    public void run() {
        while (!isInterrupted()) {
            try {
                final String response = reader.readLine();
                if (response == null) {
                    break; // Le serveur a fermé la connexion
                }
                Platform.runLater(() -> {
                    areaDiscussion.appendText(response + "\n");
                });
            } catch (IOException e) {
                System.out.println("Error reading from server: " + e.getMessage());
                break;
            }
        }
        // Nettoyer les ressources et mettre à jour l'interface utilisateur si nécessaire
        Platform.runLater(() -> {
            if (client != null) {
                client.disconnect();
            }
            // Mettre à jour l'interface utilisateur pour refléter la déconnexion si nécessaire
        });
    }
}