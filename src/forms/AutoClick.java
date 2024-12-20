package forms;

import java.io.IOException;
import java.util.Random;

public class AutoClick {
    private static boolean enabled = false;
    private static int maxDelay = 100;
    private static int minDelay = 100;

    private static Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            Random random = new Random();
            while (enabled) {
                try {
                    Thread.sleep(random.nextInt(minDelay, maxDelay + 1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                try {
                    leftClickPb.start();
                } catch (IOException e) {}
            }
        }
    };

    private static final ProcessBuilder yDoToolDaemon = new ProcessBuilder("ydotoold");
    private static final ProcessBuilder leftClickPb = new ProcessBuilder("ydotool", "click", "0x00", "0x40", "0x80");
    private static Process yDoToolDaemonProcess;

    public static void enableAutoClicker() {
        enabled = true;

        try {
            yDoToolDaemonProcess = yDoToolDaemon.start();
            new Thread(clickRunnable).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disableAutoClicker() {
        enabled = false;
        yDoToolDaemonProcess.destroy();
    }

    public static void setClickDelay(int max, int min) {
        maxDelay = max;
        minDelay = min;
    }

    public static void setClickDelay(double max, double min) {
        setClickDelay((int) (max * 1000), (int) (min * 1000));
    }
}
