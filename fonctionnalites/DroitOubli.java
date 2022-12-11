import java.sql.*;

public class DroitOubli {
   static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
   static final String USER = "meghraoa"; // A remplacer pour votre compte, sinon genere une exception
   static final String PASSWD = "abcd";
   
   public DroitOubli(String id_client) {
      try
      {
      // Enregistrement du driver Oracle
      System.out.println("Loading Oracle thin driver...");
      DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
      System.out.println("loaded.");

      // Etablissement de la connexion
      System.out.println("Connecting to the database...");
      Connection conn = DriverManager.getConnection(CONN_URL, USER, PASSWD);
      System.out.println("connected.");

      // Demarrage de la transaction (implicite dans notre cas)
      conn.setAutoCommit(false);
      conn.setTransactionIsolation(conn.TRANSACTION_SERIALIZABLE);

      // Les requetes
      // On génère un nouveau identifiant pour le client
      PreparedStatement pstmt = conn.prepareStatement
         ("select count(*) from client where nom_client is null");
      ResultSet rset = pstmt.executeQuery();
      rset.next();
      String nouveau_id = rset.getString(1) + "D";
      rset.close();
      pstmt.close();

      // D'abord on insère le nouveau identifiant dans la table client
      pstmt = conn.prepareStatement("insert into client values(?, GrenobleEats, Grenoble, Eats)");
      pstmt.setString(1, nouveau_id);
      pstmt.executeUpdate();
      pstmt.close();

      // Puis on l'insère dans la table commandeclient aussi
      pstmt = conn.prepareStatement
      ("insert into CommandeClient(id_client, id_commande, nbr_personnes) select ?, id_commande, nbr_personnes from commandeclient where id_client = ?");
      pstmt.setString(1, nouveau_id);
      pstmt.setString(2, id_client);
      pstmt.executeUpdate();
      pstmt.close();

      // Puis on supprime l'identifiant
      pstmt = conn.prepareStatement
      ("delete from Client where id_client = ?");
      pstmt.setString(1, id_client);
      pstmt.executeUpdate();
      pstmt.close();

      // Enfin, on supprime les adresses de livraison
      pstmt = conn.prepareStatement
      ("update commande set adresse_livraison = '' where id_commande = (select id_commande from commandeclient where id_client = ?)");
      pstmt.setString(1, nouveau_id);
      pstmt.executeUpdate();

      // Transaction terminée
      conn.commit();
      
      System.out.println("Votre compte a été bien supprimé.");

      // Fermetures
      pstmt.close();
      conn.close();
      }
      catch (SQLException e) 
      {
         System.err.println("failed");
         e.printStackTrace();
      }
   }
}