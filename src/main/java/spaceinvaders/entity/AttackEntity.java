package spaceinvaders.entity;

import spaceinvaders.Game;

public class AttackEntity extends Entity {

    private Game game;
    /**
     * True if this shot has been "used", i.e. its hit something
     */
    /**
     * Create a new shot from the player
     *
     * @param game   The game in which the shot has been created
     * @param sprite The sprite representing this shot
     * @param x      The initial x location of the shot
     * @param y      The initial y location of the shot
     */
    public AttackEntity(Game game, String sprite, int x, int y) {
        super(sprite, x, y);
        this.game = game;
        dy = 300;
    }

    /**
     * Request that this shot moved based on time elapsed
     *
     * @param delta The time that has elapsed since last move
     */
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);
        // if we shot off the screen, remove ourselfs // 화면 밖으러 나가면 총알 제거
        if (y > 780) {
            game.removeEntity(this);
        }
    }

    public void collidedWith(Entity other) {
    }
}