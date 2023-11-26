package com.mastering.lambdas.designpatterns.observer;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

class Game {
    // todo
    List<Rat> rats = new ArrayList<>();

    void notifyObservers() {
        if (!rats.isEmpty()) {
            rats.forEach(r -> r.update(rats.size()));
            System.out.println("Rat's attack is: " + rats.get(0).getAttack());
        } else {
            System.out.println("Rat's attack is: " + 0);
        }
    }
}

class Rat implements Closeable {
    private final Game game;
    public int attack = 1;

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    void update(int attackStrength) {
        this.setAttack(attackStrength);
    }

    public Rat(Game game) {
        // todo: rat enters game here
        System.out.println("New rat enters the game.");
        this.game = game;
        game.rats.add(this);
        game.notifyObservers();
    }

    @Override
    public void close() throws IOException {
        // todo: rat dies ;(
        int ratNumber = new Random().nextInt(game.rats.size());
        game.rats.remove(ratNumber);
        System.out.println("Rat with number: " + (ratNumber + 1) + " is dead.");
        game.notifyObservers();
        if (game.rats.isEmpty())
            throw new IOException("Game over. All rats are dead.");
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        Rat rat1 = new Rat(game);
        Rat rat2 = new Rat(game);
        Rat rat3 = new Rat(game);
        rat3.close();
        Rat rat4 = new Rat(game);
        rat4.close();
        rat1.close();
        Rat rat5 = new Rat(game);
        rat5.close();
        rat2.close();
        IntStream.range(0, 100).forEach(i -> System.out.print(new Random().nextInt(game.rats.size()) + " "));
    }
}
