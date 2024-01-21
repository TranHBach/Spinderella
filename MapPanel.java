import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MapPanel extends JPanel {
    private Image mapImage;
    private Image diceBackgroundImage;
    private Character character = new Character();
    private boolean antMapVisible = true;

    private AntPieces antPieces;

    private SpiderPieces spiderPieces;

    private StatusLabel statusLabel;

    private DicePanel antDicePanel;
    private DicePanel spiderDicePanel;
    private DicePanel generalDicePanel;

    private RollButton rollButton;

    Function repaintFunction = () -> repaint();

    Function revalidateFunction = () -> revalidate();

    public void swapMap(String updatedImagePath, boolean antMapVisible) {
        try {
            this.mapImage = ImageIO.read(new File(updatedImagePath));
            this.antMapVisible = antMapVisible;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MapPanel(String[] mapImagePath, String[] antImagesPath, String[] dicesPath, String[] spiderPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath[0]));
            diceBackgroundImage = ImageIO.read(new File(mapImagePath[1]));
            antPieces = new AntPieces(antImagesPath);
            antDicePanel = new DicePanel(dicesPath[0]);
            spiderDicePanel = new DicePanel(dicesPath[1]);
            generalDicePanel = new DicePanel(dicesPath[2]);
            spiderPieces = new SpiderPieces(spiderPath, repaintFunction, character, statusLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        statusLabel = new StatusLabel(); // Initial label text
        this.add(statusLabel);

        rollButton = new RollButton(character,
                new DicePanel[] { antDicePanel, spiderDicePanel, generalDicePanel }, statusLabel, repaintFunction,
                revalidateFunction);
        this.add(rollButton);

        setLayout(null);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("x: " + e.getX() + " y: " + e.getY());
                // Check if the mouse click is within the bounds of the piece
                if (antMapVisible) {
                    antPieces.detectAndMoveAnt(e, character, statusLabel, repaintFunction);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!antMapVisible) {
                    spiderPieces.dragSpider(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                spiderPieces.releaseSpider(e, antPieces);
                repaint();
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                spiderPieces.moveSpider(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage != null && diceBackgroundImage != null) {
            g.drawImage(mapImage, 0, 0, 750, 750, this);
            g.drawImage(diceBackgroundImage, 750, 0, 265, 750, this);
        }

        if (antMapVisible) {
            antPieces.drawAntPieces(g);
        } else {
            spiderPieces.drawSpiderPieces(g);
        }

        if (antDicePanel.getCurrentDice() != null) {
            g.drawImage(generalDicePanel.getCurrentDice(), 775, 25, this);
            g.drawImage(antDicePanel.getCurrentDice(), 775, 25 + 180 + 25, this);
            g.drawImage(spiderDicePanel.getCurrentDice(), 775, 230 + 205, this);
        }
    }
}