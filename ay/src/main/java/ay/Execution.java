package ay;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class Execution {
    @Inject
    AyConfig config;

    private final LocalDateTime startTime;
    // Map of table name to TableRecord, preserves insertion order
    private final Map<String, TableRecord> tableRankings = Collections.synchronizedMap(new LinkedHashMap<>());
    
    public Execution() {
        this.startTime = LocalDateTime.now();
    }

    public AyConfig getConfig() {
        return config;
    }
    
    
    public LocalDateTime getStartTime() {
        return startTime;
    }


    public void rankTable(TableRecord record) {
        Log.infof("Ranking table %s with ranking %s, rows %d", record.table(), record.ranking(), record.rowCount());
        tableRankings.put(record.table(), record);
    }

    public TableRecord getTableRecord(String tableName){
        return tableRankings.get(tableName);
    }
    
    @Override
    public String toString() {
        return "Execution{" +
                "startTime=" + startTime +
                '}';
    }

     public Map<String, TableRecord> getTableRankings() {
         return tableRankings;
     }

    public boolean isSchemaIncluded(String schema) {
        return getConfig().includeSchemas().contains(schema.toUpperCase());
    }

    @Inject
    PIIClassifier classifier;
    public float rank(Object value) {
        return classifier.rank(value);
    }
}
