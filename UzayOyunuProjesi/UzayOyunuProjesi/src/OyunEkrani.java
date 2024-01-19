import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class OyunEkrani extends JFrame {
    private BaslangicPaneli baslangicPaneli;
    private Oyun oyunPaneli;
    private JPanel logoPanel;
    private GridBagConstraints gbc = new GridBagConstraints();

    public OyunEkrani(String title) {
        super(title);
        ImageIcon logoImage = new ImageIcon("images/Logo.png");
        JLabel logoLabel = new JLabel(logoImage);
        logoLabel.setAlignmentX(0.5F);

        baslangicPaneli = new BaslangicPaneli(this);
        oyunPaneli = new Oyun();

        logoPanel = new JPanel();
        logoPanel.setLayout(new BorderLayout());
        logoPanel.add(logoLabel, "Center");
        this.setLayout(new BorderLayout());
        this.setLayout(new CardLayout());
        this.add(this.logoPanel, "Center");
        this.add(this.baslangicPaneli, "baslangic");
        this.add(this.oyunPaneli, "oyun");
        this.setDefaultCloseOperation(3);
        this.setSize(800, 650);
        this.setVisible(true);

        Timer logoTimer = new Timer(2000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OyunEkrani.this.logoPanel.setVisible(false);
                OyunEkrani.this.baslangicPaneli.setVisible(true);
            }
        });
        logoTimer.setRepeats(false);
        logoTimer.start();
    }

    public static void main(String[] args) {
        new OyunEkrani("UZAY OYUNU");
    }

    public void oyunBaslat() {
        ((CardLayout) getContentPane().getLayout()).show(getContentPane(), "oyun");
        oyunPaneli.baslatOyun();
        oyunPaneli.requestFocusInWindow();
    }

    class OyunNasilOynanirFrame extends JFrame {
        public OyunNasilOynanirFrame(OyunEkrani oyunEkrani) {
            setTitle("Oyun Nasıl Oynanır");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(800, 650);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.BLACK);

            try{
                Image originalImage = ImageIO.read(new File("images/Nasıl.png"));
                Image scaledImage = originalImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                ImageIcon nasilOynanirImage = new ImageIcon(scaledImage);
                JLabel resimLabel = new JLabel(nasilOynanirImage);
                panel.setLayout(new BorderLayout());
                panel.add(resimLabel, BorderLayout.CENTER);

                add(panel);
                setLocationRelativeTo(null);
                setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    class BaslangicPaneli extends JPanel {
        private Image arkaPlanResmi;

        public BaslangicPaneli(OyunEkrani oyunEkrani) {
            arkaPlanResmi = Toolkit.getDefaultToolkit().createImage("images/background.png"); // Resmin dosya adını uygun şekilde güncelleyin
            setLayout(new BorderLayout());

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setOpaque(false); // Panelin opaklığını kaldırarak arka plan resmini göster

            JButton oyunBaslatButton = new JButton(); // Boş bir buton oluştur

            // "Oyun Başlat" butonuna arka plan resmini ekle
            try {
                Image backgroundImage = ImageIO.read(new File("images/BaşlangıçButonu.png")); // Arka plan resminizin dosya adını belirtin
                ImageIcon backgroundIcon = new ImageIcon(backgroundImage.getScaledInstance(183, 63, Image.SCALE_SMOOTH));
                oyunBaslatButton.setIcon(backgroundIcon);
                oyunBaslatButton.setOpaque(false);
                oyunBaslatButton.setContentAreaFilled(false);
                oyunBaslatButton.setBorderPainted(false);

                // Butonun tıklanma olayını ekleyin
                oyunBaslatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oyunEkrani.oyunBaslat();
                        setVisible(false);
                    }
                });

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new Insets(120, 0, 0, 0);
                buttonPanel.add(oyunBaslatButton, gbc);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // "Oyun Nasıl Oynanır" butonuna arka plan resmini ekle
            try {
                Image nasilOynanirImage = ImageIO.read(new File("images/Nasıl Oynanır Butonu.png")); // Butonun arka plan resminin dosya adını belirtin
                ImageIcon nasilOynanirIcon = new ImageIcon(nasilOynanirImage.getScaledInstance(183,63, Image.SCALE_SMOOTH));
                JButton oyunNasilOynanirButton = new JButton(nasilOynanirIcon);
                oyunNasilOynanirButton.setOpaque(false);
                oyunNasilOynanirButton.setContentAreaFilled(false);
                oyunNasilOynanirButton.setBorderPainted(false);


                // Butonun tıklanma olayını ekleyin
                oyunNasilOynanirButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        oyunEkrani.new OyunNasilOynanirFrame(oyunEkrani);
                    }
                });

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new Insets(280, 0, 0, 0);
                buttonPanel.add(oyunNasilOynanirButton, gbc);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            // "Çıkış" butonuna arka plan resmini ekle
            try {
                Image nasilOynanirImage = ImageIO.read(new File("images/Çıkış Butonu.png")); // Butonun arka plan resminin dosya adını belirtin
                ImageIcon cikisIcon = new ImageIcon(nasilOynanirImage.getScaledInstance(100,100, Image.SCALE_SMOOTH));
                JButton cikisButton = new JButton(cikisIcon);
                add(buttonPanel, BorderLayout.CENTER);
                cikisButton.setOpaque(false);
                cikisButton.setContentAreaFilled(false);
                cikisButton.setBorderPainted(false);


                // Butonun tıklanma olayını ekleyin
                cikisButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new Insets(420, 0, 0, 0);
                buttonPanel.add(cikisButton, gbc);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Arka plan resmini çiz
            g.drawImage(arkaPlanResmi, 0, 0, 800, 650, this);
        }
    }
}