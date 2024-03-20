package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Gère la connexion d'un client au serveur de chat.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private String clientName;

    /**
     * Construit un gestionnaire pour un client spécifique.
     *
     * @param socket Le socket connecté au client.
     * @param server Le serveur de chat associé.
     */
    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Écoute les messages entrants du client et les diffuse.
     */
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            clientName = reader.readLine();
            server.broadcastMessage(clientName + " has joined the chat.", this);

            String message;
            while ((message = reader.readLine()) != null) {
                if ("logout".equalsIgnoreCase(message)) {
                    break;
                }
                server.broadcastMessage("[" + clientName + "]: " + message, this);
            }

            server.removeClient(this);
            socket.close();
            server.broadcastMessage(clientName + " has left the chat.", this);
        } catch (IOException ex) {
            System.out.println("Error in ClientHandler: " + ex.getMessage());
        }
    }

    /**
     * Envoie un message au client connecté par ce gestionnaire.
     *
     * @param message Le message à envoyer.
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * Obtient le nom du client géré par ce gestionnaire.
     *
     * @return Le nom du client.
     */
    public String getClientName() {
        return clientName;
    }
}
