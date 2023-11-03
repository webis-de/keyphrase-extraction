

```
tira-run \
	--image registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 \
	--input-directory data/robust04-02/ \
	--command 'java -jar /aitools-keywordextraction.jar --input $inputDataset --output $outputDir --extractor RSPExtractorTF'
```

```
docker build -t webis/aitools:2009-08-08-dev -f Dockerfile.dev .
```


