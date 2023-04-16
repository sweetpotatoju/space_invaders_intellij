package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;

public class Window extends JFrame {
    private Window window = this;
    private FirebaseTool firebaseTool;
    private GlobalStorage globalStorage;
    private JLabel label;
    private JLabel label2;
    private JLabel profileLabel;
    private String themeImagePath = "src/main/resources/sprites/windowBack.png";
    private String profileImagePath = "/sprites/basicProfile.png";




    public Window(String themeImagePath, String profileImagePath) {
        this.themeImagePath = themeImagePath;
        this.profileImagePath = profileImagePath;
        setTitle("Spaceinvaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        BackgroundMusic bgm = new BackgroundMusic("src/main/resources/audio/backgroundmusic.wav", executorService);
        executorService.execute(bgm);
        showWindow(window.themeImagePath, window.profileImagePath);
    }


    public void showWindow(String themeImagePath, String profileImagePath){
        label = new JLabel();
        label2 = new JLabel();
        setVisible(true);
        JPanel panel = new JPanel() {

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 이미지 파일 경로를 적절히 수정하세요.
                ImageIcon icon = new ImageIcon(themeImagePath);
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);


                ImageIcon basicProfile = new ImageIcon(getClass().getResource(profileImagePath));
                g.drawImage(basicProfile.getImage(), 440,100,100,100,null);
            }
        };



        panel.setLayout(null);


        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        label.setBounds(560, 110, 200, 30);


        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setForeground(Color.WHITE);
        label2.setBounds(560, 150, 200, 30);


        label.setText(GlobalStorage.getInstance().getUserID());
        label2.setText("My Best Score: " + GlobalStorage.getInstance().getUserBestScore());
        panel.add(label);
        panel.add(label2);


        JButton button1 = new JButton(); //score
        JButton button3 = new JButton(); //2p
        JButton ranking = new JButton();
        JButton mypage = new JButton();
        JButton Info = new JButton(); //howtoP lay 버튼

        //버튼 투명하게
        button1.setOpaque(false);
        button3.setOpaque(false);
        ranking.setOpaque(false);
        mypage.setOpaque(false);
        Info.setOpaque(false);

        //버튼 테투리 없애기
        button1.setBorderPainted(false);
        button3.setBorderPainted(false);
        ranking.setBorderPainted(false);
        mypage.setBorderPainted(false);
        Info.setBorderPainted(false);

        button1.setBounds(59, 50, 311, 103);
        button3.setBounds(59, 230, 311, 103);
        ranking.setBounds(59,405,311,103);
        Info.setBounds(423,230,311, 103 ); //howtoplay
        mypage.setBounds(423,405,311, 103 );

        button1.setFont(new Font("Arial", Font.PLAIN, 60));
        button3.setFont(new Font("Arial", Font.PLAIN, 20));

        button1.setBackground(new Color(36, 54, 105));
        button3.setBackground(new Color(36, 54, 105));
        ranking.setBackground(new Color(36,54, 105 ));
        mypage.setBackground(new Color(36,54, 105 ));
        Info.setBackground(new Color(36,54, 105 ));


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
                        window.dispose();
                        new Mypage(window);
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
        panel.add(button3);
        panel.add(Info);
        panel.add(ranking);
        panel.add(mypage);

        getContentPane().add(panel);
    }



    public static void main(String[] args) {
        String defaultThemeImagePath = "src/main/resources/sprites/windowBack.png";
        String defaultProfileImagePath = "/sprites/basicProfile.png";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginPage loginPage = new LoginPage(defaultThemeImagePath, defaultProfileImagePath);
                loginPage.setLoginLiscctener(new LoginPage.LoginListener() {
                    @Override
                    public void loginSuccess(String email) {
                        Window window = new Window(defaultThemeImagePath, defaultProfileImagePath);
                    }
                });
            }
        });

//        Window window = new Window();
//        window.showWindow();
    }
}