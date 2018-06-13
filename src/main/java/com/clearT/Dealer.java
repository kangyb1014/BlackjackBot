package com.clearT;

public class Dealer extends  Person{

    public boolean Under17(CardDeck cardDeck){
        if(maxSum() < 17) {
            PickCard(cardDeck);
            return true;
        }
        return false;
    }


    public String pickedCard(){
        String retStr = "";
        for(int i = 0; i < numofcard; i++){
            retStr += CardDeck.toString(card[i]) + " ";
        }
        return  retStr;
    }

}
