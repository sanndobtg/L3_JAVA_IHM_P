package Server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Description de la classe Serveur qui accepte les connexions des clients et gère les messages.
 */
public class ChatServer {
    private int port;
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    /**
     * Construit un ChatServer qui écoute sur le port renseigné.
     *
     * @param port Le port d'écoute du serveur.
     */
    public ChatServer(int port) {
        this.port = port;
    }

    /**
     * Démarre le serveur de chat et attend les connexions entrantes.
     *
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Chat Server is listening on port " + port);

        while (true) {
            try {
                System.out.println("Waiting for a new connection...");
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            } catch (IOException e) {
                System.out.println("Server exception: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Envoie un message à tous les clients sauf à l'expéditeur.
     *
     * @param message Le message à diffuser.
     * @param sender  Le client qui envoie le message.
     */
    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Supprime un client de la liste des clients connectés.
     *
     * @param client Le client à supprimer.
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected: " + client.getClientName());
    }

    /**
     * Le point d'entrée principal du serveur de chat.
     *
     * @param args Les arguments de la ligne de commande, attend le numéro de port.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(port);
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
        }
    }
}
