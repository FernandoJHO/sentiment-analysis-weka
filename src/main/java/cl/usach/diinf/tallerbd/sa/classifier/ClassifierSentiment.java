package cl.usach.diinf.tallerbd.sa.classifier;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import weka.classifiers.Classifier;
import weka.core.Instances;
import cl.usach.diinf.tallerbd.sa.clean.NormalizerTweets;
import cl.usach.diinf.tallerbd.sa.clean.StopWordsRemoval;
import cl.usach.diinf.tallerbd.sa.clean.Tokenizer;
import cl.usach.diinf.tallerbd.sa.data.InstanceTweet;
import cl.usach.diinf.tallerbd.sa.data.InstanceTweetUtils;

public class ClassifierSentiment {

	private NormalizerTweets normalizer;
	private Tokenizer tokenizer;
	private StopWordsRemoval stopWordsRemoval;
	private Classifier classifier;
	private Set<String> attributes;

	/**
	 * Constructor del clasificador
	 * @param pathClassifier ubicacion del clasificador serializado
	 * @param pathAttrs ubicacion del archivo con los atributos usadoss
	 * @param normalizer normalizador de tweetes
	 * @param tokenizer tokenizer
	 * @param stopWordsRemoval stops words removal
	 * @throws Exception
	 */
	public ClassifierSentiment(String pathClassifier, String pathAttrs,
			NormalizerTweets normalizer, Tokenizer tokenizer,
			StopWordsRemoval stopWordsRemoval) throws Exception {
		this.classifier = (Classifier) weka.core.SerializationHelper
				.read(pathClassifier);
		this.normalizer = normalizer;
		this.tokenizer = tokenizer;
		this.stopWordsRemoval = stopWordsRemoval;

		loadAttrs(pathAttrs);

	}

	/**
	 * Se cargan los atributos, que en este caso son palabras.
	 * @param pathName
	 * @throws IOException
	 */
	private void loadAttrs(String pathName) throws IOException {

		this.attributes = new HashSet<>();
		for (String attr : FileUtils.readLines(new File(pathName))) {

			String[] data = attr.split(" ");
			// la clase es ignorada ya que los nuevos tweets no poseen clase
			if (data[1].equals("@@class@@"))
				continue;
			attributes.add(data[1]);
		}
	}

	/**
	 * A partir de un texto lo clasifica
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public double classify(String content) throws Exception {

		String tweetNormalized = normalizer.normalizeTweet(content);
		List<String> tokenizedTweet = tokenizer.getStrings(tweetNormalized);
		tokenizedTweet = stopWordsRemoval.removeStopwords(tokenizedTweet);

		InstanceTweet tweet = new InstanceTweet(content, tokenizedTweet, null);
		List<InstanceTweet> tweets = Lists.newArrayList();
		tweets.add(tweet);

		// Extracción de las características, si un tweet contiene la palabra se
		// agrega esta a un mapa attr --> value
		for (InstanceTweet ins : tweets) {
			for (String word : this.attributes)
				if (ins.getTokenizedSource().contains(word))
					ins.addFeature(word, 1);
		}
		// se transforman las instancias weka pero para el procedimiento de clasificacion.
		Instances instances = InstanceTweetUtils
				.toWekaInstancesForClassification(tweets, this.attributes);

		return this.classifier.classifyInstance(instances.firstInstance());
	}

}
