package de.aitools.keywordextraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.aitools.common.datastructures.Arrays;
import de.aitools.keywordextraction.extractor.KeywordExtractor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            Namespace parsedArgs = argParser().parseArgs(args);
            String input = parsedArgs.getString("input");
            String output = parsedArgs.getString("output");
            KeywordExtractor extractor = extractors().get(parsedArgs.getString("extractor"));
            Map<String, String> documents = parseDocuments(input);
            
            runExtraction(extractor, documents, output, parsedArgs.getList("num_keyphrases"), parsedArgs.getString("extractor"));

        } catch (ArgumentParserException e) {
            argParser().handleError(e);
            System.exit(1);
            return;
        }
	}

    public static void runExtraction(KeywordExtractor extractor, Map<String, String> documents, String output, List<Integer> numKeyphrases, String extractorName) throws Exception {
        List<String> ret = new LinkedList<>();

        int documentsProcessed = 0;
        System.out.println("Start to extract keyphrases");
        
        for(Map.Entry<String, String> docIdToText: documents.entrySet()) {
            Map<Integer, List<String>> keyphrases = new HashMap<>();
            for (int k: numKeyphrases) {
                try {
                    keyphrases.put(k, extractor.getPhrases(docIdToText.getValue(), k));
                } catch(Exception e) {
                    keyphrases.put(k, new ArrayList<>());
                }
                
            }

            Map<String, Object> i = new HashMap<>();
            i.put("docno", docIdToText.getKey());
            i.put("extractor", extractorName);
            i.put("keyphrases", keyphrases);
            ret.add(new ObjectMapper().writeValueAsString(i));

            if (documentsProcessed > 0 && documentsProcessed % 1000 == 0) {
                System.out.println("Processed " + documentsProcessed + " of " + documents.size());
            }
            documentsProcessed++;
        }

        System.out.println("All keyphrases are extraced.");
        Files.createDirectories(Paths.get(output));
        try (FileOutputStream fileOutput = new FileOutputStream(Paths.get(output + "/extracted-keyphrases.jsonl.gz").toFile())) {
            Writer writer = new OutputStreamWriter(new GZIPOutputStream(fileOutput), "UTF-8");

            for (String l: ret) {
                writer.write(l + "\n");
            }

            writer.close();
        }

        System.out.println("Output was written to " + output + "/extracted-keyphrases.jsonl.gz");
    }

    private static Map<String, KeywordExtractor> extractors() {
        Map<String, KeywordExtractor> ret = new HashMap<>();

        ret.put("RSPExtractorTF", KeywordExtractorFactory.getRSPExtractorTF());
        ret.put("RSPExtractorFO", KeywordExtractorFactory.getRSPExtractorFO());
        ret.put("BCExtractorFO", KeywordExtractorFactory.getBCExtractorFO());
        ret.put("BCExtractorTF", KeywordExtractorFactory.getBCExtractorTF());

        return ret;
    }

    private static ArgumentParser argParser() {
        ArgumentParser parser = ArgumentParsers.newFor("Keyphrase Extraction with aitools").build()
                .defaultHelp(true)
                .description("Extract keyphrases from text.");

        parser.addArgument("--input")
                .help("The input directory")
                .required(true);

        parser.addArgument("--output")
                .help("The output directory")
                .required(true);


        parser.addArgument("--extractor")
            .help("The extractor used to extract keyphrases.")
            .choices(extractors().keySet())
            .required(true);

        parser.addArgument("--num-keyphrases")
            .help("The number of keyphrases to extract.")
            .nargs("+")
            .setDefault(Arrays.asList(new int[]{1, 5, 10, 15, 20, 25}))
            .type(Integer.class)
            .required(false);

        return parser;
    }

    static Map<String, String> parseDocuments(String inputDirectory) {
        Map<String, String> ret = new HashMap<>();
        System.out.println("Load Documents.");
        Path file = findFileInDirectory(inputDirectory, "rerank.jsonl.gz");
        if (file == null) {
            throw new RuntimeException("Could not find rerank.jsonl.gz in " + inputDirectory);
        }

        lines(file).map(i -> parseJson(i)).forEach(i -> {
            ret.put((String) i.get("docno"),  (String) i.get("text"));
        });

        System.out.println("Documents are Loaded.");

        return ret;
    }

    private static Map<String, Object> parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<String> lines(Path file) {
        if (file.toString().endsWith(".gz")) {
            return readLinesGzip(file);
        } 

        try {
            return Files.lines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<String> readLinesGzip(Path file) {
        try {
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(file.toFile()));
            BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
            
            return br.lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path findFileInDirectory(String directory, String fileName) {
        Path directoryPath = Paths.get(directory);
        if (Files.exists(directoryPath.resolve(fileName))) {
            return directoryPath.resolve(fileName);
        }

        if (directoryPath == null || directoryPath.toFile() == null || directoryPath.toFile().list() == null) {
            return null;
        }

        for (String p: directoryPath.toFile().list()) {
            Path ret = findFileInDirectory(directoryPath.resolve(p).toAbsolutePath().toFile().getAbsolutePath(), fileName);
            if (ret != null) {
                return ret;
            }
        }

        return null;
    }
}
