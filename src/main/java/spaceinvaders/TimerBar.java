package spaceinvaders;

import javax.swing.*;
import java.awt.*;

class TimerBar extends JLabel implements Runnable {
    int width = 450, height = 30;
    int x = 400, y = 500;
    Color color = new Color(255, 0, 0);

    int second;

    public TimerBar() {
        setBackground(color);
        setOpaque(true);
        setBounds(x, y, width, height);

        this.second = second;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000 / (width / second));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (getWidth() > 0) {
                width -= 1;	// 너비가 1씩 줄어듦
                //System.out.println(width);
//                setBounds(x, y, width, height);
            } else {
                //System.out.println("종료");
                break;
            }
        }

    }
}