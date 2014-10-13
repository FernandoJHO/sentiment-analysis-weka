package cl.usach.diinf.tallerbd.sa.classifier;

import java.util.Locale;

import cl.usach.diinf.tallerbd.sa.clean.NormalizerTweets;
import cl.usach.diinf.tallerbd.sa.clean.SimpleTokenizer;
import cl.usach.diinf.tallerbd.sa.clean.StopWordsRemoval;
import cl.usach.diinf.tallerbd.sa.clean.Tokenizer;

public final class DemoClassifier {
	
	public static void main(String[] args) throws Exception {
		String pathClassifier = "src/main/resources/model/svm.model";
		String pathAttrs = "src/main/resources/model/attrs.dat";
		NormalizerTweets normalizer = new NormalizerTweets();
		Tokenizer tokenizer = new SimpleTokenizer(new Locale("ES"));
		StopWordsRemoval stopWordsRemoval = new StopWordsRemoval();
		
		ClassifierSentiment classifierSentiment = new ClassifierSentiment(pathClassifier, pathAttrs, normalizer, tokenizer, stopWordsRemoval);
		
		// Si el la salida es 0.0 se clasifica como positivo , sino se clasifica como negativo
		if (classifierSentiment.classify("@Vm_pacheco Esta bien.")==0.0)
			System.out.println("Positivo");
		else
			System.out.println("Negativo");
		
		if (classifierSentiment.classify("Mal...se aprobo rebajar talla minima del jurel para el norte del pais a 22cm,mala senal para la discusion larga de pesca!!")==0.0)
			System.out.println("Positivo");
		else
			System.out.println("Negativo");
		
	}

}
