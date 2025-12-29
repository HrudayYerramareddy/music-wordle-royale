package com.wordleroyale.backend.model;

public class Player {
    private final String id;
    private final String name;
    private int hp;
    private boolean alive;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.hp = 100;
        this.alive = true;
    }

    public void drain(int amount) {
        if (!alive) return;

        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            alive = false;
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getHp() { return hp; }
    public boolean isAlive() { return alive; }
}
