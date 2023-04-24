package com.isep.rpg;

public class Arc extends Weapon{
    int nombreDeFleche;

    public Arc(String name, int power, int nombreDeFleche) {
        super(name, power);
        this.nombreDeFleche=nombreDeFleche;
    }
}
