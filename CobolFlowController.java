package com.example.cobolflow;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class CobolFlowController {

    @PostMapping("/flow")
    public String flow(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "HIGH_LEVEL") String viewMode
    ) throws Exception {

        // Parse COBOL into graph (your own util, NOT OpenRewrite internal class)
        CobolGraph graph = CobolParserUtil.parse(file);

        // View mode
        ViewMode mode = ViewMode.valueOf(viewMode);

        // Export to Mermaid
        return MermaidExporter.toMermaid(graph, mode);
    }
}
