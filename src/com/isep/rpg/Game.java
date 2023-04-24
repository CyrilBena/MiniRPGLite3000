package com.isep.rpg;
import com.isep.utils.ConsolParser;
import com.isep.utils.InputParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private List<Hero> heroes =new ArrayList<>();
    private List<Ennemy> ennemies=new ArrayList<>();
    Random ordreAttaque = new Random();
    private InputParser iP;

    public Game (InputParser inputParser){
        iP = inputParser;
    }
    public void run() {
        int nbHero = iP.demanderIntDepuisMessage("Veuillez indiquez le nombre d'Heros souhaité !");
        initHero(nbHero);
        initEnnemy(nbHero);
        iP.afficherMessage("Vous avez "+nbHero+" héro"+ (nbHero > 1 ? "s" : "")+" !");
        iP.afficherMessage("Vous aurez par conséquent "+ nbHero+" ennemies !");
        lancementTours();

    }
    private void initEnnemy(int nbHero) {
        for(int e=0; e<nbHero; e++) {
            ennemies.add(new Ennemy(30));
        }
    }
    private void initHero(int nbHero){
        int i=0;
        while(i<nbHero) {
            int reponse;
            reponse = iP.demanderIntDepuisMessage("Tapez 1 pour: Hunter" +
                    "\nTapez 2 pour: Warrior" +
                    "\nTapez 3 pour: Mage" +
                    "\nTapez 4 pour: Healer");
            if (reponse == 1) {
                heroes.add(createHunter());
            } else if (reponse == 2) {
                heroes.add(createWarrior());

            } else if (reponse == 3) {
                heroes.add(createMage());

            } else if (reponse == 4) {
                heroes.add(createHealer());

            }
            else{
                iP.afficherMessage("Vous avez rentré un mauvais chiifre !");
                i--; // Si la personne rentre un chiffre autre que 1,2,3,4, le i++ vas quand meme prendre en compte un combattant en plus du coup, on l'enleve avec i--
            }
            i++;
        }

    }
    private Hunter createHunter(){
        Hunter hunter = new Hunter();
        Weapon arc = new Arc ("Arc", 40, 3);
        hunter.addItem(arc);
        Food lembas = new Food("Lembas");
        hunter.addItem(lembas);

        return hunter;
    }
    private Mage createMage(){
        Mage mage = new Mage();
        Weapon baton = new Weapon("Baton",30);
        mage.addItem(baton);
        Food lembas = new Food("Lembas");
        mage.addItem(lembas);
        Potion potion = new Potion("Potion");
        mage.addItem(potion);
        return mage;
    }
    private Warrior createWarrior(){
        Warrior warrior = new Warrior();
        Weapon epee = new Weapon("Epee",30);
        warrior.addItem(epee);
        Food lembas = new Food("Lembas");
        warrior.addItem(lembas);
        return warrior;
    }
    private Healer createHealer(){
        Healer healer = new Healer();
        Weapon baton = new Weapon("Baton",10);
        healer.addItem(baton);
        Food lembas = new Food("Lembas");
        healer.addItem(lembas);
        Potion potion = new Potion("Potion");
        healer.addItem(potion);
        return healer;
    }
    private void lancementTours (){
        int tours =0;
        while(ennemies.size()>0 && tours<4  && heroes.size()>0){
            ordreAttaque();
            displayGameState();
            verificationVictoire();
            tours++;
        }
        if(heroes.size()>0){
            iP.afficherMessage("Le boss arrive !");
            boss();
        }
        resultatCombat();
    }
    private void displayGameState(){

        for(Hero hero:heroes){
            iP.afficherMessage(hero.toString()); // pour afficher les informations de l'hero, methode qu'on a fait dans Hero
        }
        for(Ennemy ennemy:ennemies){
            iP.afficherMessage(ennemy.toString());
        }
    }

    private void actions (){

        for (int i=0; i< heroes.size(); i++){
            Hero hero = heroes.get(i);

            int action= iP.demanderIntDepuisMessage("Hero n°: "+(i+1)+"\nSi vous voulez attaquer taper 1 !" +
                    "\nSi vous voulez défendre taper 2 !" +
                    "\nSi vous voulez recuperer de la vie taper 3 !" +
                    "\nSi vous voulez recuper de la mana taper 4"
            + "\nSi vous voulez soigner avec votre healer taper 5 !");

            if (action==3) {
                life(hero);
            } else if (action==4) {
                mana(hero);
            } else if (action==1) {
                attaquer(hero);
            }else if(action==2){
                defendre(hero);
            }else if(action==5){
                if(!soigner(hero)){ // ! ca signifie l'inverse donc ici "le hero n'as pas pu soigner", donc il se passe i--, et cela se passe si la methode return false, sinon si ca renvoie true on reste sur la methode soigner
                    i--;
                }
            }
        }
    }
    private void life (Hero hero){
        int i=0;
        while(i<hero.items.size()){   // on parcours la liste pour analyser chaque element de la liste et voir si c'est un objet de type food
            Item item = hero.items.get(i);
            if (item instanceof Food){
                hero.life=hero.life+((Food) item).efficacite;
                hero.items.remove(item);
                break;  // ca arrete la boucle while
            }
            i++;
        }
    }
    private void mana (Hero hero){
        int i=0;
        while(i<hero.items.size()){
            Item item = hero.items.get(i); // get sers a recuperer l'element a partir de son index
            if (item instanceof Potion){  // instanceof sers a savoir si "l'item" qu'on cherche est de la classe potion
                hero.mana=hero.mana+ ((Potion) item).efficacite;
                hero.items.remove(item);
                break;
            }
            i++;
        }

    }
    private void attaquer (Hero hero){
        if(ennemies.size()>0) {

            int i = 0;
            while (i < hero.items.size()) {
                Item item = hero.items.get(i);
                if (item instanceof Weapon) {
                    int weaponPower = ((Weapon) item).power;// on a "caster", c'est a dire qu'on a changer le type de 'item' qui etait Item en Weapon, car on a verifier que l'item selectionner etait de type Weapon

                    if (hero instanceof Mage || hero instanceof Healer) {   // || signifie ou

                        if (hero.mana >= 10) {
                            ennemies.get(0).life -= weaponPower;
                            hero.mana = hero.mana - hero.coutMana;
                        } else {
                            iP.afficherMessage("Ce hero n'a plus de mana pour attaquer !");
                        }

                    } else if (hero instanceof Hunter) {
                        Arc arc = ((Arc) item);
                        if (arc.nombreDeFleche > 0) {
                            ennemies.get(0).life -= weaponPower;
                            arc.nombreDeFleche -= 1;
                        } else {
                            iP.afficherMessage("Ce hero n'a plus de fleche pour attaquer !");
                        }
                    } else {
                        ennemies.get(0).life -= weaponPower;
                    }
                    if (ennemies.get(0).life <= 0) {
                        ennemies.remove(0);
                    }
                    break;
                }
            }
        }
    }
    private boolean soigner(Hero hero){
        if(hero instanceof Healer){
            for(Hero h : heroes){
                h.life=h.life+10;
            }
            return true;
        }else{
            iP.afficherMessage("Vous ne pouvez pas effectuer cette action, votre personnage n'est pas un healer !");
            return false;
        }

    }
    private void defendre (Hero hero){
        hero.armureActive=true;

    }
    private void attaqueEnnemi(){
        if(heroes.size()>0){
            for(Ennemy ennemy : ennemies) {
                if (heroes.get(0).armureActive == true) {
                    heroes.get(0).life -= (ennemy.damage - heroes.get(0).resistance) / 2;
                } else {
                    heroes.get(0).life -= ennemy.damage - heroes.get(0).resistance;
                }
                if (heroes.get(0).life < 1) {
                    heroes.remove(0);
                }
            }
        }
    }
    private void boss(){
        Ennemy boss = new Ennemy(60);
        ennemies.add(boss);
        while(ennemies.size()>0 && heroes.size()>0){
            ordreAttaque();
            displayGameState();
        }
    }
    private void ordreAttaque(){
       int i= ordreAttaque.nextInt(2);   // ca m'envoie un chiffre entre 0 et 1
        if(i==0){
            iP.afficherMessage("Ennemi attaque !");
            attaqueEnnemi();
            displayGameState();
            iP.afficherMessage("Vous attaquez !");
            actions();
        }else{
            iP.afficherMessage("Vous attaquez !");
            actions();
            displayGameState();
            iP.afficherMessage("Ennemi attaque !");
            attaqueEnnemi();
        }
    }
    private void resultatCombat(){
        if(heroes.size()==0){
            iP.afficherMessage("Vous avez perdu !");
        } else if (ennemies.size()==0) {
            iP.afficherMessage("Vous avez gagné !");
        }
    }
    private void verificationVictoire(){
        if(heroes.size()>0){
            for(int i=0; i< heroes.size(); i++){

               int action = iP.demanderIntDepuisMessage("Hero n°: "+(i+1)+"\nTapez 1 pour augmenter les degats"+
                        "\nTapez 2 pour augmenter la resistance"+
                        "\nTapez 3 pour augmenter l'efficacité de la potion et de la nourriture"+
                        "\nTapez 4 pour augmenter le nombre de potions et de nourritures"+
                        "\nTapez 5 pour augmenter le nombre de flèches (pour le Hunter), diminuer le coût en mana pour les sorceleurs et l’eﬀicacité de leurs sorts");

               if(action==1){
                   augmenterDegats(heroes.get(i));
               } else if (action==2) {
                   augmenterResistance(heroes.get(i));
               } else if (action==4) {
                   augmenterNombrePotionNourriture(heroes.get(i));
               } else if (action==3) {
                   augmenterEfficaciteFoodPotion(heroes.get(i).items);
               } else if (action==5) {
                   if(augmenterNombreDeFlecheOuMana(heroes.get(i))==false){
                       i--;
                   }
               }
            }
        }
    }
    public void augmenterDegats(Hero hero){
        int i=0;
        while(i<hero.items.size()){
            if(hero.items.get(i) instanceof Weapon){
                ((Weapon) hero.items.get(i)).power+=10;
                break;
            }
        }
    }
    public void augmenterResistance(Hero hero){
        hero.resistance+=5;
    }
    public void augmenterNombrePotionNourriture(Hero hero){
        hero.items.add(new Potion("mana"));
        hero.items.add(new Food("lembas"));

    }
    public void augmenterEfficaciteFoodPotion(List<Item> items){
        int i=0;
        while(i<items.size()){
            if(items.get(i) instanceof Food ){
                ((Food) items.get(i)).efficacite+=5;
            } else if (items.get(i) instanceof Potion) {
                ((Potion) items.get(i)).efficacite+=3;
            }
            i++;
        }
    }
    
    public boolean augmenterNombreDeFlecheOuMana(Hero hero){
        if(hero instanceof Hunter){
            int i=0;
           while(i<hero.items.size()){
              if(hero.items.get(i) instanceof Arc){
                  ((Arc) hero.items.get(i)).nombreDeFleche+=1;
                  return true;
              }
           }
        } else if (hero instanceof Healer || hero instanceof Mage) {
            hero.coutMana-=2;
            augmenterDegats(hero);
            return true;
        }else{
            iP.afficherMessage("Vous êtes un Warrior vous ne pouvez pas utiliser cette amelioration !");
            return false;
        }
        return false;

    }
    public static void main(String[] args) {
        Game game=new Game(new ConsolParser());
        game.run();
    }
}
