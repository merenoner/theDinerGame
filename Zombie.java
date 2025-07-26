import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class Zombie extends GameObject {

    protected Handler handler;
    protected GameObject player;

    protected int health;
    protected int attack;

    protected long shootTime=0;
    protected long jumpTime=0;
    protected boolean isJumping = false;

    public Zombie(int x, int y, EntityType type, Handler handler) {
        super(x, y, type);
        this.handler = handler;
        this.player = getPlayer();
    }

    private GameObject getPlayer() {
        for (int i=0; i < handler.entities.size(); i++) {
            GameObject tempGameObject = handler.entities.get(i);
            if (tempGameObject.getType() == EntityType.Player) {
                return tempGameObject;
            }
        }
        return null;
    }

    @Override
    public void tick() {
        if (player != null) {
            if (health <= 0) {

                //asit tüküren zombi ölünce etraftaki zombilerin
                //canını 2 azaltır - duvarları siler
                //oyuncu canını da 50 azaltır eğer o alandaysa
                if(this instanceof ZombieAsitli){
                    Rectangle etkiAlani = new Rectangle(
                            this.getX()-60,
                            this.getY()-60,
                            120,
                            120
                        );
                    
                    double random = Math.random();
                    if(random < 0.25 && Player.gun.type != GunType.Tabanca) {
                        handler.addEntity(new YedekSarjor(this.getX(), this.getY(), EntityType.YedekSarjor));
                    }

                    List<GameObject> silinecekler = new ArrayList<>();
                    silinecekler.add(this);

                    for(int j=0; j<handler.entities.size(); j++) {
                        GameObject temp = handler.entities.get(j);
                        if(temp instanceof Zombie && temp.getBounds().intersects(etkiAlani)) {
                            ((Zombie)temp).setHealth(((Zombie)temp).getHealth() - 3);
                        }

                        else if(temp instanceof Player && temp.getBounds().intersects(etkiAlani)) {
                            Player.setHealth(Player.getHealth() - 30);
                        }

                        else if(temp.getType() == EntityType.Block && temp.getBounds().intersects(etkiAlani)) {
                            silinecekler.add(temp);
                        }
                    }

                    for(GameObject sil : silinecekler) {
                        handler.removeEntity(sil);
                        if(sil instanceof Zombie) Player.setScore(Player.getScore() + 5);
                    }

                    handler.addEntity(new BombEffect(this.getX() - 60, this.getY() - 60, EntityType.AsitliZombiEfekt , 120, 120, 60, handler));

                }
                
                else {
                    double random = Math.random();
                    if(random < 0.25 && Player.gun.type != GunType.Tabanca) {
                        handler.addEntity(new YedekSarjor(this.x, this.y, EntityType.YedekSarjor));
                    }
                    handler.removeEntity(this);
                    Player.setScore(Player.getScore() + 5);
                }
                
            }

            int playerX = player.getX();
            int playerY = player.getY();

            Collision.checkSomeCollision(this, handler);

            double diffX = playerX - x;
            double diffY = playerY - y;
            double distance = Math.sqrt(diffX * diffX + diffY * diffY);

            //asit tüküren zombinin asit tükürmesi
            if(this instanceof ZombieAsitli) {
                long currentTime = System.currentTimeMillis();
                if(currentTime - shootTime > 1000) {
                    shootTime = System.currentTimeMillis();
                    handler.addEntity(new Acyd(this.getX(), this.getY(), EntityType.Asit, handler, ((Player)player)));
                }
            }

            //sürünge zombiler 230 mesafeden sonra oyuncunun sırtına atlar
            //sonra kaçabiliriz, bir süre sonra tekrar atlar
            long currentTime = System.currentTimeMillis();
            if (this instanceof ZombieSurunge && distance <= 230 && distance >= 30) {
                if (!isJumping && currentTime - jumpTime > 3500) { //3,5 saniye yeniden atlamak için güç toplar
                    jumpTime = currentTime;
                    isJumping = true;
                }

                if (isJumping && currentTime - jumpTime < 400) {
                    speed_X = (int) ((diffX / distance) * getSpeed() * 3.75);
                    speed_Y = (int) ((diffY / distance) * getSpeed() * 3.75);
                } else if (isJumping) { 
                    isJumping = false;
                    jumpTime = currentTime;
                }
            }


            else {
            speed_X = (int) ((diffX / distance) * getSpeed());
            speed_Y = (int) ((diffY / distance) * getSpeed());
            }

            x += speed_X;
            y += speed_Y;
        }
    }


    protected abstract double getSpeed();

    
    public int getHealth() {
        return health;
    }

    
    public void setHealth(int health) {
        this.health = health;
    }

    
    public int getAttack() {
        return attack;
    }

    
    public void setAttack(int attack) {
        this.attack = attack;
    }



}
