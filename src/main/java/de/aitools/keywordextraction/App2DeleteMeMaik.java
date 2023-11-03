package de.aitools.keywordextraction;

public class App2DeleteMeMaik {
    public static void main(String[] args) throws Exception {
        run("data/robust04-02", "RSPExtractorTF");
        run("data/robust04-02", "RSPExtractorFO");
        run("data/robust04-02", "BCExtractorFO");
        run("data/robust04-02", "BCExtractorTF");
    }

    private static void run(String dir, String approach) throws Exception {
        String[] args = new String[] {"--extractor", approach, "--input",  dir, "--output", dir + "/" + approach};
        App.main(args);
    }
}