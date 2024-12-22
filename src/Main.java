import forms.Clicker;

import javax.swing.*;

public class Main {
    private static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(LOOK_AND_FEEL);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(Clicker::new);
    }
}