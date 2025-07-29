package ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure;

public class ElementWithFormula extends GraphElement {
	private String dtxFormula;
	private String formula;
	

	public ElementWithFormula (
			String id,
			String label,
			String actor,
			String notes,
			String description,
			String formula,
			String dtxFormula) {
		super(id, label, actor, notes, description);
		this.formula = formula;
		this.dtxFormula = dtxFormula;
	}
	
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

	
}
