package cmg.gReason.goalgraph;

import java.util.ArrayList;
import java.util.Iterator;

import cmg.gReason.inputs.drawio.graphelementstructure.*;
import cmg.gReason.outputs.common.ErrorReporter;


/**
 * 
 * A goal model object containing a tree of {@linkplain GMNode} objects.
 * @author Sotirios Liaskos
 *
 */
public class GoalModel {
	private ErrorReporter err = null;
	
	ArrayList<GraphElement> elements = new ArrayList<GraphElement>();
	ArrayList<GMNode> goalModel = new ArrayList<GMNode>();

	private GMNode root;
	private GMNode qroot;
	
	
	public GoalModel(ErrorReporter e) {
		this.err = e;
	}
	
	private GMNode handleRelEndPoint(GraphElement e) {
		
		//TODO: Remove new GMNode
		//TODO: Remove the setType
		
		GMNode gNode = new GMNode();

		if (e instanceof Goal) {
			gNode = new GMGoal();
			gNode.setRuns(((Goal) e).getRunNum());
			((GMGoal) gNode).setRoot(((Goal) e).isRoot());
			gNode.setType("goal");
		} else if (e instanceof Task) {
			gNode = new GMTask();
			gNode.setType("task");
		} else if (e instanceof Quality) {
			gNode = new GMQuality();
			gNode.setType("quality");
			gNode.setDtxFormula(((Quality) e).getDtxFormua());
			gNode.setFormula(((Quality) e).getFormula());
			gNode.setQRoot(((Quality) e).isQRoot());
		} else if (e instanceof EffectGroup) {
			gNode = new GMEffectGroup();
			gNode.setType("effectGroup");
		} else if (e instanceof Effect) {
			gNode = new GMEffect();
			gNode.setType("effect");
			gNode.setEffectStatus(((Effect) e).getStatus());
		} else if (e instanceof Precondition) {
			gNode = new GMPrecondition();
			gNode.setType("precondition");
			gNode.setDtxFormula(((Precondition) e).getDtxFormua());
			gNode.setFormula(((Precondition) e).getFormula());
		}
		
		gNode.setId(e.getID());
		gNode.setLabel(e.getLabel());
		//TODO: Notes need to be added.
		
		return (gNode);
	}
	
	
	public GMNode GMNodeFactory(GraphElement e) {
		GMNode gNode;
		if (e instanceof Goal) {
			gNode = new GMGoal(); 
		} else if (e instanceof Task) {
			gNode = new GMTask();
		} else if (e instanceof Quality) {
			gNode = new GMQuality();
		} else if (e instanceof EffectGroup) {
			gNode = new GMEffectGroup();
		} else if (e instanceof Effect) {
			gNode = new GMEffect();
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
				err.addError("Unrecognized GraphElemen relationship type: " + e.getClass().getSimpleName() + "," + ((Link) e).getType(), "GoalModel::GMNodeFactory");
				break;
			}
			
		} else {
			gNode = null;
			err.addError("Unrecognized graph element type: " + e.getClass().getSimpleName(), "GoalModel::GMNodeFactory");
		}
		return(gNode);
	}
	
	/**
	 * Reads the list of {@linkplain GraphElement} objects (which are primarily relationships) and generates a goal model based on {@linkplain GMNode} objects.
	 * @throws Exception in a variety of situations in which the goal model is invalid.
	 */
	public void createGoalGraph() {

		for (GraphElement c:elements) {
			
			GMNode gmn = GMNodeFactory(c); 
			gmn.setId(c.getID());
			gmn.setLabel(c.getLabel());
			
			if (c instanceof Link) {
				//Create the node
				//GMNode gmn = new GMNode();
				GMNode gmnOrigin;
				GMNode gmnTarget;
				


				//Handle origin and destination
				String strOrigin = ((Link) c).getSource();
				gmnOrigin = findNodeByID(strOrigin);
				if (gmnOrigin == null) {
					GraphElement elOrigin = findElementByID(strOrigin);
					gmnOrigin = handleRelEndPoint(elOrigin);
					goalModel.add(gmnOrigin);
				}

				String strTarget = ((Link) c).getTarget();
				gmnTarget = findNodeByID(strTarget);
				if (gmnTarget == null) {
					GraphElement elTarget = findElementByID(strTarget);
					gmnTarget = handleRelEndPoint(elTarget);
					goalModel.add(gmnTarget);
				}
				
				//gmn, gmnOrigin, gmnTarget
				//TODO: add relationship validation code here.
				
				if (gmn instanceof ORDecomp) {
					gmnTarget.addORChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
				} else if (gmn instanceof ANDDecomp) {
					gmnTarget.addANDChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
				} else if (gmn instanceof PRELink) {
					gmnOrigin.addOutPrecedence(gmnTarget);
					gmnTarget.addInPrecedence(gmnOrigin);
				} else if (gmn instanceof NPRLink) {
					gmnOrigin.addOutNegPrecedence(gmnTarget);
					gmnTarget.addInNegPrecedence(gmnOrigin);
				} else if (gmn instanceof Contribution) {
					((Contribution) gmn).setContributionOrigin(gmnOrigin);
					((Contribution) gmn).setContributionOrigin(gmnTarget);
					((Contribution) gmn).setContributionWeight(Float.parseFloat(gmn.getLabel()));
					gmnOrigin.addOutContribution(gmn);
					gmnTarget.addInContribution(gmn);
				} else if (gmn instanceof EffLink) {
					gmnOrigin.setOutEffectLink(gmnTarget); //gmnTarget is an effect group
					gmnTarget.setInEffectLink(gmnOrigin); //gmnOrigin is a task
				} else if (gmn instanceof EffGroupLink) {
					gmnOrigin.addEffect(gmnTarget);
					gmnTarget.setParent(gmnOrigin);
					gmnTarget.setInWeight(Float.parseFloat(gmn.getLabel()));
				}
			} //e is Link - gmn will be discarded, LINKS ARE NOT ADDED TO THE MODEL
			
			else if (c instanceof InitializationSet) {
				gmn.setFormula(((InitializationSet) c).getFormula());
				gmn.setDtxFormula(((InitializationSet) c).getDtxFormula());
				//gmn.setType("initialization");
				goalModel.add(gmn);
			} else if (c instanceof CrossRunSet) {
				gmn.setFormula(((CrossRunSet) c).getFormula());
				gmn.setDtxFormula(((CrossRunSet) c).getDtxFormula());
				//gmn.setType("crossrun");
				goalModel.add(gmn);
			} else if (c instanceof ExportedSet) {
				gmn.setFormula(((ExportedSet) c).getFormula());
				gmn.setDtxFormula(((ExportedSet) c).getDtxFormula());
				//gmn.setType("export");
				goalModel.add(gmn);
			}
				
				/* 
				
				switch(gmn.getType()) {
				case "orDecomp":
					gmnTarget.addORChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "andDecomp":
					gmnTarget.addANDChild(gmnOrigin);
					gmnOrigin.setParent(gmnTarget);
					break;
				case "precedenceLink":
					gmnOrigin.addOutPrecedence(gmnTarget);
					gmnTarget.addInPrecedence(gmnOrigin);
					break;
				case "negPrecedenceLink":
					gmnOrigin.addOutNegPrecedence(gmnTarget);
					gmnTarget.addInNegPrecedence(gmnOrigin);
					break;
				case "contributionLink":
					gmnOrigin.addOutContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					gmnTarget.addInContribution(new Contribution(gmnOrigin,gmnTarget,gmn.getLabel()));
					break;
				case "effectLink":
					gmnOrigin.setOutEffectLink(gmnTarget); //gmnTarget is an effect group
					gmnTarget.setInEffectLink(gmnOrigin); //gmnOrigin is a task
					break;
				case "effectGroupLink":
					gmnOrigin.addEffect(gmnTarget);
					gmnTarget.setParent(gmnOrigin);
					gmnTarget.setInWeight(Float.parseFloat(gmn.getLabel()));
					break;
				}
				*/
			// c is not a link

		}//loop over elements in GraphElement Graph
		
		//Bypass effectGroups
		byPassEffectGroups();
	
		findRoot();
	}

	
	/**
	 * Removes the effect group element, such that, in the end, tasks are directly connected to the effects. 
	 */
	private void byPassEffectGroups() {
		Iterator<GMNode> itr = goalModel.iterator();
		
		while (itr.hasNext()) {
			GMNode n = itr.next();
			if (n instanceof GMEffectGroup) {
				n.getInEffectLink().setEffects(n.getEffects());
				for (GMNode o:n.getEffects())
					o.setParent(n.getInEffectLink());
				itr.remove();
			}
		}
	}
	
	
	/**
	 * Find and set the roots of the goal decomposition and quality subgraphs.  
	 * @throws Exception if multiple roots are found.
	 */
	private void findRoot() {
		ArrayList<GMNode> declaredRoots = new ArrayList<GMNode>();
		ArrayList<GMNode> declaredQRoots = new ArrayList<GMNode>();
		
		ArrayList<GMNode> foundRoots = new ArrayList<GMNode>();
		ArrayList<GMNode> foundQRoots = new ArrayList<GMNode>();
		
		boolean hasFormula = false;
		
		for (GMNode n:goalModel) {
			if ((n instanceof GMGoal)) {
				if (n.getParent()==null) {
					foundRoots.add(n);
				}
				if (((GMGoal) n).isRoot()) {
					declaredRoots.add(n);
				}
			}
			
			if ((n instanceof GMQuality)) {
				if (n.getOutgoingContributions().isEmpty()) {
					foundQRoots.add(n);
				}
				if (((GMGoal) n).isQRoot()) {
					declaredQRoots.add(n);
				}
				if ((n.hasDtxFormula() || n.hasFormula())) {
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
				err.addWarning("No root goal explicitly specified, one inferrred (" + foundRoots.get(0) + ") and will be adopted.", "GoalModel::findRoot()");
				this.root = foundRoots.get(0);
			}
			if (foundRoots.size() > 1) {
				err.addError("No root goal explicitly specified, too many inferrred (" + foundRoots + ").", "GoalModel::findRoot()");	
			}
		}

		if (declaredRoots.size() == 1) {
			this.root = declaredRoots.get(0);
			
			if (foundRoots.size() == 0) {
				err.addWarning("Neither specified root goal (" + declaredRoots.get(0) + ") nor other goals are parentless." , "GoalModel::findRoot()");	
			}
			if (foundRoots.size() == 1) {
				if (!declaredRoots.get(0).equals(foundRoots.get(0))) {
					err.addWarning("Specified root goal (" + declaredRoots.get(0) + ") does not match inferred root goal: " + foundRoots.get(0), "GoalModel::findRoot()");	
				}
			}
			if (foundRoots.size() > 1) {
				if (!foundRoots.contains(declaredRoots.get(0))) {
					err.addWarning("Specified root goal (" + declaredRoots.get(0) + ") does not match any of the inferred root goal(s): " + foundRoots, "GoalModel::findRoot()");	
				}
			}
		}

		if (declaredRoots.size() > 1) {
			err.addWarning("Too many root goals specified (" + declaredRoots + ") choose only one." , "GoalModel::findRoot()");
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
					err.addError("No root quality explicitly specified, one inferrred (" + foundQRoots.get(0) + ") but formulae have been detected. Please explicilty specify root quality goal.", "GoalModel::findRoot()");
					} else {
						err.addError("No root quality explicitly specified, one inferrred (" + foundQRoots.get(0) + ") and adopted.", "GoalModel::findRoot()");
						this.qroot = foundQRoots.get(0);	
					}
 			}
			if (foundQRoots.size() > 1) {
				err.addError("No root goal explicitly specified, too many inferrred (" + foundQRoots + ").", "GoalModel::findRoot()");	
			}
		}

		if (declaredQRoots.size() == 1) {
			this.qroot = declaredQRoots.get(0);
			
			if ((foundQRoots.size() == 0) && !hasFormula) {
				err.addWarning("Neither specified root quality (" + declaredQRoots.get(0) + ") nor other qualities are parentless." , "GoalModel::findRoot()");	
			}
			if (foundQRoots.size() == 1) {
				if (!declaredQRoots.get(0).equals(foundQRoots.get(0))) {
					err.addWarning("Specified root quality (" + declaredQRoots.get(0) + ") does not match inferred root quality: " + foundQRoots.get(0), "GoalModel::findRoot()");	
				}
			}
			if (foundQRoots.size() > 1) {
				if (!foundQRoots.contains(declaredQRoots.get(0))) {
					err.addWarning("Specified root quality (" + declaredQRoots.get(0) + ") does not match any of the inferred root qualities: " + foundQRoots, "GoalModel::findRoot()");	
				}
			}
		}

		if (declaredQRoots.size() > 1) {
			err.addError("Too many root qulities specified (" + declaredQRoots + ") choose only one." , "GoalModel::findRoot()");
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
			System.out.println(c.getLabel() + " (" + c.getType() + ") ID:" + c.getId());
			System.out.println(" --> AND Children: " + GMNode.debugPrintList(c.getANDChildren(),","));
			System.out.println(" --> OR Children: " + GMNode.debugPrintList(c.getORChildren(),","));
			System.out.println(" --> Effects: " + GMNode.debugPrintList(c.getEffects(),","));
			System.out.println(" --> InPrecedences: " + GMNode.debugPrintList(c.getIncompingPrecedences(),","));
			System.out.println(" --> OutPrecedences: " + GMNode.debugPrintList(c.getOutgoingPrecedences(),","));
			System.out.println(" --> InContributions: " + GMNode.debugPrintList(c.getIncompingContributions(),","));
			System.out.println(" --> OutContributions: " + GMNode.debugPrintList(c.getOutgoingContributions(),","));
			System.out.println(" --> InWeight: " + Float.toString(c.getInWeight()));
			System.out.println(" --> Status: " + c.getEffectStatus());
			if (c.getParent()!= null)
				System.out.println(" --> Parent: " + c.getParent().getLabel());
			else 
				System.out.println(" --> Parent: none");
		}
	}
	
}
