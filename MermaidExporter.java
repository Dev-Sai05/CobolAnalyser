package com.example.cobolflow;

import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class MermaidExporter {

    public static String toMermaid(CobolGraph graph, ViewMode mode) {

        StringBuilder sb = new StringBuilder();
        sb.append("graph TD\n");

        // ---------------------------
        // 1. Add nodes
        // ---------------------------
        for (String node : graph.graph.vertexSet()) {

            NodeType type = graph.nodeTypes.getOrDefault(node, NodeType.PARAGRAPH);

            // 🔥 FILTERING LOGIC
            if (mode == ViewMode.HIGH_LEVEL && type != NodeType.PARAGRAPH) {
                continue;
            }

            sb.append("    ")
              .append(safe(node))
              .append("[\"")
              .append(node)
              .append("\"]\n");
        }

        // ---------------------------
        // 2. Add edges
        // ---------------------------
        for (DefaultEdge edge : graph.graph.edgeSet()) {

            String from = graph.graph.getEdgeSource(edge);
            String to   = graph.graph.getEdgeTarget(edge);

            NodeType fromType = graph.nodeTypes.getOrDefault(from, NodeType.PARAGRAPH);
            NodeType toType   = graph.nodeTypes.getOrDefault(to, NodeType.PARAGRAPH);

            // 🔥 FILTER edges in HIGH_LEVEL
            if (mode == ViewMode.HIGH_LEVEL &&
                (fromType != NodeType.PARAGRAPH || toType != NodeType.PARAGRAPH)) {
                continue;
            }

            String label = graph.edgeLabels.getOrDefault(edge, "");

            sb.append("    ")
              .append(safe(from))
              .append(" -->");

            if (!label.isEmpty()) {
                sb.append("|").append(label).append("|");
            }

            sb.append(" ")
              .append(safe(to))
              .append("\n");
        }

        return sb.toString();
    }

    // ---------------------------
    // Mermaid-safe node IDs
    // ---------------------------
    private static String safe(String id) {
        return id.replaceAll("[^a-zA-Z0-9_]", "_");
    }
}
