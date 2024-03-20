package Client;

import java.io.*;
import java.net.*;

/**
 * Client pour se connecter à un serveur de chat.
 */
public class ChatClient {
    private final String hostname;
    private final int port;
    private String userName;
    private Socket socket;

    /**
     * Crée une instance de ChatClient.
     *
     * @param hostname L'adresse du serveur de chat.
     * @param port Le port du serveur de chat.
     */
    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Tente de se connecter au serveur de chat.
     *
     * @return true si la connexion est réussie, false sinon.
     */
    public boolean connect() {
        try {
            socket = new Socket(hostname, port);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // Envoyer le pseudo au serveur
            if (userName != null && !userName.isEmpty()) {
                writer.println(userName);
            }

            System.out.println("Connecté au serveur de chat");
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("Serveur introuvable: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erreur d'entrée/sortie: " + ex.getMessage());
        }
        return false;
    }

    /**
     * Se déconnecte du serveur de chat et libère les ressources associées.
     */
    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
                System.out.println("Déconnecté du serveur de chat");
            }
        } catch (IOException ex) {
            System.out.println("Erreur lors de la fermeture du socket: " + ex.getMessage());
        } finally {
            socket = null; // s'assurer que le socket est nul après la déconnexion
        }
    }

    /**
     * Vérifie si le client est connecté au serveur de chat.
     *
     * @return true si le client est connecté, false sinon.
     */
    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    /**
     * Obtient le socket connecté au serveur de chat.
     *
     * @return Le socket connecté au serveur.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Définit le nom d'utilisateur du client pour le chat.
     *
     * @param userName Le nom d'utilisateur à définir.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Obtient le nom d'utilisateur du client pour le chat.
     *
     * @return Le nom d'utilisateur du client.
     */
    public String getUserName() {
        return this.userName;
    }

    // La méthode main n'est pas nécessaire si le client est utilisé avec un contrôleur d'interface graphique.
}