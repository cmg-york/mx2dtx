package cmg.gReason.inputs.drawio.graphelementstructure;

public class Effect extends Entity {

	String status;
	
	public Effect(String id, String label, String actor, String notes, String status) {
		super(id, label, actor, notes);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
