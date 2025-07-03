package cmg.gReason.inputs.drawio.graphelementstructure;

import java.util.ArrayList;
import java.util.HashMap;

import cmg.gReason.goalgraph.EffectsParser;

public class Effect extends Entity {

	String status;
	String turnsTrueString;
	String turnsFalseString;

	
	public Effect(String id, String label, String actor, 
			String notes, String status,
			String turnsTrue, String turnsFalse
			) {
		super(id, label, actor, notes);
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

	
}
