import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ZombieNormal extends Zombie{

    public ZombieNormal(int x, int y, EntityType type, Handler handler) {
        super(x, y, type, handler);
        health = 3;
        attack = 2; //orta sağlık orta hasar
    }

    @Override
    protected double getSpeed() {
        return 3.0;
    }


    @Override
    public void render(Graphics g) {
        g.setColor(new Color(76,92,75,230));
        g.fillOval(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,32,32);
    }
    
}
