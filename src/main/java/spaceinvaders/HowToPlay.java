package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class HowToPlay extends JFrame {

    public HowToPlay() {

        setTitle("게임 설명");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    public void showHowToPlay(){
        setVisible(true);

        JPanel panel = new JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                //배경 이미지
                ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/howToPlay.png"));
                Image image = icon.getImage();
                g.drawImage(image,2,0,getWidth(),getHeight(),null);





            }
        };

        panel.setLayout(null);




        getContentPane().add(panel);
    }
//    public static void main(String[] args) {
//        HowToPlay howToPlay = new HowToPlay();
//        howToPlay.showHowToPlay();
//    }


}