package anonymaise;

import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@QuarkusMain
@TopCommand
@CommandLine.Command(name = "ay", mixinStandardHelpOptions = true)
public class AyMain implements QuarkusApplication, Runnable {
    @Inject
    CommandLine.IFactory factory; 

    @Override
    public void run() {        
        // Create shared execution data object
        var execution = new Execution();

        Log.info("AY! Anonymaise is *destructive*, sure you have a backup? ");
        // Wait 15 seconds to allow user to quit, in another method

        
        ay(execution);
    }

    private void ay(Execution execution) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Create and start the timer task
            TimerTask timerTask = new TimerTask(execution, 60, Duration.ofSeconds(1));
            Future<?> timerFuture = executor.submit(timerTask);

            // Create and start the display task
            DisplayTask displayTask = new DisplayTask(execution, 60, Duration.ofSeconds(5));
            Future<?> displayFuture = executor.submit(displayTask);

            // Wait for both tasks to complete
            timerFuture.get();
            displayFuture.get();
            
            Log.info("All tasks completed successfully!");
            Log.info("Final execution state: " + execution);
            
        } catch (InterruptedException e) {
            Log.error("Tasks were interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            Log.error("Error during execution", e);
        }
    }


    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }
}
