public class Plat {
   private int id_plat;
   private int prix_plat;
   private int quantite;
   
   public Plat(int id_plat, int quantite, int prix_plat) {
       this.id_plat = id_plat;
       this.prix_plat = prix_plat;
       this.quantite = quantite;
   }

   public int getId() {
       return id_plat;
   }

   public int getPrix() {
       return prix_plat;
   }

   public int getQuantite() {
       return quantite;
   }
}
