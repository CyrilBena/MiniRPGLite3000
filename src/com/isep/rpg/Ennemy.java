package com.isep.rpg;

public class Ennemy extends Combatant{
    public Ennemy(int damage) {
        this.damage=damage;
    }

    @Override
    public String toString() {    // pareil que hero
        return "Ennemy{" +
                "life=" + life +
                ", mana=" + mana +
                '}';
    }
}
