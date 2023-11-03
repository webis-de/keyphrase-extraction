# docker build -t registry.webis.de/code-research/tira/tira-user-ows/keyphrase-extraction:1.0 .
FROM webis/aitools:2009-08-08-dev

ADD assembly.xml pom.xml /code/
ADD lib /code/lib
ADD src /code/src

RUN cd /code \
	&& mvn clean install \
	&& mv target/aitools-keywordextraction-1.0-SNAPSHOT-jar-with-all-dependencies.jar /aitools-keywordextraction.jar \
	&& cd / \
	&& rm -R /code

