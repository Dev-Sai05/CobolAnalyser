package com.example.cobolflow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CobolSimpleScanner {

    private static final Pattern PARA =
            Pattern.compile("^\\s*([A-Z0-9-]+)\\.", Pattern.MULTILINE);

    private static final Pattern PERFORM =
            Pattern.compile("PERFORM\\s+([A-Z0-9-]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern IF =
            Pattern.compile("\\bIF\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern EXEC_SQL =
            Pattern.compile("EXEC\\s+SQL", Pattern.CASE_INSENSITIVE);

    public static void scan(String src, CobolGraph g) {

        String currentPara = "START";
        g.addNode(currentPara);

        Matcher p = PARA.matcher(src);
        while (p.find()) {
            String para = p.group(1);
            g.addEdge(currentPara, para, "FLOW");
            currentPara = para;
        }

        Matcher perf = PERFORM.matcher(src);
        while (perf.find()) {
            g.addEdge(currentPara, perf.group(1), "PERFORM");
        }

        if (IF.matcher(src).find()) {
            g.addDecision(currentPara, "IF");
        }

        if (EXEC_SQL.matcher(src).find()) {
            g.addSql(currentPara);
        }
    }
}
