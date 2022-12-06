import java.sql.*;
import java.util.Scanner;
import java.util.Queue;
import java.lang.String;
import java.util.LinkedList;

public class ParcoursRestaurants{
    
    static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    static final String USER = "conanje"; // A remplacer pour votre compte, sinon genere une exception
    static final String PASSWD = "conanje";

    public ParcoursRestaurants(){
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
    else{
        //le mdp est bon, on peut continuer
        Queue<String> CategoriesMere = new LinkedList<String>();
        //on stocke une liste des categories
        pstmt = conn.prepareStatement
        ("select * from RelationCategorie");
        ResultSet toutesLesCategories = pstmt.executeQuery();
        System.out.println("Categories ?");
        String categ = scan.next();
        //l'utilisateur doit terminer sa liste de commande par "fin"
        while(!categ.equals(("fin"))){
            //on verifie que categ est bien une categorie dans la liste
            while(!toutesLesCategories.getString(2).equals(categ) || !toutesLesCategories.isAfterLast()){
                toutesLesCategories.next();
            }
            //si c'est le cas, on l'ajoute dans la liste
            if(toutesLesCategories.getString(2).equals(categ)) CategoriesMere.add(categ);
            //on remet le curseur au début
            toutesLesCategories.first();
            //on met à jour categ
            categ = scan.next();
        }
        //on passe maintenant à la liste etendue des categories 
        Queue<String> CategoriesFille = new LinkedList<String>();
        while(!CategoriesMere.isEmpty()){
            categ = CategoriesMere.poll();
            CategoriesFille.add(categ);
            toutesLesCategories.first();
            while(!toutesLesCategories.isAfterLast()){
                if(toutesLesCategories.getString(1).equals(categ)){
                    //on ajoute toutes les categories filles recursivement
                    CategoriesMere.add(toutesLesCategories.getString(2));
                }
                toutesLesCategories.next();
            }
        }
        pstmt.close();
        //on peut alors commencer à preparer la selection
        PreparedStatement PrepListeRestau = conn.prepareStatement
        ("select nom_resto from CategorieResto Join Restaurant on mail_resto Order by note_resto DESC Order by nom_resto Where ?");
        //on prepare la condition :
        String condition = "";
        condition.concat("categorie_fille = ");
        condition.concat(CategoriesFille.poll());
        while(!CategoriesFille.isEmpty()){
            condition.concat(" OR categorie_fille = ");
            condition.concat(CategoriesFille.poll());
        }
        PrepListeRestau.setString(1, condition);
        ResultSet listeRestaurant = PrepListeRestau.executeQuery();
        //on affiche tout dans le terminal
        while(!listeRestaurant.isAfterLast()){
            System.out.println(listeRestaurant.getString(0));
            listeRestaurant.next();
        }
        
        //on traite maintenant la selection par horaire
        System.out.println("voulez-vous trier pour un horaire");
        if("oui".equals(scan.next())){
            System.out.println("le jour svp");
            String jourSemaine = scan.next();
            System.out.println("l'heure svp");
            int heure = Integer.parseInt(scan.next());
            PrepListeRestau = conn.prepareStatement
            ("select nom_resto from (CategorieResto Join Restaurant on mail_resto) JOIN OuvertA on mail_resto Order by note_resto DESC and nom_resto Where ? AND WHERE jour_semaine = ? And (? Between horaire_midi_debut And horaire_midi_fin Or ? Between horaire_midi_debut And horaire_midi_fin))");
            PrepListeRestau.setString(1, condition);
            PrepListeRestau.setString(2, jourSemaine);
            PrepListeRestau.setInt(3, heure);
            PrepListeRestau.setInt(4, heure);
            listeRestaurant = PrepListeRestau.executeQuery();
            //on affiche tout dans le terminal
            while(!listeRestaurant.isAfterLast()){
                System.out.println(listeRestaurant.getString(1));
                listeRestaurant.next();
            }
        }
        //consultation de la fiche d'un restaurant
        PreparedStatement ficheComplete = conn.prepareStatement
        ("select nom_resto, resto_adresse, nom_plat, prix_plat From RestAurant join Menu on mail_resto Where nom_resto = ?");
        System.out.println("voir la fiche de ?");
        String nomResto = scan.next();
        while(!nomResto.equals("fin")){
            ficheComplete.setString(1, nomResto);
            ResultSet laFiche = ficheComplete.executeQuery();
            //ici on decidera comment traiter la fiche
            nomResto = scan.next();
        }        
        PrepListeRestau.close();
    }
    conn.close();
    } catch (SQLException e) {
        System.err.println("failed");
        e.printStackTrace(System.err);
    }
}   
}
