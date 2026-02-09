package com.example.cobolflow;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

public class CobolParserUtil {

    public static CobolGraph parse(MultipartFile file) throws Exception {

        // Save upload to temp file
        Path temp = Files.createTempFile("cobol-", ".cbl");
        file.transferTo(temp.toFile());

        // Build graph using your builder
        CobolGraphBuilder builder = new CobolGraphBuilder();
        return builder.build(temp);
    }
}
