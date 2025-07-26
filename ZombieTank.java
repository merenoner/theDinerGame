import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class ZombieTank extends Zombie{
    public ZombieTank(int x, int y, EntityType type, Handler handler) {
        super(x, y, type, handler);
        health = 5;
        attack = 4;
    }

    @Override
    protected double getSpeed() {
        return 2.0;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(70,70,40,230));
        g.fillRect(x, y, 40, 40);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,40,40);
    }

}
