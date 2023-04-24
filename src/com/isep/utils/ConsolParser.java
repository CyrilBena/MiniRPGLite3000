package com.isep.utils;

import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsolParser implements InputParser {

    @Override
    public int demanderIntDepuisMessage(String message) {
        System.out.println(message);
        Scanner sc = new Scanner(System.in);
        int chiffre = sc.nextInt();
        return chiffre;
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }
}
