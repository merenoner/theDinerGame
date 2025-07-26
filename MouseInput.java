
//AÇIKLAMA
//bullet atma kontrolü üzerinde gerçekten çok uzun zaman uğraştım
//basılı tutarak atmayı düzeltsem başka bir şey bozuluyordu vs..
//o yüzden MouseInput dosyasında çok fazla dışarıdan kaynak kullandım, Timer Component vs şahsen doğrudan yaptığım şeyler değil.

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MouseInput extends MouseAdapter implements Serializable{

    private Handler handler;
    private boolean isPressed = false;
    private Timer timer;
    private Camera camera;
    private TimerTask shootTask;
    public static long lastShotTime = 0;
    private Component gameComponent;
    private Player player;
    public static long magazine_period;
    
    public static boolean isReloading;
    public static boolean isTime;
    //bunlar ekrana yazdırılması gerekecek şeyler için statikler


    public MouseInput(Handler handler, Camera camera, Component gameComponent, Player player) {
        this.handler = handler;
        this.gameComponent = gameComponent;
        this.player = player;
        this.camera = camera;
        timer = null;
    }

    public void mousePressed(MouseEvent e) {
        isPressed = true;
        startShooting();
    }

    public void mouseReleased(MouseEvent e) {
        isPressed = false;
        stopShooting();
    }

    private void shoot() {
        MouseInput.magazine_period = 60 * 1000 / Player.getFire();

        long currentTime = System.currentTimeMillis();

        long seconds = currentTime - lastShotTime;
        MouseInput.isTime = seconds < magazine_period;


        if (!isPressed || MouseInput.isTime|| isReloading || Player.getMagazine() <= 0) {
            return;
        }

        lastShotTime = currentTime;

        //mouse konumunu net alabilmek için
        Point p = gameComponent.getMousePosition();
        if (p == null) return;
        int mouse_x = (int) (p.x + camera.getX());
        int mouse_y = (int) (p.y + camera.getY());

        
        double angle = Math.atan2(mouse_y - player.getY(), mouse_x - player.getX());

        //tabanca keskin nişancı ve roketatar için yalnızca mermi atımı/güncellemesi
        //delme olayını colissionda bullet yok etmede halledicem keskin nişanc için
        //patlama olayı da aynı şekilde.


        //piyade için sapmaya göre angle güncellenir
        if(Player.gun.type == GunType.Piyade) {
            Random random = new Random();
            double max = 30.0;
            double sapma = Math.toRadians((random.nextDouble()* 2 * max)-max);

            angle += sapma;

            handler.addEntity(new Bullet(player.getX() + 16, player.getY() + 24, EntityType.Bullet, handler, angle));
            Player.setMagazine(Player.getMagazine()-1);
        }

        else if(Player.gun.type == GunType.Tabanca ||
            Player.gun.type == GunType.KeskinNisanci ||
            Player.gun.type == GunType.Roketatar) {
            handler.addEntity(new Bullet(player.getX() + 16, player.getY() + 24, EntityType.Bullet, handler, angle));
            Player.setMagazine(Player.getMagazine()-1);
        }


        //pompalı için 9 tane aralıklı atar
        else if(Player.gun.type == GunType.Pompali) {
            double angleFark = Math.toRadians(45) / 8; //5 derece

            for (int i = 0; i < 9; i++) {
                double bulletAngle = angle - Math.toRadians(22.5) + (i * angleFark);
                handler.addEntity(new Bullet(player.getX() + 16, player.getY() + 24, EntityType.Bullet, handler, bulletAngle)); 
            }
            Player.setMagazine(Player.getMagazine()-1);
        }

    }

    //herhangi bi press durumunda timer başlatılır, bu sayede tek tık ve basılı tutma için de mermi atımı
    //optimize edilmiş olur
    private void startShooting() {
        if (timer == null) {

            timer = new Timer();
            shootTask = new TimerTask() {
                @Override
                public void run() {
                    shoot();
                }
            };
            timer.scheduleAtFixedRate(shootTask, 0, magazine_period+15);
        }
    }

    private void stopShooting() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            shootTask = null;
        }
    }
}




