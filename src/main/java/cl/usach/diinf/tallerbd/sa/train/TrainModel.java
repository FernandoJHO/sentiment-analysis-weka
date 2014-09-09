package cl.usach.diinf.tallerbd.sa.train;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Evaluation;
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
		
		// Se leen los Tweets de entrenamiento
		List<String> tweetPositivos = FileUtils.readLines(new File("src/main/resources/data/positivo.txt"));
		List<String> tweetNegativos = FileUtils.readLines(new File("src/main/resources/data/negativo.txt"));
		
		List<InstanceTweet> tweets = Lists.newArrayList();	
		
		// Se construyen los objetos para el preprocesamiento de los Tweets
		NormalizerTweets normalizer = new NormalizerTweets();
		Tokenizer tokenizer = new SimpleTokenizer(new Locale("ES"));
		StopWordsRemoval stopWordsRemoval = new StopWordsRemoval();
		
		// preprocesamiento de los tweets positivos
		for (String tweet : tweetPositivos){
			String tweetNormalized = normalizer.normalizeTweet(tweet);
			List<String> tokenizedTweet = tokenizer.getStrings(tweetNormalized);
			tokenizedTweet = stopWordsRemoval.removeStopwords(tokenizedTweet);
			InstanceTweet instanceTweet = new InstanceTweet(tweet, tokenizedTweet,TweetLabel.POSITIVO);
			tweets.add(instanceTweet);
		}
		
		// preprocesamiento de los tweets negativos
		for (String tweet : tweetNegativos){
			String tweetNormalized = normalizer.normalizeTweet(tweet);
			List<String> tokenizedTweet = tokenizer.getStrings(tweetNormalized);
			tokenizedTweet = stopWordsRemoval.removeStopwords(tokenizedTweet);
			InstanceTweet instanceTweet = new InstanceTweet(tweet, tokenizedTweet,TweetLabel.NEGATIVO);
			tweets.add(instanceTweet);
		}
		
		// Obtención de los atributos, en este caso serán el vocabulario que podemos encontrar en los tweets
		Set<String> vocabulary = new HashSet<>();
		for (InstanceTweet ins: tweets){
			vocabulary.addAll(ins.getTokenizedSource());
		}
				
		// Extracción de las características, si un tweet contiene la palabra se agrega esta a un mapa attr --> value
		for (InstanceTweet ins: tweets){
			for (String word : vocabulary)
				if (ins.getTokenizedSource().contains(word))
					ins.addFeature(word, 1);
		}
		
		// se transformar las InstanceTweet en instancias de weka
		Instances data = InstanceTweetUtils.toWekaInstances(tweets, vocabulary);

		// se aplica un algoritmo de selección para reducir la dimensionalidad
		AttributeSelection filter = new AttributeSelection(); 
		InfoGainAttributeEval eval = new InfoGainAttributeEval();
		Ranker search = new Ranker();
		search.setNumToSelect(200);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		filter.setInputFormat(data);

		// generate new data
		data = Filter.useFilter(
				data, filter);
		
		// Se crea un clasificador SVM
		LibSVM classifier = new LibSVM();
		classifier.setGamma(0.1);

		// Se entrena un modelo utilizando 10 fold cross validation
		Evaluation modeleval = new Evaluation(data);
		modeleval.crossValidateModel(classifier, data,
				10, new Random(1));
		
		// Se muestran métricas de evaluación del modelo construido
		System.out.println(modeleval.toSummaryString());
		System.out.println(modeleval.toClassDetailsString());
		
		 // Serializar el modelo
		 ObjectOutputStream oos = new ObjectOutputStream(
		                            new FileOutputStream("src/main/resources/model/svm.model"));
		 oos.writeObject(classifier);
		 oos.flush();
		 oos.close();
		
	}

}
