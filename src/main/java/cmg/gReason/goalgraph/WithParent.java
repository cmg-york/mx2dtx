package cmg.gReason.goalgraph;

public class WithParent extends GMNode {

	protected WithParent parent = null;
	
	public void setParent(WithParent parent) {
		this.parent = parent;
	}
	
	public WithParent getParent() {
		return this.parent;
	}
}
