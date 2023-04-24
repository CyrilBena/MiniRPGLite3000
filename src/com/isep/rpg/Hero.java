package com.isep.rpg;

import java.util.ArrayList;
import java.util.List;

abstract class Hero extends Combatant{
    public List<Item> items=new ArrayList<>();
    public void addItem(Item item){
        items.add(item);
    }

    @Override
    public String toString() {        // on a generer une methode qui affiche le contenu des variables
        return "Hero{" +
                "items=" + items +
                ", life=" + life +
                ", mana=" + mana +
                '}';
    }
}
