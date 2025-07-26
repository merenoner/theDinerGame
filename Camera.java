import java.io.Serializable;

public class Camera implements Serializable{
    
    private float x;
    private float y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject anyObject) {
        x += ((anyObject.getX() - x) - 1000/2) * 0.05f;
        y += ((anyObject.getY() - y) - 563/2) * 0.05f;
        //internetten alındı, smooth olarak geçmesi için

        if( x<=0 ) x=-4;
        if( x>=1032 ) x = 1032;
        if( y<=0 ) y=0;
        if( y>=579 ) y=579;
    }

    public float getX() {
        return x;
    }

    
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
