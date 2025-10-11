
package ay;

import ay.Ranking;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class DisplayTask implements Callable<Void> {
    @Inject
    Execution execution;

    private int intervalSeconds = 5; // Default refresh interval, can be set via setter or constructor

    /*
     * Implement the method call() so that it runs indefinitively.
     * while it runs, clear the console and render execution.
     * on the first line, print the name of the program (anymaise) and the elapsed time.
     * on the rest, render a list of all the tables processed so far, with their ranking.
     * Refresh the display every intervalSeconds.
     */
    @Override
    public Void call() throws Exception {
        // Print immediately on start
        clearScreen();
        printHeader();
        printTableColumns();
        while (true) {
            Thread.sleep(intervalSeconds * 1000L);
            clearScreen();
            printHeader();
            printTableColumns();
        }
    }

    /**
     * Prints many newlines to simulate clearing the screen.
     */
    private void clearScreen() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    /**
     * Prints the program name and elapsed time.
     */
    private void printHeader() {
        LocalDateTime start = execution.getStartTime();
        Duration elapsed = Duration.between(start, LocalDateTime.now());
        long seconds = elapsed.getSeconds();
        String elapsedStr = String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
        System.out.println("anymaise | elapsed: " + elapsedStr);
    }

    /**
     * Prints the processed tables and their rankings in two columns.
     */
    private void printTableColumns() {
        var tableMap = execution.getTableRankings();
        if (tableMap.isEmpty()) {
            System.out.println("No tables processed yet.");
            return;
        }
        java.util.List<TableRecord> records = new java.util.ArrayList<>();
        for (var entry : tableMap.values()) {
            if (TableRecord.isIgnored(entry.ranking())) continue;
            records.add(entry);
        }
        // Sort tables by ranking descending
        records.sort((a, b) -> Float.compare(b.ranking(), a.ranking()));
        int tableNameWidth = 32;
        int ratingWidth = 8;
        int colNameWidth = 24;
        int colRatingWidth = 8;
        String tableHeaderFmt = "% -" + tableNameWidth + "s %" + ratingWidth + ".2f";
        String colFmt = " %-" + colNameWidth + "s %" + colRatingWidth + ".2f";
        for (TableRecord record : records) {
            // Table name and rating
            System.out.printf(tableHeaderFmt, record.table(), record.ranking());
            // Columns on the same line, sorted by rating desc
            var columns = record.columnRecords();
            if (columns != null && !columns.isEmpty()) {
                var colList = new java.util.ArrayList<>(columns.entrySet());
                colList.removeIf(e -> TableRecord.isIgnored(e.getValue()));
                colList.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));
                for (var colEntry : colList) {
                    System.out.printf(colFmt, colEntry.getKey(), colEntry.getValue());
                }
            }
            System.out.println(); // Only one newline per table
        }
    }



    /**
     * Determines the column width for a single column layout.
     */
    private int getTerminalColWidthSingle() {
        int termWidth = 80;
        try {
            Process p = new ProcessBuilder("tput", "cols").redirectErrorStream(true).start();
            java.io.BufferedReader r = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
            String s = r.readLine();
            if (s != null) termWidth = Integer.parseInt(s.trim());
        } catch (Exception ignored) {}
        int colWidth = termWidth - 7; // 6 for rank, 1 for space
        if (colWidth < 20) colWidth = 20;
        return colWidth;
    }

    /**
     * Extracts the table name after the last dot.
     */
    private String extractShortName(String fullName) {
        return fullName.contains(".") ? fullName.substring(fullName.lastIndexOf('.') + 1) : fullName;
    }

    // Removed unused getTerminalColWidth()

}
