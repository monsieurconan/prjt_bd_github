import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Commande {
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "sekkala"; // A remplacer pour votre compte, sinon genere une exception
    static final String PASSWD = "sekkala";
    
    public Commande(String mail_resto,
                    int nbr_personnes,
                    String id_client,
                    String adress_livraison,
                    String type_commande,
                    ArrayList<Plat> plats
                    ) {
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

            
            // Vérifier d'abord si le type de commande est présent dans le restaurant choisi
            PreparedStatement stmt = conn.prepareStatement(
            "select * from TypeCommandePossible where type_commande = ? and mail_resto = ?");
            stmt.setString(1,type_commande);
            stmt.setString(2, mail_resto);
            ResultSet rset = stmt.executeQuery();
            if (!rset.next()){ // si la requête ne donne aucun résultat
                system.out.println("Malheureusement, ce restaurant ne présente pas le type de commande que vous souhaitez.");
            }
            stmt.close();
            rset.close();



            // Si le nombre de places disponibles au restaurant est insuffisant, on informe le client
            if (type_commande == "sur_place"){
                stmt = conn.prepareStatement
                ("select nbr_place from Restaurant where mail_resto = ?");
                stmt.setString(1, mail_resto);
                rset = stmt.executeQuery();
                int nbr_places_total = rset.getInt(1);
                stmt.close();
                rset.close();

                stmt = conn.prepareStatement("select count(mail_resto) from commande where date_commande between ? and ? and type_commande = 'sur place'");

                // Récuperer la plage horaire du matin ou du soir

                PreparedStatement stmt1 = conn.prepareStatement
                ("select horaire_midi_debut, horaire_midi_fin, horaire_soir_debut, horaire_soir_fin from OuvertA where mail_resto = ? ");
                stmt1.setString(1, mail_resto);
                rset = stmt1.executeQuery();
                stmt1.close();

                Long horaire_midi_debut = rset.getLong(1);
                Long horaire_midi_fin = rset.getLong(2);
                Long horaire_soir_debut = rset.getLong(3);
                Long horaire_soir_fin = rset.getLong(3);
                rset.close();
                long timeNow = Time.valueOf(java.time.LocalTime.now()).getTime();

                if (timeNow < horaire_midi_fin) {
                    stmt.setLong(1, horaire_midi_debut);
                    stmt.setLong(2, horaire_midi_fin);
                }
                else {
                    stmt.setLong(1, horaire_soir_debut);
                    stmt.setLong(2, horaire_soir_fin);
                }

                rset = stmt.executeQuery();
                int nbrPlacesOccupees = rset.getInt(1);
                stmt.close();
                rset.close();


                // Vérifier si les places sont disponibles
                if (nbr_places_total - nbrPlacesOccupees < nbr_personnes) {
                    System.out.println("Ce restaurant n'a pas assez de places disponibles pour vous");
                }
            }

                            
            //Recuperation de l'id de la commande
            int id_commande;
            stmt = conn.prepareStatement
            ("select max(id_commande) from Commande");
            rset = stmt.executeQuery();
            id_commande = rset.getInt(1) + 1;
            stmt.close();
            rset.close();

            // insertion des plats
            for (Plat plat : plats) {
                stmt = conn.prepareStatement
                ("insert into composeDe values(?, ?, ?)");
                stmt.setInt(1, id_commande);
                stmt.setInt(2, plat.getId());
                stmt.setInt(3, plat.getQuantite());
                stmt.executeUpdate();
                stmt.close();
            }

            // Insertion dans commandeClient
            stmt = conn.prepareStatement
            ("insert into commandeClient values(?, ?, ?)");
            stmt.setString(1, id_client);
            stmt.setInt(2, id_commande);
            stmt.setInt(3, nbr_personnes);
            stmt.executeUpdate();
            stmt.close();

            // Insertion dans commande
            String status = "attente de confirmation";
            stmt = conn.prepareStatement
            ("insert into commande values(?, null, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, id_commande);
            stmt.setLong(2, Time.valueOf(java.time.LocalTime.now()).getTime());
            stmt.setString(4, status);
            stmt.setInt(3, prixCommande(plats));
            stmt.setString(5, adress_livraison);
            stmt.setString(6, mail_resto);
            stmt.setString(7, type_commande);
            stmt.executeUpdate();
            stmt.close();
            
            //transaction terminée
            conn.commit();
            
            System.out.println("Votre commande est en attente de confirmation.");

            //fermeture de la connexion
            conn.close();
            
        }
        catch (SQLException e) 
        {
         System.err.println("failed");
         e.printStackTrace();
        }
        }

    /*
    retourne le prix totale de la commande
    */
    private int prixCommande(ArrayList<Plat> plats) {
        int prix = 0;
        for (Plat plat : plats) {
            prix += plat.getPrix() * plat.getQuantite();
        }
        return prix;
    }

}
