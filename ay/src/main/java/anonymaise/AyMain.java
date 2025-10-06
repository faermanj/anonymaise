package anonymaise;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine.Command;

@TopCommand
@Command(name = "ay", mixinStandardHelpOptions = true, description = "Database Anonymaiser")
public class AyMain implements Runnable {

    @Override
    public void run() {
        // No-op: show help/usage or run a default action if needed
        System.out.println("AY AY AY! Anonyimaise WILL destroy your data, last chance to quit and BACKUP!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        System.out.println("ay ay ay.");
    }
}
