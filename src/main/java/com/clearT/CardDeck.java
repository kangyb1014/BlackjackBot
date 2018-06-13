package com.clearT;

public class CardDeck {
    private boolean[] Deck = new boolean[53];
    private int remainCard = Deck.length;

    CardDeck(){
        for(int i = 0; i < Deck.length; i++)
            Deck[i] = true;
    }

    public int pickRandomCard(){
        if(remainCard == 0)
            return -1;
        int cardNum;
        do{
            cardNum = (int)(Math.random() * 52) + 1;
        }while(Deck[cardNum] == false);
        Deck[cardNum] = false;
        return cardNum;
    }

    public static int valueOfCard(int cardnum) {
        while (cardnum > 13) cardnum -= 13;
        if (cardnum > 9)
            return 10;
        return cardnum;
    }

    public static String toString(int card){

        String retString = "";
        if(card >= 1 && card < 14) {
            retString += "Spade ";
        }
        else if (card >= 14 && card < 27) {
            retString += "Heart ";
            card -= 13;
        }
        else if (card >= 27 && card < 40) {
            retString += "Diamond ";
            card -= 26;
        }
        else if (card >= 40 && card < 53) {
            retString += "Club ";
            card -= 39;
        }

        if (card == 1)
            retString += "A";
        else if(card <= 10)
            retString += Integer.toString(card);
        else if(card == 11)
            retString += "J";
        else if (card == 12)
            retString += "Q";
        else if (card == 13)
            retString += "K";

        return retString;

    }

}
