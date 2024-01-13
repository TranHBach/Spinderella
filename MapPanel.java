import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MapPanel extends JPanel {
    private Image mapImage;
    private ArrayList<Image> pieceImage = new ArrayList<>(); // Image for the piece
    private ArrayList<Piece> pieces = new ArrayList<>();
    private LinkedList<int[]> possiblePositions = new LinkedList<>();
    private Character character = new Character();
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
    private int pieceWidth = 30; // Desired width for the piece image
    private int pieceHeight = 30; // Desired height for the piece image
    private int[] currentPositionIndex = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int[] countWin = new int[] { 0, 0, 0, 0 };

    private Image[] diceImages = new Image[6];
    private Image current_dice;
    private int dice = -1;

    private TurnLabel turnLabel;

    public MapPanel(String mapImagePath, String[] pieceImagesPath, String dicesPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            for (int[] position : Positions) {
                possiblePositions.add(position);
            }
            for (int i = 0; i < pieceImagesPath.length; i++) {

                // Create 4 ant for each player
                for (int j = 0; j < 4; j++) {
                    Image newPieceImage = ImageIO.read(new File(pieceImagesPath[i])); // Load the piece image
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

            for (int i = 1; i < 7; i++) {
                diceImages[i - 1] = ImageIO.read(new File(dicesPath + i + ".png"));
                diceImages[i - 1] = diceImages[i - 1].getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            }
            current_dice = diceImages[0];

        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton rollButton = new JButton("Roll Die");
        rollButton.setBounds(50, 150, 100, 50);
        rollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollButton.setEnabled(false);
                // roll for 3 seconds
                long startTime = System.currentTimeMillis();
                Thread rollThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long endTime = System.currentTimeMillis();
                        try {
                            while ((endTime - startTime) / 1000F < 3) {
                                // roll dice
                                character.rollAntDie();
                                
                                dice = character.remainingAntMove;
                                // Assuming you have ImgService class to handle image updates
                                // Update dice images

                                current_dice = diceImages[dice - 1];
                                
                                
                                repaint();
                                revalidate();

                                // sleep thread
                                Thread.sleep(60);

                                endTime = System.currentTimeMillis();
                            }
                            turnLabel.setMove(character.remainingAntMove);
                            turnLabel.setTurn(String.valueOf(character.getTurn() + 1));
                            turnLabel.updateText();

                            rollButton.setEnabled(true);
                        } catch (InterruptedException ex) {
                            System.out.println("Threading Error: " + ex);
                        }
                    }
                });
                rollThread.start();
            }
        });
        this.add(rollButton);
        setLayout(null);

        turnLabel = new TurnLabel("Player 1 ", "0"); // Initial label text
        add(turnLabel);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if the mouse click is within the bounds of the piece
                int currentTurn = character.getTurn();
                for (int i = currentTurn * 4; i < currentTurn * 4 + 4; i++) {
                    int pieceX = pieces.get(i).getX() - pieceWidth / 2;
                    int pieceY = pieces.get(i).getY() - pieceHeight / 2;
                    if (e.getX() >= pieceX && e.getX() <= pieceX + pieceWidth &&
                            e.getY() >= pieceY && e.getY() <= pieceY + pieceHeight &&
                            currentPositionIndex[i] != 15 && character.remainingAntMove > 0) {

                        System.out.println("move pieces");
                        // Move the piece to the next possible position
                        currentPositionIndex[i] = currentPositionIndex[i] + 1;

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


                        // 15 is the number of moves needed to reach the end. Change to 1 if need to test end game screen.
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

                        repaint(); // Redraw the panel to show the updated position
                        break;
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

        if (pieceImage != null && pieces != null) {
            for (int i = 0; i < pieceImage.size(); i++) {
                int x = pieces.get(i).getX() - pieceWidth / 2; // Adjust position to center the piece image
                int y = pieces.get(i).getY() - pieceHeight / 2; // Adjust position to center the piece image
                g.drawImage(pieceImage.get(i), x, y, this); // Draw the resized piece image at the piece's position
            }
        }

        if (current_dice != null) {
            g.drawImage(current_dice, 50, 50, this);
        }
    }
}