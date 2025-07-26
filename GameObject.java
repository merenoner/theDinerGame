import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public abstract class GameObject implements Serializable{
    int x, y;
    float speed_X = 0, speed_Y = 0;
    int height, width;
    EntityType type;

    public GameObject (int x, int y, EntityType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public abstract Rectangle getBounds();

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType e) {
        this.type = e;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getSpeed_X() {
        return speed_X;
    }

    public void setSpeed_X(float speed_X) {
        this.speed_X = speed_X;
    }

    public float getSpeed_Y() {
        return speed_Y;
    }

    public void setSpeed_Y(float speed_Y) {
        this.speed_Y = speed_Y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


}
