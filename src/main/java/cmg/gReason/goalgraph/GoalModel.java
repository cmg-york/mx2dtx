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
		GMNode gNode = new GMNode();

		gNode.setId(e.getID());
		gNode.setLabel(e.getLabel());
		//TODO: Notes need to be added.

		if (e instanceof Goal) {
			gNode.setRuns(((Goal) e).getRunNum());
			gNode.setType("goal");
		} else if (e instanceof Task) {
			gNode.setType("task");
		} else if (e instanceof Quality) {
			gNode.setType("quality");
			gNode.setDtxFormula(((Quality) e).getDtxFormua());
			gNode.setFormula(((Quality) e).getFormula());
			gNode.setQRoot(((Quality) e).isQRoot());
		} else if (e instanceof EffectGroup) {
			gNode.setType("effectGroup");
		} else if (e instanceof Effect) {
			gNode.setType("effect");
			gNode.setEffectStatus(((Effect) e).getStatus());
		} else if (e instanceof Precondition) {
			gNode.setType("precondition");
			gNode.setDtxFormula(((Precondition) e).getDtxFormua());
			gNode.setFormula(((Precondition) e).getFormula());
		}
		return (gNode);
	}
	
	/**
	 * Reads the list of {@linkplain GraphElement} objects (which are primarily relationships) and generates a goal model based on {@linkplain GMNode} objects.
	 * @throws Exception in a variety of situations in which the goal model is invalid.
	 */
	public void createGoalGraph() throws Exception {

		for (GraphElement c:elements) {
			if (c instanceof Link) {
				//Create the node
				GMNode gmn = new GMNode();
				GMNode gmnOrigin;
				GMNode gmnTarget;
				
				gmn.setId(c.getID());
				gmn.setLabel(c.getLabel());
				gmn.setType(((Link) c).getType());
				
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
			// c is not a link
			} else if (c instanceof InitializationSet) {
				GMNode ini = new GMNode();
				ini.setLabel(c.getLabel());
				ini.setId(c.getID());
				ini.setFormula(((InitializationSet) c).getFormula());
				ini.setDtxFormula(((InitializationSet) c).getDtxFormula());
				ini.setType("initialization");
				goalModel.add(ini);
			} else if (c instanceof CrossRunSet) {
				GMNode  crs = new GMNode();
				crs.setLabel(c.getLabel());
				crs.setId(c.getID());
				crs.setFormula(((CrossRunSet) c).getFormula());
				crs.setDtxFormula(((CrossRunSet) c).getDtxFormula());
				crs.setType("crossrun");
				goalModel.add(crs);
			} else if (c instanceof ExportedSet) {
				GMNode  exr = new GMNode();
				exr.setLabel(c.getLabel());
				exr.setId(c.getID());
				exr.setFormula(((ExportedSet) c).getFormula());
				exr.setDtxFormula(((ExportedSet) c).getDtxFormula());
				exr.setType("export");
				goalModel.add(exr);
			}
		}//loop 
		
		//Bypass effectGroups
		byPassEffectGroups();
	
		try {
			findRoot();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * Removes the effect group element, such that, in the end, tasks are directly connected to the effects. 
	 */
	private void byPassEffectGroups() {
		Iterator<GMNode> itr = goalModel.iterator();
		
		while (itr.hasNext()) {
			GMNode n = itr.next();
			if (n.getType().equals("effectGroup")) {
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
	private void findRoot() throws Exception {
		ArrayList<String> roots = new ArrayList<String>();
		ArrayList<String> qRoots = new ArrayList<String>();
		
		for (GMNode n:goalModel) {
			if (n.getType().equals("goal") && (n.getParent()==null) ) {
				this.root = n;
				roots.add(n.getLabel());
			}
			if (n.getType().equals("quality") && n.isQRoot()) {
				System.out.println(n.getCamelLabel() + " is root");
				this.qroot = n;
				qRoots.add(n.getLabel());
			}
		}
		
		if (roots.size() > 1)
			throw new Exception("Error: Too many root goals, 1 expected, " + roots.size() + " found: " + roots);

		if (qRoots.size() > 1) {
			throw new Exception("Error: Too many quality root goals, 1 expected, " + qRoots.size() + " found: " + qRoots + ".");
		} else if (qRoots.size() == 0) {
			for (GMNode n:goalModel) {
				if (n.getType().equals("quality") && n.getOutgoingContributions().isEmpty()) {
					this.qroot = n;
					qRoots.add(n.getLabel());
				}
			}
			if (qRoots.size() >1)
				throw new Exception("Error: Too many quality root goals, 1 expected, " + qRoots.size() + " found: " + qRoots + ". Pick one manually but adding property root = true.");
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
