package cmg.gReason.goalgraph;

public class GMEffect extends WithParent {
	// For effects only
	protected String effectStatus;
	
	// Effects, Qualities
	protected float inWeight;
	
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
