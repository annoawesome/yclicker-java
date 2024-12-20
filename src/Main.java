import forms.Clicker;

import javax.swing.*;

public class Main {
    private static String LOOK_AND_FEEL = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(LOOK_AND_FEEL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Clicker clickerFrame = new Clicker();
            }
        });
    }
}