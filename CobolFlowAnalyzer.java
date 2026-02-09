package com.example.cobolflow;

public class CobolFlowAnalyzer {

    public static CobolGraph analyze(String source) {

        CobolGraph graph = new CobolGraph();

        // --- HIGH LEVEL STRUCTURE ---
        // 1. Detect paragraphs
        // 2. Detect PERFORM
        // 3. Detect IF / ELSE
        // 4. Detect EXEC SQL
        // 5. Build logical flow (not line-by-line)

        CobolSimpleScanner.scan(source, graph);

        return graph;
    }
}
