package de.aitools.keywordextraction;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class SubstringExtraction {

	public String extractSubstring(String paragraph){
			List<String> vec = new ArrayList<String>();
			String subText="";
			int wordCount=0;
			int startIndex=0;
			int endIndex=5;
			StringTokenizer sentenceTokenizer=new StringTokenizer(paragraph, " ");			
			while(sentenceTokenizer.hasMoreTokens()){
				String word=sentenceTokenizer.nextToken();
				subText+=word+" ";
				wordCount++;
				vec.add(word);
				
			}	
//			for(int x=0; x<vec.size(); x++){
//				subText=vec.subList(startIndex, endIndex).toString();
//				System.out.println(subText);
//			}
			System.out.println(wordCount % 5);
			System.out.println(wordCount);
		return subText;
		}

	
	public static void main(String[] args) {
		String text = "We  have  shown  in  the  previous  paragraphs  that  the  rough  set  model  makes  it  possible to  rank  documents  with  or  without  weighted  representations.  We  have  also  shown  that boolean  logic  can  be  built  into  the  model.  Both  rough  sets  and  fuzzy  sets  are extensions  to  standard  set  theory.  However,  rough  sets  applied  to  information  retrieval";
		SubstringExtraction sse = new SubstringExtraction();
		sse.extractSubstring(text);
	}

}
