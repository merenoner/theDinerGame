import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectOutputStream;

public class Window{
    
    public Window(int width, int height, String title, Game game, Thread thread, SoundPlayer sp) {

        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));

        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Ortaya hizala

        JLabel infoLabel = new JLabel("Oyun Kontrolleri: ");
        JButton pauseButton = new JButton("Duraklat");
        JButton resumeButton = new JButton("Devam et");
        JButton saveButton = new JButton("Kaydet");
        JButton loadButton = new JButton("Yükle(Kaydedilen Varsa)");
        JButton musicButton = new JButton("Müziği Kapat/Aç");

        buttonPanel.add(infoLabel);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(musicButton);

        pauseButton.addActionListener(e -> {
            game.isRunning = false;
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException k) {
                    k.printStackTrace();
                    }
            }
            
        }
        );


        resumeButton.addActionListener(e -> {
            if (!game.isRunning) {
                game.isRunning = true;
                game.thread = new Thread(game);
                game.thread.start();
            }
        });

        


        saveButton.addActionListener(e -> {
            GameSave newSave = new GameSave();

            //yazılacak save dosyasına verileri girmek için
            game.readDatasToSave(newSave);

            //dosyaya yazz
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("oyunKaydi.ser"))) {
                oos.writeObject(newSave);
                System.out.println("oyun başarıyla kaydedildi");
            } catch (IOException io) {
                io.printStackTrace();
                }

        });

        loadButton.addActionListener(e -> {
            //dosyayı gamesave objesine oku
            GameSave kayitli = game.savedDatasToImply();

            //oyunda güncellenmesi gereken her şeyi güncelle
            game.updateDatas(kayitli);

        });

        musicButton.addActionListener(e -> {
            if(game.isSong) {
                sp.stop();
                game.isSong = false;
            }
            else {
                sp.play();
                game.isSong = true;
            }
        });

        frame.add(buttonPanel, BorderLayout.SOUTH); // Butonları aşağıya ekle
        frame.add(game, BorderLayout.CENTER); // Oyun alanını ortada tut
        frame.setVisible(true);
        
    }    
}