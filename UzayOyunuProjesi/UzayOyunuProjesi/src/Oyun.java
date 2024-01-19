import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

public class Oyun extends JPanel implements KeyListener, ActionListener {
    private ArrayList<Canavar> canavarList = new ArrayList<>();
    private Timer timer = new Timer(17, this);
    public boolean gameOver = false;
    private int olenCanavar = 0;
    private int score = 0;
    private BufferedImage uzayGemiImage, arkaPlanImage, cerceve1, cerceve2;
    SimpleDataStorage simpleDataStorage = new SimpleDataStorage();
    private String soru[]={"8+2","6X3","7-3","10/2"};
    private int cevap[]={10,18,4,5};
    private Random random = new Random();
    private int atesEkleY = 8;
    private int canavarEkleY = 1;
    private int uzayGemisiX = 400;
    private int uzayGemisiEkleX = 20;
    private int uzayGemisiY = 480;
    private int sayac = 0;
    private int sure = 0;
    Boolean cevapDogruMu = false;
    private int enYuksekScore = 0;
    private int oyunPanelWidth = 800;
    private int oyunPanelHeight = 650;
    private int sureKontrol = 0;

    enum GameStatus {
        NOT_STARTED,
        RUNNING,
        GAME_OVER;
    }

    public Oyun() {
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        try {
          //  cerceve2 = ImageIO.read(new FileImageInputStream(new File("images/Cerceve2.png")));
            //cerceve1 = ImageIO.read(new FileImageInputStream(new File("images/Cerceve.png")));
            arkaPlanImage = ImageIO.read(new FileImageInputStream(new File("images/Arkaplan.png")));
            uzayGemiImage = ImageIO.read(new FileImageInputStream(new File("images/Cocuk.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CanavarlariBaslat();
    }

    public void baslatOyun() {
        timer.start();
        MuzikEkle("Muzik/oyunEsnasında.wav");
        String scoreString = simpleDataStorage.loadData("score");
        int currentScore = 0;

        try {
            currentScore = Integer.parseInt(scoreString);
        } catch (NumberFormatException e) {
            // Hata durumunda yapılacak işlemler
            e.printStackTrace();
        }
        enYuksekScore = currentScore;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(cerceve2, -5, 0, getWidth(), getHeight(), this);
        g.drawImage(arkaPlanImage, 4, 5, oyunPanelWidth - 10, oyunPanelHeight - 10, this);
        g.drawImage(uzayGemiImage, uzayGemisiX, uzayGemisiY, uzayGemiImage.getWidth(), uzayGemiImage.getHeight(), this);

        for (Canavar canavar : canavarList) {
            g.drawImage(canavar.getImage(), canavar.getX(), canavar.getY(),
                    canavar.getImage().getWidth(), canavar.getImage().getHeight(), this);
        }



        g.drawImage(cerceve1, 0, 0, oyunPanelWidth, oyunPanelHeight, this);

        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Süre: " + sure, getWidth() - 180, 90);

        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Skor: " + score, getWidth() - 180, 110);

        if(score == cevap[0]){
            gameOver = true;

        } else{
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Soru: " + soru[0], 20, 20);

        }


        //oyun bitti
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 200));
            g.drawString("Game Over", 100, 150);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SureKontrol();
        if (!gameOver) {
            for (int i = 0; i < canavarList.size(); i++) {
                Canavar canavar = canavarList.get(i);
                canavar.setY(canavar.getY() + canavarEkleY);

                if (canavar.getY() > getHeight()) {
                    canavarList.remove(i);
                    canavarList.add(new Canavar(random.nextInt(oyunPanelWidth - 70), -50));
                }
                CarpismaKontrol();
            }
            OyunBittiMiKontrol();
            repaint();
        }
    }


    private void CanavarlariBaslat() {
        for (int i = 0; i < 10; i++) {
            canavarList.add(new Canavar(random.nextInt(oyunPanelWidth - 70), -50));
        }
    }

    private void OyunBittiMiKontrol() {
        for (Canavar canavar : canavarList) {
            if (canavar.getY() >= getHeight() - 187) {
                gameOver = false;
                break;
            }
        }
        if (gameOver) {
            OyunBitti();
        }
    }

    private void YenidenBaşlat() {
        canavarList.clear();
        uzayGemisiX = 380;
        gameOver = false;
        olenCanavar = 0;
        timer.restart();
        CanavarlariBaslat();
        sayac = 0;
        sure = 0;
        canavarEkleY = 1;
        uzayGemisiEkleX = 20;
        atesEkleY = 8;
        enYuksekScore = Integer.parseInt(simpleDataStorage.loadData("score"));
        score = 0;
        repaint();
    }


    private void OyunBitti() {
        JFrame frame = new JFrame("Oyun Bitti");
        frame.setSize(800, 650);
        frame.setLocationRelativeTo(null);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton yenidenBaslatButton = new JButton("Yeniden Başlat");
        yenidenBaslatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Yeniden başlatma işlemleri burada yapılır
                YenidenBaşlat();
                frame.dispose();
            }
        });

        JButton cikisButton = new JButton("Çıkış");
        cikisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Çıkış işlemleri burada yapılır
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(yenidenBaslatButton);
        buttonPanel.add(cikisButton);

        frame.setLayout(new BorderLayout());
        frame.add(gameOverLabel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Penc
    }

    //süre kontrolü sağlar
    private void SureKontrol() {
        sayac++;
        if (sayac > 70) {
            sure++;
            sureKontrol++;
            sayac = 0;
            if (sureKontrol > 100) {
                sureKontrol = 0;
                canavarEkleY += 1;
                sayac = 0;
                uzayGemisiEkleX += 6;
                atesEkleY += 4;
            }
        }
    }

    private int EnYuksekScoreKontrol(int yeniSkor, int yüksekSkor) {
        if (yeniSkor > yüksekSkor) {
            return yeniSkor;
        } else {
            return yüksekSkor;
        }
    }

    private void MuzikEkle(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }


    public void CarpismaKontrol() {
        Rectangle uzayGemisiRect = new Rectangle(uzayGemisiX, uzayGemisiY, uzayGemiImage.getWidth(), uzayGemiImage.getHeight());

        for (int i = 0; i < canavarList.size(); i++) {
            Canavar canavar = canavarList.get(i);
            Rectangle canavarRect = new Rectangle(canavar.getX(), canavar.getY(), canavar.getImage().getWidth(), canavar.getImage().getHeight());

            if (uzayGemisiRect.intersects(canavarRect)) {
                // Çarpışma tespit edildi, çocuğu kaldır ve puan ekle
                canavarList.remove(i);
                int puan = 0;

                switch (canavar.getImageIndex()) {
                    case 1:
                        puan = 1;
                        break;
                    case 2:
                        puan = 2;
                        break;
                    case 3:
                        puan = -1;
                        break;
                    case 4:
                        puan = -2;
                        break;
                }

                score += puan;
                olenCanavar++;
                MuzikEkle("Muzik/Carpisma.wav");
                i--; // Liste boyutu değiştiği için indis azaltılmalı
                canavarList.add(new Canavar(random.nextInt(oyunPanelWidth - 2 * canavar.getImage().getWidth()), -50));
                break;
            }
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            if (uzayGemisiX <= 15) {
                uzayGemisiX = 15;
            } else {
                uzayGemisiX -= uzayGemisiEkleX;
            }
        } else if (c == KeyEvent.VK_RIGHT) {
            if (uzayGemisiX >= (oyunPanelWidth) - 70) {
                uzayGemisiX = oyunPanelWidth - 70;
            } else {
                uzayGemisiX += uzayGemisiEkleX;
            }
        } else if (c==KeyEvent.VK_UP){
            if (uzayGemisiEkleX<40){
                uzayGemisiEkleX+=5;
            }
        } else if (c==KeyEvent.VK_DOWN) {
            if (uzayGemisiEkleX>5){
                uzayGemisiEkleX-=5;
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

class Canavar {
    private int x;
    private int y;
    private BufferedImage image;
    private int imageIndex; // Hangi resmin yüklendiğini takip etmek için

    public Canavar(int x, int y) {
        this.x = x;
        this.y = y;
        this.imageIndex = new Random().nextInt(4) + 1; // Rastgele bir resim seç
        try {
            image = ImageIO.read(new FileImageInputStream(new File("images/A" + imageIndex + ".png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getImageIndex() {
        return imageIndex;
    }
}

