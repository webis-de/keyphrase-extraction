package de.aitools.keywordextraction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.junit.Assert;
import org.junit.Test;

public class EndToEntTest {
    @Test
    public void rspeTfExtractorFor5Phrases() throws Exception {
        List<String> expected = Arrays.asList(
            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"empty-01\",\"keyphrases\":{\"5\":" +
                "[]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS4-41991\",\"keyphrases\":{\"5\":"+
                "[\"freeh\",\"fbi director\",\"east european\",\"russian crime\",\"internal affairs\",\"world\"]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS3-23986\",\"keyphrases\":{\"5\":" +
                "[\"respect\",\"state\",\"internal affairs\",\"law enforcement\",\"considerable\",\"organized crime\",\"abuse\",\"charged\",\"increased\",\"members\"]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS4-40260\",\"keyphrases\":{\"5\":" +
                "[\"banditry and other manifestations of organized\",\"manifestations of organized crime\",\"russian federation\",\"internal affairs\",\"prosecutor\"]}}"
        );
        List<String> actual = runKeyphraseExtraction("RSPExtractorTF", 5);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rspeOfExtractorFor5Phrases() throws Exception {
        List<String> expected = Arrays.asList(
            "{\"extractor\":\"RSPExtractorFO\",\"docno\":\"empty-01\",\"keyphrases\":{\"5\":" +
                "[]}}",

            "{\"extractor\":\"RSPExtractorFO\",\"docno\":\"FBIS4-41991\",\"keyphrases\":{\"5\":"+
                "[\"fbi director\",\"east european\",\"russian crime\",\"freeh\",\"internal affairs\",\"world\"]}}",

            "{\"extractor\":\"RSPExtractorFO\",\"docno\":\"FBIS3-23986\",\"keyphrases\":{\"5\":" +
                "[\"state\",\"internal affairs\",\"organized crime\",\"law enforcement\",\"considerable\",\"abuse\",\"charged\",\"increased\",\"respect\",\"members\"]}}",

            "{\"extractor\":\"RSPExtractorFO\",\"docno\":\"FBIS4-40260\",\"keyphrases\":{\"5\":" +
                "[\"banditry and other manifestations of organized\",\"manifestations of organized crime\",\"russian federation\",\"prosecutor\",\"internal affairs\"]}}"
        );
        List<String> actual = runKeyphraseExtraction("RSPExtractorFO", 5);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void BcExtractorFoExtractorFor5Phrases() throws Exception {
        List<String> expected = Arrays.asList(
            "{\"extractor\":\"BCExtractorFO\",\"docno\":\"empty-01\",\"keyphrases\":{\"5\":" +
                "[]}}",

            "{\"extractor\":\"BCExtractorFO\",\"docno\":\"FBIS4-41991\",\"keyphrases\":{\"5\":"+
                "[\"organized postcommunist crime\",\"organized crime\",\"russian crime\",\"vladimir nadein fbi\",\"considerable number\"]}}",

            "{\"extractor\":\"BCExtractorFO\",\"docno\":\"FBIS3-23986\",\"keyphrases\":{\"5\":" +
                "[\"real state\",\"further increase\",\"public control\",\"sharp increase\",\"international smuggling crime groups\"]}}",

            "{\"extractor\":\"BCExtractorFO\",\"docno\":\"FBIS4-40260\",\"keyphrases\":{\"5\":" +
                "[\"aforementioned crimes\",\"federation additional measures\",\"presidential edict\",\"other serious crimes\",\"organized crime\"]}}"
        );
        List<String> actual = runKeyphraseExtraction("BCExtractorFO", 5);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void BcExtractorTfExtractorFor5Phrases() throws Exception {
        List<String> expected = Arrays.asList(
            "{\"extractor\":\"BCExtractorTF\",\"docno\":\"empty-01\",\"keyphrases\":{\"5\":" +
                "[]}}",

            "{\"extractor\":\"BCExtractorTF\",\"docno\":\"FBIS4-41991\",\"keyphrases\":{\"5\":"+
                "[\"organized crime\",\"communism old criminal structures\",\"fbi director\",\"criminal world\",\"russian crime\"]}}",

            "{\"extractor\":\"BCExtractorTF\",\"docno\":\"FBIS3-23986\",\"keyphrases\":{\"5\":" +
                "[\"percent\",\"internal affairs organs\",\"economic crime\",\"economic sphere\",\"respect\"]}}",

            "{\"extractor\":\"BCExtractorTF\",\"docno\":\"FBIS4-40260\",\"keyphrases\":{\"5\":" +
                "[\"other manifestations\",\"individual localities\",\"other serious crimes\",\"russian federation\",\"counterintelligence organs\"]}}"
        );
        List<String> actual = runKeyphraseExtraction("BCExtractorTF", 5);
        
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void rspeTfExtractorFor7Phrases() throws Exception {
        List<String> expected = Arrays.asList(
            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"empty-01\",\"keyphrases\":{\"7\":" +
                "[]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS4-41991\",\"keyphrases\":{\"7\":"+
                "[\"collapse\",\"senate hearings\",\"ministry of internal affairs\",\"louis freeh\",\"department\",\"criminal world\",\"democracy\",\"east european\",\"russian crime\",\"number\",\"international\",\"criminal structures\",\"experts\",\"news conference\"]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS3-23986\",\"keyphrases\":{\"7\":" +
                "[\"respect\",\"state\",\"internal affairs\",\"law enforcement\",\"considerable\",\"organized crime\",\"abuse\",\"charged\",\"increased\",\"members\"]}}",

            "{\"extractor\":\"RSPExtractorTF\",\"docno\":\"FBIS4-40260\",\"keyphrases\":{\"7\":" +
                "[\"banditry and other manifestations of organized\",\"manifestations of organized crime\",\"russian federation federal\",\"cities and individual localities\",\"prosecutor s office\",\"present edict\",\"fight\",\"crimes\",\"organized criminal\",\"implementation\",\"cases\",\"counterintelligence organs\"]}}"
        );
        List<String> actual = runKeyphraseExtraction("RSPExtractorTF", 7);
        
        Assert.assertEquals(expected, actual);
    }

    private List<String> runKeyphraseExtraction(String approach, int numberOfKeywordsToExtract) throws Exception {
        String tmpDir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();
        String[] args = new String[] {"--extractor", approach, "--input",  "src/test/resources", "--output", tmpDir, "--num-keyphrases", "" + numberOfKeywordsToExtract};
        App.main(args);

        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(Paths.get(tmpDir + "/extracted-keyphrases.jsonl.gz").toFile()));
        BufferedReader br = new BufferedReader(new InputStreamReader(gzip));

        return br.lines().collect(Collectors.toList());
    }
}
