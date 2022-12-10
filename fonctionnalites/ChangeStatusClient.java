import java.sql.*;
import java.lang.String;
import java.time.*;
import java.util.Scanner;

public class ChangeStatusClient{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "conanje"; // A remplacer pour votre compte, sinon genere une exception
    static final String PASSWD = "conanje";

    public ChangeStatusClient(String id_client){
    try{
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
    Scanner scan = new Scanner(System.in);
    PreparedStatement pst = conn.prepareStatement
    ("Select id_commande, status from Commande Join CommmandeClient on id_commande Where  id_client = ?");
    pst.setString(1, id_client);
    ResultSet commandes = pst.executeQuery();
    commandes.first();
    while(!commandes.next()){
        System.out.println(commandes.getString(1)+commandes.getString(2));
    }
    System.out.println("Quelle commande voulez-vous");
    String id_commande = scan.next();

    String status;
    System.out.println("Quel status voulez-vous affecter à la commande ?");
    status = scan.next();
    while(!status.equals("litige") && !status.equals("annulée par le client")){
        System.out.println("Mauvais status, veuillez vérifier");
        status = scan.next();
    }
    if(!status.equals("terminée")){
        pst = conn.prepareStatement("Update Commande Set status = ? Where id_commande = ?");
        pst.setString(1, status);
        pst.setString(2, id_commande);
        pst.executeUpdate();
    }
    else{
        pst = conn.prepareStatement("Update Commande Set status = ?, date_fin = ? Where id_commande = ?");
        pst.setString(1, status);
        pst.setLong(2, Timestamp.from(Instant.now()).getTime());
        pst.setString(3, id_commande);
        pst.executeUpdate();
    }

    pst.close();
    conn.close();
    }
    catch (SQLException e) {
        System.err.println("failed");
        e.printStackTrace(System.err);
    }
    }
}
