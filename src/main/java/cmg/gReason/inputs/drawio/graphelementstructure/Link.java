package cmg.gReason.inputs.drawio.graphelementstructure;

public class Link extends GraphElement {

	String source;
	String target;
	String type;
	
	public Link(String id, 
			String label, 
			String actor, 
			String notes,
			String type,
			String source,
			String target
			) {
		super(id, label, actor, label, notes);
		this.source = source;
		this.target = target;
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
