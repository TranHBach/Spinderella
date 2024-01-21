import javax.swing.*;
import java.awt.*;

class StatusLabel extends JPanel {
    private JLabel playerTurnLabel = new JLabel("Player turn: " + 0);;
    private JLabel objectMoveLabel = new JLabel("Object Move: " + 0);
    private JLabel antMoveLabel = new JLabel("Ant Move: " + 0);
    private JLabel spiderMoveLabel = new JLabel("Spider Move: " + 0);
    private Function repaintFunction;

    public void initializeAllLabel() {
        Font gameFont = new Font("Comic Sans MS", Font.PLAIN, 16); // You can adjust the size as needed
        playerTurnLabel.setFont(gameFont);
        antMoveLabel.setFont(gameFont);
        spiderMoveLabel.setFont(gameFont);
        objectMoveLabel.setFont(gameFont);

        Color textColor = new Color(255, 255, 255);
        playerTurnLabel.setForeground(textColor);
        antMoveLabel.setForeground(textColor);
        spiderMoveLabel.setForeground(textColor);
        objectMoveLabel.setForeground(textColor);
    }

    public void addLabelToPanel() {
        add(playerTurnLabel);
        add(antMoveLabel);
        add(objectMoveLabel);
        add(spiderMoveLabel);
    }

    public StatusLabel(Function repaintFunction) {
        super(new GridLayout(2, 2));
        this.repaintFunction = repaintFunction;
        initializeAllLabel();

        addLabelToPanel();

        setBounds(20, 10, 350, 60); // Adjust the position and size as needed
        // setOpaque(true); // Make the label opaque so that the background color is
        // visible

        setBackground(new Color(0, 0, 0, 0)); // Set the background color to white

    }

    public void setTurn(String nextTurn) {
        playerTurnLabel.setText("Player turn: " + nextTurn);
        playerTurnLabel.revalidate();
        repaintFunction.apply();
    }

    public void setAntMove(int numMove) {
        antMoveLabel.setText("Ant Move: " + String.valueOf(numMove));
        repaintFunction.apply();
    }

    public void setSpiderMove(int numMove) {
        spiderMoveLabel.setText("Spider Move: " + String.valueOf(numMove));
        repaintFunction.apply();
    }

    public void setObjectMove(int obj) {
        if (obj % 3 == 0) {
            objectMoveLabel.setText("Object Move: " + "Either");
        } else if (obj % 3 == 1) {
            objectMoveLabel.setText("Object Move: " + "Ant");
        } else if (obj % 3 == 2) {
            objectMoveLabel.setText("Object Move: " + "Spider");
        }
        repaintFunction.apply();
    }
}
