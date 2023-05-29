package spaceinvaders.entity;

import spaceinvaders.SpriteStore;

public class showEntity extends Entity{
    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    private String[] imgArray = new String[4];
    public showEntity(int x, int y,int idx) {
        super("sprites/xIcon.png", x, y);
        this.imgArray[0] = "sprites/heart.GIF";
        this.imgArray[1] = "sprites/accelator.gif";
        this.imgArray[2] = "sprites/keyreverse.png";
        this.imgArray[3] = "sprites/brokenBox.png";
        this.sprite=SpriteStore.get().getSprite(imgArray[idx]);
    }
    @Override
    public void collidedWith(Entity other) {
    }
}
