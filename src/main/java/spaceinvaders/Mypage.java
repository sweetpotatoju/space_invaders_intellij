package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;


public class Mypage extends JFrame {
    private Mypage mypage = this;
    private FirebaseTool firebaseTool;
    private GlobalStorage globalStorage;
    private String currentTheme;
    private String currentProfilePicture;
    private JComboBox<String> themeSelector;
    private JLabel background;
    private JComboBox<String> pictureSelector;
    private JLabel profileLabel;
    private String themeImagePath = "";
    private String profileImagePath = "";
    private Window window;
    private BackgroundMusic backgroundMusic;
//    private OtherWindow otherWindow;

    public Mypage(Window window) {
//        OtherWindow Window = new OtherWindow();
//        this.otherWindow = Window;
        this.window = window;
        setTitle("Spaceinvaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        firebaseTool = FirebaseTool.getInstance();
        globalStorage = GlobalStorage.getInstance();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] themes = {"1", "2", "3", "4", "5"};
        themeSelector = new JComboBox<>(themes);
        themeSelector.setBounds(450, 420, 305, 30);
        themeSelector.setBackground(Color.decode("#E6E6FA"));
        panel.add(themeSelector);

        String[] profilePictures = {"1", "2", "3", "4", "5"};
        pictureSelector = new JComboBox<>(profilePictures);
        pictureSelector.setBounds(450, 460, 305, 30);
        pictureSelector.setBackground(Color.PINK);
        panel.add(pictureSelector);


        // add event listeners to handle theme and profile picture selection
        themeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = (String) themeSelector.getSelectedItem();
                firebaseTool.setUserTheme(globalStorage.getUserID(), currentTheme);
                globalStorage.setUserTheme(currentTheme); // 현재 테마 업데이트
                applyTheme(currentTheme);
            }
        });

        pictureSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentProfilePicture = (String) pictureSelector.getSelectedItem();
                firebaseTool.setUserProfileImage(globalStorage.getUserID(), currentProfilePicture);
                globalStorage.setUserProfileImage(currentProfilePicture); // 현재 프로필 이미지 업데이트
                applyProfilePicture(currentProfilePicture);
            }
        });

        JButton apply = new JButton("적용");
        apply.setBounds(650,510,80,30);
        apply.setBackground(Color.WHITE);
        apply.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                mypage.dispose();

                if (backgroundMusic != null) {
                    backgroundMusic.stop();
                }


//                otherWindow.stopBackgroundMusic();
//                otherWindow.setBackgroundMusic();



                String themeImagePath = ImagePath.getThemeImagePathMap().get(GlobalStorage.getInstance().getUserTheme());
                String profileImagePath = ImagePath.getProfileImagePathMap().get(GlobalStorage.getInstance().getUserProfileImage());
                new Window(themeImagePath, profileImagePath);

            }
        });
//        apply.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mypage.dispose();
//                        new Window(themeImagePath, profileImagePath);
//                    }
//                });
//                thread.start();
//            }
//        });

        panel.add(apply);
        background = new JLabel();
        background.setBounds(0, 0, getWidth(), getHeight());
        panel.add(background);
        getContentPane().add(panel);

        System.out.println("user theme : " + globalStorage.getUserTheme() + " user profile : " + globalStorage.getUserProfileImage());

        applyTheme(globalStorage.getUserTheme());
        applyProfilePicture(globalStorage.getUserProfileImage());

        setVisible(true);


    }

//    private String[][] themeList = {{"1", "Theme1.jpg"},{"2", "Theme2.jpg"},{"3", "Theme3.jpg"},{"4", "Theme4.jpg"},{"5", "Theme5.jpg"}};



    private void applyTheme(String theme) {
        int width = 800; // 원하는 이미지 폭
        int height = 600; // 원하는 이미지 높이

        if (theme.equals("1")) {
            this.themeImagePath = "src/main/resources/sprites/Theme1.jpg";
        } else if (theme.equals("2")) {
            this.themeImagePath = "src/main/resources/sprites/Theme2.jpg";
        } else if (theme.equals("3")) {
            this.themeImagePath = "src/main/resources/sprites/Theme3.jpg";
        } else if (theme.equals("4")) {
            this.themeImagePath = "src/main/resources/sprites/Theme4.jpg";
        } else if (theme.equals("5")) {
            this.themeImagePath = "src/main/resources/sprites/Theme5.jpg";
        }

        ImageIcon icon = new ImageIcon(this.themeImagePath);
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        background.setIcon(icon);
    }


    private void applyProfilePicture(String picture) {
//        this.profileImagePath = "/sprites/Profile1.jpg"; // 기본 프로필 사진 경로
        int profileWidth = 100; // 프로필 사진 폭
        int profileHeight = 100; // 프로필 사진 높이

        if (picture.equals("1")) {
            this.profileImagePath = "/sprites/Profile1.jpg";
        } else if (picture.equals("2")) {
            this.profileImagePath = "/sprites/Profile2.jpg";
        } else if (picture.equals("3")) {
            this.profileImagePath = "/sprites/Profile3.png";
        } else if (picture.equals("4")) {
            this.profileImagePath = "/sprites/Profile4.png";
        } else if (picture.equals("5")) {
            this.profileImagePath = "/sprites/Profile5.png";
        }

        ImageIcon profileIcon = new ImageIcon(getClass().getResource(ImagePath.getProfileImagePathMap().get(picture)));
        Image profileImage = profileIcon.getImage().getScaledInstance(profileWidth, profileHeight, Image.SCALE_SMOOTH);
        profileIcon = new ImageIcon(profileImage);
//        background.setIcon(profileIcon);

        /*// 기존에 추가된 프로필 사진 라벨이 있다면 먼저 제거합니다.
        if (profileLabel != null) {
            background.remove(profileLabel);
        }

*/
        profileLabel = new JLabel(profileIcon);
        profileLabel.setBounds(550, 100, profileWidth, profileHeight);

        background.add(profileLabel); // background 라벨 위에 새로운 프로필 사진을 추가
        background.setComponentZOrder(profileLabel, 0); // profileLabel 라벨이 background 라벨 위에 나타나도록 설정
        background.validate();
        //applyTheme(currentTheme);
    }
//    public static void main(String[] args){
//
//        Mypage mypage = new Mypage();
//
//
//    }

}