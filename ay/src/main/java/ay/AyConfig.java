package ay;

import java.util.List;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "ay")
public interface AyConfig {
    @WithDefault("PUBLIC")
    List<String> includeSchemas();

    @WithDefault("0.1")
    float samplePercentage();

    @WithDefault("0.90")
    float stepSize();

    @WithDefault("100")
    Long testDataVolume();
}
