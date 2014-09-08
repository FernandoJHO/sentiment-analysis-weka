package cl.usach.diinf.tallerbd.sa.clean;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Sets;

public class StopWordsRemoval {
	
	private final Set<String> stopwords;

	public StopWordsRemoval() throws IOException {
		super();
		this.stopwords = loadStopwords();
	}

	private Set<String> loadStopwords() throws IOException {
		return Sets.newHashSet(FileUtils.readLines(new File("src/main/resources/stopwords/stopwords-es")));
	}
	
	public List<String> removeStopwords(List<String> words){
		
		words.removeAll(this.stopwords);
		return words;
	
	}
	
	

}
