package cl.usach.diinf.tallerbd.sa.features;

import cl.usach.diinf.tallerbd.sa.data.InstanceTweet;

/**
 * Interfaz para extraer de características a partir de una instancia de un tweet
 * @author rvasquez
 *
 */
public interface FeatureExtractor {
	
	/**
	 * Agrega una característica a un tweet modificando el mapa de características
	 * @param instanceTweet
	 * @return
	 */
	InstanceTweet extractFeature(InstanceTweet instanceTweet);
	
}
