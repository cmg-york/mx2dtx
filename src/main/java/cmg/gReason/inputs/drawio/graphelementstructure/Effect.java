package cmg.gReason.inputs.drawio.graphelementstructure;

public class Effect extends Entity {

	String status;
	String turnsTrueString = null;
	String turnsFalseString = null;

	
	public Effect(String id, String label, String actor, 
			String notes, String description,
			String status,
			String turnsTrue, String turnsFalse
			) {
		super(id, label, actor, notes, description);
		this.turnsTrueString = turnsTrue;
		this.turnsFalseString = turnsFalse;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getTurnsTrue() {
		return(this.turnsTrueString);
	}

	public String getTurnsFalse() {
		return(this.turnsFalseString);
	}

	public boolean hasCustom() {
		return (turnsTrueString!=null) || (turnsFalseString!=null); 
	}
	
}
