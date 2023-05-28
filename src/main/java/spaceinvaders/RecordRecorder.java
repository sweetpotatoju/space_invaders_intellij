package spaceinvaders;

public class RecordRecorder {
    private int scoreStamp;
    public void scoreModeAdd(int score){ scoreStamp += score; }
    public int getScore() { return scoreStamp; }
    public void scoreInit() { scoreStamp = 0; }
}
