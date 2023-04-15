package spaceinvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;


public class Mypage extends JFrame {
    private FirebaseTool firebaseTool;
    private GlobalStorage globalStorage;
    private String currentTheme;
    private String currentProfilePicture;
    private JComboBox<String> themeSelector;
    private JLabel background;
    private JComboBox<String> pictureSelector;
    private JLabel profileLabel;

    public Mypage() {
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
        panel.add(themeSelector);

        String[] profilePictures = {"1", "2", "3", "4", "5"};
        pictureSelector = new JComboBox<>(profilePictures);
        pictureSelector.setBounds(450, 460, 305, 30);
        panel.add(pictureSelector);

        // add event listeners to handle theme and profile picture selection
        themeSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentTheme = (String) themeSelector.getSelectedItem();
                firebaseTool.setUserTheme(globalStorage.getUserID(), currentTheme);
                globalStorage.setUserTheme(currentTheme); // 햔재 테마 업데이트
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

        background = new JLabel();
        background.setBounds(0, 0, getWidth(), getHeight());
        panel.add(background);
        getContentPane().add(panel);

        System.out.println("user theme : " + globalStorage.getUserTheme() + " user profile : " + globalStorage.getUserProfileImage());

        applyTheme(globalStorage.getUserTheme());
        applyProfilePicture(globalStorage.getUserProfileImage());

        setVisible(true);
    }

    private void applyTheme(String theme) {
        String imagePath = "/sprites/Theme1.jpg";
        int width = 800; // 원하는 이미지 폭
        int height = 600; // 원하는 이미지 높이

        if (theme.equals("1")) {
            imagePath = "/sprites/Theme1.jpg";
        } else if (theme.equals("2")) {
            imagePath = "/sprites/Theme2.jpg";
        } else if (theme.equals("3")) {
            imagePath = "/sprites/Theme3.jpg";
        } else if (theme.equals("4")) {
            imagePath = "/sprites/Theme4.jpg";
        } else if (theme.equals("5")) {
            imagePath = "/sprites/Theme5.jpg";
        }

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        icon = new ImageIcon(image);
        background.setIcon(icon);
    }


    private void applyProfilePicture(String picture) {
        String imagePath = "/sprites/Profile1.jpg"; // 기본 프로필 사진 경로
        int profileWidth = 100; // 프로필 사진 폭
        int profileHeight = 100; // 프로필 사진 높이

        if (picture.equals("1")) {
            imagePath = "/sprites/Profile1.jpg";
        } else if (picture.equals("2")) {
            imagePath = "/sprites/Profile2.jpg";
        } else if (picture.equals("3")) {
            imagePath = "/sprites/Profile3.jpg";
        } else if (picture.equals("4")) {
            imagePath = "/sprites/Profile4.jpg";
        } else if (picture.equals("5")) {
            imagePath = "/sprites/Profile5.jpg";
        }

        ImageIcon profileIcon = new ImageIcon(getClass().getResource(imagePath));
        Image profileImage = profileIcon.getImage().getScaledInstance(profileWidth, profileHeight, Image.SCALE_SMOOTH);
        profileIcon = new ImageIcon(profileImage);

        /*// 기존에 추가된 프로필 사진 라벨이 있다면 먼저 제거합니다.
        if (profileLabel != null) {
            background.remove(profileLabel);
        }

*/
        profileLabel = new JLabel(profileIcon);
        profileLabel.setBounds(550, 200, profileWidth, profileHeight);

        background.add(profileLabel); // background 라벨 위에 새로운 프로필 사진을 추가
        background.setComponentZOrder(profileLabel, 0); // profileLabel 라벨이 background 라벨 위에 나타나도록 설정
        background.validate();
        //applyTheme(currentTheme);
    }
/*    public static void main(String[] args){

        Mypage mypage = new Mypage();


    } */

}