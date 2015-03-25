package cl.usach.diinf.tallerbd.sa.features;

import cl.usach.diinf.tallerbd.sa.data.InstanceTweet;

public class EmoticonsFeatureExtractor implements FeatureExtractor{

	@Override
	public InstanceTweet extractFeature(InstanceTweet instanceTweet) {
		
		int countPosEmoticons = 0;
		int countNegEmoticons = 0;
		for (String token : instanceTweet.getTokenizedSource()) {
			
			if (token.contains(":)")){
				countPosEmoticons++;
			}
			if (token.contains(":(")){
				countNegEmoticons++;
			}
		}
		
		instanceTweet.addFeature("posEmoticons", countPosEmoticons);
		instanceTweet.addFeature("negEmoticons", countNegEmoticons);
		instanceTweet.addFeature("length", instanceTweet.getTokenizedSource().size());
		return instanceTweet;
	}
	
	

}
