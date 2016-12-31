import java.io.*;
import java.net.*; 
import java.util.*;


public class Serveur {

    private static final String USAGE = "Usage: java Serveur";

    /** N du port sur lequel le serveur fonctionne  */
    private static final int PORT_NUMBER = 8008;

    /**liste des clients du serveur ,
     * */
    private List<PrintWriter> clients;

    /** creation d'un nouveau server. */
    public Serveur() {
        clients = new LinkedList<PrintWriter>();
    }

    /** demarrerle  serveur . */
    public void start() {
        System.out.println("Le serveur est en marche sur le port "
                           + PORT_NUMBER + "!"); 
        try {
            ServerSocket s = new ServerSocket(PORT_NUMBER); 
            for (;;) {
                Socket incoming = s.accept(); 
                new ClientHandler(incoming).start(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("le serveur s'est arreter."); 
    }

    /** Ajouter un nouveau client  */
    private void addClient(PrintWriter out) {
        synchronized(clients) {
            clients.add(out);
        }
    }

    /** retirer un client . */
    private void removeClient(PrintWriter out) {
        synchronized(clients) {
            clients.remove(out);
        }
    }

    /** enovyer le msg a tout les client  */
    private void broadcast(String msg) {
        for (PrintWriter out: clients) {
            out.println(msg);
            out.flush();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(USAGE);
            System.exit(-1);
        }
        new Serveur().start();
    }

    /** thread qui recoi le msg et l'envoi a tout les client du serveur en broadcasts
     * */
    private class ClientHandler extends Thread {

        /** Socket pour lire les message du client  */
        private Socket incoming; 

        /** Crée un hander pour servir le client sur le socket donné. */
        public ClientHandler(Socket incoming) {
            this.incoming = incoming;
        }

        /** Démarre la réception et la diffusion de messages. */
        public void run() {
            PrintWriter out = null;
            try {
                out = new PrintWriter(
                        new OutputStreamWriter(incoming.getOutputStream()));
                
                // informer le serveur du nouveau client 
                Serveur.this.addClient(out);

                out.print("Bienvenu au chat  ");
         
                out.flush();

                BufferedReader in 
                    = new BufferedReader(
                        new InputStreamReader(incoming.getInputStream())); 
                for (;;) {
                    String msg = in.readLine(); 
                    if (msg == null) {
                        break; 
                    } else {
                        if (msg.trim().equals("BYE")) 
                            break; 
                        System.out.println("Received: " + msg);
                        // difuser le msg recu 
                        Serveur.this.broadcast(msg);
                    }
                }
                incoming.close(); 
                Serveur.this.removeClient(out);
            } catch (Exception e) {
                if (out != null) {
                    Serveur.this.removeClient(out);
                }
                e.printStackTrace(); 
            }
        }
    }
}
