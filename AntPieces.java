import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.awt.event.MouseEvent;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Graphics;

class AntPieces extends JPanel {
    private ArrayList<Ant> antPieces = new ArrayList<>();
    private ArrayList<Image> pieceImage = new ArrayList<>(); // Image for the piece
    private int pieceWidth = 30; // Desired width for the piece image
    private int pieceHeight = 30; // Desired height for the piece image;
    private Function triggerEndGameFunction;

    private LinkedList<int[]> possiblePositions = new LinkedList<>();
    private int[][] Positions = {
            { 619, 37, 686, 41, 615, 107, 684, 108 }, // Example possible position 1 (x, y)
            { 515, 198, },
            { 406, 306, },
            { 299, 407, },
            { 408, 510, },
            { 505, 410, },
            { 615, 514, },
            { 514, 613, },
            { 307, 613, },
            { 193, 513, },
            { 195, 308 },
            { 303, 196 },
            { 194, 92 },
            { 86, 199 },
            { 91, 409 },
            { 60, 607 },
            // Add more possible positions as needed
    };
    private int[] currentPositionIndex = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int[] countWin = new int[] { 0, 0, 0, 0 };
    private ArrayList<Stack<Integer>> antIndexOnTop = new ArrayList<>();

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

    public boolean executeAnt(int antPosition) {
        int topOfPosition = antIndexOnTop.get(antPosition).lastElement();
        if (topOfPosition == -1) {
            return false;
        }
        antPieces.get(topOfPosition).killed();
        currentPositionIndex[topOfPosition] = 0;
        return true;

    }

    public void printAntIndexOnTop() {
        for (int i = 0; i < 17; i++) {
            if (antIndexOnTop.get(i).empty()) {
                antIndexOnTop.get(i).add(-1);
            } else {
                System.out.print(antIndexOnTop.get(i).lastElement() + " ");
            }
        }
        System.out.println();
    }

    public void loadPieces(String[] antImagesPath, Character character) {
        try {
            for (int i = 0; i < character.getPlayerCount(); i++) {
                for (int[] position : Positions) {
                    possiblePositions.add(position);
                }

                // Create 4 ant for each player
                for (int j = 0; j < 4; j++) {
                    Image newPieceImage = ImageIO.read(new File(antImagesPath[i])); // Load the piece image
                    newPieceImage = newPieceImage.getScaledInstance(pieceWidth, pieceHeight, Image.SCALE_SMOOTH); // Resize
                                                                                                                  // the
                                                                                                                  // piece
                                                                                                                  // image
                    pieceImage.add(newPieceImage);
                    Ant newPiece = new Ant(possiblePositions.get(0)[0 + i * 2],
                            possiblePositions.get(0)[1 + i * 2], i);
                    antPieces.add(newPiece);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AntPieces(Function triggerEndGameFunction) {
        this.triggerEndGameFunction = triggerEndGameFunction;
        for (int i = 0; i < 17; i++) {
            antIndexOnTop.add(new Stack<>());
        }
        for (int i = 0; i < 17; i++) {
            if (antIndexOnTop.get(i).empty()) {
                antIndexOnTop.get(i).add(-1);
            }
        }

    }

    public ArrayList<Ant> getPieces() {
        return antPieces;
    }

    public ArrayList<Image> getPieceImage() {
        return pieceImage;
    }

    public void drawAntPieces(Graphics g) {
        if (pieceImage != null && antPieces != null) {
            for (int i = 0; i < pieceImage.size(); i++) {
                int x = antPieces.get(i).getX() - pieceWidth / 2; // Adjust position to center the piece image
                int y = antPieces.get(i).getY() - pieceHeight / 2; // Adjust position to center the piece image
                g.drawImage(pieceImage.get(i), x, y, this); // Draw the resized piece image at the piece's position
            }
        }
    }

    public void detectAndMoveAnt(MouseEvent e, Character character, StatusLabel statusLabel, Function repaintFunction) {
        int currentTurn = character.getTurn();
        int numberOfPlayer = character.getPlayerCount();
        for (int i = currentTurn * 4; i < currentTurn * 4 + 4; i++) {
            int pieceX = antPieces.get(i).getX() - pieceWidth / 2;
            int pieceY = antPieces.get(i).getY() - pieceHeight / 2;
            if (e.getX() >= pieceX && e.getX() <= pieceX + pieceWidth &&
                    e.getY() >= pieceY && e.getY() <= pieceY + pieceHeight &&
                    currentPositionIndex[i] != 15 && character.remainingAntMove > 0) {
                // Move the piece to the next possible position
                playSound("Sound/cartoon-jump-6462.wav");
                int currentPosition = currentPositionIndex[i];
                int nextPosition = currentPosition + 1;
                if (currentPosition != 0) {
                    antIndexOnTop.get(currentPosition).pop();
                }
                currentPositionIndex[i] = nextPosition;
                antIndexOnTop.get(nextPosition).add(i);
                // printAntIndexOnTop();

                // Reduce remaining move count
                character.reduceAntMoveCount();
                statusLabel.setAntMove(character.remainingAntMove);
                if (character.objectMove == 3) {
                    character.objectMove = 1;
                    character.remainingSpiderMove = 0;
                    statusLabel.setObjectMove(character.objectMove);
                    statusLabel.setSpiderMove(character.remainingSpiderMove);
                }

                // Change player turn
                if (character.remainingAntMove == 0) {
                    character.changeTurn();
                    statusLabel.setTurn(String.valueOf(character.getTurn() + 1));
                }

                // 15 is the number of moves needed to reach the end. Change to 1 if need to
                // test end game screen.
                if (currentPositionIndex[i] == 15) {
                    int playerID = Math.round(i / numberOfPlayer);
                    countWin[playerID]++;
                    // Check if one player got 3 ant to finish line => Print end game.
                    if (countWin[playerID] == 3) {
                        triggerEndGameFunction.apply();
                    }
                }

                int[] newPosition = possiblePositions.get(currentPositionIndex[i]);

                int x, y;
                if (i < 4) {
                    x = 0;
                    y = 0;
                } else if (i < 8) {
                    x = 35;
                    y = 0;
                } else if (i < 12) {
                    x = 0;
                    y = 35;
                } else {
                    x = 35;
                    y = 35;
                }
                // antPieces.get(i).setX(newPosition[0] + x);
                // antPieces.get(i).setY(newPosition[1] + y);

                // repaintFunction.apply();
                antPieces.get(i).move(newPosition[0] + x, newPosition[1] + y, repaintFunction);
                break;
            }
        }
    }

}
