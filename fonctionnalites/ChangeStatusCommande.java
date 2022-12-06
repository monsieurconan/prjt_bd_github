import java.sql.*;
import java.lang.String;

public class ChangeStatusCommande{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "conanje"; // A remplacer pour votre compte, sinon genere une exception
    static final String PASSWD = "conanje";

    public ChangeStatusCommande(){
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
    java.util.Scanner scan = new Scanner(System.in);
    PrepareStatement pst = conn.prepareStatement
    ("Select id_commande, status from Commande Join CommmandeClient on id_commande Where (mail_resto = ? or id_client = ?);");
    System.out.println("votre id");
    String id_client_ou_resto = scan.next();
    pst.setString(1, id_client_ou_resto);
    pst.setString(1, id_client_ou_resto);
    ResultSet commandes = pst.executeQuery();
    commandes.first();
    while(!commandes.isAfterLast()){
        System.out.println(commandes.getString(1)+commandes.getString(2));
    }
    System.out.println("quelle commande voulez-vous");
    String id_commande = scan.next();
    //on pourrait verifier que l'id est bon
    System.out.println("client ou resto");
    String status;
    if("resto".equals(scan.next())){
        System.out.println("quel status voulez-vous affecter à la commande ?");
        status = scan.next();
        while(!status.equals("validée") && !status.equals("disponible") && !status.equals("en livraison") 
        && !status.equals("annulée par le resto") && !status.equals("en préparation") && !status.equals("terminée")){
            System.out.println("mauvais status, veuillez vérifier");
            status = scan.next();
        }
        if(status.equals("terminée")){
            pst = conn.prepareStatement("Update Commande Set status = ?, date_fin = ? Where id_commande = ?;");
            pst.setString(1, status);
            pst.setInt(2, Timestamp.from(Instant.now()).getTime());
            pst.setString(3, id_commande);
            pst.executeUpdate();
        }
        else{
            pst = conn.prepareStatement("Update Commande Set status = ? Where id_commande = ?;");
            pst.setString(1, status);
            pst.setString(2, id_commande);
            pst.executeUpdate();
        }
    }
    else if("client".equals(scan.next())){
        System.out.println("quel status voulez-vous affecter à la commande ?");
        status = scan.next();
        while(!status.equals("litige") && !status.equals("annulée par le client")){
            System.out.println("mauvais status, veuillez vérifier");
            status = scan.next();
        }
        if(!status.equals("terminée")){
            pst = conn.prepareStatement("Update Commande Set status = ? Where id_commande = ?;");
            pst.setString(1, status);
            pst.setString(2, id_commande);
            pst.executeUpdate();
        }
        else{
            pst = conn.prepareStatement("Update Commande Set status = ?, date_fin = ? Where id_commande = ?;");
            pst.setString(1, status);
            pst.setInt(2, Timestamp.from(Instant.now()).getTime());
            pst.setString(3, id_commande);
            pst.executeUpdate();
        }

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
