package com.example.cobolflow;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CobolParser {

    public static CobolGraph parse(MultipartFile file) throws Exception {

        CobolGraph graph = new CobolGraph();
        String previous = "START";

        graph.addNode(previous);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(file.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {

            line = line.trim();
            if (line.isEmpty()) continue;

            if (isExecutable(line)) {
                graph.addNode(line);
                graph.addEdge(previous, line, "next");
                previous = line;
            }
        }

        graph.addNode("END");
        graph.addEdge(previous, "END", "end");

        return graph;
    }

    private static boolean isExecutable(String line) {
        return line.startsWith("IF")
                || line.startsWith("PERFORM")
                || line.startsWith("CALL")
                || line.startsWith("DISPLAY")
                || line.startsWith("MOVE")
                || line.startsWith("READ")
                || line.startsWith("WRITE")
                || line.startsWith("STOP RUN");
    }
}
