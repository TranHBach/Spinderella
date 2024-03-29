import java.util.*;
import java.awt.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;
import java.io.File;

public class SpiderPieces extends JPanel {
    private static final int PARKERINITIALPOSITION = 30;
    private static final int PETERINITIALPOSITION = 31;
    private Spider[] spider = new Spider[2];
    private LinkedList<int[]> possiblespiderPosition = new LinkedList<>();
    private int[] initialSpiderPosition = { 114, 113 };
    Map<Integer, Integer> spiderPositionToAntPosition = new HashMap<>();

    private Function repaintFunction;
    private Character character;
    private StatusLabel statusLabel;

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

    // 105 pixel each location
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

    public void drawStringBetweenSpiders(Graphics g) {
        int spider1X = spider[0].getX();
        int spider1Y = spider[0].getY();
        int spider2X = spider[1].getX();
        int spider2Y = spider[1].getY();
        g.setColor(Color.WHITE); // Set the color for the string
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5)); // Adjust the thickness as needed
        g2d.drawLine(spider1X, spider1Y, spider2X, spider2Y); // Draw a line between the spiders

    }

    public double calculateDistanceBetweenSpiders() {
        double distance = 0;
        int spider1X = spider[0].getX();
        int spider1Y = spider[0].getY();
        int spider2X = spider[1].getX();
        int spider2Y = spider[1].getY();
        distance = pythagore(spider1X, spider1Y, spider2X, spider2Y);
        return distance;
    }

    public void dragSpider(MouseEvent e) {
        for (Spider sp : spider) {
            int spiderX = sp.getX() - 100 / 2;
            int spiderY = sp.getY() - 100 / 2;
            // Check if the mouse click is within the bounds of the spider
            if (e.getX() >= spiderX && e.getX() <= spiderX + 100 &&
                    e.getY() >= spiderY && e.getY() <= spiderY + 100) {
                sp.dragging(true);
            }
        }
    }

    public void moveSpider(MouseEvent e) {
        for (Spider sp : spider) {
            if (sp.isDragged()) {
                sp.moveTo(e.getX(), e.getY());
                repaintFunction.apply();
            }
        }
    }

    public boolean preventSameLocation(int index, int currentSpider) {
        int otherSpider = -1;
        if (currentSpider == 0) {
            otherSpider = 1;
        } else if (currentSpider == 1) {
            otherSpider = 0;
        }
        if (spider[otherSpider].index == index) {
            return false;
        } else {
            return true;
        }
    }

    public void releaseSpider(MouseEvent e, AntPieces antPieces) {
        for (int i = 0; i < 2; i++) {
            Spider sp = spider[i];
            if (sp.isDragged()) {
                sp.dragging(false);
                int index = findNearestPosition(e.getX(), e.getY());
                // Update spider's position when released
                double moveDistance = pythagore(sp.getCurrentX(), sp.getCurrentY(),
                        possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                if (((moveDistance > 140 && moveDistance < 150)
                        || (100 < moveDistance && moveDistance < 110))
                        && character.remainingSpiderMove > 0 && preventSameLocation(index, i)) {
                    sp.moveTo(possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                    playSound("Sound/interface-124464.wav");
                    sp.setCurrentPosition(sp.getX(), sp.getY());
                    sp.index = index;
                    character.remainingSpiderMove--;
                    statusLabel.setSpiderMove(character.remainingSpiderMove);
                    if (character.objectMove == 3) {
                        character.objectMove = 2;
                        character.remainingAntMove = 0;
                        statusLabel.setObjectMove(character.objectMove);
                        statusLabel.setAntMove(character.remainingAntMove);
                    }

                    if (character.remainingSpiderMove == 0) {
                        character.changeTurn();
                        statusLabel.setTurn(String.valueOf(character.getTurn() + 1));
                    }

                    double distanceBetweenSpiders = calculateDistanceBetweenSpiders();
                    if (spiderPositionToAntPosition.containsKey(spider[0].index)
                            && (100 < distanceBetweenSpiders && distanceBetweenSpiders < 150)) {
                        int antPosition = spiderPositionToAntPosition.get(spider[0].index);
                        boolean isAntKilled = antPieces.executeAnt(antPosition);
                        if (isAntKilled) {
                            Random rand = new Random();
                            for (int j = 0; j < 2; j++) {
                                Spider tempSp = spider[j];
                                int randomPosition = rand.nextInt(36);
                                if (preventSameLocation(randomPosition, j)) {
                                    randomPosition = rand.nextInt(36);
                                }
                                tempSp.moveTo(possiblespiderPosition.get(randomPosition)[0],
                                        possiblespiderPosition.get(randomPosition)[1]);
                                tempSp.setCurrentPosition(tempSp.getX(), tempSp.getY());
                                tempSp.index = randomPosition;
                            }
                        }
                    }
                } else {
                    sp.failedmove();
                }
                break;
            }
        }
    }

    public void drawSpiderPieces(Graphics g) {
        for (Spider sp : spider) {
            int spiderX = sp.getX() - 100 / 2;
            int spiderY = sp.getY() - 100 / 2;
            g.drawImage(sp.getSpiderImage(), spiderX, spiderY, this);
        }
    }

    public SpiderPieces(String[] spiderPath, Function repaintFunction, Character character, StatusLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.repaintFunction = repaintFunction;
        this.character = character;

        int[] antPosToSpider = { 25, 20, 15, 22, 27, 34, 29, 17, 10, 8, 13, 6, 1, 3 };
        for (int i = 1; i <= 14; i++) {
            spiderPositionToAntPosition.put(antPosToSpider[i - 1], i);
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int x = initialSpiderPosition[0] + 104 * i;
                int y = initialSpiderPosition[1] + 104 * j;
                possiblespiderPosition.add(new int[] { x, y });
            }
        }
        // Spider[0] is Parker and Spider[1] is Peter, Parker get ant
        spider[0] = new Spider(PARKERINITIALPOSITION, possiblespiderPosition.get(PARKERINITIALPOSITION)[0],
                possiblespiderPosition.get(PARKERINITIALPOSITION)[1],
                spiderPath[0]);
        spider[1] = new Spider(PETERINITIALPOSITION, possiblespiderPosition.get(PETERINITIALPOSITION)[0],
                possiblespiderPosition.get(PETERINITIALPOSITION)[1],
                spiderPath[1]);

    }
}
