package cmg.gReason.inputs.drawio.graphelementstructure;

public class Quality extends GraphElement {
	private String dtxFormua;
	private Boolean isRoot;
	private String formula;
	
	public String getDtxFormua() {
		return dtxFormua;
	}

	public void setDtxFormua(String dtxFormua) {
		this.dtxFormua = dtxFormua;
	}

	public Boolean isQRoot() {
		return isRoot;
	}

	public void setQRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public Quality(
			String id,
			String label,
			String actor,
			String notes,
			String formula,
			String dtxFormula,
			Boolean isRoot
			) {
		super(id, label, actor, notes);
		this.formula = formula;
		this.dtxFormua = dtxFormula;
		this.isRoot = isRoot;
	}
}
