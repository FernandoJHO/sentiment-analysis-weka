package cl.usach.diinf.tallerbd.sa.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Representa una instancia de un tweet
 * @author rvasquez
 *
 */
public class InstanceTweet {
	
	private final String source;
	private final List<String> tokenizedSource;
	private final TweetLabel label;
	private Map<String, Integer> features;
	
	/**
	 * Constructor
	 * @param source tweet original sin ninguna modificación
	 * @param tokenizedSource tweet tokenizado 
	 * @param label etiqueta del tweet
	 */
	public InstanceTweet(String source, List<String> tokenizedSource,
			TweetLabel label) {
		super();
		this.source = source;
		this.tokenizedSource = tokenizedSource;
		this.label = label;
		this.features = new HashMap<>();
	}

	public String getSource() {
		return source;
	}

	public List<String> getTokenizedSource() {
		return tokenizedSource;
	}
	
	public Integer getFeature(String attr){
		return this.features.get(attr);
	}
	
	public void addFeature(String attr, Integer value){
		 this.features.put(attr, value);
	}

	public TweetLabel getLabel() {
		return label;
	}
	
	public Set<String> getAttributes(){
		return this.features.keySet();
	}
	

	@Override
	public String toString() {
		return "InstanceTweet [source=" + source + ", tokenizedSource="
				+ tokenizedSource + ", label=" + label + ", features="
				+ features + "]";
	}

	
	
}
