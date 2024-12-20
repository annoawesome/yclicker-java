package forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Optional;

public class Clicker extends JFrame {
    private JPanel contentPane;
    private JLabel clickIntervalLabel;
    private JTextField secondsMaxField;
    private JTextField secondsMinField;
    private JButton startButton;
    private JButton stopButton;

    boolean enabled = false;

    public Clicker() {
        setTitle("yClicker Java");
        setCloseOperation();
        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startButtonClicked();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                stopButtonClicked();
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getID() == KeyEvent.KEY_PRESSED && keyEvent.getKeyCode() == KeyEvent.VK_F8) {
                    toggleClicker();
                }

                return false;
            }
        });
    }

    private void startButtonClicked() {
        Optional<Double> maxDelay = getDoubleFromString(secondsMaxField.getText());
        Optional<Double> minDelay = getDoubleFromString(secondsMinField.getText());

        if (maxDelay.isEmpty() && minDelay.isEmpty())
            return;

        enabled = true;

        if (maxDelay.isEmpty())
            maxDelay = minDelay;
        else if (minDelay.isEmpty())
            minDelay = maxDelay;

        if (minDelay.get() > maxDelay.get()) {
            Optional<Double> tmp = minDelay;
            minDelay = maxDelay;
            maxDelay = tmp;
        }

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        AutoClick.setClickDelay(maxDelay.get(), minDelay.get());
        AutoClick.enableAutoClicker();
    }

    private void stopButtonClicked() {
        enabled = false;

        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        AutoClick.disableAutoClicker();
    }

    private void toggleClicker() {
        enabled = !enabled;

        if (enabled) startButtonClicked();
        else stopButtonClicked();
    }

    private Optional<Double> getDoubleFromString(String str) {
        try {
            return Optional.of(Double.parseDouble(str));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void setCloseOperation() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
