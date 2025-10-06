package anonymaise;

import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.PicocliRunner;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;

@QuarkusMain
@TopCommand
@CommandLine.Command(name = "ay", mixinStandardHelpOptions = true)
public class AyMain implements QuarkusApplication, Runnable {
    @Inject
    CommandLine.IFactory factory; 

    @Override
    public void run() {
        Log.info("Ay...");
    }

    @Override
    public int run(String... args) throws Exception {
        return new CommandLine(this, factory).execute(args);
    }
}
