package spaceinvaders;


import javax.swing.*;
import java.awt.*;

public class Timer extends JLabel implements Runnable {
    int width = 75, height = 75;
    int x = 350, y = 50;

    int second;


    public Timer(int second) {
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
                Thread.sleep(1000);	// 1초
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (second > 0) {
                second -= 1;		// 1초씩 줄어듦
                setText(second + "");
                repaint();
                revalidate();
            } else {
                //System.out.println("종료");
                break;
            }
        }
    }
}