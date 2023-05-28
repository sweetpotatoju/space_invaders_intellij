package spaceinvaders;

import spaceinvaders.entity.AlienEntity;
import spaceinvaders.entity.ItemEntity;
import spaceinvaders.entity.ShipEntity;
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
        if (ship.is2P()) display = new ItemEntity("sprites/itemBox.png",game, 775, 550);
        else display = new ItemEntity("sprites/itemBox.png",game,5, 550);
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
                if (ship.getLife()>2)break;
                ship.LifeIncrease();
                System.out.println("Life");
                break;
            case 2://Ship Accelation
                ship.accelation();
                System.out.println("Accel");
                break;
            case 3://Ship keyReverse
                ship.keyReverse();
                System.out.println("Reverse");
                break;
            case 4://Alien speedUp
                System.out.println("Invasion");
                AlienEntity.alienInvasion();
                break;
            default:
        }
    }
}
