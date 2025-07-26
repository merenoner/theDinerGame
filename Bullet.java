import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends GameObject {

    private Handler handler;
    private double bulletSpeed;
    private double angle;


    public Bullet(int x, int y, EntityType type, Handler handler, double angle) {
        super(x, y, type);
        this.handler = handler;
        this.angle = angle;
        this.bulletSpeed = Player.gun.speed;
    }

    @Override
    public void tick() {
        x += bulletSpeed * Math.cos(angle);
        y += bulletSpeed * Math.sin(angle);

        for(int i = 0; i<handler.entities.size(); i++) {

            GameObject anyObject = handler.entities.get(i);

            if(anyObject instanceof Block) {

                if(getBounds().intersects(anyObject.getBounds())){
                    handler.removeEntity(this);

                    if(Player.gun.type == GunType.Roketatar) {
                        Rectangle etkiAlani = new Rectangle(
                            anyObject.getX()-75,
                            anyObject.getY()-75,
                            150,
                            150
                        );

                        for(int j=0; j<handler.entities.size(); j++) {
                            GameObject temp = handler.entities.get(j);

                            if(temp instanceof Zombie && temp.getBounds().intersects(etkiAlani)) {
                                ((Zombie)temp).setHealth(((Zombie)temp).getHealth() - 3);
                            }

                            else if(temp.getType() == EntityType.Block && temp.getBounds().intersects(etkiAlani)) {
                                handler.removeEntity(temp);
                            }

                        }

                        handler.addEntity(new BombEffect(anyObject.getX()-75, anyObject.getY()-75, EntityType.RoketatarEfekt, 150, 150, 60, handler));
                    }

                }
                    
            }

        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 8, 8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 8,8);
    }
    
}
