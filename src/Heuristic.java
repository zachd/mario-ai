package ucsc_mario_astar;

public class Heuristic {
	byte n;
	 //public int H(Node startNode, Node endNode) { int WEIGHT = 10; int distance = (Math.abs(startNode.getX() - endNode.getX()) + Math.abs(startNode.getY() - endNode.getY()));
	 //return (distance * WEIGHT); }
	public Heuristic (byte node){
		n=node;
	}
	
	public int estimate (byte node){
		if (node==0) return 0;
		if (node==-10) return 99;
		return 0; //Not going to be 0 later on >.>
	}
	
}
