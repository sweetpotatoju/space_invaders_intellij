package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;

public class LifeEntity extends Entity{
    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    private Game game;
    private int Life;
    private Sprite[] status = new Sprite[2];

    public LifeEntity(Game game, int x, int y) {
        super("sprites/heart.gif", x, y);
        status[0] = sprite;
        status[1] = SpriteStore.get().getSprite("sprites/lostHeart.gif");
        this.game = game;
    }
    public void LifeDecrease(){
        sprite = status[1];
    }

    @Override
    public void collidedWith(Entity other) {
    }
}
