package spaceinvaders;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import javax.swing.ImageIcon;

public class Window extends JFrame{

    public Window() {


//        super("Spaceinvaders ");
//
//        // 프레임 크기 설정
//        setSize(800, 600);
//
//        // 프레임을 화면 가운데에 배치
//        setLocationRelativeTo(null);
//
//        // 프레임을 닫았을 때 메모리에서 제거되도록 설정
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/sprites/Background.png")));
//        Image backgroundImage = backgroundIcon.getImage();
//
//        // 배경 이미지 표시할 JLabel 생성
//        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
//
//        // JLabel 위치와 크기 설정
//        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
//
//        // 프레임에 배경 이미지 JLabel 추가
//        getContentPane().add(backgroundLabel);
//
//        // 레이아웃 설정
//        getContentPane().setLayout(null);
//
//        // 버튼 생성
//        JButton btn1 = new JButton("mashupmode");
//        JButton btn2 = new JButton("scoremode");
//        JButton btn3 = new JButton("2P mode");
//
//        // ★ 버튼 위치와 크기 설정
//        btn1.setBounds(25, 400, 250, 200);
//        btn2.setBounds(275, 400, 250, 200);
//        btn3.setBounds(525, 400, 250, 200);
//
//        // ★ 프레임에다가 버튼 추가
//        getContentPane().add(btn1);
//        getContentPane().add(btn2);
//        getContentPane().add(btn3);
//
//        // 프레임이 보이도록 설정
//        setVisible(true);
//    }
        {
            setTitle("Spaceinvaders");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel() {

                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // 이미지 파일 경로를 적절히 수정하세요.
                    ImageIcon icon = new ImageIcon(getClass().getResource("/sprites/Background.png"));
                    Image image = icon.getImage();
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
                }
            };

            panel.setLayout(null);

            JButton button1 = new JButton("mashupmode");
            JButton button2 = new JButton("scoremode");
            JButton button3 = new JButton("2P mode");

            button1.setBounds(25, 400, 250, 200);
            button2.setBounds(275, 400, 250, 200);
            button3.setBounds(525, 400, 250, 200);

            button1.setFont(new Font("Arial", Font.PLAIN, 20));
            button2.setFont(new Font("Arial", Font.PLAIN, 20));
            button3.setFont(new Font("Arial", Font.PLAIN, 20));

            button1.setBackground(new Color(36, 54, 105));
            button2.setBackground(new Color(36, 54, 105));
            button3.setBackground(new Color(36, 54, 105));

            button2.setForeground(Color.WHITE);
            button1.setForeground(Color.WHITE);
            button3.setForeground(Color.WHITE);

            panel.add(button1);
            panel.add(button2);
            panel.add(button3);



            getContentPane().add(panel);
            setVisible(true);
        } }


        public static void main (String[]args){
            new Window();

        }

    }