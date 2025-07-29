package ca.yorku.cmg.istardt.reader.goalgraph;

/**
 * Represents a contribution link object.
 * 
 * @author Sotirios Liaskos
 *
 */
public class Contribution extends GMNode {
	public GMNode origin;
	public GMNode target;
	public float weight;
	
	public Contribution() {
		
	}
	
	public Contribution(GMNode gmnOrigin, GMNode gmnTarget, String weight) {
		origin = gmnOrigin;
		target = gmnTarget;
		this.weight = Float.parseFloat(weight);
	}
	public GMNode getContributionOrigin() {
		return origin;
	}
	public void setContributionOrigin(GMNode origin) {
		this.origin = origin;
	}
	public GMNode getContributionTarget() {
		return target;
	}
	public void setContributionTarget(GMNode target) {
		this.target = target;
	}
	public float getContributionWeight() {
		return weight;
	}
	public void setContributionWeight(float weight) {
		this.weight = weight;
	}
}
