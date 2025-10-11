package ay;

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

    @Inject
    Execution execution;

    @Inject
    AyTask ayTask;

    @Inject
    DisplayTask displayTask;

    @Override
    public void run() {        
        Log.info("AY! Anonymaise is *destructive*, sure you have a backup? ");
        Log.info("Included schemas: " + execution.getConfig().includeSchemas());  
        ay();
        Log.info("AY! Anonymaise finished! " + execution);
    }

    private void ay() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Create and start the timer task
            Future<?> timerFuture = executor.submit(ayTask);

            // Create and start the display task
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
