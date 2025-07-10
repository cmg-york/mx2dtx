
# Building iStarDT-V models

## Setting Up
- Open draw.io
- Create New Diagram -> Blank Diagram
- Follow the steps described in [[Nina/gmGraphAdapter/README|README]] in order to load the iStarDT-V library. 
- Your working space should look as follows

![[Pasted image 20250710101101.png]]

- Go to File -> Properties
- Make sure Compressed in unchecked
	- mx2dtx can only read uncompressed DRAWIO files.
	- It does not matter if you 
![[Pasted image 20250710102822.png]]

## Drawing Models

- Drag and drop elements from the library. Double click to update their label.
	- Though the translator cleans HTML that draw.io adds to the label, it is better to avoid boldface, italics and carriage returns in the labels
- **Relationships must snap on the endpoints.** To achieve this, first drag the end point of the relationship over the element you wish to connect. Move the mouse around the shape and release your mouse button (drop) only when the teal-blue glow appears on the perimeter of the shape. 

**Step 1:** Move endpoint of relationship over the element, until the the teal-blue glow appears on the perimeter of the shape. 
![[Pasted image 20250710101551.png]]
 - **Step 2:** Release mouse to drop the end-point and connect the entity to the link. 
![[Pasted image 20250710101642.png]]


## Goals, Tasks and Effects
### Goal and Task Decomposition
- Create a hierarchy rooted on a (hard) goal, and AND- and OR-decomposed into other goals and eventually tasks.
- At the leaf level there should only be tasks and tasks cannot be further AND- and OR- decomposed. Ergo:
	- If you need to decompose a task replace it with a goal.
	- If you don't know how to further decompose a goal, turn it into a task.
	![[Pasted image 20250710104705.png]]
### Adding pre and npr links
### Adding Effects to Tasks
- Once having 
