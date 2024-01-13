import javax.swing.*;
import java.awt.Color;

class TurnLabel extends JLabel {
    public String playerTurn;
    public String remainingMove;
    public TurnLabel(String initialText, String initialRemainingMove) {
        super("<html><div style='padding: 6px;'>Player Turn: " + initialText + "<br>Remaining move: "
                + initialRemainingMove + "</div></html>");
        setBounds(280, 20, 150, 40); // Adjust the position and size as needed
        setOpaque(true); // Make the label opaque so that the background color is visible
        setBackground(Color.WHITE); // Set the background color to white
    }

    public void updateText() {
        setText("<html><div style='padding: 6px;'>Player Turn: " + playerTurn + "<br>Remaining move: " + remainingMove
                + "</div></html>");
    }

    public void setTurn(String nextTurn) {
        playerTurn = nextTurn;
    }

    public void setMove(int numMove) {
        remainingMove = String.valueOf(numMove);
    }

}
