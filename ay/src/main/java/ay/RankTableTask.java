package ay;

import java.sql.Connection;

public class RankTableTask implements Runnable {
    Execution execution;
    Connection getConnection;
    String catalog;
    String schema;
    String table;
    String type;

    public RankTableTask(Execution execution, Connection conn, String catalog, String schema, String table, String type) {
        this.execution = execution;
        this.getConnection = conn;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.type = type;
    }

    @Override
    public void run() {
        // Replace with actual table processing logic
        System.out.println("Processing table: " + table);
        long rowCount = countRows();
        var includeSchema = execution.isSchemaIncluded(schema);
        Float ranking = includeSchema ? Ranking.HIGHEST.getValue() : Ranking.IGNORED.getValue();
        this.execution.rankTable(
            new TableRecord(catalog, schema, table, type, rowCount, ranking)
        );
    }

    private long countRows() {
        long rowCount = 0L;
        String fqtn = (catalog != null && !catalog.isEmpty()) ? String.format("%s.%s.%s", catalog, schema, table) : String.format("%s.%s", schema, table);
        String sql = "SELECT COUNT(*) FROM " + fqtn;
        try (var stmt = getConnection.createStatement(); var rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                rowCount = rs.getLong(1);
            }
        } catch (Exception e) {
            System.err.println("Failed to count rows for table " + fqtn + ": " + e.getMessage());
        }
        return rowCount;
    }
}
