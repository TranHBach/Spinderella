import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class RollButton extends JButton {
    public RollButton(String buttonLabel, Character character, DicePanel dicePanel, TurnLabel turnLabel,
            Function repaintFunction, Function revalidateFunction) {
        super(buttonLabel);
        setBounds(50, 150, 100, 50);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setEnabled(false);
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

                                dicePanel.updateDiceImage(character.remainingAntMove);

                                repaintFunction.apply();
                                revalidateFunction.apply();

                                // sleep thread
                                Thread.sleep(60);

                                endTime = System.currentTimeMillis();
                            }
                            
                            turnLabel.setMove(character.remainingAntMove);
                            turnLabel.setTurn(String.valueOf(character.getTurn() + 1));
                            turnLabel.updateText();

                            setEnabled(true);
                        } catch (InterruptedException ex) {
                            System.out.println("Threading Error: " + ex);
                        }
                    }
                });
                rollThread.start();
            }
        });
    }

}
