package Client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.control.TextField;

/**
 * Thread d'écriture pour le client de chat. Il envoie des messages au serveur.
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private ChatClient client;
    private Socket socket;
    private TextField entreeMessage;

    /**
     * Construit un WriteThread pour envoyer des messages au serveur.
     *
     * @param socket Le socket connecté au serveur.
     * @param client Le client de chat.
     * @param entreeMessage Le champ de texte pour entrer les messages.
     */
    public WriteThread(Socket socket, ChatClient client, TextField entreeMessage) {
        this.client = client;
        this.socket = socket;
        this.entreeMessage = entreeMessage;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException e) {
            System.out.println("Error getting output stream: " + e.getMessage());
            interrupt();
        }
    }

    /**
     * Envoie un message au serveur.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }
    
    /**
     * Exécute le thread. Cette méthode n'est pas utilisée dans ce cas car la
     * méthode sendMessage est appelée directement depuis le contrôleur. Cependant,
     * si vous souhaitez gérer le nettoyage ou d'autres logiques, vous pouvez
     * l'implémenter ici.
     */
    public void run() {
        // La méthode run n'est pas utilisée dans ce cas.
    }

    /**
     * Appelle cette méthode pour fermer le PrintWriter et libérer les ressources.
     */
    public void closeWriter() {
        if (writer != null) {
            writer.close();
        }
    }
}