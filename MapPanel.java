import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;

class MapPanel extends JPanel {
    private Image mapImage;
    private Image diceBackgroundImage;
    private Image spiderMan;
    private Image spiderMan2;

    private Image title;
    private Image[] number = new Image[3];
    private Image chooseNumPlayer;

    private Character character = new Character();
    public int mapVisibility = 1;
    public int numberOfPlayers = 2;
    public boolean Menu1 = true;
    public String antImagesPath[];
    public String spiderPath;

    private AntPieces antPieces;

    private SpiderPieces spiderPieces;

    private StatusLabel statusLabel;

    private DicePanel antDicePanel;
    private DicePanel spiderDicePanel;
    private DicePanel generalDicePanel;

    private RollButton rollButton;
    private PlayButton playButton;
    private QuitButton quitButton;

    private NextButton nextButton;
    private PlusButton plusButton;
    private MinusButton minusButton;

    Function repaintFunction = () -> repaint();

    Function revalidateFunction = () -> revalidate();

    Function triggerEndGameFunction = () -> triggerEndGame();

    Function triggerGameScreen = () -> triggerGameScreen();

    Function setupMenu2 = () -> setupMenu2();

    Function plus = () -> plus();
    Function minus = () -> minus();

    public void setMenu2Component(boolean status) {
        nextButton.setVisible(status);
        plusButton.setVisible(status);
        minusButton.setVisible(status);
    }

    public void setAllGameComponentStatus(boolean status) {
        statusLabel.setVisible(status);
        rollButton.setVisible(status);
        antDicePanel.setVisible(status);
        spiderDicePanel.setVisible(status);
        generalDicePanel.setVisible(status);
        quitButton.setVisible(!status);
        playButton.setVisible(!status);
        setMenu2Component(status);
    }

    public void setupMenu2() {
        quitButton.setVisible(false);
        playButton.setVisible(false);
        setMenu2Component(true);
        Menu1 = false;
    }

    public void triggerGameScreen() {
        setAllGameComponentStatus(true);
        setMenu2Component(false);
        character.setPlayerCount(numberOfPlayers);
        antPieces.loadPieces(antImagesPath, character);
        antPieces.drawAntPieces(getGraphics());
        swapMap("Map/map.jpg", 2);
    }

    public void triggerEndGame() {
        VictoriousLabel victoriousLabel = new VictoriousLabel("Player " + (character.getTurn() + 1));
        this.add(victoriousLabel);
        setAllGameComponentStatus(false);
        quitButton.setVisible(false);
        playButton.setVisible(false);
        swapMap("VictoryScreen/VictoryScreen.png", 4);
    }

    // 1 for Menu, 2 for ant map visible, 3 for spider map visible, 4 for victory
    // screen visible
    public void swapMap(String updatedImagePath, int setMapVisible) {
        try {
            this.mapImage = ImageIO.read(new File(updatedImagePath));
            this.mapVisibility = setMapVisible;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void plus() {
        if (numberOfPlayers < 4) {
            numberOfPlayers++;
            repaint();
        }
    }

    public void minus() {
        if (numberOfPlayers > 2) {
            numberOfPlayers--;
            repaint();
        }
    }

    public MapPanel(String[] mapImagePath, String[] antImagesPath, String[] dicesPath, String[] spiderPath) {
        statusLabel = new StatusLabel(repaintFunction); // Initial label text
        this.antImagesPath = antImagesPath;
        this.add(statusLabel);

        try {
            mapImage = ImageIO.read(new File(mapImagePath[0]));
            diceBackgroundImage = ImageIO.read(new File(mapImagePath[1]));
            spiderMan = ImageIO.read(new File("Menu/spiderMan.png"));
            spiderMan2 = ImageIO.read(new File("Menu/spiderManRight.png"));
            title = ImageIO.read(new File("Menu/AlternativeTitle.png"));
            antPieces = new AntPieces(triggerEndGameFunction);
            chooseNumPlayer = ImageIO.read(new File("Menu/textNumber.png"));
            antDicePanel = new DicePanel(dicesPath[0]);
            spiderDicePanel = new DicePanel(dicesPath[1]);
            generalDicePanel = new DicePanel(dicesPath[2]);
            spiderPieces = new SpiderPieces(spiderPath, repaintFunction, character, statusLabel);
            for (int i = 0; i < 3; i++) {
                number[i] = ImageIO.read(new File("Menu/" + (i + 2) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        rollButton = new RollButton(character,
                new DicePanel[] { antDicePanel, spiderDicePanel, generalDicePanel }, statusLabel, repaintFunction,
                revalidateFunction);
        this.add(rollButton);

        playButton = new PlayButton(setupMenu2);
        quitButton = new QuitButton();
        nextButton = new NextButton(triggerGameScreen);
        plusButton = new PlusButton(plus);
        minusButton = new MinusButton(minus);

        this.add(quitButton);
        this.add(playButton);
        this.add(nextButton);
        this.add(plusButton);
        this.add(minusButton);

        character.addRollButton(rollButton);
        setLayout(null);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("x: " + e.getX() + " y: " + e.getY());
                // Check if the mouse click is within the bounds of the piece
                if (mapVisibility == 2) {
                    antPieces.detectAndMoveAnt(e, character, statusLabel, repaintFunction);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (mapVisibility == 3) {
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

        if (mapVisibility == 1) {
            g.drawImage(mapImage, 0, 0, 995, 750, this);
            g.drawImage(title, 95, 150, (int)Math.round(620 * 1.3), (int)Math.round(155 * 1.3), this);
            g.drawImage(spiderMan, 0, 0, 1332 / 6, 1188 / 6, this);
            g.drawImage(spiderMan2, 850, 0, 100, 100, this);
            repaint();
            if (Menu1) {
                setAllGameComponentStatus(false);
            } else {
                g.drawImage(number[numberOfPlayers - 2], 438, 480, 100, 100, this);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                g.drawImage(chooseNumPlayer, 151, 400, 694, 58, this);
            }
        }

        if (mapImage != null && diceBackgroundImage != null && (mapVisibility == 2 || mapVisibility == 3)) {
            g.drawImage(mapImage, 0, 0, 750, 750, this);
            g.drawImage(diceBackgroundImage, 750, 0, 265, 750, this);
        }

        if (mapVisibility == 2) {
            antPieces.drawAntPieces(g);
        } else if (mapVisibility == 3) {
            spiderPieces.drawSpiderPieces(g);
            spiderPieces.drawStringBetweenSpiders(g);
        } else if (mapVisibility == 4) {
            g.drawImage(mapImage, 0, 0, 995, 750, this);
        }

        if (antDicePanel.getCurrentDice() != null && (mapVisibility == 2 || mapVisibility == 3)) {
            g.drawImage(generalDicePanel.getCurrentDice(), 775, 25, this);
            g.drawImage(antDicePanel.getCurrentDice(), 775, 25 + 180 + 25, this);
            g.drawImage(spiderDicePanel.getCurrentDice(), 775, 230 + 205, this);
        }
    }
}