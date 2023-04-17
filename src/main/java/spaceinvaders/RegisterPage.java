package spaceinvaders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterPage extends JFrame {
    //ID 입력 필드
    private JTextField tResisterID;
    //계정 생성 버튼
    private JButton btnAddAccount;
    //레지스터 폼 패널
    private JPanel resisterPanel;
    //PW 입력 필드
    private JPasswordField tResisterPw;

    private FirebaseTool firebaseTool;

    private GlobalStorage globalStorage;
//    private String themeImagePath;
//    private String profileImagePath;

    public RegisterPage() {
//        this.themeImagePath = themeImagePath;
//        this.profileImagePath = profileImagePath;

        setContentPane(resisterPanel);
        setTitle("resister");
        setSize(450, 300);
        setVisible(true);
        Dimension frameSize = getSize();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        //화면 중앙에 띄우기
        setLocation((windowSize.width - frameSize.width) / 2, (windowSize.height - frameSize.height) / 2);

        firebaseTool = FirebaseTool.getInstance();
        globalStorage = GlobalStorage.getInstance();

        //resister 버튼
        btnAddAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String id = tResisterID.getText();
                    String pw = tResisterPw.getText();

                    firebaseTool.Signup(id, pw);

                } catch (NullPointerException exception) {
                    exception.printStackTrace();
                }

//                new LoginPage(themeImagePath, profileImagePath);
                new LoginPage();
                dispose();
            }
        });
    }


}