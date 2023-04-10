package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;

public class Window extends JFrame {

    public Window() {

        setTitle("Spaceinvaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


    }
    public void showWindow(){
        setVisible(true);
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 이미지 파일 경로를 적절히 수정하세요.
                ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/Background.png"));
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

                ImageIcon btImage = new ImageIcon(getClass().getResource("/sprites/gameModeButton.png"));
                g.drawImage(btImage.getImage(), 23, 400, 750, 100, null);

                ImageIcon myPageButton = new ImageIcon(getClass().getResource("/sprites/myPageButton.png"));
                g.drawImage(myPageButton.getImage(), 700, 30, 50, 50, null);
            }
        };

        panel.setLayout(null);

        JButton button1 = new JButton("mashupmode");
        JButton button2 = new JButton("scoremode");
        JButton button3 = new JButton("2P mode");
        JButton myPageButton = new JButton();
        JButton ranking = new JButton();

        //버튼 투명하게
        button1.setOpaque(false);
        button2.setOpaque(false);
        button3.setOpaque(false);
        myPageButton.setOpaque(false);


        //버튼 테투리 없애기
        myPageButton.setBorderPainted(false);

        button1.setBounds(25, 400, 250, 100);
        button2.setBounds(275, 400, 250, 100);
        button3.setBounds(525, 400, 250, 100);
        myPageButton.setBounds(700, 30, 50, 50);

        button1.setFont(new Font("Arial", Font.PLAIN, 20));
        button2.setFont(new Font("Arial", Font.PLAIN, 20));
        button3.setFont(new Font("Arial", Font.PLAIN, 20));

        button1.setBackground(new Color(36, 54, 105));
        button2.setBackground(new Color(36, 54, 105));
        button3.setBackground(new Color(36, 54, 105));
        myPageButton.setBackground(new Color(36, 54, 105));

        button2.setForeground(Color.WHITE);
        button1.setForeground(Color.WHITE);
        button3.setForeground(Color.WHITE);

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

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(myPageButton);

        getContentPane().add(panel);

//                    new LoginPage();
//                    new MFirebaseTool().hashCode();

    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage();
                new MFirebaseTool().hashCode();
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