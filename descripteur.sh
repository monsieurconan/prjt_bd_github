#!/bin/bash
echo "Veuillez entrer l'email client:"
read emailClient
echo "Veuillez entrer votre mot de passe:"
read mdp
echo "Veuillez choisir une des fonctionnalités suivante :"
echo "1. Parcours des Restaurant"
echo "2. Commande"
echo "3. Droit a l'oubli"
echo "4. Changer le statut d'un commande"
echo "5. Faire une évaluation"
echo "6. Quitter"
read choix
while [ "$choix" -ne "6" ]
do
echo "Veuillez choisir une des fonctionnalités suivante :"
echo "1. Parcours des Restaurant"
echo "2. Commande"
echo "3. Droit a l'oubli"
echo "4. Changer le statut d'un commande"
echo "5. Faire une évaluation"
echo "6. Quitter"
read choix
if [ "$choix" -eq "1" ]
then
./fonctionnalites/ParcoursRestaurants.java
elif [ "$choix" -eq "2" ]
then
echo "Veuillez entrer le mail du Restaurant:\n"
read emailResto
echo "Veuillez entrer le nombre de personnes:\n"
read nbrPersonne
echo "Veuillez entrer le type de commande\n"
read type
echo "Veuillez entrer l'adresse de livraison(mettre null si la commande n'est pas une livraison)\n"
read adresse
echo "Veuillez entrer les plats':\n"
read plats
javac fonctionnalites/Commande.java
java /fonctionnalites/Commande.java $emailResto $nbrPersonne $emailClient $adresse $type $plats
elif [ "$choix" -eq "3" ]
then
javac fonctionnalites/DroitOubli.java
java fonctionnalites/DroitOubli.java $emailClient
elif [ "$choix" -eq "4" ]
then
javac fonctionnalites/ChangeStatusCommande.java
java fonctionnalites/ChangeStatusCommande.java
elif [ "$choix" -eq "5" ]
then
echo "Veuillez entrer le numéro de commande:\n"
read idCommande
javac fonctionnalites/Evaluation.java
java fonctionnalites/Evaluation.java $idCommande
else 
echo "Le numéro ne correspond à aucun choix.\n"
fi
done
echo Aurevoir !
exit 0
