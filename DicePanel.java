import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

class DicePanel extends JPanel {
    private Image[] diceImages = new Image[6];
    private Image current_dice;
    private int dice = -1;

    public DicePanel(String dicesPath) {
        try {
            for (int i = 1; i < 7; i++) {
                diceImages[i - 1] = ImageIO.read(new File(dicesPath + i + ".png"));
                diceImages[i - 1] = diceImages[i - 1].getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            }
            current_dice = diceImages[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateDiceImage(int diceSide) {
        dice = diceSide;
        current_dice = diceImages[dice - 1];
    }

    public Image getCurrentDice() {
        return current_dice;
    }
}
