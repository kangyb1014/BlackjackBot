

package com.clearT;

import net.dv8tion.jda.client.entities.Application;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;

public class BotManager extends JFrame {

    private static JDA jda;
    DiscordIconPanel discordIconPanel = new DiscordIconPanel();

    public static void main(String[] args) {
        JDABuilder jb = new JDABuilder(AccountType.BOT);
        jb.setAutoReconnect(true);
        jb.setToken(Ref.token);
        jb.addEventListener(new BlackJackBot());

        try{
            jda = jb.buildBlocking();
        } catch (LoginException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        BotManager frame = new BotManager();

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screensize = tk.getScreenSize();
        frame.setTitle("디스코드 블랙잭 봇");
        frame.setLocation(screensize.width / 2 - 400 , screensize.height / 2 - 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setVisible(true);
    }

    public BotManager(){

        setLayout(new BorderLayout());
        add(discordIconPanel,BorderLayout.CENTER);
    }
}

class DiscordIconPanel extends JPanel{
    private JLabel discordIcon = new JLabel();
    private JTextArea discordDescription = new JTextArea();
    private String discriptionString = "디스코드 블랙잭 봇입니다. 본 프로그램을 실행한 상태로, \n" + Ref.botaddLink + "\n에 접속해 봇을 추가하세요.";

    public DiscordIconPanel(){

        setLayout(new BorderLayout());
        discordIcon.setIcon(new ImageIcon("image/discordicon.gif"));
        add(discordIcon,BorderLayout.NORTH);

        discordDescription.setFont(new Font("font",Font.BOLD, 16));
        setDiscordDescription(discriptionString);
        add(discordDescription,BorderLayout.CENTER);

    }

    public void setDiscordDescription(String str) {
        discordDescription.setText(str);
    }

}
