package cl.usach.diinf.tallerbd.sa.train;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

import com.google.common.collect.Lists;

import cl.usach.diinf.tallerbd.sa.clean.NormalizerTweets;
import cl.usach.diinf.tallerbd.sa.clean.SimpleTokenizer;
import cl.usach.diinf.tallerbd.sa.clean.StopWordsRemoval;
import cl.usach.diinf.tallerbd.sa.clean.Tokenizer;
import cl.usach.diinf.tallerbd.sa.data.InstanceTweet;
import cl.usach.diinf.tallerbd.sa.data.InstanceTweetUtils;
import cl.usach.diinf.tallerbd.sa.data.TweetLabel;

public class TrainModel {
	
	private TrainModel(){}
	
	public static void main(String[] args) throws Exception {
		
		List<String> tweetPositivos = FileUtils.readLines(new File("src/main/resources/data/positivo.txt"));
		List<String> tweetNegativos = FileUtils.readLines(new File("src/main/resources/data/negativo.txt"));
		
		List<InstanceTweet> tweets = Lists.newArrayList();		
		NormalizerTweets normalizer = new NormalizerTweets();
		Tokenizer tokenizer = new SimpleTokenizer(new Locale("ES"));
		StopWordsRemoval stopWordsRemoval = new StopWordsRemoval();
		
		for (String tweet : tweetPositivos){
			String tweetNormalized = normalizer.normalizeTweet(tweet);
			List<String> tokenizedTweet = tokenizer.getStrings(tweetNormalized);
			tokenizedTweet = stopWordsRemoval.removeStopwords(tokenizedTweet);
			InstanceTweet instanceTweet = new InstanceTweet(tweet, tokenizedTweet,TweetLabel.POSITIVO);
			tweets.add(instanceTweet);
		}
		
		for (String tweet : tweetNegativos){
			String tweetNormalized = normalizer.normalizeTweet(tweet);
			List<String> tokenizedTweet = tokenizer.getStrings(tweetNormalized);
			tokenizedTweet = stopWordsRemoval.removeStopwords(tokenizedTweet);
			InstanceTweet instanceTweet = new InstanceTweet(tweet, tokenizedTweet,TweetLabel.NEGATIVO);
			tweets.add(instanceTweet);
		}
		
		
		Set<String> vocabulary = new HashSet<>();
		for (InstanceTweet ins: tweets){
			vocabulary.addAll(ins.getTokenizedSource());
		}
				
		for (InstanceTweet ins: tweets){
			for (String word : vocabulary)
				if (ins.getTokenizedSource().contains(word))
					ins.addFeature(word, 1);
		}
		
		Instances data = InstanceTweetUtils.toWekaInstances(tweets, vocabulary);

		AttributeSelection filter = new AttributeSelection(); // package //
		// weka.filters.supervised.attribute!
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
		search.setNumToSelect(200);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);

		// generate new data
		data = Filter.useFilter(
				data, filter);
		
		LibSVM classifier = new LibSVM();
		
		classifier.setGamma(0.1);

		Evaluation modeleval = new Evaluation(data);
		modeleval.crossValidateModel(classifier, data,
				10, new Random(1));
			
		StringBuilder resultEvaluation = new StringBuilder();

		resultEvaluation.append("\t")
				.append(modeleval.pctCorrect() / 100).append("\t")
				.append(modeleval.weightedPrecision()).append("\t")
				.append(modeleval.weightedRecall()).append("\t")
				.append(modeleval.weightedFMeasure()).append("\n");
		
		System.out.println(resultEvaluation.toString());
		System.out.println(modeleval.toSummaryString());
		System.out.println(modeleval.toClassDetailsString());
		
	}

}
