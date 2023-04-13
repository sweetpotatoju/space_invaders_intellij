package spaceinvaders.entity;

import spaceinvaders.Game;

public class ItemEntity extends Entity {
    private long moveSpeed;
    private int players;
    private Game game;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    public ItemEntity(Game game, int x, int y) {
        super("sprites/heart.gif", x, y);
        this.game = game;
        moveSpeed = 200;
        this.setHorizontalMovement(moveSpeed);
        this.setVerticalMovement(moveSpeed);
    }
    public void move(long delta) {
        if ((dx < 0) && (x < 10)) {
            dx = -dx;
            return;
        }
        if ((dx > 0) && (x > 750)) {
            dx = -dx;
        }
        //y = 10
        if ((dy > 0) && (y > 550)) {
            game.removeEntity(this);
            return;
        }

        super.move(delta);
    }
    @Override
    public void collidedWith(Entity other) {
    }
}
