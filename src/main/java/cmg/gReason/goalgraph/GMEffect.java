package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GMEffect extends WithParent {
	// For effects only
	protected String effectStatus;
	EffectsParser parserTrue = 	new EffectsParser();
	EffectsParser parserFalse = new EffectsParser();
	
	// Effects, Qualities
	protected float inWeight;
	
	
	public EffectsParser getParserTrue() {
		return parserTrue;
	}

	public void setParserTrue(EffectsParser parserTrue) {
		this.parserTrue = parserTrue;
	}

	public EffectsParser getParserFalse() {
		return parserFalse;
	}

	public void setParserFalse(EffectsParser parserFalse) {
		this.parserFalse = parserFalse;
	}

	public boolean useLabel() {
		return(parserTrue.getPropositions().isEmpty() &&
				parserTrue.getVariables().isEmpty() &&
				parserFalse.getPropositions().isEmpty() &&
				parserFalse.getVariables().isEmpty());
	}
	
	
	public List<String> getAllPredicates() {
		List<String> combinedPredicates = new ArrayList<String>();
		combinedPredicates.addAll(parserTrue.getPropositions());
		combinedPredicates.addAll(parserFalse.getPropositions());
		return (combinedPredicates);
	}
	
	public ArrayList<String> getTruePredicates(){
		if (this.useLabel()) {
			return new ArrayList<> (List.of(this.getCamelLabel()));
		} else {
			return(parserTrue.getPropositions());
		}
	}

	public ArrayList<String> getFalsePredicates(){
		return(parserFalse.getPropositions());
	}

	public HashMap<String,String> getVariables(){
		return(parserTrue.getVariables());
	}
	
	public void setTurnsTrue(String turnsTrue) {
		this.parserTrue.parse(turnsTrue);
	}

	public void setTurnsFalse(String turnsFalse) {
		this.parserFalse.parse(turnsFalse);
	}
		
    /**
     * Get the effect status of the node (for effects only).
     * 
     * @return The effect status of the node: "success" of "failure"
     */
	public String getEffectStatus() {
		return effectStatus;
	}

    /**
     * Set the effect status of the node (for effects only).
     * 
     * @param effectStatus The effect status to be set, one of  "success" of "failure".
     */
	public void setEffectStatus(String effectStatus) {
		this.effectStatus = effectStatus;
	}
	
	
    /**
     * Get the incoming weight of the node (for effects only). For effects it is the probability of the effect.
     * 
     * @return The probability of the effect.
     */
	public float getInWeight() {
		return inWeight;
	}


    /**
     * Set the incoming weight of the node (for effectsonly).
     * 
     * @param inWeight The incoming weight to be set.
     */
	public void setInWeight(float inWeight) {
		this.inWeight = inWeight;
	}
	
	
}
