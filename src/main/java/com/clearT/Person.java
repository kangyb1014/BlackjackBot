package com.clearT;

public class Person {
    //sumofvalue[0]: all A are caculated to 1
    //sumofvalue[1]: when 2 A are picked, 1 A is calculated to 10
    //sumofvalue[2]: when 2 A are picked, 2 A are calculated to 10

    protected int sumofvalue[];
    protected int[] card;
    protected int numofcard;
    protected int numofA;

    Person(){
        initializer();
    }

    protected  void initializer(){
        sumofvalue = new int[3];
        for(int i = 0; i < sumofvalue.length; i++)
            sumofvalue[i] = 0;
        card = new int[21];
        numofcard = 0;
        numofA = 0;
    }

    public boolean isBursted(){
        if (sumofvalue[0] > 21){
            return true;
        }
        return false;
    }

    public void PickCard(CardDeck cardDeck) {
        card[numofcard] = cardDeck.pickRandomCard();
        int cardValue = CardDeck.valueOfCard(card[numofcard]);
        //debug
        System.out.println("뽑은 카드: " + cardValue);

        sumofvalue[0] += cardValue;
        if(cardValue == 1){
            numofA++;
            if(numofA < 2){
                sumofvalue[1] += 11;
            }
            else
                sumofvalue[1] += 1;

            if(numofA < 3){
                sumofvalue[2] += 11;
            }
            else
                sumofvalue[2] += 1;
        }
        else{
            sumofvalue[1] += cardValue;
            sumofvalue[2] += cardValue;
        }

        numofcard++;
    }

    public int getCardAt(int index){
        return card[index];
    }

    public int lastCard(){
        if(numofcard == 0)  return -1;
        return card[numofcard - 1];
    }

    public int getNumofcard() {
        return numofcard;
    }

    public int maxSum(){
        if(sumofvalue[2] <= 21)
            return sumofvalue[2];
        if(sumofvalue[1] <= 21)
            return  sumofvalue[1];
        return  sumofvalue[0];
    }

    public String sumToString(){
        String retStr = Integer.toString(sumofvalue[0]);

        for(int i = 1; i < sumofvalue.length; i++){
            if(sumofvalue[i] < 22 && sumofvalue[i] != sumofvalue[i - 1]) {
                retStr += " 또는 " + Integer.toString(sumofvalue[i]) + " ";
            }
        }
        return retStr;
    }

}
