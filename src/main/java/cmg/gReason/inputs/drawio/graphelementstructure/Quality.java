package cmg.gReason.inputs.drawio.graphelementstructure;

public class Quality extends ElementWithFormula {
	private Boolean isRoot;

	public Boolean isQRoot() {
		return isRoot;
	}

	public void setQRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public Quality(
			String id,
			String label,
			String actor,
			String notes,
			String description, 
			String formula,
			String dtxFormula,
			Boolean isRoot
			) {
		super(id, label, actor, notes, description, formula, dtxFormula);
		this.isRoot = isRoot;
	}
}
