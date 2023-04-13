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

    private FirebaseTool firebaseTool;

    private GlobalStorage globalStorage;


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

        firebaseTool = FirebaseTool.getInstance();
        globalStorage = GlobalStorage.getInstance();

        //OK버튼
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ID = tID.getText();
                pw = tpw.getPassword();

                if (ID.isEmpty() || Arrays.toString(pw).isEmpty()) {
                    message.setText("아이디 또는 비밀번호를 입력하세요");
                } else {
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

    private void getDataByEmail() {
        try {
            String id = tID.getText();
            String pw = tpw.getText();

            if (firebaseTool.Login(id, pw)) {
                JOptionPane.showMessageDialog(null, "Hello" + " " + id);
                setVisible(false);
                new Window();

                // 로그인 성공시 LoginListener의 loginSuccess 메소드 호출하여 처리할 로직 구현
                if (loginListener != null) {
                    loginListener.loginSuccess(id);
                }

            } else {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
            }

        } catch (NullPointerException ex) {
            Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setLoginLiscctener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public interface LoginListener {
        void loginSuccess(String email);
    }
}