import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Canvas;

public class Game extends Canvas implements Runnable{

    public boolean isRunning = false;
    public Thread thread;

    private Handler handler;
    private Camera camera;
    private BufferedImage map = null;
    public static Player player;

    public KeyboardInput ki;
    public MouseInput mi;
    public boolean isSong = false;

    private boolean [] dalga = new boolean[12];
    public static boolean dalgaBittiYazisi;

    public static int tour = 0;
    public static int zombieIndex = 0;

    public Player.gun silah;

    public Game() {
        SoundPlayer theDinerMusic = new SoundPlayer("/res/theDinerMusic.wav");
        theDinerMusic.loop();
        isSong = true;

        handler = new Handler();
        camera = new Camera(0, 0);
        new Window(1000, 563, "TheDINER", this, thread, theDinerMusic);
    
    
        BufferedImageLoader loader = new BufferedImageLoader();
        map = loader.loadImage("/res/theDiner_map.png");
    
        
        loadMap(map);
    
        
        start();
    
        
        player = new Player(100, 100, EntityType.Player, handler);
        handler.addEntity(player);

        silah = new Player.gun();

        ki = new KeyboardInput(handler);
        mi = new MouseInput(handler, camera, this, player);

        //load esnasında bunların silinebilmesi için ki mi olarak tuttum
        this.addKeyListener(ki);
        this.addMouseListener(mi);
        this.addMouseMotionListener(mi);

        for (; tour < 12; tour++) {
            dalga[tour] = true;
        
            if (tour == 0) Player.gun.setGun(GunType.Tabanca);

            else if (tour == 1) {
                //nedenini bilmediğim bi şekilde silahı Piyade olarak set etmeme rağmen
                //ilk mermi atışlarında hız güncellenmiyodu
                //senkronize etmeyle alakalı bir şeydi ama bulamadım problemimi
                //bu yüzden bu kısımda manuel olarak atış hızını da güncelledim ve düzeldi
                //diğer silah geçişlerde hiçbir sıkıntı yok bu geçişte neden oldu bilmiyorum..
                MouseInput.magazine_period = 60*1000/600;
                Player.gun.setGun(GunType.Piyade);
            }

            else if (tour == 3) Player.gun.setGun(GunType.Pompali);
            else if (tour == 5) Player.gun.setGun(GunType.KeskinNisanci);
            else if (tour == 10) Player.gun.setGun(GunType.Roketatar);
        
            int zombieCount = 8 + (int) (tour * 1.1); //zombi sayısı artsın
            int sleepTime = Math.max(600, 1200 - (tour * 60)); //sleeping düşsün
        
            for (; zombieIndex< zombieCount; zombieIndex++) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
                if (zombieIndex%4 == 3) {
                    handler.addEntity(new ZombieSurunge(1080, 300, EntityType.SurungeZombi, handler));
                } else if (zombieIndex%4 == 1) {
                    handler.addEntity(new ZombieAsitli(600, 550, EntityType.AsitTukurenZombi, handler));
                    if (tour >= 3)
                    handler.addEntity(new ZombieTank(600, 350, EntityType.TankZombi, handler));
                } else if (zombieIndex%4 == 0){
                    handler.addEntity(new ZombieNormal(530, 548, EntityType.NormalZombi, handler));
                } else {
                    if(tour>4 && tour%16 == 1)
                        handler.addEntity(new ZombieAsitli(1200, 850, EntityType.AsitTukurenZombi, handler));
                    else if(tour>4 && tour%16 == 3)
                        handler.addEntity(new ZombieSurunge(1200, 850, EntityType.SurungeZombi, handler));
                }
                if (tour >= 3 && zombieIndex%16 == 4) { //tank zombiler daha da gelmeye başlasın
                    handler.addEntity(new ZombieTank(650, 360-(tour*6), EntityType.TankZombi, handler));
                }
            }
            
        
            while (dalga[tour]) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
                boolean zombiVarMi = false;
                for (int i = 0; i<handler.entities.size();i++) {
                    GameObject e = handler.entities.get(i);
                    if (e instanceof Zombie) {
                        zombiVarMi = true;
                        break;
                    }
                }
        
                if (!zombiVarMi) {
                    if (dalga[11] == true){
                        JOptionPane.showMessageDialog(null, "12 turu da başarıyla tamamladın! toplam skor: " + Player.getScore(), "you ate", JOptionPane.PLAIN_MESSAGE);
                        System.exit(0);
                    }
                    break;
                }
            }

            zombieIndex = 0;
        
            dalgaBittiYazisi = true;
        
            try {
                Thread.sleep(3000); //3 sn ara
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dalga[tour] = false;
            dalgaBittiYazisi = false;
        }



    }
    public void resumeGame() {
        isRunning = true;
    }

    void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handler.setDown(false);
        handler.setLeft(false);
        handler.setRight(false);
        handler.setUp(false);

        this.requestFocus();
        long lastTime = System.nanoTime();
        int FPS = 60;
        double ns = 1000000000 / (double)FPS;
        double delta = 0;
        
        long timer = System.currentTimeMillis();
        @SuppressWarnings("unused")
        int frames = 0;
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                tick();
                delta--;
            }

            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }
    
    public void tick() {
        camera.tick(player); //kamera kayması
        handler.tick();     //tüm varlıklar için tick
        System.out.println(player.x + " " + player.y);
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        //DRAW DRAAW DRAAW

        g.setColor(new Color(225, 225, 235, 240));
        g.fillRect(0, 0, 1000, 563);

        g2d.translate(-camera.getX(), -camera.getY());

        handler.render(g);

        g2d.translate(camera.getX(), camera.getY());

        String healthText = "Toplam Kalan Can: " + String.format("%.1f",  Player.getHealth());
        String scoreText = "Toplam Skor: " + Player.getScore();
        String yedekText = "Yedek Şarjörün: ";
        String reloadingWarnText = "ŞARJÖR DEĞİŞTİRİLİYOR...";
        String dalgaWarn = "Zombi Dalga";
        String dalgaBittiWarn = "tebrikler, sonraki dalga için hazırlan!";

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        int padding = 10;
        g.drawString(healthText, getWidth() - g.getFontMetrics().stringWidth(healthText) - padding, 20);
        g.drawString(scoreText, getWidth() - g.getFontMetrics().stringWidth(scoreText) - padding, 50);

        g.drawString("Elindeki Silah: " + Player.gun.type.toString() + " /// Şarjör: " + Player.getMagazine(), padding, 20);

        if(Player.gun.type == GunType.Tabanca) {
            g.drawString(yedekText + "sınırsız!", padding, 50);
        }
        else {
            g.drawString(yedekText + + Player.gun.yedekSarjor + " adet", padding, 50);   
        }
        


        //şarjör değiştirme yazısı ayarları
        long currentTime = System.currentTimeMillis();
        if (currentTime - KeyboardInput.ReloadCurrentTime < 1000) MouseInput.isReloading = true;
        else MouseInput.isReloading = false;
        if(MouseInput.isReloading && (Player.gun.yedekSarjor > 0 || Player.gun.type == GunType.Tabanca)) 
            g.drawString(reloadingWarnText, padding, 80);
        else if(Player.gun.yedekSarjor <= 0 && Player.gun.type != GunType.Tabanca)
            g.drawString("reload yapılamaz...", padding, 80);
        

        //ateş için hazırlanma süresi uyarısı
        //piyade ve tabanca için yazmasını istemedim, hızlılar zaten
        if (currentTime - MouseInput.lastShotTime < MouseInput.magazine_period) MouseInput.isTime = true;
        else MouseInput.isTime = false;
        if (MouseInput.isTime /*&& Player.gun.type != GunType.Piyade && Player.gun.type != GunType.Tabanca*/) 
            g.drawString("Ateş etmek için " + (MouseInput.magazine_period - (double)(currentTime - MouseInput.lastShotTime))/1000+ " saniye daha bekle. ", padding, 100);
        
        
        
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 21));
        padding = 450;
        for(int i=0;i<dalga.length;i++) {

            if(dalgaBittiYazisi) {
                g.drawString(dalgaBittiWarn, getWidth() - g.getFontMetrics().stringWidth(scoreText) - padding + 20, 30);
                if(dalga[0] || dalga[2] || dalga[4] || dalga[9]) {
                    g.drawString("kullandığın silah güncellendi.", getWidth() - g.getFontMetrics().stringWidth(scoreText) - padding + 20, 60);
                }
            }

            else if(dalga[i]) {
                g.drawString(dalgaWarn+" "+(i+1), getWidth() - g.getFontMetrics().stringWidth(scoreText) - padding + 60, 30);
            }
        }
        
        //draw DRAWWWWWWWWWW biti

        g.dispose();
        bs.show();

    }

    private void loadMap(BufferedImage image) {
        int weight = image.getWidth();
        int height = image.getHeight();

        for(int w=0; w<weight; w++) {
            for(int h=0; h<height; h++) {
                int pixel = image.getRGB(w, h);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
            

            if(red == 255 && green == 0 && blue == 0)
                handler.addEntity(new Block(w*32, h*32, EntityType.Block));

            if(red == 255 && green == 100 && blue == 0) {
                handler.addEntity(new Block(w*32, h*32, EntityType.KirilmazBlock));
            }
            }
        }
    }


    public static void main(String[] args) {
        new Game();
    }

    void readDatasToSave(GameSave newSave) {
        newSave.player = Game.player;

        newSave.camera = camera;

        newSave.playerHealth = Player.getHealth();
        newSave.playerScore = Player.getScore();

        newSave.dalga = dalga;

        newSave.handler = handler;

        newSave.tour = Game.tour;
        newSave.zombieIndex = Game.zombieIndex;
        

        newSave.gunType = Player.gun.type;
    }

    GameSave savedDatasToImply() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("oyunKaydi.ser"))) {
        return (GameSave) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("KAYDEDILMIS DOSYA YOK.");
            return null;
        }
        catch (IOException e) {
            e.getMessage();
            return null;
        }
    }

    void updateDatas(GameSave kayitli) {
        Game.player = kayitli.player;

        try{
            player.loadPlayerIcon();
        } catch (Exception e) {
            System.out.println("player görüntüsü yüklenemedi, kırmızı kutu olarak devam et");
        }
        

        camera = kayitli.camera;

        Player.setHealth(kayitli.playerHealth);
        Player.setScore(kayitli.playerScore);

        dalga = kayitli.dalga;

        handler = kayitli.handler;
        handler.setDown(false);
        handler.setLeft(false);
        handler.setRight(false);
        handler.setUp(false);
        //gereksiz hareketleri önlemesi için handlerdaki değerleri false ladık.

        this.removeKeyListener(ki);
        this.removeMouseListener(mi);
        this.removeMouseMotionListener(mi);
        //tüm listenerleri silip yeni handler ve player için tekrardan ekliyorum
        ki = new KeyboardInput(handler);
        mi = new MouseInput(handler, camera, this, player);

        this.addKeyListener(ki);
        this.addMouseListener(mi);
        this.addMouseMotionListener(mi);

        Game.tour = kayitli.tour;
        Game.zombieIndex = kayitli.zombieIndex;

        Player.gun.setGun(kayitli.gunType);
    }

    
}