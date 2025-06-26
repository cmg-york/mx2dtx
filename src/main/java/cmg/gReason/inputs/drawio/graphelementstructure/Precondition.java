package cmg.gReason.inputs.drawio.graphelementstructure;

public class Precondition extends GraphElement {
	private String dtxFormua;
	private Boolean isRoot;
	private String formula;
	
	public Precondition(
			String id,
			String label,
			String actor,
			String notes,
			String formula,
			String dtxFormula
			) {
		super(id, label, actor, notes);
		this.formula = formula;
		this.dtxFormua = dtxFormula;
	}

	public String getDtxFormua() {
		return dtxFormua;
	}

	public void setDtxFormua(String dtxFormua) {
		this.dtxFormua = dtxFormua;
	}

	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
}
