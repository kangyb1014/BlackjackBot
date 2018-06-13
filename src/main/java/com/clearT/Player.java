package com.clearT;

import net.dv8tion.jda.core.entities.User;

public class Player extends  Person{

    private User user;
    private int gold;


    Player(User user){
        this.user = user;
        gold = 100;
    }

    void decreaseGold(int val){
        gold -= val;
    }

    void increaseGold(int val){
        gold += val;
    }

    public int getGold() {return gold;}

    public User getUser() {
        return user;
    }
}
