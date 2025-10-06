package anonymaise;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Execution {
    private final AtomicInteger counter;
    private final LocalDateTime startTime;
    
    public Execution() {
        this.counter = new AtomicInteger(0);
        this.startTime = LocalDateTime.now();
    }
    
    public int increment() {
        return counter.incrementAndGet();
    }
    
    public int getCurrentCount() {
        return counter.get();
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    @Override
    public String toString() {
        return "Execution{" +
                "count=" + getCurrentCount() +
                ", startTime=" + startTime +
                '}';
    }
}