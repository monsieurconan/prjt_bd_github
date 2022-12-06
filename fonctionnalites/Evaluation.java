import java.sql.*;
import java.util.Scanner;

public class Evaluation {
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "meghraoa"; // A remplacer pour votre compte, sinon genere une exception
    static final String PASSWD = "abcd";

    public Evaluation(int id_commande) {
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
        
        // On lit l'évaluation du client
        Scanner scann = new Scanner(System.in);
        System.out.println("Vos avis nous intéressent. Comment avez vous trouver votre commande ?");
        String avis = scann.nextLine();
        
        System.out.println("Donner une évaluation entre 1 et 5");
        int eval = scann.nextInt();

        if (eval < 0 || eval > 5) {
            System.out.println("Evaluation non possible.");
        }
        else {
            PreparedStatement stmt = conn.prepareStatement
                ("insert into Evaluation values(?, ?, ?, ?)");
            stmt.setInt(1, id_commande);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setString(3, avis);
            stmt.setInt(4, eval);
            stmt.executeUpdate();
            stmt.close();
        }

        // Transaction terminée
        conn.commit();

        System.out.println("Merci");

        // On doit maintenant mettre à jour l'évaluation du restaurant
        // On récupère le restaurant qui a fait la commande
        PreparedStatement stmt = conn.prepareStatement
            ("select distinct mail_resto from commande where id_commande = ?");
        ResultSet rset = stmt.executeQuery();
        String mail_resto = rset.getString(1);
        rset.close();
        stmt.close();

        // Puis on va calculer la moyenne
        stmt = conn.prepareStatement
            ("select avg(note_evaluation) from evaluation join commande on evaluation.id_evaluation = commande.id_commande where mail_resto = ?");
        stmt.setString(1, mail_resto);
        rset = stmt.executeQuery();
        float eval_moyenne = rset.getFloat(1);
        rset.close();
        stmt.close();

        // Enfin, on va modifier la moyenne
        stmt = conn.prepareStatement
            ("update restaurant set note_resto = ? where mail_resto = ?");
        stmt.setFloat(1, eval_moyenne);
        stmt.setString(2, mail_resto);
        stmt.executeUpdate();
        stmt.close();

        // Transaction terminée
        conn.commit();

        // Fermeture
        conn.close();;

        }
        catch (SQLException e) 
        {
           System.err.println("failed");
           e.printStackTrace();
        }
    }
}
