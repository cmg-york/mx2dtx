package cmg.gReason.goalgraph;

import java.util.ArrayList;

public class GMTask extends WithParent {
	/* For tasks and temporarily effect groups */
	protected ArrayList<GMNode> effects  = new ArrayList<GMNode>();
	
	/* For tasks */
	protected GMNode outEffectLink = null;
	
	/* For effects and temporarily effect groups */
	protected GMNode inEffectLink = null;

    /**
     * Add a node as an effect to this node (for tasks and temporarily effect groups).
     * 
     * @param e The effect node to be added.
     */
	public void addEffect(GMNode e) {
		effects.add(e);
	}
	
	 /**
     * Set the list of effect nodes for this node (for tasks and temporarily effect groups).
     * 
     * @param effs The list of effect nodes to be set.
     */
	public void setEffects(ArrayList<GMNode> effs) {
		this.effects = effs;
	}
	
	public ArrayList<GMNode> getEffects(){
		return this.effects;
	}
	
	
	   /**
     * Set a node as the incoming effect link for this node (for effects and temporarily effect groups).
     * 
     * @param e The incoming effect link node to be set.
     */
	public void setInEffectLink(GMNode e) {
		this.inEffectLink = e;
	}
	
    /**
     * Get the incoming effect link node for this node (for effects and temporarily effect groups).
     * 
     * @return The incoming effect link node.
     */
	public GMNode getInEffectLink() {
		return(this.inEffectLink);
	}
	
    /**
     * Set a node as the outgoing effect link for this node (for tasks).
     * 
     * @param e The outgoing effect link node to be set.
     */
	public void setOutEffectLink(GMNode e) {
		this.outEffectLink = e;
	}
	
    /**
     * Get the outgoing effect link node for this node (for tasks).
     * 
     * @return The outgoing effect link node.
     */
	public GMNode getOutEffectLink() {
		return(this.outEffectLink);
	}


}
