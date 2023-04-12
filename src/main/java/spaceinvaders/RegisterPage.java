package spaceinvaders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import spaceinvaders.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private FirebaseAuth mFirebaseAuth;

    public RegisterPage() {
        setContentPane(resisterPanel);
        setTitle("resister");
        setSize(450, 300);
        setVisible(true);
        Dimension frameSize = getSize();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        //화면 중앙에 띄우기
        setLocation((windowSize.width - frameSize.width) / 2, (windowSize.height - frameSize.height) / 2);

        mFirebaseAuth = FirebaseAuth.getInstance(MFirebaseTool.getFirebaseApp());

        //resister 버튼
        btnAddAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mFirebaseAuth.createUser(new UserRecord.CreateRequest()
                            .setEmail(tResisterID.getText())
                            .setEmailVerified(false)
                            .setDisplayName(String.valueOf(tResisterPw.getPassword())));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    String encodedEmail = null;
                    DatabaseReference usersRef = ref.child(encodedEmail);

                    int score = 0;

                    Map<String, User> users = new HashMap<>();
                    users.put(encodedEmail, new User(tResisterID.getText(),String.valueOf(tResisterPw.getPassword()),"someValue",score));

                    usersRef.setValueAsync(users);

                    Logger.getLogger(RegisterPage.class.getName()).log(Level.INFO, "유저 생성 성공");
                    JOptionPane.showMessageDialog(null, "유저 생성 성공");
                }
                catch (FirebaseAuthException ex){
                    Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
                }

                new LoginPage();
                dispose();
            }
        });
    }

}