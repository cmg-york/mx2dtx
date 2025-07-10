package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.Iterator;

import cmg.gReason.inputs.drawio.graphelementstructure.CrossRunSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Effect;
import cmg.gReason.inputs.drawio.graphelementstructure.EffectGroup;
import cmg.gReason.inputs.drawio.graphelementstructure.ElementWithFormula;
import cmg.gReason.inputs.drawio.graphelementstructure.ExportedSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Goal;
import cmg.gReason.inputs.drawio.graphelementstructure.GraphElement;
import cmg.gReason.inputs.drawio.graphelementstructure.InitializationSet;
import cmg.gReason.inputs.drawio.graphelementstructure.Link;
import cmg.gReason.inputs.drawio.graphelementstructure.Precondition;
import cmg.gReason.inputs.drawio.graphelementstructure.Quality;
import cmg.gReason.inputs.drawio.graphelementstructure.Task;

import cmg.gReason.outputs.common.ErrorReporter;


/**
 * 
 * A goal model object containing a tree of {@linkplain GMNode} objects.
 * @author Sotirios Liaskos
 * TODO: The dependency to graphelementstructure must be removed.
 */
public class GoalModel {

	private ErrorReporter err = null;

	private Validator validator = null;

	private IdentifierRegistry identifiers;
	private ConditionExpressionParser parser;

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
	}

	public void buildIdentifierRegistry() {
		identifiers.build();
	}

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


//	private GMNode handleRelEndPoint(GraphElement e) {
//
//		GMNode gNode;
//
//		if (e instanceof Goal) {
//			gNode = new GMGoal();
//			((GMGoal) gNode).setRuns(((Goal) e).getRunNum());
//			((GMGoal) gNode).setRoot(((Goal) e).isRoot());
//		} else if (e instanceof Task) {
//			gNode = new GMTask();
//		} else if (e instanceof Quality) {
//			gNode = new GMQuality();
//			((WithFormula) gNode).setDtxFormula(((Quality) e).getDtxFormula());
//			((WithFormula) gNode).setFormula(((Quality) e).getFormula());
//			((GMQuality) gNode).setQRoot(((Quality) e).isQRoot());
//		} else if (e instanceof EffectGroup) {
//			gNode = new GMEffectGroup();
//		} else if (e instanceof Effect) {
//			gNode = new GMEffect();
//			((GMEffect) gNode).setEffectStatus(((Effect) e).getStatus());
//			gNode.setLabel(e.getLabel());
//			//GMEffect will parse the string in element
//			((GMEffect) gNode).setTurnsTrue(((Effect) e).getTurnsTrue());
//			((GMEffect) gNode).setTurnsFalse(((Effect) e).getTurnsFalse());
//			identifiers.addEffectPredicates(((GMEffect) gNode).getTruePredicates());
//			identifiers.addEffectPredicates(((GMEffect) gNode).getFalsePredicates());
//			identifiers.addEffectVariables(((GMEffect) gNode).getVariables());
//
//		} else if (e instanceof Precondition) {
//			gNode = new GMPrecondition();
//			gNode.setLabel(e.getLabel());
//			//What formula returns has been taken care of.
//			String preFormula = ((Precondition) e).getFormula();
//			//Parse the formula for the purpose of populating the identifiers
//			parser.parse(preFormula);
//			if (((GMPrecondition) gNode).hasFormula()) {
//				identifiers.addIdentifier(e.getLabel(), "preconditionID");
//				System.out.println(e.getLabel() + "HAS a formula");
//			} else {
//				System.out.println(e.getLabel() + "DOES NOT HAVE formula??");
//			}
//
//		} else {
//			err.addError("Unexpected relationship endpoint type: " + e.getClass().getSimpleName() + " (" + e.getLabel() + ")", "GoalModel::handleRelEndPoint()");
//			err.printAll();
//			//System.exit(-1);
//			gNode = null;
//		}
//
//		gNode.setId(e.getID());
//		gNode.setLabel(e.getLabel());
//		//TODO: Notes need to be added.
//
//		return (gNode);
//	}


	public GMNode GMNodeFactory(GraphElement e) {
		GMNode gNode;
		
		// A first pass
		if (e instanceof Goal) {
			gNode = new GMGoal(); 
			((GMGoal) gNode).setRuns(((Goal) e).getRunNum());
			((GMGoal) gNode).setRoot(((Goal) e).isRoot());
		} else if (e instanceof Task) {
			gNode = new GMTask();
		} else if (e instanceof Quality) {
			gNode = new GMQuality();
			((GMQuality) gNode).setQRoot(((Quality) e).isQRoot());
		} else if (e instanceof EffectGroup) {
			gNode = new GMEffectGroup();
		} else if (e instanceof Effect) {
			gNode = new GMEffect();
			((GMEffect) gNode).setEffectStatus(((Effect) e).getStatus());
			//This is a clean camel label
			gNode.setLabel(e.getLabel());
			//In both statements below setTurnsX will parse the string into 
			//arraylists or hashmaps.
			//If empty, getTruePredicates uses the label.
			((GMEffect) gNode).setTurnsTrue(((Effect) e).getTurnsTrue());
			((GMEffect) gNode).setTurnsFalse(((Effect) e).getTurnsFalse());
			identifiers.addEffectPredicates(((GMEffect) gNode).getTruePredicates());
			identifiers.addEffectPredicates(((GMEffect) gNode).getFalsePredicates());
			identifiers.addEffectVariables(((GMEffect) gNode).getVariables());

		} else if (e instanceof Precondition) {
			gNode = new GMPrecondition();
		} else if (e instanceof InitializationSet) {
			gNode = new GMInitializationSet();
		} else if (e instanceof ExportedSet) {
			gNode = new GMExportedSet();
		} else if (e instanceof CrossRunSet) {
			gNode = new GMCrossRunSet();
		} else if (e instanceof Link) {
			switch (((Link) e).getType()) {
			case "orDecomp":
				gNode = new ORDecomp();
				break;
			case "andDecomp":
				gNode = new ANDDecomp();
				break;
			case "precedenceLink":
				gNode = new PRELink();
				break;
			case "negPrecedenceLink":
				gNode = new NPRLink();
				break;
			case "contributionLink":
				gNode = new Contribution();
				break;
			case "effectLink":
				gNode = new EffLink();
				break;
			case "effectGroupLink":
				gNode = new EffGroupLink();
				break;
			default:
				gNode = null;
				err.addError("Unrecognized GraphElement relationship type: " + e.getClass().getSimpleName() + "," + ((Link) e).getType(), "GoalModel::GMNodeFactory");
				break;
			}

		} else {
			gNode = null;
			err.addError("Unrecognized GraphElement type: " + e.getClass().getSimpleName(), "GoalModel::GMNodeFactory");
		}
		
		
		//PASS 2: Objects have been created. Additional considerations
		
		gNode.setId(e.getID());
		gNode.setLabel(e.getLabel());//NODE: labels have been cleaned an camelized.
		gNode.setDescription(e.getDescription());
		
		//Quality, Precondition, Unlinked boxes
		if (e instanceof ElementWithFormula) {
			((WithFormula) gNode).setFormula(((ElementWithFormula) e).getFormula());
			((WithFormula) gNode).setDtxFormula(((ElementWithFormula) e).getDtxFormula());
		}
		
		if (e instanceof Precondition) {
			String preFormula;
			if (((WithFormula) gNode).hasFormula()) {
				//It is a named formula
				preFormula = ((WithFormula) gNode).getFormula();
				//Note: the label below has been cleaned and camelized at reading
				identifiers.addIdentifier(e.getLabel(), "preconditionID");
				identifiers.addEffectPredicate(((WithFormula) gNode).getLabel());
			} else {
				//The label is the formula
				preFormula = ((WithFormula) gNode).getLabel();
			}
			((GMPrecondition) gNode).setDtxFormula(parser.parse(preFormula));
		}
		
		if (e instanceof Effect) {
			if (!( (GMEffect) gNode).useLabel()) {
				err.addWarning("Effect labeled '" + gNode.getDescription() + "' has non-empty custom properties. "
						+ "Visible label '" + gNode.getDescription() + "' cannot be used as identifier. Refer to the identifiers in turnsTrue/turnsFalse instead.", "GoalModel::GMNodeFactory");
			}
		}
		
		return(gNode);
	}


//	/**
//	 * Reads the list of {@linkplain GraphElement} objects (which are primarily relationships) and generates a goal model based on {@linkplain GMNode} objects.
//	 * @throws Exception in a variety of situations in which the goal model is invalid.
//	 */
//	public void createGoalGraph() {
//
//		for (GraphElement c:elements) {
//
//			GMNode gmn = GMNodeFactory(c); 
//			gmn.setId(c.getID());
//			gmn.setLabel(c.getLabel());
//
//			if (c instanceof Link) {
//
//				GMNode gmnOrigin;
//				GMNode gmnTarget;
//
//				System.out.println("Handling link: " + c.getLabel());
//
//				//Handle origin and destination
//				String strOrigin = ((Link) c).getSource();
//				System.out.println("Link origin is: " + strOrigin);
//
//				gmnOrigin = findNodeByID(strOrigin);
//				if (gmnOrigin == null) {
//					GraphElement elOrigin = findElementByID(strOrigin);
//					System.out.println("Handling origin endpoint: " + elOrigin.getLabel());
//					gmnOrigin = handleRelEndPoint(elOrigin);
//					goalModel.add(gmnOrigin);
//				}
//
//				String strTarget = ((Link) c).getTarget();
//				gmnTarget = findNodeByID(strTarget);
//				if (gmnTarget == null) {
//					GraphElement elTarget = findElementByID(strTarget);
//					gmnTarget = handleRelEndPoint(elTarget);
//					goalModel.add(gmnTarget);
//				}
//
//				//gmn, gmnOrigin, gmnTarget
//				validator.validateRelationship(gmnOrigin, gmnTarget, gmn);
//
//				if (err.hasErrors()) {
//					err.printAll();
//					//System.exit(-1);
//				}
//
//
//				if (gmn instanceof ORDecomp) {
//					((GMGoal) gmnTarget).addORChild(gmnOrigin);
//					((WithParent) gmnOrigin).setParent((WithParent) gmnTarget);
//				} else if (gmn instanceof ANDDecomp) {
//					((GMGoal) gmnTarget).addANDChild(gmnOrigin);
//					((WithParent) gmnOrigin).setParent((WithParent) gmnTarget);
//				} else if (gmn instanceof PRELink) {
//					gmnOrigin.addOutPrecedence(gmnTarget);
//					gmnTarget.addInPrecedence(gmnOrigin);
//				} else if (gmn instanceof NPRLink) {
//					gmnOrigin.addOutNegPrecedence(gmnTarget);
//					gmnTarget.addInNegPrecedence(gmnOrigin);
//				} else if (gmn instanceof Contribution) {
//					((Contribution) gmn).setContributionOrigin(gmnOrigin);
//					((Contribution) gmn).setContributionOrigin(gmnTarget);
//					((Contribution) gmn).setContributionWeight(Float.parseFloat(gmn.getLabel()));
//					gmnOrigin.addOutContribution(gmn);
//					((GMQuality) gmnTarget).addInContribution(gmn);
//				} else if (gmn instanceof EffLink) {
//					((GMTask) gmnOrigin).setOutEffectLink(gmnTarget); //gmnTarget is an effect group
//					((GMEffectGroup) gmnTarget).setInEffectLink(gmnOrigin); //gmnOrigin is a task
//				} else if (gmn instanceof EffGroupLink) {
//					((GMEffectGroup) gmnOrigin).addEffect(gmnTarget);
//					((GMEffect) gmnTarget).setParent((GMEffectGroup) gmnOrigin);
//					((GMEffect) gmnTarget).setInWeight(Float.parseFloat(gmn.getLabel()));
//				}
//			} //e is Link - gmn will be discarded, LINKS ARE NOT ADDED TO THE MODEL
//
//			else if (c instanceof InitializationSet) {
//				((WithFormula) gmn).setFormula(((InitializationSet) c).getFormula());
//				((WithFormula) gmn).setDtxFormula(((InitializationSet) c).getDtxFormula());
//				goalModel.add(gmn);
//			} else if (c instanceof CrossRunSet) {
//				((WithFormula) gmn).setFormula(((CrossRunSet) c).getFormula());
//				((WithFormula) gmn).setDtxFormula(((CrossRunSet) c).getDtxFormula());
//				goalModel.add(gmn);
//			} else if (c instanceof ExportedSet) {
//				((WithFormula) gmn).setFormula(((ExportedSet) c).getFormula());
//				((WithFormula) gmn).setDtxFormula(((ExportedSet) c).getDtxFormula());
//				goalModel.add(gmn);
//			} else if (c instanceof Precondition) {
//				((WithFormula) gmn).setFormula(((Precondition) c).getFormula());
//				((WithFormula) gmn).setDtxFormula(((Precondition) c).getDtxFormula());
//				goalModel.add(gmn);
//				System.out.println(c.getLabel() + " ADDED to the GMNodes as standalone as " + gmn.getClass().getSimpleName());
//			}
//
//		}//loop over elements in GraphElement Graph
//
//		//Bypass effectGroups
//		byPassEffectGroups();
//
//		findRoot();
//
//		//Build the identifiers registry 
//		buildIdentifierRegistry();
//
//		//Model now complete - perform remaining validation checks
//		validator.generalValidation();
//		validator.predicateValidation();
//
//		if (err.hasErrors()) {
//			err.printAll();
//			//System.exit(-1);
//		}
//
//
//	}


	public void createGoalGraph() {

		//PASS 1: Add all non-relationship elements first
		for (GraphElement c:elements) {
			if (!(c instanceof Link)) {
				GMNode gmn = GMNodeFactory(c); 
				goalModel.add(gmn);
			}
		} 


		//PASS 2: Add relationships now 
		for (GraphElement c:elements) {
			if ((c instanceof Link)) {
				GMNode gmn = GMNodeFactory(c); 
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
		buildIdentifierRegistry();

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
					System.out.println("Quality " + n.getCamelLabel() + " is declared root.");
				} else {
					System.out.println("Quality " + n.getCamelLabel() + " is not declared root.");
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

	public void addElement(GraphElement e) {
		elements.add(e);
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
