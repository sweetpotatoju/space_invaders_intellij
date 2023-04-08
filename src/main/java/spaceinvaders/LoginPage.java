package spaceinvaders;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginPage extends JFrame {
    //id 입력 필드
    private JTextField tID;
    //클리어 버튼
    private JButton btnClear;
    //ok 버튼
    private JButton btnOK;
    //로그인 패널
    private JPanel loginPanel;
    //pw 입력 필드
    private JPasswordField tpw;
    //회원가입 버튼
    private JButton btnResister;
    //메세지 출력 라벨
    private JLabel message;
    //사용자 아이디
    private static String ID = null;
    //사용자 비밀번호
    private char[] pw = null;
    private LoginListener loginListener;


    public LoginPage() {
        setContentPane(loginPanel);
        setTitle("Login");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Dimension frameSize = getSize();
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        //화면을 중앙에 띄우기
        setLocation((windowSize.width - frameSize.width) / 2, (windowSize.height - frameSize.height) / 2);

        //OK버튼
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ID = tID.getText();
                pw = tpw.getPassword();

                if (ID.isEmpty() || Arrays.toString(pw).isEmpty()){
                    message.setText("아이디 또는 비밀번호를 입력하세요");
                }
                else {
                    getDataByEmail();
                }
            }
        });

        //clear 버튼
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tID.setText("");
                tpw.setText("");
            }
        });

        //resister 버튼
        btnResister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterPage();
                dispose();
            }
        });
    }

    private void getDataByEmail(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(MFirebaseTool.getFirebaseApp());
        UserRecord userRecord = null;
        try {
            userRecord = firebaseAuth.getUserByEmail(tID.getText());
            String email = userRecord.getEmail();
            String uid = userRecord.getUid();
            String password = userRecord.getDisplayName();


            if (password.equals(String.valueOf(tpw.getPassword()))){
                JOptionPane.showMessageDialog(null, "Hello" + " " + email);
                setVisible(false);
              ;
                new Window();

                // 로그인 성공시 LoginListener의 loginSuccess 메소드 호출하여 처리할 로직 구현
                if (loginListener != null) {
                    loginListener.loginSuccess(email);
                }


            } else {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
            }

            recoverUserData(uid);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recoverUserData(String uid){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(MFirebaseTool.getFirebaseApp());
        UserRecord userRecord = null;
        try {
            userRecord = firebaseAuth.getUser(uid);

            Logger.getLogger(LoginPage.class.getName()).log(Level.INFO, "유저 데이터를 성공적으로 Fetch:");
            Logger.getLogger(LoginPage.class.getName()).log(Level.INFO, userRecord.getUid());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }


    public void setLoginLiscctener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public interface LoginListener {
        void loginSuccess(String email);
    }
    }