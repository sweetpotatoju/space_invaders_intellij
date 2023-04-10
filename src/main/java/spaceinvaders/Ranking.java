package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;
import javax.swing.ImageIcon;

public class Ranking extends JFrame{

    int myHighestScore = 100;
    int myRanking = 1;
    String playerName = "이다현";
    int playerScore = 100;

    public Ranking(){

        setTitle("Player Ranking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    public void showRanking(){
        setVisible(true);

        JPanel panel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                //배경 이미지
                ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/rankingPageBack.png"));
                Image image = icon.getImage();
                g.drawImage(image,2,0,getWidth(),getHeight(),null);

                //점수판


                g.drawImage(image,2,0,getWidth(),getHeight(),null);
                g.setColor(Color.BLACK); // 파란색 선택
                g.fillRect(150,100,500,400);

                ImageIcon playerRankImage = new ImageIcon(getClass().getResource("/sprites/playerRank.png"));
                g.drawImage(playerRankImage.getImage(),230,50,300,100,null);

                //내 점수
                g.setColor(Color.WHITE);
                g.fillRect(200,440,400,50);

                //내 등수
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(200,440,75,50);
                g.setColor(Color.BLACK);
                Font font = new Font("여기어때 잘난체", Font.BOLD, 35);
                g.setFont(font);
                g.drawString(myRanking+"등",200,480);
                Font font2 = new Font("여기어때 잘난체", Font.BOLD, 25);
                //내 점수
                g.setFont(font2);
                g.drawString("내 최고 점수: "+myHighestScore+"점", 290,475);
                //랭킹(플레이어 순위)
                g.setColor(Color.WHITE);
                g.drawString("1등"+"     "+playerName+"        "+playerScore+"",285,150);
                g.drawString("2등"+"     "+playerName+"        "+playerScore+"",285,180);
                g.drawString("3등"+"     "+playerName+"        "+playerScore+"",285,210);
                g.drawString("4등"+"     "+playerName+"        "+playerScore+"",285,240);
                g.drawString("5등"+"     "+playerName+"        "+playerScore+"",285,270);
                g.drawString("6등"+"     "+playerName+"        "+playerScore+"",285,300);
                g.drawString("7등"+"     "+playerName+"        "+playerScore+"",285,330);
                g.drawString("8등"+"     "+playerName+"        "+playerScore+"",285,360);
                g.drawString("9등"+"     "+playerName+"        "+playerScore+"",285,390);
                g.drawString("10등"+"     "+playerName+"        "+playerScore+"",271,420);


//                JLabel label1 = new JLabel("1등"+"     ")

            }
        };

        panel.setLayout(null);

        //홈으로 돌아가는 버튼 생성
        JButton goHome = new JButton();

        //버튼 투명하게
        goHome.setOpaque(false);
        //버튼 테두리 없애기
        goHome.setBorderPainted(false);

        goHome.setBounds(630,17,125,52);

        goHome.setBackground(new Color(36,54,105));
        goHome.setForeground(Color.WHITE);


        panel.add(goHome);


        getContentPane().add(panel);
    }
    public static void main(String[] args) {
        Ranking ranking = new Ranking();
        ranking.showRanking();
    }

}