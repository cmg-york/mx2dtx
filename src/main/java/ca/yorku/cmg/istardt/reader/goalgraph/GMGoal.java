package ca.yorku.cmg.istardt.reader.goalgraph;

import java.util.ArrayList;

public class GMGoal extends WithParent {
	private boolean root;
	protected String runs;
	protected ArrayList<GMNode> orChildren  = new ArrayList<GMNode>();
	protected ArrayList<GMNode> andChildren  = new ArrayList<GMNode>();

	
	public String getRuns() {
		return (runs.equals("") ? "1" : runs) ;
	}

	public void setRuns(String runs) {
		this.runs = runs;
	}
	
	public boolean isRoot() {
		return (root);
	}
	
	public void setRoot(boolean isRoot) {
		this.root = isRoot;
	}
	
	
	   /**
     * Add an AND child to the node.
     * 
     * @param e The child node to be added.
     */
	public void addANDChild(GMNode e) {
		andChildren.add(e);
	}
	
    /**
     * Add an OR child to the node.
     * 
     * @param e The child node to be added.
     */
	public void addORChild(GMNode e) {
		orChildren.add(e);
	}
	
	
	/**
	 * Read Node Information
	 */
	


	public ArrayList<GMNode> getORChildren(){
		return this.orChildren;
	}
	
	public ArrayList<GMNode> getANDChildren(){
		return this.andChildren;
	}
	
	
	
	
}
