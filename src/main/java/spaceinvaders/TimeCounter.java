package spaceinvaders;


import javax.swing.*;
import java.awt.*;

public class TimeCounter extends JLabel implements Runnable {
    int width = 75, height = 75;
    int x = 350, y = 50;

    int second;


    public TimeCounter(int second) {
        setOpaque(true);
        setBounds(x, y, width, height);
        setForeground(Color.BLUE);
        setText(second + "");
        setFont(new Font("맑은고딕", Font.PLAIN, 50));
        setHorizontalAlignment(JLabel.CENTER);

        this.second = second;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);	// 0.1초
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (second >= 0) {
                second += 0.1;		// 0.1초씩 증가
                setText(String.format("%.2f", second / 1000.0));
                repaint();
                revalidate();
            } else {
                //System.out.println("종료");
                break;
            }
        }
    }
}