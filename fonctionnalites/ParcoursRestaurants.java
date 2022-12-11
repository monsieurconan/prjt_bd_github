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
    
    //le mdp est bon, on peut continuer
    Queue<String> CategoriesMere = new LinkedList<String>();
    //on stocke une liste des categories
    PreparedStatement pstmt = conn.prepareStatement
    ("select * from RelationCategorie");
    ResultSet toutesLesCategories = pstmt.executeQuery();
    Scanner scan = new Scanner(System.in);
    System.out.println("Categories ?");
    String categ = scan.next();
    //l'utilisateur doit terminer sa liste de commande par "fin"
    while(!categ.equals(("fin"))){
        //on verifie que categ est bien une categorie dans la liste
        while(!toutesLesCategories.getString(2).equals(categ) || !toutesLesCategories.nextLine()){
            toutesLesCategories.nextLine();
        }
        //si c'est le cas, on l'ajoute dans la liste
        if(toutesLesCategories.getString(2).equals(categ)) CategoriesMere.add(categ);
        //on remet le curseur au début
        toutesLesCategories.first();
        //on met à jour categ
        categ = scan.next();
    }
    //si la liste des catégories est vide, on la remplit avec les catégories préférées de l'utilisateur
    if(CategoriesMere.isEmpty()){
        PreparedStatement pstmt = conn.prepareStatement
        ("select categorie_fille, avg(note_evaluation)  from (CategorieResto join Commande on mail_resto) join Evaluation on id_commande order by avg(note_evaluation) desc");
        ResultSet Categoriespreferee = pstmt.executeQuery();
        Categoriespreferee.first();
        int i = 0;
        while(!Categoriespreferee.isAfterLast() && i<3){
            CategoriesMere.add(Categoriespreferee.getString(1));
            Categoriespreferee.nextLine();
            i++;
        }
    }
    //on compte le nombre d'aretes pour prevenir des pb de boubles infinis
    toutesLesCategories.first();
    int limite = 0;
    while(!toutesLesCategories.isAfterLast()){
        toutesLesCategories.nextLine();
        limite++;
    }
    //on passe maintenant à la liste etendue des categories 
    Queue<String> CategoriesFille = new LinkedList<String>();
    int nb_arete_exploree;
    while(!CategoriesMere.isEmpty() && nb_arete_exploree<limite){
        categ = CategoriesMere.poll();
        CategoriesFille.add(categ);
        toutesLesCategories.first();
        while(!toutesLesCategories.isAfterLast()){
            if(toutesLesCategories.getString(1).equals(categ)){
                //on ajoute toutes les categories filles recursivement
                CategoriesMere.add(toutesLesCategories.getString(2));
                nb_arete_exploree<limite
            }
            toutesLesCategories.nextLine();
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
        System.out.println(listeRestaurant.getString(1));
        listeRestaurant.nextLine();
    }
    
    //on traite maintenant la selection par horaire
    System.out.println("Voulez-vous trier pour un horaire");
    if("oui".equals(scan.next())){
        System.out.println("le jour svp");
        String jourSemaine = scan.next();
        System.out.println("l'heure svp");
        int heure = scan.nextInt();
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
            listeRestaurant.nextLine();
        }
    }
    //consultation de la fiche d'un restaurant
    PreparedStatement ficheComplete = conn.prepareStatement
    ("select nom_resto, resto_adresse, nom_plat, prix_plat From RestAurant join Menu on mail_resto Where nom_resto = ?");
    System.out.println("Voir la fiche de ?");
    String nomResto = scan.next();
    while(!nomResto.equals("fin")){
        ficheComplete.setString(1, nomResto);
        ResultSet laFiche = ficheComplete.executeQuery();
        //ici on decide comment traiter la fiche
        System.out.print(laFiche);
        nomResto = scan.next();
    }        
    PrepListeRestau.close();
    conn.close();
    }
    catch (SQLException e) {
        System.err.println("failed");
        e.printStackTrace(System.err);
        }
    }
}

