package cmg.gReason.goalgraph;

public class WithFormula extends GMNode {
	protected String formula;
	protected String dtxFormula;
	
	public boolean hasFormula() {
		return( !(formula.equals("")) && !(formula == null) );
	}
	
	public boolean hasDtxFormula() {
		return( !(dtxFormula.equals("")) && !(dtxFormula == null) );
	}	

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getDtxFormula() {
		return dtxFormula;
	}

	public void setDtxFormula(String dtxFormula) {
		this.dtxFormula = dtxFormula;
	}
}
