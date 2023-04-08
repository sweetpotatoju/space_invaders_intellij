package spaceinvaders;

import javax.swing.*;
import java.awt.*;

public class Ranking extends JLabel {
    int width = 400, height = 300;
    int x = 200, y = 150;

    /*유저 최고 점수*/
    int userHighestScore = 0;
    /*유저 아이디*/
    String userID;


    public Ranking(String userID, int userHighestScore) {
        setOpaque(true);
        setBounds(x, y, width, height);
        setForeground(Color.BLUE);
        setText(userID + ":"+ userHighestScore+"");
        setFont(new Font("맑은고딕", Font.PLAIN, 10));
        setHorizontalAlignment(JLabel.CENTER);

        this.userID = userID;
        this.userHighestScore = userHighestScore;
    }


}
