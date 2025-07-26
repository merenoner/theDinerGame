import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BombEffect extends GameObject {
    private int duration;
    private Handler handler;

    public BombEffect(int x, int y, EntityType type, int width, int height, int duration, Handler handler) {
        super(x, y, type);
        this.width = width;
        this.height = height;
        this.duration = duration;
        this.handler = handler;
    }

    @Override
    public void tick() {
        duration--;  
        if (duration <= 0) {
            handler.removeEntity(this);
        }
    }

    @Override
    public void render(Graphics g) {
        if(type == EntityType.AsitliZombiEfekt) g.setColor(new Color(0, 255, 0, 80));
        else if(type == EntityType.RoketatarEfekt) g.setColor(new Color(255, 80, 30, 80));

        g.fillRect(x, y, width, height);
    }


    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
