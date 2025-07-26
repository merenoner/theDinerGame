import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Block extends GameObject{

    int width = 32;
    int height = 32;

    public Block(int x, int y, EntityType type) {
            super(x, y, type);
        }
    
    @Override
    public void tick() {
        //boş
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(123, 98, 145,205));
        g.fillRect(x, y, 32, 32);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32,32);
    }
    
}
