import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

class PlayButton extends JButton implements MouseListener {
    private Image backgroundImage;
    private Image hoverImage;
    private boolean isHovered = false;

    private static Clip playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return clip;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public PlayButton(Function triggerGameScreen) {
        Clip menuSound = playSound("Sound/Menu.wav");
        try {
            backgroundImage = ImageIO.read(new File("Menu/PixelPlayButtonHover.png"));
            hoverImage = ImageIO.read(new File("Menu/PixelPlayButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        int width = (int) Math.round(626 / 2);
        int height = (int) Math.round(309 / 2);
        setBounds(340, 450, width, height);

        addMouseListener(this); // Add MouseListener to handle hover effect

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuSound.close();
                triggerGameScreen.apply();
                playSound("Sound/Game.wav");
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30)); // Adjust the arc width and
                                                                                         // height as needed
        if (isHovered) {
            g2d.drawImage(hoverImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        g2d.setClip(null); // Reset clip to avoid affecting other drawings

        g2d.dispose();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isHovered = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isHovered = false;
        repaint();
    }

    // The below code does not do anything,
    // it's here for inheritance of MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
