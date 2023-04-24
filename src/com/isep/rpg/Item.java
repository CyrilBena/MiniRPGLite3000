package com.isep.rpg;

abstract class Item {
    private String name;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                '}';
    }

    public Item (String name){
        this.name=name;
    }


}
