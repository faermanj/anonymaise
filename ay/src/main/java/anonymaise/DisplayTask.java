package anonymaise;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

public class DisplayTask implements Callable<Void> {
    private final Execution execution;
    private final int maxCount;
    private final Duration displayInterval;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public DisplayTask(Execution execution, int maxCount, Duration displayInterval) {
        this.execution = execution;
        this.maxCount = maxCount;
        this.displayInterval = displayInterval;
    }

    @Override
    public Void call() throws Exception {
        while (execution.getCurrentCount() < maxCount) {
            try {
                Thread.sleep(displayInterval.toMillis());
                clearConsole();
                displayExecutionInfo();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }

    private void displayExecutionInfo() {
        LocalDateTime now = LocalDateTime.now();
        Duration elapsed = Duration.between(execution.getStartTime(), now);
        
        System.out.println("=== Execution Status ===");
        System.out.println("Timer: " + execution.getCurrentCount() + " seconds");
        System.out.println("Started at: " + execution.getStartTime().format(timeFormatter));
        System.out.println("Current time: " + now.format(timeFormatter));
        System.out.println("Elapsed: " + elapsed.getSeconds() + " seconds");
        System.out.println("Execution: " + execution);
    }

    private void clearConsole() {
        // ANSI escape codes to clear console
        System.out.print("\033[2J\033[H");
        System.out.flush();
    }
}
