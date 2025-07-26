import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;


public class YedekSarjor extends GameObject{

    public YedekSarjor(int x, int y, EntityType type) {
            super(x, y, type);
    }
    
        @Override
    public void render(Graphics g) {
        g.setColor(new Color(79,158,138,200));
        g.fillRect(x, y, 20, 30);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x,y,20,30);
    }

    @Override
    public void tick() {
        //boş hareketsiz çünkü
    }
}
