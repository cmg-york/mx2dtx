package ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure;

public class Goal extends Entity {
	private String runNum;
	private Boolean isRoot;
	
	public Goal(String id, 
			String label, 
			String actor, 
			String notes, 
			String description,
			String runNum,
			boolean isRoot) {
		super(id, label, actor, notes, description);
		this.runNum = runNum;
		this.isRoot = isRoot;
	}

	public String getRunNum() {
		return runNum;
	}

	public void setRunNum(String runNum) {
		this.runNum = runNum;
	}
	
	public Boolean isRoot() {
		return (isRoot);
	}
	
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
}
