package com.humblebundle;

public class Runner {

    public static void main(String[] args) {

        Chess game = new Chess();
        game.setInputs("input.txt");

        game.getMoves("output.txt");

    }
}
