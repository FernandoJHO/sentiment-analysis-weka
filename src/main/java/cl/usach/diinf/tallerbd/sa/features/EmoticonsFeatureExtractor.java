package cl.usach.diinf.tallerbd.sa.features;

import java.util.Iterator;

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
		
			
		
		// TODO Auto-generated method stub
		return instanceTweet;
	}
	
	

}
