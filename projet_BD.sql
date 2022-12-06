create table Restaurant(
    mail_resto varchar(100) primary key,
    nom_resto varchar(100),
    tel_resto varchar(15),
    adresse_resto varchar(100),
    nbr_place int check(nbr_place >=0),
    note_resto float check (note_resto >=0.0 AND note_resto <= 5.0)
);

create table Commande(
    id_commande int primary key,
    date_fin int,
    date_commande int, /*Trouver comment comparer des int pour ajouter quelquechose comme : check (date_commande <= date_fin) */
    prix_commande float check (prix_commande >= 0),
    status varchar(100) check (status = 'attente de confirmation' 
    or status = 'validée' 
    or status = 'disponible' 
    or status = 'en livraison'
    or status = 'annulée par le client'
    or status = 'annulée par le resto'
    or status = 'terminé'
    or status = 'en préparation'
    or status = 'litige'),
    adresse_livraison varchar(100),
    mail_resto varchar(100),
    foreign key(mail_resto) references Restaurant(mail_resto),
    type_commande varchar(100) check (type_commande = 'à emporter'
    or type_commande = 'sur place' 
    or type_commande = 'livraison')
);

create table Evaluation
(
    id_evaluation int primary key,
    date_evaluation int,
    avis varchar(100),
    note_evaluation int check (note_evaluation >= 0 AND note_evaluation <= 5),
    foreign key(id_evaluation) references Commande(id_commande)
);

create table Plat(
    id_plat int primary key,
    nom_plat varchar(100),
    description_plat varchar(100)
);

create table Menu
(
    mail_resto varchar(100),
    id_plat int,
    nom_plat varchar(100),
    prix_plat float check (prix_plat >=0),
    primary key(mail_resto, id_plat),
    foreign key(mail_resto) references Restaurant(mail_resto),
    foreign key(id_plat) references Plat(id_plat)
);


create table ComposeDe
(
    id_commande int,
    id_plat int,
    quantite int check (quantite > 0),
    primary key(id_commande, id_plat),
    foreign key(id_commande) references Commande(id_commande),
    foreign key(id_plat) references Plat(id_plat)
);

create table OuvertA
(
    mail_resto varchar(100) unique,
    jour_semaine int,
    horaire_midi_debut int, /* check (horaire_midi_debut < horaire_midi_fin) */
    horaire_midi_fin int,
    horaire_soir_debut int, /* check (horaire_soir_debut < horaire_soir_fin) */
    horaire_soir_fin int,
    primary key(mail_resto, jour_semaine),
    foreign key(mail_resto) references Restaurant(mail_resto)
);

create table RelationCategorie(
    Categorie_fille varchar(100) primary key,
    Categorie_mere varchar(100)
);

create table CategorieResto
(
    mail_resto varchar(100),
    categorie_fille varchar(100),
    primary key(mail_resto, categorie_fille),
    foreign key(mail_resto) references Restaurant(mail_resto),
    foreign key(categorie_fille) references RelationCategorie(categorie_fille)
);

create table Client( 
    id_client varchar(100) primary key,
    mdp varchar(100),
    nom_client varchar(30),
    prenom_client varchar(30)
);

create table Allergenes(
    id_plat int primary key,
    allergenes varchar(100),
    foreign key(id_plat) references Plat(id_plat)
);

create table CommandeClient(
    id_client varchar(100) ,
    id_commande int ,
    nbr_personnes int check (nbr_personnes > 0),
    foreign key(id_client) references Client(id_client)
        on delete cascade,
    foreign key(id_commande) references Commande(id_commande),
    Primary key (id_client, id_commande)
);

create table TypeCommandePossible (
    mail_resto varchar(100) primary key,
    type_commande varchar(100) check (type_commande = 'à emporter'
    or type_commande = 'sur place' 
    or type_commande = 'livraison')
);
