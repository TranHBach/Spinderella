import java.util.*;
import java.awt.Graphics;

import javax.swing.JPanel;

import java.awt.event.MouseEvent;

public class SpiderPieces extends JPanel {
    private static final int PARKERINITIALPOSITION = 30;
    private static final int PETERINITIALPOSITION = 31;
    private Spider[] spider = new Spider[2];
    private LinkedList<int[]> possiblespiderPosition = new LinkedList<>();
    private int[] initialSpiderPosition = { 114, 113 };
    private ArrayList<Integer> listOfRearPosition = new ArrayList<>(
            Arrays.asList(0, 1, 2, 3, 4, 5, 6, 12, 18, 24, 11, 17, 23, 29, 30, 31, 32, 33, 34, 35));
    Map<Integer, Integer> spiderPositionToAntPosition = new HashMap<>();

    private Function repaintFunction;
    private Character character;
    private StatusLabel statusLabel;

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
            int spiderX = sp.getX() - 50 / 2;
            int spiderY = sp.getY() - 50 / 2;
            // Check if the mouse click is within the bounds of the spider
            if (e.getX() >= spiderX && e.getX() <= spiderX + 50 &&
                    e.getY() >= spiderY && e.getY() <= spiderY + 50) {
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

    public void releaseSpider(MouseEvent e, AntPieces antPieces) {
        for (Spider sp : spider) {
            if (sp.isDragged()) {
                sp.dragging(false);
                int index = findNearestPosition(e.getX(), e.getY());
                // Update spider's position when released
                double moveDistance = pythagore(sp.getCurrentX(), sp.getCurrentY(),
                        possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                boolean isRearAndMoveRear = false;
                if (listOfRearPosition.contains(sp.index) && listOfRearPosition.contains(index)) {
                    isRearAndMoveRear = true;
                }
                if (((moveDistance > 140 && moveDistance < 150)
                        || (100 < moveDistance && moveDistance < 110 && isRearAndMoveRear))
                        && character.remainingSpiderMove > 0) {
                    sp.moveTo(possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
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
                        int antPosition = spiderPositionToAntPosition.get(sp.index);
                        antPieces.executeAnt(antPosition);
                    }
                } else {
                    sp.failedmove();
                }
            }
        }
    }

    public void drawSpiderPieces(Graphics g) {
        for (Spider sp : spider) {
            int spiderX = sp.getX() - 50 / 2;
            int spiderY = sp.getY() - 50 / 2;
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
        spider[0] = new Spider(possiblespiderPosition.get(PARKERINITIALPOSITION)[0],
                possiblespiderPosition.get(PARKERINITIALPOSITION)[1],
                spiderPath[0]);
        spider[1] = new Spider(possiblespiderPosition.get(PETERINITIALPOSITION)[0],
                possiblespiderPosition.get(PETERINITIALPOSITION)[1],
                spiderPath[1]);

    }
}
