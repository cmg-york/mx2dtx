package cmg.gReason.inputs.drawio.graphelementstructure;

public class Goal extends Entity {
	private String runNum;
	
	public Goal(String id, 
			String label, 
			String actor, 
			String notes, 
			String runNum) {
		super(id, label, actor, notes);
		this.runNum = runNum;
	}

	public String getRunNum() {
		return runNum;
	}

	public void setRunNum(String runNum) {
		this.runNum = runNum;
	}
	
	

}
