package cmg.gReason.outputs.common;

import cmg.gReason.goalgraph.GoalModel;

public abstract class Translator {
	protected OutputManager outputManager;
	protected GoalModel g;
	
	/**
	 * Produces the translation in string format and 
	 * sets it to a variable accessible through getSpec() 
	 */
	protected abstract void produceTranslation();
	
	
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
	}
	
	/**
	 * Translates a {@linkplain GoalModel} object set using {@linkplain Translator#setModel(GoalModel)} and 
	 * saves it to the output file specified via {@linkplain Translator#setOutFile(String)}
	 * If output file has not been specified, prints output to screen. 
	 * @throws Exception If the {@linkplain GoalModel} object has not been set.
	 */
	public void translate() throws Exception {
		
		if (g == null)
			throw new Exception("translate: GoalModel object is null");
		
		System.out.println("Translating...");
		
		//t = new dtxTranslator(m);
		//t.translate();
		
		produceTranslation();
		
		System.out.println("Outputing...");
		if (outputManager.outFileEmpty()) {
			System.out.println(getSpec());
		} else {
			outputManager.saveOutput();
		}
	}
	
	
}
