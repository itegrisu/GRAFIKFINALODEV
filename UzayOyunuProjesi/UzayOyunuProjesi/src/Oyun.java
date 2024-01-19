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
    private ArrayList<Meyve> meyveList = new ArrayList<>();
    private Timer timer = new Timer(17, this);
    public boolean gameOver = false;
    private int olenMeyve = 0;
    private int score = 0;
    private BufferedImage uzayGemiImage, arkaPlanImage, cerceve1, cerceve2;
    SimpleDataStorage simpleDataStorage = new SimpleDataStorage();
    private String soru[]={"8+2","6X3","7-3","10/2"};
    private int cevap[]={10,18,4,5};
    private Random random = new Random();
    private int meyveEkleX = 8;
    private int meyveEkleY = 1;
    private int cocuk = 400;
    private int cocukEkleX = 20;
    private int cocukY = 480;
    private int sayac = 0;
    private int sure = 0;
    Boolean cevapDogruMu = false;
    private int enYuksekScore = 0;
    private int oyunPanelWidth = 800;
    private int oyunPanelHeight = 650;
    private int sureKontrol = 0;

    public Oyun() {
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        try {
            arkaPlanImage = ImageIO.read(new FileImageInputStream(new File("images/Arkaplan.png")));
            uzayGemiImage = ImageIO.read(new FileImageInputStream(new File("images/Cocuk.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CocuklariBaslat();
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
        g.drawImage(uzayGemiImage, cocuk, cocukY, uzayGemiImage.getWidth(), uzayGemiImage.getHeight(), this);

        for (Meyve meyve : meyveList) {
            g.drawImage(meyve.getImage(), meyve.getX(), meyve.getY(),
                    meyve.getImage().getWidth(), meyve.getImage().getHeight(), this);
        }



        g.drawImage(cerceve1, 0, 0, oyunPanelWidth, oyunPanelHeight, this);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Süre: " + sure, getWidth() - 180, 90);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Skor: " + score, getWidth() - 180, 110);

        if(score == cevap[0]){
            gameOver = true;

        } else{
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Soru: " + soru[0], 20, 20);

        }


        //oyun bitti
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Game Over", 100, 150);
            g.drawString("Skor :"+ score,100,250);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SureKontrol();
        if (!gameOver) {
            for (int i = 0; i < meyveList.size(); i++) {
                Meyve meyve = meyveList.get(i);
                meyve.setY(meyve.getY() + meyveEkleY);

                if (meyve.getY() > getHeight()) {
                    meyveList.remove(i);
                    meyveList.add(new Meyve(random.nextInt(oyunPanelWidth - 70), -50));
                }
                CarpismaKontrol();
            }
            OyunBittiMiKontrol();
            repaint();
        }
    }


    private void CocuklariBaslat() {
        for (int i = 0; i < 10; i++) {
            meyveList.add(new Meyve(random.nextInt(oyunPanelWidth - 70), -50));
        }
    }

    private void OyunBittiMiKontrol() {
        for (Meyve meyve : meyveList) {
            if (meyve.getY() >= getHeight() - 187) {
                gameOver = false;
                break;
            }
        }
        if (gameOver) {
            OyunBitti();
        }
    }

    private void YenidenBaşlat() {
        meyveList.clear();
        cocuk = 380;
        gameOver = false;
        olenMeyve = 0;
        timer.restart();
        CocuklariBaslat();
        sayac = 0;
        sure = 0;
        meyveEkleY = 1;
        cocukEkleX = 20;
        meyveEkleX = 8;
        enYuksekScore = Integer.parseInt(simpleDataStorage.loadData("score"));
        score = 0;
        repaint();
    }


    private void OyunBitti() {
        JFrame frame = new JFrame("Oyun Bitti");
        frame.setSize(800, 650);
        frame.setLocationRelativeTo(null);

        JLabel gameOverLabel = new JLabel("Skor " + score);
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
                meyveEkleY += 1;
                sayac = 0;
                cocukEkleX += 6;
                meyveEkleX += 4;
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
        Rectangle uzayGemisiRect = new Rectangle(cocuk, cocukY, uzayGemiImage.getWidth(), uzayGemiImage.getHeight());

        for (int i = 0; i < meyveList.size(); i++) {
            Meyve meyve = meyveList.get(i);
            Rectangle canavarRect = new Rectangle(meyve.getX(), meyve.getY(), meyve.getImage().getWidth(), meyve.getImage().getHeight());

            if (uzayGemisiRect.intersects(canavarRect)) {
                // Çarpışma tespit edildi, çocuğu kaldır ve puan ekle
                meyveList.remove(i);
                int puan = 0;

                switch (meyve.getImageIndex()) {
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
                olenMeyve++;
                MuzikEkle("Muzik/Carpisma.wav");
                i--; // Liste boyutu değiştiği için indis azaltılmalı
                meyveList.add(new Meyve(random.nextInt(oyunPanelWidth - 2 * meyve.getImage().getWidth()), -50));
                break;
            }
        }
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            if (cocuk <= 15) {
                cocuk = 15;
            } else {
                cocuk -= cocukEkleX;
            }
        } else if (c == KeyEvent.VK_RIGHT) {
            if (cocuk >= (oyunPanelWidth) - 70) {
                cocuk = oyunPanelWidth - 70;
            } else {
                cocuk += cocukEkleX;
            }
        } else if (c==KeyEvent.VK_UP){
            if (cocukEkleX <40){
                cocukEkleX +=5;
            }
        } else if (c==KeyEvent.VK_DOWN) {
            if (cocukEkleX >5){
                cocukEkleX -=5;
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

class Meyve {
    private int x;
    private int y;
    private BufferedImage image;
    private int imageIndex; // Hangi resmin yüklendiğini takip etmek için

    public Meyve(int x, int y) {
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

