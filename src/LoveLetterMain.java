 


import java.util.Arrays;

import main.FinalSelectionPolicy;
import main.MCTS;
import main.Move;

public class LoveLetterMain {

	public static void main(String[] args) {
		MCTS mcts = new MCTS();
		mcts.setExplorationConstant(0.2);
		mcts.setTimeDisplay(true);
		// Move move;
		mcts.setOptimisticBias(0.0d);
		mcts.setPessimisticBias(0.0d);
		// mcts.setMoveSelectionPolicy(FinalSelectionPolicy.robustChild);
		// int []scores = new int[3];
	}
}
