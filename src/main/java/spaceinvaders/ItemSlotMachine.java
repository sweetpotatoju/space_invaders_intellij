package spaceinvaders;

import spaceinvaders.entity.ShipEntity;
import spaceinvaders.entity.showEntity;

import java.util.TimerTask;

public class ItemSlotMachine {
    private Game game;
    private ShipEntity ship;
    private TimerTask showTask;
    public ItemSlotMachine(Game game, ShipEntity ship){
        this.game = game;
        this.ship = ship;
        showItem();
    }
    public void showItem() {
        if(ship.isAffected()){
            System.out.println("Still affected.");
            return;
        }
        int itemIdx = (int) (Math.random() * 4);
        showEntity display = new showEntity(0, 0,itemIdx);
        if (ship.is2P()) display.setLocation(775,550);
        else display.setLocation(5,550);
        switch(itemIdx){
            case 0://Life add
                if (ship.getLife()>2){
                    System.out.println("Life is Fulled");
                    break;
                }
                ship.LifeIncrease();
                System.out.println("Life");
                break;
            case 1://Ship Accelation
                if(ship.isAffected())return;
                ship.setEffected(true);
                ship.accelation();
                System.out.println("Accel");
                break;
            case 2://Ship keyReverse
                if(ship.isAffected())return;
                ship.setEffected(true);
                ship.keyReverse();
                System.out.println("Reverse");
                break;
            default:
                System.out.println("Nothing");
        }
        game.addEntity(display);
        long startTime = SystemTimer.getTime();
        showTask = new TimerTask(){
            @Override
            public void run() {
                long durationTime = SystemTimer.getTime();
                if(durationTime-startTime>5000){
                    ship.setEffected(false);
                    game.removeEntity(display);
                    game.removeTask(showTask);
                }
            }
        };
        game.addTask(showTask,1,100);
    }
}
