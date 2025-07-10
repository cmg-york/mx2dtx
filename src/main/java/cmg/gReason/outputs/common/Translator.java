package cmg.gReason.outputs.common;

import cmg.gReason.goalgraph.GoalModel;
import cmg.gReason.inputs.drawio.ConditionExpressionParser;

public abstract class Translator {
	protected OutputManager outputManager;
	protected GoalModel g;
	protected ConditionExpressionParser parser;
	
	/**
	 * Produces the translation in string format and 
	 * sets it to a variable accessible through getSpec(). 
	 * This is the starting point of your custom translator.
	 */
	protected abstract void produceTranslation(boolean timeStamp);

	protected void produceTranslation() {
		this.produceTranslation(true);
	}

	
	
	/**
	 * Gets the specification string produced by produceTranslation
	 * @return
	 */
	public abstract String getSpec();
	
	
	public Translator() {
		outputManager = new OutputManager();
		outputManager.setTranslator(this);
	}
	
	public void setOutFile(String outfile) {
		outputManager.setOutFile(outfile);
	}

	public void setModel(GoalModel model) {
		this.g = model;
		parser = new ConditionExpressionParser(this.g.getIdentifierRegistry());
	}
	
	public GoalModel getModel() {
		return(this.g);
	}
	
	
	
	/**
	 * Translates a {@linkplain GoalModel} object set using {@linkplain Translator#setModel(GoalModel)} and 
	 * saves it to the output file specified via {@linkplain Translator#setOutFile(String)}
	 * If output file has not been specified, prints output to screen. 
	 * @throws Exception If the {@linkplain GoalModel} object has not been set.
	 */
	public void translate(boolean timeStamp) throws Exception {
		
		if (g == null)
			throw new Exception("translate: GoalModel object is null");
		
		System.out.println("Translating...");
		
		produceTranslation(timeStamp);
		
		System.out.println("Outputing...");
		if (outputManager.outFileEmpty()) {
			System.out.println(getSpec());
		} else {
			outputManager.saveOutput();
		}
	}
	
	public void translate() throws Exception {
		this.translate(true);
	}
	
}
