package spaceinvaders;

import spaceinvaders.entity.ItemEntity;
import spaceinvaders.entity.ShipEntity;

import java.util.Timer;
import java.util.TimerTask;

public class ItemSlotMachine {
    private Game game;
    private ShipEntity ship;
    public ItemSlotMachine(Game game, ShipEntity ship){
        this.game = game;
        this.ship = ship;
    }
    public void spinItem() {
        int itemIdx = (int) (Math.random() * 5);
        ItemEntity display;
        if (ship.is2P()) display = new ItemEntity(game, "sprites/itemBox.png", 775, 550);
        else display = new ItemEntity(game, "sprites/itemBox.png", 5, 550);
        game.addEntity(display);
        for (int j = 0; j < 100; j++) {
            for (int i = 1; i < 6; i++) {
                display.showIt(i);
            }
        }
        display.showIt(itemIdx);
        game.removeEntity(display);
        switch(itemIdx){
            case 1://Life add
                if (ship.getLife()>2)return;
                ship.LifeIncrease();
                System.out.println("Life");
                return;
            case 2://Ship Accelation
                ship.accelation();
                System.out.println("Accel");
                return;
            case 3://Ship keyReverse
                game.keyReverse(ship.is2P());
                System.out.println("Reverse");
                return;
            case 4://Alien speedUp
                System.out.println("Invasion");
                game.alienInvasion();
                return;
            default:
                return;//nothing
        }
    }
}
