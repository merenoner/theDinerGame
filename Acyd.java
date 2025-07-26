import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;

public class Acyd extends GameObject{
    private Handler handler;
    private double acydSpeed;
    private double angle;

    public Acyd(int x, int y, EntityType type, Handler handler,Player player) {
        super(x, y, type);
        this.handler = handler;
        acydSpeed = 6;
        angle = Math.atan2(player.getY() - y, player.getX() - x);
    }

    @Override
    public void tick() {
        x += acydSpeed * Math.cos(angle);
        y += acydSpeed * Math.sin(angle);
        
        for(int i = 0; i<handler.entities.size(); i++) {

            GameObject anyObject = handler.entities.get(i);

            if(anyObject.getType() == EntityType.Block) {

                if(getBounds().intersects(anyObject.getBounds())){
                    handler.removeEntity(this);
                }
                    
            }

        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0,100,0,240));
        g.fillOval(x, y, 8, 8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 8,8);
    }
    
}
