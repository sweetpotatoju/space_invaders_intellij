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
    private Sprite[] status = new Sprite[2];

    public LifeEntity(Game game, String ref, int x, int y,boolean alien) {
        super(ref, x, y);
        if (alien) {
            status[0] = sprite;
            status[1] = SpriteStore.get().getSprite("sprites/bossLostHp.png");
        } else {
            status[0] = sprite;
            status[1] = SpriteStore.get().getSprite("sprites/lostHeart.gif");
            this.game = game;
        }
    }
    public void onIt() { sprite = status[0]; }
    public void offIt() { sprite = status[1]; }
    @Override
    public void collidedWith(Entity other) {
    }
}
