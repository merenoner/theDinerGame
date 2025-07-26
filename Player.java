
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;

public class Player extends GameObject{

    Handler handler;
    int width = 64;
    int height = 64;

    private static double health = 1000;
    private static int score = 0;
    public transient BufferedImage ikon;

    
    public Player(int x, int y, EntityType type, Handler handler) {
        super(x, y, type);
        this.handler = handler;
        
        loadPlayerIcon();
    }

    public void loadPlayerIcon() {
        BufferedImageLoader loader = new BufferedImageLoader();
        ikon = loader.loadImage("/res/ikon.png");
    }

    public static class gun {

        protected static int max_magazine;
        protected static int magazine;
        protected static int fire;
        protected static GunType type;
        
        protected static int yedekSarjor;
        protected static double speed;
        
        public gun()
            {
                //obje oluşturulurken tabanca ile başlanır
                max_magazine = 12;
                magazine = 12;
                fire = 120;
                type = GunType.Tabanca;
                speed = 7;
            }
        
        public static void setGun(GunType type) {
            gun.type = type;
            if(type == GunType.Tabanca) {
                max_magazine = 12;
                magazine = 12;
                fire = 120;
                speed = 7; //kontrol edilmeyecek - sonsuz
                }
            
            else if(type == GunType.Piyade) {
                max_magazine = 30;
                magazine = 30;
                fire = 600;
                speed = 8;
                yedekSarjor = 2;
            }

            else if(type == GunType.Pompali) {
                max_magazine = 5;
                magazine = 5;
                fire = 60;
                speed = 10;
                yedekSarjor = 3;
            }

            else if(type == GunType.KeskinNisanci) {
                max_magazine = 5;
                magazine = 5;
                fire = 30;
                speed = 13;
                yedekSarjor = 4;
            }

            else if(type == GunType.Roketatar) {
                max_magazine = 1;
                magazine = 1;
                fire = 10;
                speed = 10;
                yedekSarjor = 5;
            }
        }
    }

    public static int getmax_magazine() {
        return gun.max_magazine;
    }

    public static int getMagazine() {
        return gun.magazine;
    }

    public static void setMagazine(int magazine) {
        gun.magazine = magazine;
    }

    public static int getFire() {
        return gun.fire;
    }

    public static void setFire(int fire) {
        gun.fire = fire;
    }


    @Override
    public void tick() {
        if(Player.health <= 0) {
            JOptionPane.showMessageDialog(null, "öldün malesef :)) gbye", "goodbye", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        x += speed_X;
        y += speed_Y;

        Collision.checkSomeCollision(this,handler);

        if(handler.isUp()) speed_Y = -4;
        else if(!handler.isDown()) speed_Y = 0;
        
        if(handler.isDown()) speed_Y = 4;
        else if(!handler.isUp()) speed_Y = 0;

        if(handler.isRight()) speed_X = 4;
        else if(!handler.isLeft()) speed_X = 0;

        if(handler.isLeft()) speed_X = -4;
        else if(!handler.isRight()) speed_X = 0;
    }


    @Override
    public void render(Graphics g) {
        if (ikon != null) {
            g.drawImage(ikon, x, y, width, height, null);
        } else {
            //resim herhangi bişey yüzünden yüklenmezse
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    

    
    public static double getHealth() {
        return health;
    }

    
    public static void setHealth(Double Newhealth) {
        health = Newhealth;
    }

    public static int getScore() {
        return score;
    }

    
    public static void setScore(int Newscore) {
        score = Newscore;
    }

}
