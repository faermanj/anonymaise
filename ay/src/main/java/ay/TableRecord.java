package ay;

public record TableRecord(
    String catalog,
    String schema,
    String table,
    String type,
    Long rowCount,
    Float ranking
) {
    @Override
    public String toString() {
        return "%s.%s.%s (rows: %d, rank: %s)".formatted(
            catalog == null ? "" : catalog,
            schema == null ? "" : schema,
            table == null ? "" : table,
            rowCount == null ? 0 : rowCount,
            ranking == null ? "N/A" : String.format("%.2f", ranking)
        );
    }

    public static boolean isIgnored(Float rank) {
        return rank != null && rank.equals(ay.Ranking.IGNORED.getValue());
    }
}
