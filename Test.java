import java.util.Scanner;
import java.sql.*;

public class Test {

   static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
   static final String USER = "meghraoa"; // A remplacer pour votre compte, sinon genere une exception
   static final String PASSWD = "abcd";
   public static void main(String[] args) {
      System.out.println("Bienvenue dans GrenobleEat\nVous êtes ?\n1- Un restaurant\n2- Un client");
      Scanner scann = new Scanner(System.in);
      int choix = scann.nextInt();
      if (choix == 1) {
         System.out.println("Veuillez choisir une des fonctionnalités suivante :\n1- Voir commandes\2- Quitter");
         choix = scann.nextInt();
         if (choix == 1) {

         }
         else if (choix == 2) {

         } else {
            System.out.println("Choix incorrect !");
         }
      }
      else if (choix == 2) {
         try{
            // Enregistrement du driver Oracle
            System.out.println("Loading Oracle thin driver...");
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            System.out.println("loaded.");
        
            // Etablissement de la connexion
            System.out.println("Connecting to the database...");
            Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
            System.out.println("connected.\n");
        
            // Demarrage de la transaction (implicite dans notre cas)
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(conn.TRANSACTION_SERIALIZABLE);
            
            // Execution des requetes
            PreparedStatement pstmt = conn.prepareStatement
            ("select mdp from Client where id_client = ?");
            System.out.println("Identifiant :");
            java.util.Scanner scan = new Scanner(System.in);
            String id_client= scan.nextLine();
            pstmt.setString(1, id_client);
            ResultSet rset = pstmt.executeQuery();
            System.out.println("Mot de passe :");
            String psswd = scan.nextLine();
            if (!rset.next()) System.out.println("Mauvais identifiant !");
            else if (!psswd.equals(rset.getString(1))){
                System.out.println("Mauvais mot de passe !");
            }
            else {
               System.out.println("Connexion réussie\n");
               System.out.println("Veuillez choisir une des fonctionnalités suivante :\n1- Commande\n2- Evaluation\n3- Supprimer votre compte\n4- Quitter");
               choix = scann.nextInt();
            }
            // Fermeture de connexion
            rset.close();
            pstmt.close();
            conn.close();
         }
         catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
         }
      }
      else {
         System.out.println("Choix incorrect !");
      }
   }
}