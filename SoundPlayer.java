import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


//sesi çekebilmek için internetten fazlasıyla yararlandım.
public class SoundPlayer {

    private Clip clip;

    public SoundPlayer(String filePath) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(filePath);
            if (audioSrc == null) {
                throw new IllegalArgumentException("Ses dosyası bulunamadı!");
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));

            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
