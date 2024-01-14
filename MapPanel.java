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

    private AntPieces antPieces;

    private TurnLabel turnLabel;

    private DicePanel dicePanel;

    Function repaintFunction = () -> repaint();
    
    Function revalidateFunction = () -> revalidate();

    public MapPanel(String mapImagePath, String[] pieceImagesPath, String dicesPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            antPieces = new AntPieces(pieceImagesPath);
            dicePanel = new DicePanel(dicesPath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        turnLabel = new TurnLabel("Player 1 ", "0"); // Initial label text
        this.add(turnLabel);
        
        RollButton rollButton = new RollButton("Roll Dice", character, dicePanel, turnLabel, repaintFunction, revalidateFunction);
        this.add(rollButton);
        
        setLayout(null);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if the mouse click is within the bounds of the piece
                antPieces.detectAndMoveAnt(e, character, turnLabel, repaintFunction);
                
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        antPieces.drawAntPieces(g);

        if (dicePanel.getCurrentDice() != null) {
            g.drawImage(dicePanel.getCurrentDice(), 50, 50, this);
        }
    }
}