package cmg.gReason.inputs.drawio.graphelementstructure;

public class InitializationSet extends GraphElement {
	private String dtxFormula;
	private String formula;
	
	public String getDtxFormula() {
		return dtxFormula;
	}

	public void setDtxFormula(String dtxFormula) {
		this.dtxFormula = dtxFormula;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	
	public InitializationSet(
			String id,
			String label,
			String actor,
			String notes,
			String formula,
			String dtxFormula
			) {
		super(id, label, actor, notes);
		this.formula = formula;
		this.dtxFormula = dtxFormula;
	}
}
