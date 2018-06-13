package com.clearT;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BlackJackBot extends ListenerAdapter {

    public static JDA jda;
    private Dealer dealer;
    private CardDeck cardDeck;
    private Player player;
    private int gameStep;
    private int betting;
    User playingUser;


    BlackJackBot(){
        gameStep = 0;
        playingUser = null;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent evt){
        // objects
        User user = evt.getAuthor();
        MessageChannel ch = evt.getChannel();
        Message msg = evt.getMessage();
        if(user.isBot()) return;
        System.out.println("채팅 메시지:" + msg.getContentRaw());

        if(playingUser != null && playingUser.equals(user) == false){
            return;
        }
        playingUser = user;

        if(msg.getContentRaw().equalsIgnoreCase("!블랙잭 시작")) {
            if (gameStep > 0) {
                ch.sendMessage("게임이 이미 진행중입니다.").queue();
                return;
            }

            //game start
            dealer = new Dealer();
            cardDeck = new CardDeck();
            player = Playerlist.searchPlayer(user);
            if(player.getGold() <= 0){
                ch.sendMessage("골드가 부족해 게임을 진행할 수 없습니다.").queue();
                return;
            }
            gameStep = 1;

            ch.sendMessage("블랙잭 게임을 시작합니다! 베팅할 금액을 입력하세요").queue();
        }

        else if(gameStep == 1 && msg.getContentRaw().length() > 7 &&msg.getContentRaw().substring(0,7).equalsIgnoreCase("!블랙잭 베팅")){
            gameStep1(evt);
        }

        else if(msg.getContentRaw().equalsIgnoreCase("!블랙잭 상태")){
            ch.sendMessage("" + gameStep).queue();
        }

        else if (gameStep == 2 && msg.getContentRaw().equalsIgnoreCase("!예")){
            player.PickCard(cardDeck);
            ch.sendMessage("당신이 뽑은 카드는 " +
                    CardDeck.toString(player.lastCard()) + "입니다.").queue();
            ch.sendMessage("카드 값의 합은 " + player.sumToString() + "입니다.").queue();

            if(player.isBursted()){
                ch.sendMessage("당신의 카드 값의 합이 21을 넘었습니다. 게임에서 패배합니다.").queue();
                dealerWin(evt);
                return;
            }

            if(dealer.Under17(cardDeck)){
                ch.sendMessage("딜러는 카드 한 장을 더 가져갔습니다.").queue();
            }
            else{
                ch.sendMessage("딜러는 카드를 더 뽑지 않았습니다.").queue();
            }

            ch.sendMessage("카드를 더 뽑으시겠습니까? (!예 / !아니오)").queue();

        }

        else if (gameStep == 2 && msg.getContentRaw().equalsIgnoreCase("!아니오")){
            int dealerDraw = 0;
            while(dealer.Under17(cardDeck)){
                dealerDraw++;
            }
            if(dealerDraw == 0){
                ch.sendMessage("딜러는 카드를 더 가져가지 않았습니다.").queue();
            }
            else if (dealerDraw > 0){
                ch.sendMessage("딜러는 카드 " + dealerDraw + "장을 더 가져갔습니다").queue();
            }

            ch.sendMessage("딜러의 카드를 공개합니다.").queue();
            ch.sendMessage("딜러의 카드는 " + dealer.pickedCard() + "였습니다.").queue();
            ch.sendMessage("딜러 카드의 합은 " + dealer.maxSum() + "였습니다").queue();

            System.out.println("딜러:" + dealer.maxSum() + " 플레이어:" + player.maxSum());

            if(dealer.isBursted()){
                ch.sendMessage("딜러 카드의 합이 21을 넘었습니다. 게임에서 승리했습니다!").queue();
                playerWin(evt);
                return;
            }

            if(player.maxSum() <= dealer.maxSum()){
                ch.sendMessage("당신의 카드 값의 합이 딜러의 카드 값의 합을 넘지 못했습니다. 게임에서 패배합니다.").queue();
                dealerWin(evt);
                return;
            }

            if(player.maxSum() > dealer.maxSum()){
                ch.sendMessage("당신의 카드 값의 합이 딜러의 카드 값의 합을 넘었습니다. 게임에서 승리했습니다!").queue();
                playerWin(evt);
                return;
            }
        }

        else if(msg.getContentRaw().equals("!re")){
            ch.sendMessage("관리자 권한으로 강제종료합니다").queue();
            gameStep = 0;
            playingUser = null;
        }

        else if (msg.getContentRaw().equalsIgnoreCase("!블랙잭 명령어")){
            ch.sendMessage("!블랙잭 시작 : 블랙잭 게임을 시작합니다.\n" +
                    "!블랙잭 베팅 [금액] : 카드를 받기 전 베팅할 금액을 입력합니다.\n" +
                    "!블랙잭 상태 : 현재 게임이 어느 정도 진행되고 있는가를 알려줍니다.\n" ).queue();
        }
    }

    private void gameStep1(MessageReceivedEvent evt){
        User user = evt.getAuthor();
        MessageChannel ch = evt.getChannel();
        Message msg = evt.getMessage();

        try {
            betting = Integer.parseInt(msg.getContentRaw().substring(8));
        }catch (NumberFormatException e){
            ch.sendMessage("정확한 숫자를 입력하세요").queue();
            e.printStackTrace();
            return;
        }

        if(betting < 1){
            ch.sendMessage("0골드 이하를 베팅할 수는 없습니다").queue();
            return;
        }
        if(player.getGold() < betting) {
            ch.sendMessage("현재 " + player.getGold() + " 골드가 있습니다. 더 큰 금액을 걸 수는 없습니다.").queue();
            return;
        }
        player.decreaseGold(betting);
        ch.sendMessage(betting + " 골드를 베팅해 " + player.getGold() + " 골드가 남았습니다.").queue();

        player.PickCard(cardDeck);
        player.PickCard(cardDeck);
        ch.sendMessage("당신이 뽑은 카드는 " + CardDeck.toString(player.getCardAt(0)) + "와 " +
                 CardDeck.toString(player.getCardAt(1)) + "입니다.").queue();
        ch.sendMessage("카드 값의 합은 " + player.sumToString() + "입니다.").queue();

        dealer.PickCard(cardDeck);
        dealer.PickCard(cardDeck);
        ch.sendMessage("딜러가 뽑은 두 장의 카드 중 하나는 " + CardDeck.toString(dealer.lastCard()) + " 입니다.").queue();

        ch.sendMessage("카드를 더 뽑으시겠습니까? !예 / !아니오").queue();

        gameStep = 2;
    }

    private void playerWin(MessageReceivedEvent evt){
        evt.getChannel().sendMessage("베팅 금액 " + betting + "의 2배인 " + (betting*2) + " 골드를 획득했습니다.").queue();
        player.increaseGold(betting * 2);
        evt.getChannel().sendMessage("현재 보유 중인 골드는 " + player.getGold() + " 골드 입니다").queue();
        gameStep = 0;
        playingUser = null;
    }

    private void dealerWin(MessageReceivedEvent evt){
        evt.getChannel().sendMessage("현재 보유 중인 골드는 " + player.getGold() + " 골드 입니다").queue();
        gameStep = 0;
        playingUser = null;
    }

}
