package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class Window extends JFrame {
    private FirebaseTool firebaseTool;

    private GlobalStorage globalStorage;

    public Window() {
        setTitle("Spaceinvaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        new BackgroundMusic();

    }

    public void showHowTo(){
        Frame ht = new Frame();
        ht.setSize(500, 400);


//        JPanel panel = new JPanel(){
//            public void paintComponent(Graphics g){
//                super.paintComponent(g);
//
//
//                g.setColor(Color.BLACK);
//                g.fillRect(150, 100, 500, 400);
//            }
//        };
    }


    public void showWindow(){
        setVisible(true);
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 이미지 파일 경로를 적절히 수정하세요.
                ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/windowBackground.png"));
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

                ImageIcon btImage = new ImageIcon(getClass().getResource("/sprites/windowButtons.png"));
                g.drawImage(btImage.getImage(), 59, 30, 690, 510, null);

//                ImageIcon optionButton = new ImageIcon(getClass().getResource("/sprites/optionButton.png"));
//                g.drawImage(optionButton.getImage(), 440,430,320,110, null);


            }
        };

        panel.setLayout(null);

        JButton button1 = new JButton();
        JButton button2 = new JButton();
        JButton button3 = new JButton();
        JButton optionButton = new JButton();
        JButton ranking = new JButton();
        JButton howTo = new JButton();

        //버튼 투명하게
        button1.setOpaque(false);
        button2.setOpaque(false);
        button3.setOpaque(false);
        optionButton.setOpaque(false);
        ranking.setOpaque(false);
        howTo.setOpaque(false);


        //버튼 테투리 없애기
        optionButton.setBorderPainted(false);
        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button3.setBorderPainted(false);
        ranking.setBorderPainted(false);
        howTo.setBorderPainted(false);

        button1.setBounds(59, 30, 300, 110);
        button2.setBounds(59, 160, 300, 110);
        button3.setBounds(59, 300, 300, 110);
        ranking.setBounds(59,430,300,110);
        optionButton.setBounds(410, 430, 305, 110);
        howTo.setBounds(410,300,305, 110 );

        button1.setFont(new Font("Arial", Font.PLAIN, 60));
        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        button3.setFont(new Font("Arial", Font.PLAIN, 20));

        button1.setBackground(new Color(36, 54, 105));
        button2.setBackground(new Color(36, 54, 105));
        button3.setBackground(new Color(36, 54, 105));
        optionButton.setBackground(new Color(36, 54, 105));
        ranking.setBackground(new Color(36,54, 105 ));
        howTo.setBackground(new Color(36,54, 105 ));

        button2.setForeground(Color.WHITE);
        button1.setForeground(Color.WHITE);
        button3.setForeground(Color.WHITE);
        ranking.setForeground(Color.WHITE);
        howTo.setForeground(Color.WHITE);

        button3.addActionListener(new ActionListener() {

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

        button1.addActionListener(new ActionListener() {

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

        howTo.addActionListener(new ActionListener() {
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


        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(optionButton);
        panel.add(ranking);
        panel.add(howTo);

        getContentPane().add(panel);


    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage();
                loginPage.setLoginLiscctener(new LoginPage.LoginListener() {
                    @Override
                    public void loginSuccess(String email) {
                        Window window = new Window();
                        window.showWindow();

                    }
                });
            }
        });
    }
}