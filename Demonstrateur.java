import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

public class Demonstrateur {

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
            System.out.println("Veuillez indiquer votre identifiant :");
            String id_resto = scann.nextLine();
            new ChangeStatusRestaurant(id_resto);
         }
         else if (choix == 2) {
            System.out.println("Adios");
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
            String id_client = scan.nextLine();
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
               System.out.println("Veuillez choisir une des fonctionnalités suivante :\n1- Commande\n2- Evaluation\n3- Supprimer votre compte\n4- Modifier une commande\n5- Quitter");
               choix = scann.nextInt();
               if (choix == 1) {
                  new ParcoursRestaurants();
                  System.out.println("Quel restaurant souhaiter vous ? (adresse mail)");
                  String mail_resto = scann.nextLine();
                  System.out.println("Veuillez indiquer le type de commande :");
                  String type_commande = scann.nextLine();
                  int nbr_personnes;
                  if (type_commande == "sur place") {
                     System.out.println("Veuillez indiquer le nombre de personnes :");
                     nbr_personnes = scann.nextInt();
                  } else 
                  {
                     nbr_personnes = 1;
                  }
                  String adresse_livraison;
                  if (type_commande == "livraison") {
                     System.out.println("Veuillez indiquer l'adresse de livraison :");
                     adresse_livraison = scann.nextLine();
                  } else {
                     pstmt = conn.prepareStatement
                        ("select adresse_resto from restaurant where mail_resto = ?");
                     pstmt.setString(1, mail_resto);
                     rset = pstmt.executeQuery();
                     assert(rset.next());
                     adresse_livraison = rset.getString(1);
                     rset.close();
                     pstmt.close();
                  }
                  ArrayList<Plat> plats = new ArrayList<Plat>();
                  choix = 1;
                  while (choix == 1) {
                     System.out.println("Veuillez indiquer l'identifiant du plat que vous voulez :");
                     int id_plat = scann.nextInt();
                     System.out.println("Veuillez indiquer la quantité :");
                     int quantite = scann.nextInt();
                     pstmt = conn.prepareStatement
                        ("select prix_plat from Menu where mail_resto = ? and id_plat = ?");
                     pstmt.setString(1, mail_resto);
                     pstmt.setInt(2, id_plat);
                     rset = pstmt.executeQuery();
                     assert(rset.next());
                     int prix_plat = rset.getInt(1);
                     plats.add(new Plat(id_plat, quantite, prix_plat));
                     rset.close();
                     pstmt.close();
                     System.out.println("Voulez vous ajouter un autre plat ?\n1- Oui\n2- Non");
                     choix = scann.nextInt();
                  }
                  System.out.println("Votre commande va bientôt être traitée.");
                  new Commande(mail_resto, nbr_personnes, id_client, adresse_livraison, type_commande, plats);
               } else if (choix == 2) {
                  // Ici on va afficher au client la liste des commndes terminée et non encore évalué
                  pstmt = conn.prepareStatement
                     ("select id_commande, date_commande, mail_resto, type_commande from commande natural join commandeclient where id_client = ? and status = 'terminé' and not exists (select * from evaluation where id_evaluation = id_commande)");
                  pstmt.setString(1, id_client);
                  rset = pstmt.executeQuery();
                  ArrayList<Integer> commandeEvaluer = new ArrayList<Integer>();
                  while (rset.next()) {
                     int id_commande = rset.getInt("id_commande");
                     int date_commande = rset.getInt("date_commande");
                     String mail_resto = rset.getString("mail_resto");
                     String type_commande = rset.getString("type_commande");
                     commandeEvaluer.add(id_commande);
                     System.out.println("--> ID : " + Integer.toString(id_commande) + " | Date de commande : " + Integer.toString(date_commande) + " | Mail resto : " + mail_resto + " | Type de commande : " + type_commande);
                  }
                  //
                  System.out.println("Veuillez entrer l'id de commande que vous voulez évaluer :");
                  int id_commande = scan.nextInt();
                  if (!commandeEvaluer.contains(id_commande)) {
                     System.out.println("Commande non présente ou déjà évaluée");
                  }
                  else {
                     new Evaluation(id_commande);
                  }
               } else if (choix == 3) {
                  System.out.println("Etes-vous sûr de vouloir supprimer votre compte ?\n1- Oui\n2- Non");
                  choix = scann.nextInt();
                  if (choix == 1) {
                     new DroitOubli(id_client);
                  }
               } else if (choix == 4) {
                  new ChangeStatusClient(id_client);
               } 
               else if (choix == 5) {
                  System.out.println("Adios");
               } else {
                  System.out.println("Choix incorrect !");
               }
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