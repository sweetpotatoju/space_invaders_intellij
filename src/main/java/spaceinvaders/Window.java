package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class Window extends JFrame {
    private FirebaseTool firebaseTool;

    private GlobalStorage globalStorage;

    private JLabel label;
    private JLabel label2;


    public Window() {
        setTitle("Spaceinvaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        new BackgroundMusic();

    }




    public void showWindow(){
        label = new JLabel();
        label2 = new JLabel();
        setVisible(true);
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 이미지 파일 경로를 적절히 수정하세요.
                ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/windowBack.png"));
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

//                ImageIcon btImage = new ImageIcon(getClass().getResource("/sprites/buttons1.jpg"));
//                g.drawImage(btImage.getImage(), 59, 30, 690, 510, null);

//                ImageIcon optionButton = new ImageIcon(getClass().getResource("/sprites/optionButton.png"));
//                g.drawImage(optionButton.getImage(), 440,430,320,110, null);
                ImageIcon basicProfile = new ImageIcon(getClass().getResource("/sprites/basicProfile.png"));
                g.drawImage(basicProfile.getImage(), 440,100,100,100,null);

            }
        };





        panel.setLayout(null);

//        JLabel playerName = new JLabel(); //플레이어 아이디
//        JLabel playerBC = new JLabel(); //플레이어 최고점수
//

        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setBounds(560, 110, 200, 30);
//        panel.add(label);

        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setForeground(Color.WHITE);
        label2.setBounds(560, 150, 200, 30);
//        panel.add(label2);

//        FirebaseTool firebaseTool1 = new FirebaseTool();
//        firebaseTool1.GetUserProfileImage(GlobalStorage.getInstance().getUserID());

        label.setText(GlobalStorage.getInstance().getUserID()+"님");
        label2.setText("My Best Score: " + GlobalStorage.getInstance().getUserBestScore());
        panel.add(label);
        panel.add(label2);


        JButton button1 = new JButton(); //score
//        JButton button2 = new JButton();
        JButton button3 = new JButton(); //2p
        JButton ranking = new JButton();
        JButton mypage = new JButton();
        JButton Info = new JButton(); //howtoP lay 버튼

        //버튼 투명하게
        button1.setOpaque(false);
//        button2.setOpaque(false);
        button3.setOpaque(false);
        ranking.setOpaque(false);
        mypage.setOpaque(false);
        Info.setOpaque(false);

        //버튼 테투리 없애기
        button1.setBorderPainted(false);
//        button2.setBorderPainted(false);
        button3.setBorderPainted(false);
        ranking.setBorderPainted(false);
        mypage.setBorderPainted(false);
        Info.setBorderPainted(false);

        button1.setBounds(59, 50, 311, 103);
//        button2.setBounds(59, 160, 300, 110);
        button3.setBounds(59, 230, 311, 103);
        ranking.setBounds(59,405,311,103);
        Info.setBounds(423,230,311, 103 ); //howtoplay
        mypage.setBounds(423,405,311, 103 );

        button1.setFont(new Font("Arial", Font.PLAIN, 60));
//        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        button3.setFont(new Font("Arial", Font.PLAIN, 20));

        button1.setBackground(new Color(36, 54, 105));
//        button2.setBackground(new Color(36, 54, 105));
        button3.setBackground(new Color(36, 54, 105));
        ranking.setBackground(new Color(36,54, 105 ));
        mypage.setBackground(new Color(36,54, 105 ));
        Info.setBackground(new Color(36,54, 105 ));

//        button2.setForeground(Color.WHITE);
        button1.setForeground(Color.WHITE);
        button3.setForeground(Color.WHITE);
        ranking.setForeground(Color.WHITE);
        mypage.setForeground(Color.WHITE);
        Info.setForeground(Color.WHITE);

        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Game game = new Game("1p");
                        game.gameLoop();
                    }
                });
                thread.start();
            }
        });

        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Game game = new Game("2p");
                        game.gameLoop();
                    }
                });
                thread.start();
            }
        });

        ranking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Ranking ranking = new Ranking();
                    }
                });
                thread.start();
            }
        });

        mypage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mypage mypage = new Mypage();
                    }
                });
                thread.start();
            }
        });


        Info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                        HowToPlay howToPlay = new HowToPlay();
                        howToPlay.showHowToPlay();
                    }
        });





        panel.add(button1);
//        panel.add(button2);
        panel.add(button3);
        panel.add(Info);
        panel.add(ranking);
        panel.add(mypage);

        getContentPane().add(panel);


    }




    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                LoginPage loginPage = new LoginPage();
//                loginPage.setLoginLiscctener(new LoginPage.LoginListener() {
//                    @Override
//                    public void loginSuccess(String email) {
//                        Window window = new Window();
//                        window.showWindow();
//
//                    }
//                });
//            }
//        });

        Window window = new Window();
        window.showWindow();
    }
}