package com.example.cobolflow;

import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CobolGraphBuilder {

    private static final Pattern PARA =
        Pattern.compile("^\\s*\\d+\\s+([A-Z0-9-]+)\\.", Pattern.CASE_INSENSITIVE);

    private static final Pattern PERFORM =
        Pattern.compile("\\bPERFORM\\s+([A-Z0-9-]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern GOTO =
        Pattern.compile("\\bGO\\s+TO\\s+([A-Z0-9-]+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern IF =
        Pattern.compile("\\bIF\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern ELSE =
        Pattern.compile("\\bELSE\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern END_IF =
        Pattern.compile("\\bEND-IF\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern EVALUATE =
        Pattern.compile("\\bEVALUATE\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern WHEN =
        Pattern.compile("\\bWHEN\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern END_EVALUATE =
        Pattern.compile("\\bEND-EVALUATE\\b", Pattern.CASE_INSENSITIVE);

    private static final Pattern EXEC_SQL =
        Pattern.compile("\\bEXEC\\s+SQL\\b", Pattern.CASE_INSENSITIVE);

    public CobolGraph build(Path file) throws Exception {

        CobolGraph cg = new CobolGraph();
        String current = null;

        Deque<String> ifStack = new ArrayDeque<>();
        Deque<String> evalStack = new ArrayDeque<>();

        int ifCount = 1;
        int evalCount = 1;

        for (String line : Files.readAllLines(file)) {

            Matcher pm = PARA.matcher(line);
            if (pm.find()) {
                current = pm.group(1);
                cg.graph.addVertex(current);
                continue;
            }

            if (current == null) continue;

            Matcher perf = PERFORM.matcher(line);
            while (perf.find()) {
                cg.graph.addVertex(perf.group(1));
                var e = cg.graph.addEdge(current, perf.group(1));
                cg.edgeLabels.put(e, "PERFORM");
            }

            Matcher go = GOTO.matcher(line);
            while (go.find()) {
                cg.graph.addVertex(go.group(1));
                var e = cg.graph.addEdge(current, go.group(1));
                cg.edgeLabels.put(e, "GOTO");
            }

            if (IF.matcher(line).find()) {
                String ifNode = "IF_" + ifCount++;
                cg.graph.addVertex(ifNode);
                cg.edgeLabels.put(cg.graph.addEdge(current, ifNode), "IF");
                ifStack.push(ifNode);
            }

            if (ELSE.matcher(line).find() && !ifStack.isEmpty()) {
                cg.edgeLabels.put(
                    cg.graph.addEdge(ifStack.peek(), current),
                    "ELSE"
                );
            }

            if (END_IF.matcher(line).find() && !ifStack.isEmpty()) {
                ifStack.pop();
            }

            if (EVALUATE.matcher(line).find()) {
                String evalNode = "EVAL_" + evalCount++;
                cg.graph.addVertex(evalNode);
                cg.edgeLabels.put(
                    cg.graph.addEdge(current, evalNode),
                    "EVALUATE"
                );
                evalStack.push(evalNode);
            }

            if (WHEN.matcher(line).find() && !evalStack.isEmpty()) {
                cg.edgeLabels.put(
                    cg.graph.addEdge(evalStack.peek(), current),
                    "WHEN"
                );
            }

            if (END_EVALUATE.matcher(line).find() && !evalStack.isEmpty()) {
                evalStack.pop();
            }

            if (EXEC_SQL.matcher(line).find()) {
                String sqlNode = "SQL_" + current;
                cg.graph.addVertex(sqlNode);
                cg.edgeLabels.put(
                    cg.graph.addEdge(current, sqlNode),
                    "EXEC SQL"
                );
            }
        }
        return cg;
    }
}
