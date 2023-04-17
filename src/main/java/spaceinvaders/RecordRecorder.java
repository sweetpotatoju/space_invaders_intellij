package spaceinvaders;

public class RecordRecorder {
    private Game game;
    private int scoreStamp;
    private long targetTime;
    private String howLongLive;
    public RecordRecorder(Game game){
        this.game = game;
    }
    public void scoreModeAdd(int score){ scoreStamp += score; }
    public int getScore() { return scoreStamp; }
    public void scoreInit() { scoreStamp = 0; }
    public void LoopModeResult(){} //howLongLive = game.giveSurvivalTime(); }
}
