package spaceinvaders;

public class RecordRecorder {
    private Game game;
    private int scoreStamp;
    private long targetTime;
    private String howLongLive;
    public RecordRecorder(Game game){
        this.game = game;
    }
    public void TimeAttackMode(){
        //targetTime - startTime;
    }
    public void scoreMode(int score){ scoreStamp = game.giveKillScore(); printRecord(); }
    public void LoopMode(){ howLongLive = game.giveSurvivalTime(); printRecord(); }
    public void printRecord(){System.out.println(howLongLive+"/"+scoreStamp);}
}
