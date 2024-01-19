import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MapPanel extends JPanel {
    private Image mapImage;
    private Character character = new Character();
    private boolean antMapVisible = true;

    private AntPieces antPieces;

    private TurnLabel turnLabel;

    private DicePanel dicePanel;

    private RollButton rollButton;

    Function repaintFunction = () -> repaint();

    Function revalidateFunction = () -> revalidate();

    private Spider[] spider = new Spider[2];
    private LinkedList<int[]> possiblespiderPosition = new LinkedList<>();
    private int spiderMove = 5;
    private int[] initialSpiderPosition = { 114, 113 };
    private ArrayList<Integer> listOfRearPosition = new ArrayList<>(
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 12, 18, 24, 11, 17, 23, 29, 30, 31, 32, 33, 34, 35));
    // 105 pixel each location

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

    public double pythagore(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public int findNearestPosition(int x, int y) {
        double minDistance = 9999;
        int i = 0;
        int res = 0;
        for (int[] position : possiblespiderPosition) {
            double distance = pythagore(x, y, position[0], position[1]);
            if (distance < minDistance) {
                minDistance = distance;
                res = i;
            }
            i++;
        }
        return res;
    }

    public MapPanel(String mapImagePath, String[] antImagesPath, String dicesPath, String[] spiderPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            antPieces = new AntPieces(antImagesPath);
            dicePanel = new DicePanel(dicesPath);
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    int x = initialSpiderPosition[0] + 104 * i;
                    int y = initialSpiderPosition[1] + 104 * j;
                    possiblespiderPosition.add(new int[] { x, y });
                }
            }
            spider[0] = new Spider(possiblespiderPosition.get(0)[0],
                    possiblespiderPosition.get(0)[1],
                    spiderPath[0]);
            spider[1] = new Spider(possiblespiderPosition.get(5)[0],
                    possiblespiderPosition.get(5)[1],
                    spiderPath[1]);

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
                System.out.println("x: " + e.getX() + "    y: " + e.getY());
                // Check if the mouse click is within the bounds of the piece
                if (antMapVisible) {
                    antPieces.detectAndMoveAnt(e, character, turnLabel, repaintFunction);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!antMapVisible) {
                    for (Spider sp : spider) {
                        int spiderX = sp.getX() - 50 / 2;
                        int spiderY = sp.getY() - 50 / 2;
                        // Check if the mouse click is within the bounds of the spider
                        if (e.getX() >= spiderX && e.getX() <= spiderX + 50 &&
                                e.getY() >= spiderY && e.getY() <= spiderY + 50) {
                            sp.dragging(true);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (Spider sp : spider) {
                    if (sp.isDragged()) {
                        sp.dragging(false);
                        ;
                        int index = findNearestPosition(e.getX(), e.getY());
                        System.out.println(index);
                        // Update spider's position when released
                        double moveDistance = pythagore(sp.getCurrentX(), sp.getCurrentY(),
                                possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                        System.out.println(moveDistance);
                        boolean isRearAndMoveRear = false;
                        if (listOfRearPosition.contains(sp.index) && listOfRearPosition.contains(index)) {
                            isRearAndMoveRear = true;
                        }
                        if (((moveDistance > 140 && moveDistance < 150)
                        || (100 < moveDistance && moveDistance < 110 && isRearAndMoveRear)) && spiderMove > 0) {
                            sp.moveTo(possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                            sp.setCurrentPosition(sp.getX(), sp.getY());
                            sp.index = index;
                            spiderMove--;
                        } else {
                            sp.failedmove();
                        }
                        repaint();
                    }
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                for (Spider sp : spider) {
                    if (sp.isDragged()) {
                        sp.moveTo(e.getX(), e.getY());
                        repaint();
                    }
                }
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
            for (Spider sp : spider) {
                int spiderX = sp.getX() - 50 / 2;
                int spiderY = sp.getY() - 50 / 2;
                g.drawImage(sp.getSpiderImage(), spiderX, spiderY, this);
                String movesText = "Moves: " + spiderMove;
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString(movesText, 250, 50);
            }
        }

        if (dicePanel.getCurrentDice() != null && antMapVisible) {
            g.drawImage(dicePanel.getCurrentDice(), 50, 50, this);
        }
    }
}