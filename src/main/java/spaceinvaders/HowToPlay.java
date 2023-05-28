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


//                //홈버튼 이미지
//                ImageIcon homeIcon = new ImageIcon(getClass().getResource("sprites/goHome.png"));
//                Image image1 = homeIcon.getImage();
//                g.drawImage(image1,360,500,80,40,null);

            }
        };

        JButton homeButton = new JButton();
        homeButton.setOpaque(false);
        homeButton.setBorderPainted(false);
        homeButton.setContentAreaFilled(false);
        homeButton.setBounds(360, 500, 50, 50);
        homeButton.setForeground(Color.WHITE);

        // 홈 버튼에 클릭 이벤트 리스너 등록
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 창 종료
                dispose();
            }
        });

        panel.add(homeButton);
        panel.setLayout(null);






        getContentPane().add(panel);
    } }
//    public static void main(String[] args) {
///        HowToPlay howToPlay = new HowToPlay();
////        howToPlay.showHowToPlay();
////    }
//
//
