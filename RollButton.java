import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.sound.sampled.*;

class RollButton extends JButton implements MouseListener {
    private Image backgroundImage;
    private Image hoverImage;
    private boolean isHovered = false;

    private static void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RollButton(Character character, DicePanel[] allDicePanels, StatusLabel statusLabel,
            Function repaintFunction, Function revalidateFunction) {
        try {
            backgroundImage = ImageIO.read(new File("Dices/rollDiceButton.png"));
            hoverImage = ImageIO.read(new File("Dices/hoverDiceButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        int width = (int) Math.round(273 / 2);
        int height = (int) Math.round(204 / 2);
        setBounds(799, 630, width, height);

        addMouseListener(this); // Add MouseListener to handle hover effect

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnabled(false);
                // roll for 3 seconds
                playSound("Sound/dice-142528.wav");
                long startTime = System.currentTimeMillis();
                Thread rollThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long endTime = System.currentTimeMillis();
                        try {
                            while ((endTime - startTime) / 100F < 3) {
                                // roll dice
                                character.rollAllDie();

                                DicePanel antDicePanel = allDicePanels[0];
                                DicePanel spiderDicePanel = allDicePanels[1];
                                DicePanel generalDicePanel = allDicePanels[2];

                                antDicePanel.updateDiceImage(character.remainingAntMove);
                                spiderDicePanel.updateDiceImage(character.remainingSpiderMove);
                                generalDicePanel.updateDiceImage(character.objectMove);

                                repaintFunction.apply();
                                revalidateFunction.apply();

                                // sleep thread
                                Thread.sleep(60);

                                endTime = System.currentTimeMillis();
                            }

                            character.preventMoveBoth();
                            statusLabel.setAntMove(character.remainingAntMove);
                            statusLabel.setTurn(String.valueOf(character.getTurn() + 1));
                            statusLabel.setSpiderMove(character.remainingSpiderMove);
                            statusLabel.setObjectMove(character.objectMove);

                            setEnabled(true);
                        } catch (InterruptedException ex) {
                            System.out.println("Threading Error: " + ex);
                        }
                    }
                });
                rollThread.start();
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

        // Draw oval border
        int arcWidth = 30; // Adjust the arc width for roundness
        int arcHeight = 30; // Adjust the arc height for roundness
        int borderSize = 2; // Adjust the border size

        // Calculate adjusted coordinates to account for the stroke size
        int x = borderSize / 2;
        int y = borderSize / 2;
        int width = getWidth() - borderSize;
        int height = getHeight() - borderSize;

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(borderSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawRoundRect(x, y, width, height, arcWidth, arcHeight);

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
