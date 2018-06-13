package com.clearT;

import net.dv8tion.jda.core.entities.User;

public class Playerlist {
    private static Player[] players = new Player[100];
    private static int numOfplayers = 0;

    public static Player searchPlayer(User user){
        for(int i = 0; i < numOfplayers; i++){
            System.out.println("플레이어가 존재하는가? : " + user.equals(players[i].getUser()));
            if(user.equals(players[i].getUser())) {
                players[i].initializer();
                return players[i];
            }
        }

        Player newPlayer = new Player(user);
        players[numOfplayers] = newPlayer;
        numOfplayers++;
        return newPlayer;
    }
}
