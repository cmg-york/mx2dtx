package cmg.gReason.inputs.drawio;


/**
 * 
 * A draw.io graph element. Can be entity (unary concept) or relationship (binary concept).
 * 
 * @author Sotirios Liaskos
 *
 */
public class GraphElementOLD {

	// All elements
	private String id;
	private String category;
	private String type;
	private String label;
	private String actor;
	private String formula;
	private String dtxFormula;
	private String notes;
	private String runNum;
	
	public String getRunNum() {
		return runNum;
	}

	public void setRunNum(String runNum) {
		this.runNum = runNum;
	}

	//Relationships only
	private String originID;
	private String targetID;
	
	//Effects only
	private String status;
	private boolean setRoot;
	
	
	/**
	 * 
	 * Create a GraphElement with id, type, label and category. Reserved for entities (unary concepts)
	 * @param id A string uniquely identifying the element in the diagram.
	 * @param type The type of concept represented.
	 * @param label The label of the shape.
	 * @param category Whether it is an "entity" or a "relationship"
	 */
	public GraphElementOLD(
			String id, 
			String type, 
			String label, 
			String category, 
			String actor,
			String formula,
			String dtxFormula,
			String notes) {
		this.id = id;
		this.type = type;
		this.label = label;
		this.category = category;
		this.actor = actor;
		this.formula = formula;
		this.dtxFormula = dtxFormula;
		this.notes = notes;
	}
	
	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
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

	/**
	 * 
	 * Create a GraphElement with id, type, label and category. Reserved for entities (unary concepts)
	 * @param id A string uniquely identifying the element in the diagram.
	 * @param type The type of concept represented.
	 * @param label The label of the shape.
	 * @param category Whether it is an "entity" or a "relationship"
	 */
	public GraphElementOLD(
			String id, 
			String type, 
			String label, 
			String category) {
		this.id = id;
		this.type = type;
		this.label = label;
		this.category = category;
		this.actor = "default";
	}
	
	
	/**
	 * Create a GraphElement with id, type, label, category as well as pointers to an origin and a destination GraphElement. 
	 * Reserved for relationships (binary concepts).
	 * @param id A string uniquely identifying the element in the diagram.
	 * @param type The type of concept represented.
	 * @param label The label of the shape.
	 * @param category Whether it is an "entity" or a "relationship".
	 * @param originID The id of the origin of the link.
	 * @param targetID The id of the destination of the link.
	 */
	public GraphElementOLD(
			String id, 
			String type, 
			String label, 
			String category, 
			String originID, 
			String targetID) {
		this(id, type, label, category);
		this.originID = originID;
		this.targetID = targetID;
	}
	
    /**
     * Get the status of the GraphElement. Reserved for effects.
     *
     * @return The status of the GraphElement. Normally "success" or "failure". 
     */
	public String getStatus() {
		return status;
	}

	 /**
     * Set the status of the GraphElement. Reserved for effects.
     *
     * @param status The status to be set for the GraphElement. Can be "success" or "failure".
     */
	public void setStatus(String status) {
		this.status = status;
	}

    /**
     * Get the unique ID of the GraphElement.
     *
     * @return The unique ID of the GraphElement.
     */
	public String getID() {
		return id;
	}

	 /**
     * Get the type of the GraphElement.
     *
     * @return The type of the GraphElement. Can be any of the concepts of iStar-A.
     */
	public String getType() {
		return type;
	}

    /**
     * Get the label of the GraphElement.
     *
     * @return The label of the GraphElement.
     */
	public String getLabel() {
		return label;
	}

    /**
     * Get the origin ID of the GraphElement (valid for relationships).
     *
     * @return The origin ID of the GraphElement.
     */
	public String getOriginID() {
		return originID;
	}

    /**
     * Set the origin ID of the GraphElement (valid for relationships).
     *
     * @param originID The origin ID to be set for the GraphElement.
     */
	public void setOriginID(String originID) {
		this.originID = originID;
	}
	

    /**
     * Get the target ID of the GraphElement (valid for relationships).
     *
     * @return The target ID of the GraphElement.
     */
	public String getTargetID() {
		return targetID;
	}

    /**
     * Set the target ID of the GraphElement (valid for relationships).
     *
     * @param targetID The target ID to be set for the GraphElement.
     */
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

    /**
     * Get the category of the GraphElement (whether it is an "entity" or a "relationship").
     *
     * @return The category of the GraphElement.
     */
	public String getCategory() {
		return this.category;
	}

	public void setQRoot(boolean b) {
		this.setRoot = b;
	}
	
	public boolean isQRoot() {
		return (this.setRoot);
		
	}
}
