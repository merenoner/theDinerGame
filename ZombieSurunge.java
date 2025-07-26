import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class ZombieSurunge extends Zombie{

    public ZombieSurunge(int x, int y, EntityType type, Handler handler) {
        super(x, y, type, handler);
        health = 2;
        attack = 2;
    }

    @Override
    protected double getSpeed() {
        return 4.0;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(158,0,3,230));
        g.fillOval(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
    
}