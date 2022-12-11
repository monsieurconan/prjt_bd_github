Insert into Restaurant values('contact@mcdo.fr', 'McDo', '666666666', 'place Grenette Chicago sur isere', 485, 0.0);
Insert into Restaurant values('manon.ninja', 'Tacos de Grenoble', '8495817593', 'a cote du lamartine TMTC Gre', 18, 0.0);
Insert into Restaurant values('Leo.BG@julesvernes.grenoble', 'Le Jules Vernes', '6583940284', 'En dessous de chez Jules', 60000, 0.0);
insert into Restaurant values('ehfkajn', 'resto_a', '4567890987', 'adresse_x', -2, 0.0);

Insert into Commande values(1, 2, 1, 15,'en livraison', '3 rue Saint-Joseph', 'contact@mcdo.fr', 'livraison');
Insert into Commande values(2, '2021-05-22 12:34:24', '2021-05-22 12:39:37', 15, null, 'contact@mcdo.fr', 'Sur Place');
Insert into Commande values(3, '2021-06-22 12:34:24', '2021-05-22 12:39:37', 15, null, 'contact@mcdo.fr', 'Sur Place');
Insert into Commande values(2, '2021-05-22 12:34:24', '2021-05-22 12:39:37', 15, null, 'contact@mcdo.fr', 'Sur Place');
Insert into Commande values(4, '2021-05-22 12:34:24', '2021-05-22 12:39:37', 15, null, 'contact@mcdo.fr', 'bizarre');
Insert into Commande values(5, '2021-05-22 12:38:24', '2021-05-22 12:39:37', -5, null, 'contact@mcdo.fr', 'A Emporte');


insert into Plat values(2, 'cheeseburger', 'un hamburger élaboré avec plusieurs steaks hachés');
insert into Plat values(3, 'Menu bigmac', 'Menu qui comprend un hamburger élaboré avec plusieurs steaks hachés');
insert into Plat values(4, 'wrap chèvre', 'Chèvre entouré dune crêpe de maïs');
insert into Plat values(5, 'tacos M', 'tacos de taille M');
insert into Plat values(6, 'tortilla', 'plat avec des patates');
insert into Plat values(7, 'canette', 'boisson en canette');
insert into Plat values(8, 'biere');
insert into Plat values(9, 'brushetta');

insert into Client values('coucou', 'chienchat12$', 'Dupond', 'Paul');
insert into Client values('hola', 'bebecadum34', 'Shmidt', 'Wolfgang');
insert into Client values(5, 'M1stlessang', 'Rodriguez', 'Théo');
insert into Client values(6, 'filou34', 'Wagner', 'Marie');
insert into Client values(7, 'annadu06', 'Marmier', 'Jules');
insert into Client values(8, 'oieghkzjvn(ubfz', 'Peyraud', 'Flo');
insert into Client values(9, 'kjpoj','Maillard', 'Fanny25');
insert into Client values(5, 'aefjmofuàç!ç!', 'Jeannesson', 'Tom');

Insert into CommandeClient values('coucou', 1, 2);
Insert into CommandeClient values('hola', 1, 3);
Insert into CommandeClient values(3, 3, 7);
Insert into CommandeClient values(4, 4, 1);
Insert into CommandeClient values(5, 5, 1);
Insert into CommandeClient values(6, 6, 4);
Insert into CommandeClient values(13, 20,-1);
Insert into CommandeClient values(3, 4, 0);


Insert into RelationCategorie values('Americain', 'Fast-food');
Insert into RelationCategorie values('Japonais', 'Asiatique');
Insert into RelationCategorie values('Sushi', 'Japonais');
Insert into RelationCategorie values('Francaise', 'Gastronomie');
Insert into RelationCategorie values('Francaise', 'Bistrot');


insert into Evaluation values(1, 0, 'jaime', 4);
Insert into Evaluation values(2, '2021-05-23 13:49:21', 'pas trop ouf', -2);
Insert into Evaluation values(3, '2020-11-08 16:07:18', 'bof', 0);
Insert into Evaluation values(4, '2019-02-02 21:16:16', 'éç!(ç!', 8);
Insert into Evaluation values(5, '2017-04-28 21:50:12', 'aie', 3);
Insert into Evaluation values(6, '2015-07-24 00:12:20', 'c etait bien', 2);
Insert into Evaluation values(7, '2013-10-16 13:36:10', 'pas cuit', null);
Insert into Evaluation values(8, null, null, null, null);

Insert into Menu values('contact@mcdo.fr', 2, 'cheeseburger', 2);
Insert into Menu values('contact@mcdo.fr', 5, 'mcflurry', 4);
Insert into Menu values('contact@mcdo.fr', 3, 'menu bigmac', 12);
Insert into Menu values('contact@mcdo.fr', 4, 'wrap chèvre', 4);
Insert into Menu values('manon.ninja', 1, 'tacos M', 5);
Insert into Menu values('manon.ninja', 2, 'tacos M', 2);
Insert into Menu values('manon.ninja', 2, 'cannette', 2);
Insert into Menu values('Leo.BG@julesvernes.grenoble', 1, 'bière', 3);
Insert into Menu values('Leo.BG@julesvernes.grenoble', 2, 'brushetta', dge);

insert into ComposeDe values (1,1,1);
insert into ComposeDe values (1,2,3);
insert into ComposeDe values (1,3,-2);
insert into ComposeDe values (1,2,0);
insert into ComposeDe values(2, 2, 40);
insert into ComposeDe values(-1, 3, 12);
insert into ComposeDe values(4, 3, null);
insert into ComposeDe values(5, null, null);

Insert into OuvertA values('contact@mcdo.fr', 1, 8, 15, 15, 22);
Insert into OuvertA values('contact@mcdo.fr', 2, 9, 13, 15, 22);
Insert into OuvertA values('contact@mcdo.fr', 3, 8, 15, 12, 22);
Insert into OuvertA values('contact@mcdo.fr', 4, 8, 20, 15, 22);
Insert into OuvertA values('contact@mcdo.fr', 5, 8, 15, 15, 22);
Insert into OuvertA values('manon.ninja', 1, 0, 15, 15, '23h59');
Insert into OuvertA values('Leo.BG@julesvernes.grenoble', 1, '35h27', '23h30', null, null);
Insert into OuvertA values('Leo.BG@julesvernes.grenoble', 2, 22, 21, 20, 19);


Insert into Allergenes values(2, '143§§');
Insert into Allergenes values(null, 'gluten');
Insert into Allergenes values(6, 'gluten, fromage');
Insert into Allergenes values(8, 'hublon');
Insert into Allergenes values(9, 'gluten, fromage');
Insert into Allergenes values(10, 'fromage, olives');

Insert into TypeCommandePossible values('contact@mcdo.fr', 'Sur Place');
Insert into TypeCommandePossible values('manon.ninja', 'Livraison');
Insert into TypeCommandePossible values('contact@mcdo.fr', 'chelou');
Insert into TypeCommandePossible values('manon.ninja', 'A Emporter');

insert into CategorieResto values('contact@mcdo.fr', 'Americain');
insert into CategorieResto values('det@gg.fr', 'breton');
insert into CategorieResto values('Leo.BG@julesvernes.grenoble', 'bar');

commit;