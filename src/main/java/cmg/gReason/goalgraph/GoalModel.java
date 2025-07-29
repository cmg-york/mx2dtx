package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.Iterator;

import cmg.gReason.inputs.drawio.graphelementstructure.Effect;
import cmg.gReason.inputs.drawio.graphelementstructure.GraphElement;
import cmg.gReason.inputs.drawio.graphelementstructure.Link;
import cmg.gReason.outputs.common.ErrorReporter;


/**
 * A goal model object containing a tree of {@linkplain GMNode} objects.
 * @author Sotirios Liaskos
 */
public class GoalModel {

	private ErrorReporter err = null;
	private Validator validator = null;
	private IdentifierRegistry identifiers;
	private ConditionExpressionParser parser;
	private GMNodeFactory gmF;
	
	ArrayList<GraphElement> elements = new ArrayList<GraphElement>();
	ArrayList<GMNode> goalModel = new ArrayList<GMNode>();

	private GMGoal root;
	private GMQuality qroot;

	public GoalModel(ErrorReporter e) {
		this.err = e;
		identifiers = new IdentifierRegistry(this,e);
		parser = new ConditionExpressionParser(identifiers);
		identifiers.setParser(parser);
		validator = new Validator(identifiers, e, this);
		gmF = new GMNodeFactory(e,identifiers,parser);
	}


	
	/**
	 * Adds a {@linkplain GraphElement} object (a diagrammatic element) to the list thereof. 
	 * @param e The {@linkplain GraphElement} to be added.
	 */
	public void addElement(GraphElement e) {
		elements.add(e);
	}

	/**
	 * Generates a list of interconnected {@linkplain GMNode} objects based on the list of {@linkplain GraphElement} objects.
	 * It presumes that all relevant {@linkplain GraphElement} objects of the diagrams have been 
	 * added through a series of {@linkplain GoalModel#addElement(GraphElement)} calls.    
	 */
	public void createGoalGraph() {

		//PASS 1: Add all non-relationship elements first
		for (GraphElement c:elements) {
			if (!(c instanceof Link)) {
				GMNode gmn = gmF.createGMNodeFromGraphElement(c); 
				goalModel.add(gmn);
			}
		} 


		//PASS 2: Add relationships now 
		for (GraphElement c:elements) {
			if ((c instanceof Link)) {
				GMNode gmn = gmF.createGMNodeFromGraphElement(c); 
				gmn.setId(c.getID());
				gmn.setLabel(c.getLabel());
				gmn.setDescription(c.getDescription());

				GMNode gmnOrigin;
				GMNode gmnTarget;

				//Handle origin and destination
				String strOrigin = ((Link) c).getSource();

				gmnOrigin = findNodeByID(strOrigin);
				if (gmnOrigin == null) {
					err.addError("Cannot find origin of link " + c.getLabel() , "GoalModel:createGoalGraph()");
				}

				String strTarget = ((Link) c).getTarget();
				gmnTarget = findNodeByID(strTarget);
				if (gmnTarget == null) {
					err.addError("Cannot find target of link " + c.getLabel() , "GoalModel:createGoalGraph()");
				}

				//gmn, gmnOrigin, gmnTarget
				validator.validateRelationship(gmnOrigin, gmnTarget, gmn);

				if (err.hasErrors()) {
					err.printAll();
					//System.exit(-1);
				}

				//Proceed to populate other fields
				if (gmn instanceof ORDecomp) {
					((GMGoal) gmnTarget).addORChild(gmnOrigin);
					((WithParent) gmnOrigin).setParent((WithParent) gmnTarget);
				} else if (gmn instanceof ANDDecomp) {
					((GMGoal) gmnTarget).addANDChild(gmnOrigin);
					((WithParent) gmnOrigin).setParent((WithParent) gmnTarget);
				} else if (gmn instanceof PRELink) {
					gmnOrigin.addOutPrecedence(gmnTarget);
					gmnTarget.addInPrecedence(gmnOrigin);
				} else if (gmn instanceof NPRLink) {
					gmnOrigin.addOutNegPrecedence(gmnTarget);
					gmnTarget.addInNegPrecedence(gmnOrigin);
				} else if (gmn instanceof Contribution) {
					((Contribution) gmn).setContributionOrigin(gmnOrigin);
					((Contribution) gmn).setContributionTarget(gmnTarget);
					((Contribution) gmn).setContributionWeight(Float.parseFloat(gmn.getLabel()));
					gmnOrigin.addOutContribution(gmn);
					((GMQuality) gmnTarget).addInContribution(gmn);
				} else if (gmn instanceof EffLink) {
					((GMTask) gmnOrigin).setOutEffectLink(gmnTarget); //gmnTarget is an effect group
					((GMEffectGroup) gmnTarget).setInEffectLink(gmnOrigin); //gmnOrigin is a task
				} else if (gmn instanceof EffGroupLink) {
					((GMEffectGroup) gmnOrigin).addEffect(gmnTarget);
					((GMEffect) gmnTarget).setParent((GMEffectGroup) gmnOrigin);
					((GMEffect) gmnTarget).setInWeight(Float.parseFloat(gmn.getLabel()));
				}
			}//Instance of link			
		} //Pass 2: loop
		
		//Bypass effectGroups
		byPassEffectGroups();

		findRoot();

		//Build the identifiers registry 
		identifiers.build();
		//buildIdentifierRegistry();

		//Model now complete - perform remaining validation checks
		validator.generalValidation();
		validator.predicateValidation();

		if (err.hasErrors()) {
			err.printAll();
			//System.exit(-1);
		}
		
	}



	/**
	 * Removes the effect group element, such that, in the end, tasks are directly connected to the effects. 
	 */
	private void byPassEffectGroups() {
		Iterator<GMNode> itr = goalModel.iterator();

		while (itr.hasNext()) {
			GMNode n = itr.next();
			if (n instanceof GMEffectGroup) {
				((GMTask) ((GMEffectGroup) n).getInEffectLink()).setEffects(((GMEffectGroup) n).getEffects());
				for (GMNode o:((GMEffectGroup) n).getEffects())
					((WithParent) o).setParent((WithParent) ((GMEffectGroup) n).getInEffectLink());
				itr.remove();
			}
		}
	}


	/**
	 * Find and set the roots of the goal decomposition and quality subgraphs.  
	 * @throws Exception if multiple roots are found.
	 */
	private void findRoot() {
		ArrayList<GMGoal> declaredRoots = new ArrayList<GMGoal>();
		ArrayList<GMQuality> declaredQRoots = new ArrayList<GMQuality>();

		ArrayList<GMGoal> foundRoots = new ArrayList<GMGoal>();
		ArrayList<GMQuality> foundQRoots = new ArrayList<GMQuality>();

		boolean hasFormula = false;

		for (GMNode n:goalModel) {
			if ((n instanceof GMGoal)) {
				if (((WithParent) n).getParent()==null) {
					foundRoots.add((GMGoal) n);
				}
				if (((GMGoal) n).isRoot()) {
					declaredRoots.add((GMGoal) n);
				}
			}

			if ((n instanceof GMQuality)) {
				if (n.getOutgoingContributions().isEmpty()) {
					foundQRoots.add((GMQuality) n);
				}
				if (((GMQuality) n).isQRoot()) {
					declaredQRoots.add((GMQuality) n);
				} else {
				}
				if ((((WithFormula) n).hasDtxFormula() || ((WithFormula) n).hasFormula())) {
					hasFormula = true;
				}
			}
		}


		/*
		 * Root Hard Goal
		 */

		this.root = null;

		if (declaredRoots.size() == 0) {
			if (foundRoots.size() == 0) {
				err.addError("No root goal specified or found, 1 expected. " , "GoalModel::findRoot()");	
			}
			if (foundRoots.size() == 1) {
				err.addWarning("No root goal explicitly specified, one inferred (" + foundRoots.get(0).getLabel() + ") and will be adopted.", "GoalModel::findRoot()");
				this.root = (GMGoal) foundRoots.get(0);
			}
			if (foundRoots.size() > 1) {
				err.addError("No root goal explicitly specified, too many inferred (" + foundRoots.toString() + ").", "GoalModel::findRoot()");	
			}
		}

		if (declaredRoots.size() == 1) {
			this.root = (GMGoal) declaredRoots.get(0);

			if (foundRoots.size() == 0) {
				err.addWarning("Neither specified root goal (" + declaredRoots.get(0).getLabel() + ") nor other goals are parentless." , "GoalModel::findRoot()");	
			}
			if (foundRoots.size() == 1) {
				if (!declaredRoots.get(0).equals(foundRoots.get(0))) {
					err.addWarning("Specified root goal (" + declaredRoots.get(0).getLabel() + ") does not match inferred root goal: " + foundRoots.get(0), "GoalModel::findRoot()");	
				}
			}
			if (foundRoots.size() > 1) {
				if (!foundRoots.contains(declaredRoots.get(0))) {
					err.addWarning("Specified root goal (" + declaredRoots.get(0).getLabel() + ") does not match any of the inferred root goal(s): " + foundRoots, "GoalModel::findRoot()");	
				}
			}
		}

		if (declaredRoots.size() > 1) {
			err.addWarning("Too many root goals specified (" + declaredRoots.toString() + ") choose only one." , "GoalModel::findRoot()");
		}



		/*
		 * Root Quality Goal
		 */
		this.qroot = null;

		if (declaredQRoots.size() == 0) {
			if (foundQRoots.size() == 0) {
				err.addError("No root quality specified or found, 1 expected. " , "GoalModel::findRoot()");	
			}
			if (foundQRoots.size() == 1) {
				if (hasFormula) {
					err.addError("No root quality explicitly specified, one inferred (" + foundQRoots.get(0).getLabel() + ") but formulae have been detected. Please explicilty specify root quality goal.", "GoalModel::findRoot()");
				} else {
					err.addWarning("No root quality explicitly specified, one inferred (" + foundQRoots.get(0).getLabel() + ") and adopted.", "GoalModel::findRoot()");
					this.qroot = (GMQuality) foundQRoots.get(0);	
				}
			}
			if (foundQRoots.size() > 1) {
				err.addError("No root quality explicitly specified, too many inferred (" + foundQRoots.toString() + ").", "GoalModel::findRoot()");	
			}
		}

		if (declaredQRoots.size() == 1) {
			this.qroot = (GMQuality) declaredQRoots.get(0);

			if ((foundQRoots.size() == 0) && !hasFormula) {
				err.addWarning("Neither specified root quality (" + declaredQRoots.get(0).getLabel() + ") nor other qualities are parentless." , "GoalModel::findRoot()");	
			}
			if (foundQRoots.size() == 1) {
				if (!declaredQRoots.get(0).equals(foundQRoots.get(0))) {
					err.addWarning("Specified root quality (" + declaredQRoots.get(0).getLabel() + ") does not match inferred root quality: " + foundQRoots.get(0), "GoalModel::findRoot()");	
				}
			}
			if (foundQRoots.size() > 1) {
				if (!foundQRoots.contains(declaredQRoots.get(0))) {
					err.addWarning("Specified root quality (" + declaredQRoots.get(0).getLabel() + ") does not match any of the inferred root qualities: " + foundQRoots, "GoalModel::findRoot()");	
				}
			}
		}

		if (declaredQRoots.size() > 1) {
			err.addError("Too many root qualities specified (" + declaredQRoots.toString() + ") choose only one." , "GoalModel::findRoot()");
		}

		if (err.hasErrors()) {
			err.printAll();
			System.exit(-1);
		}

	}



	/**
	 * 
	 * 
	 * M I S C   G E T T E R S,   S E T T E R S,   A N D   H E L P E R S 
	 *  
	 * 
	 */


	public GMNode findNodeByID(String ID) {
		GMNode n = null;
		for (GMNode m:goalModel) {
			if (m.getId().equals(ID))
				n = m; 
		}
		return (n);
	}

	public GraphElement findElementByID(String ID) {
		GraphElement n = null;
		for (GraphElement m:elements) {
			if (m.getID().equals(ID))
				n = m; 
		}
		return (n);
	}

	
	// Registry access
	public ArrayList<String> getPredicates() {
		return identifiers.getPredicates();
	}

	public ArrayList<String> getVariables() {
		return identifiers.getVariables();
	}

	public String getIdentifierType(String identifier) {
		return identifiers.getIdentifierType(identifier);
	}

	public IdentifierRegistry getIdentifierRegistry() {
		return identifiers;
	}
	
	


	public ArrayList<GMNode> getGoalModel() {
		return goalModel;
	}


	/**
	 * Returns the root of the qualities graph.
	 * @return The root of the qualities graph.
	 */
	public GMNode getQRoot() {
		return qroot;
	}

	/**
	 * Returns the root of the hardgoal graph.
	 * @return The root of the hardgoal graph.
	 */
	public GMNode getRoot() {
		return root;
	}


	/**
	 * 
	 * 
	 * D E B U G I N G 
	 *  
	 * 
	 */


	public void debugPrintGraphElementList() {
		for (GraphElement c : elements) {
			System.out.println("-----");
			System.out.println("Object Id  : " + c.getID());
			System.out.println("Type       : " + c.getClass().getSimpleName());
			System.out.println("Label      : " + c.getLabel());
			if (c instanceof Link) {
				System.out.println("Rel. Type  : " + ((Link) c).getType());
				System.out.println("Origin     : " + ((Link) c).getSource());
				System.out.println("Target     : " + ((Link) c).getTarget());
			}
			if (c instanceof Effect) {
				System.out.println("Status     : " + ((Effect) c).getStatus());
			}
			System.out.println();
		}
	}

	public void debugGoalModelFlat() {
		for (GMNode c:goalModel) {
			System.out.println(c.getLabel() + " (" + c.getClass().getSimpleName() + ") ID:" + c.getId());
			System.out.println(" --> AND Children: " + GMNode.debugPrintList(((GMGoal) c).getANDChildren(),","));
			System.out.println(" --> OR Children: " + GMNode.debugPrintList(((GMGoal) c).getORChildren(),","));
			System.out.println(" --> Effects: " + GMNode.debugPrintList(((GMTask) c).getEffects(),","));
			System.out.println(" --> InPrecedences: " + GMNode.debugPrintList(c.getIncomingPrecedences(),","));
			System.out.println(" --> OutPrecedences: " + GMNode.debugPrintList(c.getOutgoingPrecedences(),","));
			System.out.println(" --> InContributions: " + GMNode.debugPrintList(((GMQuality) c).getIncompingContributions(),","));
			System.out.println(" --> OutContributions: " + GMNode.debugPrintList(c.getOutgoingContributions(),","));
			System.out.println(" --> InWeight: " + Float.toString(((GMEffect) c).getInWeight()));
			System.out.println(" --> Status: " + ((GMEffect) c).getEffectStatus());
			if (((WithParent) c).getParent()!= null)
				System.out.println(" --> Parent: " + ((WithParent) c).getParent().getLabel());
			else 
				System.out.println(" --> Parent: none");
		}
	}

}
