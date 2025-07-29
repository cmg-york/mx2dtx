package ca.yorku.cmg.istardt.reader.inputs.drawio.graphelementstructure;


/**
 * 
 * A draw.io graph element. Can be entity (unary concept) or relationship (binary concept).
 * 
 * @author Sotirios Liaskos
 *
 */
public class GraphElement {

	// All elements
	private String id;
	private String label;
	private String actor;
	private String notes;
	private String description;
	
	/**
	 * 
	 * Create a GraphElement with id, type, label and category. Reserved for entities (unary concepts)
	 * @param id A string uniquely identifying the element in the diagram.
	 * @param label The label of the shape.
	 * @param actor The actor in which the element is found.
	 * @param notes Any notes added by the modelers
	 */
	public GraphElement(
			String id,
			String label,
			String actor,
			String notes,
			String description) {
		this.id = id;
		this.label = label;
		this.actor = actor;
		this.notes = notes;
		this.description = description;
	}
	
	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
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
     * Get the label of the GraphElement.
     *
     * @return The label of the GraphElement.
     */
	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

}
