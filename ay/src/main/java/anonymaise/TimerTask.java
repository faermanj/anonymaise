package anonymaise;

import java.time.Duration;
import java.util.concurrent.Callable;

public class TimerTask implements Callable<Void> {
    private final Execution execution;
    private final int maxCount;
    private final Duration interval;

    public TimerTask(Execution execution, int maxCount, Duration interval) {
        this.execution = execution;
        this.maxCount = maxCount;
        this.interval = interval;
    }

    @Override
    public Void call() throws Exception {
        for (int i = 0; i < maxCount; i++) {
            execution.increment();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }
}