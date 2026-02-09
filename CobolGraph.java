package com.example.cobolflow;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class CobolGraph {

    public DefaultDirectedGraph<String, DefaultEdge> graph =
            new DefaultDirectedGraph<>(DefaultEdge.class);

    public Map<DefaultEdge, String> edgeLabels = new HashMap<>();

    // 🔥 Node → Type mapping
    public Map<String, NodeType> nodeTypes = new HashMap<>();

    // --------------------
    // Node creation
    // --------------------

    // ✅ DEFAULT (backward compatible)
    public void addNode(String node) {
        addNode(node, NodeType.PARAGRAPH);
    }

    // ✅ EXPLICIT TYPE
    public void addNode(String node, NodeType type) {
        graph.addVertex(node);
        nodeTypes.put(node, type);
    }

    // --------------------
    // Edge creation
    // --------------------
    public void addEdge(String from, String to, String label) {
        graph.addVertex(from);
        graph.addVertex(to);
        DefaultEdge e = graph.addEdge(from, to);
        if (e != null) {
            edgeLabels.put(e, label);
        }
    }

    // --------------------
    // IF / ELSE / EVALUATE
    // --------------------
    public void addDecision(String parent, String decisionName) {

        String decisionNode = parent + "_" + decisionName;

        addNode(parent, NodeType.PARAGRAPH);
        addNode(decisionNode, NodeType.IF);

        addEdge(parent, decisionNode, decisionName);
    }

    // --------------------
    // EXEC SQL
    // --------------------
    public void addSql(String parent) {

        String sqlNode = parent + "_SQL";

        addNode(parent, NodeType.PARAGRAPH);
        addNode(sqlNode, NodeType.SQL);

        addEdge(parent, sqlNode, "EXEC SQL");
    }
}
