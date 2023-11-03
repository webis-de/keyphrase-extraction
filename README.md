# Keyphrase Extraction

This repository contains keyphrase extraction appraoches from [AItools](https://webis.de/research/aitools.html). AItools was a suite to address mining and retrieval tasks and by the Webis Group that is not maintained anymore (but the algorithms or their outputs might still be useful).

This repository uses a dev container to improve reproducibility.

## Run within tira.

```
tira-run \
	--image registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 \
	--input-directory data/robust04-02/ \
	--command 'java -jar /aitools-keywordextraction.jar --input $inputDataset --output $outputDir --extractor RSPExtractorTF'
```

```
tira-run \
	--image registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 \
	--input-directory data/robust04-02/ \
	--command 'java -jar /aitools-keywordextraction.jar --input $inputDataset --output $outputDir --extractor RSPExtractorFO'
```


```
tira-run \
	--image registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 \
	--input-directory data/robust04-02/ \
	--command 'java -jar /aitools-keywordextraction.jar --input $inputDataset --output $outputDir --extractor BCExtractorFO'
```


```
tira-run \
	--image registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 \
	--input-directory data/robust04-02/ \
	--command 'java -jar /aitools-keywordextraction.jar --input $inputDataset --output $outputDir --extractor BCExtractorTF'
```

```
docker build -t webis/aitools:2009-08-08-dev -f Dockerfile.dev .
```


