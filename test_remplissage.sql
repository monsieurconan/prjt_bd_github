Insert into Restaurant values('contact@mcdo.fr', 'McDo', '666666666', 'place Grenette Chicago sur isere', 485, 0.0);

Insert into Commande values(1, 2, 1, 15,'en livraison', '3 rue Saint-Joseph', 'contact@mcdo.fr', 'livraison');

insert into Plat values(2, 'cheeseburger', 'un hamburger élaboré avec plusieurs steaks hachés');
insert into Plat values(3, 'Menu bigmac', 'Menu qui comprend un hamburger élaboré avec plusieurs steaks hachés');

insert into Client values('coucou', 'chienchat12$', 'Dupond', 'Paul');
insert into Client values('hola', 'bebecadum34', 'Shmidt', 'Wolfgang');

Insert into CommandeClient values('coucou', 1, 2);
Insert into CommandeClient values('hola', 1, 3);

Insert into RelationCategorie values('Americain', 'Fast-food');

insert into Evaluation values(1, 0, 'jaime', 4);

insert into Menu values('contact@mcdo.fr', 2, 'cheeseburger', 2);

insert into ComposeDe values (1,2,2);

Insert into OuvertA values('contact@mcdo.fr', 1, 8, 15, 15, 22);

Insert into TypeCommandePossible values('contact@mcdo.fr', 'livraison');

insert into CategorieResto values('contact@mcdo.fr', 'Americain');