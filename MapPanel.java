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
    private Character character = new Character();
    private boolean antMapVisible = true;

    private AntPieces antPieces;

    private SpiderPieces spiderPieces;

    private TurnLabel turnLabel;

    private DicePanel dicePanel;

    private RollButton rollButton;

    Function repaintFunction = () -> repaint();

    Function revalidateFunction = () -> revalidate();

    public void swapMap(String updatedImagePath, boolean antMapVisible) {
        try {
            this.mapImage = ImageIO.read(new File(updatedImagePath));
            this.antMapVisible = antMapVisible;
            turnLabel.setVisible(antMapVisible);
            rollButton.setVisible(antMapVisible);
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public MapPanel(String mapImagePath, String[] antImagesPath, String dicesPath, String[] spiderPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            antPieces = new AntPieces(antImagesPath);
            dicePanel = new DicePanel(dicesPath);
            spiderPieces = new SpiderPieces(spiderPath, repaintFunction);
        } catch (IOException e) {
            e.printStackTrace();
        }

        turnLabel = new TurnLabel("Player 1 ", "0"); // Initial label text
        this.add(turnLabel);

        rollButton = new RollButton("Roll Dice", character, dicePanel, turnLabel, repaintFunction, revalidateFunction);
        this.add(rollButton);

        setLayout(null);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("x: " + e.getX() + " y: " + e.getY());
                // Check if the mouse click is within the bounds of the piece
                if (antMapVisible) {
                    antPieces.detectAndMoveAnt(e, character, turnLabel, repaintFunction);
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
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        if (antMapVisible) {
            antPieces.drawAntPieces(g);
        } else {
            spiderPieces.drawSpiderPieces(g);
        }

        if (dicePanel.getCurrentDice() != null && antMapVisible) {
            g.drawImage(dicePanel.getCurrentDice(), 50, 50, this);
        }
    }
}