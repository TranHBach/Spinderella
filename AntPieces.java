import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseEvent;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.Graphics;

class AntPieces extends JPanel {
    private ArrayList<Piece> pieces = new ArrayList<>();
    private ArrayList<Image> pieceImage = new ArrayList<>(); // Image for the piece
    private int pieceWidth = 30; // Desired width for the piece image
    private int pieceHeight = 30; // Desired height for the piece image;

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

    public AntPieces(String[] antImagesPath) {
        for (int i = 0; i < 17; i++) {
            antIndexOnTop.add(new Stack<>());
        }
        try {
            for (int i = 0; i < antImagesPath.length; i++) {
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
                    Piece newPiece = new Piece(possiblePositions.get(0)[0 + i * 2],
                            possiblePositions.get(0)[1 + i * 2]);
                    pieces.add(newPiece);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public ArrayList<Image> getPieceImage() {
        return pieceImage;
    }

    public void drawAntPieces(Graphics g) {
        if (pieceImage != null && pieces != null) {
            for (int i = 0; i < pieceImage.size(); i++) {
                int x = pieces.get(i).getX() - pieceWidth / 2; // Adjust position to center the piece image
                int y = pieces.get(i).getY() - pieceHeight / 2; // Adjust position to center the piece image
                g.drawImage(pieceImage.get(i), x, y, this); // Draw the resized piece image at the piece's position
            }
        }
    }

    public void detectAndMoveAnt(MouseEvent e, Character character, TurnLabel turnLabel, Function repaintFunction) {
        int currentTurn = character.getTurn();
        for (int i = currentTurn * 4; i < currentTurn * 4 + 4; i++) {
            int pieceX = pieces.get(i).getX() - pieceWidth / 2;
            int pieceY = pieces.get(i).getY() - pieceHeight / 2;
            if (e.getX() >= pieceX && e.getX() <= pieceX + pieceWidth &&
                    e.getY() >= pieceY && e.getY() <= pieceY + pieceHeight &&
                    currentPositionIndex[i] != 15 && character.remainingAntMove > 0) {

                // Move the piece to the next possible position
                int currentPosition = currentPositionIndex[i];
                int nextPosition = currentPosition + 1;
                if (currentPosition != 0) {
                    antIndexOnTop.get(currentPosition).pop();
                }
                currentPositionIndex[i] = nextPosition;
                antIndexOnTop.get(nextPosition).add(i);
                printAntIndexOnTop();

                // Reduce remaining move count
                character.reduceAntMoveCount();
                turnLabel.setMove(character.remainingAntMove);
                turnLabel.updateText();

                // Change player turn
                if (character.remainingAntMove == 0) {
                    character.changeTurn();
                    turnLabel.setTurn(String.valueOf(character.getTurn() + 1));
                    turnLabel.updateText();
                }

                // 15 is the number of moves needed to reach the end. Change to 1 if need to
                // test end game screen.
                if (currentPositionIndex[i] == 15) {
                    int playerID = Math.round(i / 4);
                    countWin[playerID]++;
                    // Check if one player got 3 ant to finish line => Print end game.
                    if (countWin[playerID] == 3) {
                        System.out.println("end game");
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
                pieces.get(i).setX(newPosition[0] + x);
                pieces.get(i).setY(newPosition[1] + y);

                repaintFunction.apply();
                break;
            }
        }
    }

}