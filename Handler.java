import java.util.LinkedList;
import java.awt.Graphics;
import java.io.Serializable;

public class Handler implements Serializable{
    LinkedList<GameObject> entities = new LinkedList<GameObject>();
    
    private boolean up = false, down = false, right = false, left = false;

    public void fillTheMagazine() {
        if(Player.gun.yedekSarjor <= 0 && Player.gun.type != GunType.Tabanca) {
            MouseInput.isReloading = false;
            return;
        }

        MouseInput.isReloading = true;        
        Player.setMagazine(Player.getmax_magazine());
        Player.gun.yedekSarjor--;
    }

    public void tick() {
        for(int i=0; i<entities.size(); i++) {
            entities.get(i).tick();
        }
    }

    public void render(Graphics g) {
        for(int i=0; i<entities.size(); i++) {
            entities.get(i).render(g);
        }
    }

    public void addEntity(GameObject newObj) {
        entities.add(newObj);
    }

    public void removeEntity(GameObject Entity) {
        entities.remove(Entity);
    }


    public boolean isUp() {
        return up;
    }
    public void setUp(boolean set) {
        this.up = set;
    }

    public boolean isDown() {
        return down;
    }
    public void setDown(boolean set) {
        this.down = set;
    }

    public boolean isRight() {
        return right;
    }
    public void setRight(boolean set) {
        this.right = set;
    }

    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean set) {
        this.left = set;
    }

}
