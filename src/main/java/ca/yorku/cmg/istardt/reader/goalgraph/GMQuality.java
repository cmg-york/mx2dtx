package ca.yorku.cmg.istardt.reader.goalgraph;

import java.util.ArrayList;

public class GMQuality extends WithFormula {
	protected boolean isQRoot;
	protected ArrayList<GMNode> inContr = new ArrayList<GMNode>();
	
	public void setQRoot(boolean root) {
		this.isQRoot = root;
	}
	public boolean isQRoot() {
		return (this.isQRoot);
	}
	
    /**
     * Add a node as an incoming contribution to this node.
     * 
     * @param e The contribution node to be added. A {@linkplain Contribution} object.
     */
	public void addInContribution(GMNode e) {
		inContr.add(e);
	}
	
	public ArrayList<GMNode> getIncompingContributions(){
		return this.inContr;
	}
	
}
