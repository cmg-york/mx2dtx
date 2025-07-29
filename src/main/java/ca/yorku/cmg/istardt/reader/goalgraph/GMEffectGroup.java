package ca.yorku.cmg.istardt.reader.goalgraph;

import java.util.ArrayList;

public class GMEffectGroup extends WithParent {
	/* For tasks and temporarily effect groups */
	protected ArrayList<GMNode> effects  = new ArrayList<GMNode>();

	
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
	



}
