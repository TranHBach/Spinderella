import javax.swing.*;
import java.awt.*;

public class VictoriousLabel extends JLabel {
    public VictoriousLabel(String playerName) {
        super(playerName + " is victorious!");
        Font gameFont = CustomFontLoader.loadFont("Font/SuperLegendBoy-4w8Y.ttf", 48);
        Color textColor = new Color(5,228,107);
        this.setFont(gameFont);
        this.setForeground(textColor);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);
        setBounds((995 - 800) / 2, 575, 800, 100);
        setBackground(new Color(0, 0, 0, 0)); // Set the background color to white
    }
}
