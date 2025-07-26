import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ZombieAsitli extends Zombie {
    
    public ZombieAsitli(int x, int y, EntityType type, Handler handler) {
        super(x, y, type, handler);
        health = 2;
        attack = 2;
    }
    
    
    @Override
    protected double getSpeed() {
        return 3.0;
    }


    @Override
    public void render(Graphics g) {
        g.setColor(new Color(39,100,42,230));
        g.fillOval(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
}
