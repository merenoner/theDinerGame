import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardInput extends KeyAdapter{

    Handler handler;
    static long ReloadCurrentTime;

    public KeyboardInput(Handler handler) {
        this.handler = handler;
    }



    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(key == KeyEvent.VK_R) {
            ReloadCurrentTime = System.currentTimeMillis();
            handler.fillTheMagazine();
            }

        
        if(key == KeyEvent.VK_W) handler.setUp(true);
        if(key == KeyEvent.VK_A) handler.setLeft(true);
        if(key == KeyEvent.VK_D) handler.setRight(true);
        if(key == KeyEvent.VK_S) handler.setDown(true);
        
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        for(int i=0; i < handler.entities.size() ; i++) {
            GameObject temp = handler.entities.get(i);

            if (temp.getType() == EntityType.Player) {

                if(key == KeyEvent.VK_W) handler.setUp(false);
                if(key == KeyEvent.VK_A) handler.setLeft(false);
                if(key == KeyEvent.VK_D) handler.setRight(false);
                if(key == KeyEvent.VK_S) handler.setDown(false);

            }
        }
    }
}
