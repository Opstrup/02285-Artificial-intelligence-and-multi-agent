package searchclient;

import java.util.Comparator;
import java.util.HashMap;

import searchclient.NotImplementedException;

public abstract class Heuristic implements Comparator<Node> {
	//Cache the heuristic of a node
	private HashMap<Node,Integer> heuristicMap = new HashMap<>();
	public Heuristic(Node initialState) {
		// Here's a chance to pre-process the static parts of the level.
	}

	public int h(Node n) {
		Integer h = heuristicMap.get(n);
		if (h==null){
			int goalCount = HeuristicHelper.goalCount(n);
			int boxDistance = HeuristicHelper.boxDistanceToGoal(n);
			h = Math.max(goalCount,boxDistance);
			heuristicMap.put(n,h);
		}
		return h;

	}

	public abstract int f(Node n);

	@Override
	public int compare(Node n1, Node n2) {
		return this.f(n1) - this.f(n2);
	}

	public static class AStar extends Heuristic {
		public AStar(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return n.g() + this.h(n);
		}

		@Override
		public String toString() {
			return "A* evaluation";
		}
	}

	public static class WeightedAStar extends Heuristic {
		private int W;

		public WeightedAStar(Node initialState, int W) {
			super(initialState);
			this.W = W;
		}

		@Override
		public int f(Node n) {
			return n.g() + this.W * this.h(n);
		}

		@Override
		public String toString() {
			return String.format("WA*(%d) evaluation", this.W);
		}
	}

	public static class Greedy extends Heuristic {
		public Greedy(Node initialState) {
			super(initialState);
		}

		@Override
		public int f(Node n) {
			return this.h(n);
		}

		@Override
		public String toString() {
			return "Greedy evaluation";
		}
	}
}
