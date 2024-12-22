package forms;

import java.io.IOException;
import java.util.HexFormat;
import java.util.Random;

public class AutoClick {
    private static final int LEFT_MB = 0x00;
    private static final int RIGHT_MB = 0x01;
    private static final int MIDDLE_MB = 0x02;
    private static final int MOUSE_DOWN_MASK = 0x40;
    private static final int MOUSE_UP_MASK = 0x80;
    private static final int DEFAULT_HOLD_TIME = 25;
    public static final int MIN_HOLD_TIME = 25;

    private static boolean enabled = false;
    private static boolean suppressed = true;
    private static boolean doubleClick = false;
    private static boolean enableDaemon = false;
    private static int maxDelay = 100;
    private static int minDelay = 100;
    private static int mouseButtonHexCode = LEFT_MB;
    private static int holdTime = DEFAULT_HOLD_TIME;
    private static int repeatTimes = 0;

    private static Runnable finalRunnable;

    private static final Runnable clickRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (!suppressed)
                    leftClickPb.start();
            } catch (IOException ignored) {}
        }
    };

    private static final Runnable loopedClickRunnable = new Runnable() {
        @Override
        public void run() {
            Random random = new Random();
            int i = 0;

            while (enabled) {
                try {
                    Thread.sleep(random.nextInt(minDelay, maxDelay + 1));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                clickRunnable.run();

                // for loop
                if (repeatTimes > 0 && !suppressed) {
                    i++;
                    if (i > repeatTimes) {
                        break;
                    }
                }
            }

            if (repeatTimes > 0 && finalRunnable != null)
                finalRunnable.run();
        }
    };

    private static final ProcessBuilder yDoToolDaemon = new ProcessBuilder("ydotoold");
    private static final ProcessBuilder leftClickPb = new ProcessBuilder();
    private static Process yDoToolDaemonProcess;

    public static void enableAutoClicker() {
        enabled = true;

        if (doubleClick) {
            leftClickPb.command(
                    "ydotool",
                    "click",
                    "--next-delay=" + holdTime,
                    toSmallHex(mouseButtonHexCode),
                    toSmallHex(mouseButtonHexCode | MOUSE_DOWN_MASK),
                    toSmallHex(mouseButtonHexCode | MOUSE_UP_MASK),
                    toSmallHex(mouseButtonHexCode | MOUSE_DOWN_MASK),
                    toSmallHex(mouseButtonHexCode | MOUSE_UP_MASK)
            );
        } else {
            leftClickPb.command(
                    "ydotool",
                    "click",
                    "--next-delay=" + holdTime,
                    toSmallHex(mouseButtonHexCode),
                    toSmallHex(mouseButtonHexCode | MOUSE_DOWN_MASK),
                    toSmallHex(mouseButtonHexCode | MOUSE_UP_MASK)
            );
        }

        try {
            if (enableDaemon)
                yDoToolDaemonProcess = yDoToolDaemon.start();
            new Thread(loopedClickRunnable).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toSmallHex(int hex) {
        return HexFormat.of().toHexDigits(hex).substring(6);
    }

    public static void killDaemon() {
        if (yDoToolDaemonProcess != null && yDoToolDaemonProcess.isAlive())
            yDoToolDaemonProcess.destroy();
    }

    public static void disableAutoClicker() {
        enabled = false;
        killDaemon();
    }

    public static void setClickDelay(int max, int min) {
        maxDelay = max;
        minDelay = min;
    }

    public static void setClickDelay(double max, double min) {
        setClickDelay((int) (max * 1000), (int) (min * 1000));
    }

    public static void setMouseButton(String mouseButton) {
        if ("Left".equals(mouseButton)) mouseButtonHexCode = LEFT_MB;
        if ("Middle".equals(mouseButton)) mouseButtonHexCode = MIDDLE_MB;
        if ("Right".equals(mouseButton)) mouseButtonHexCode = RIGHT_MB;
    }

    public static void setDoubleClick(boolean newDoubleClick) {
        doubleClick = newDoubleClick;
    }

    public static void setHoldTime(int hT) {
        holdTime = hT;
    }

    public static void setDefaultHoldTime() {
        setHoldTime(DEFAULT_HOLD_TIME);
    }

    public static void setSuppressed(boolean suppressed) {
        AutoClick.suppressed = suppressed;
    }

    public static void setRepeatTimes(int repeatTimes) {
        AutoClick.repeatTimes = repeatTimes;
    }

    public static void setEnableDaemon(boolean enableDaemon) {
        AutoClick.enableDaemon = enableDaemon;
    }

    public static void uponFinish(Runnable runnable) {
        finalRunnable = runnable;
    }
}
