package agents;
import loveletter.*;


import java.util.Arrays;

import agents.Agent22243339;
import agents.RandomAgent;
import agents.heuristicAgent;
import agents.BorkedAgent;
import agents.exampleAgent;


public class Stats extends LoveLetter{

    public Stats(){

    }

    public static void getData() {
        Agent[] agents = {new RandomAgent(),new BorkedAgent(), new Agent22243339(), new heuristicAgent()};

        String[] columnName = {"Agent","Win Rate (Out of 100 games)", "Avg score per game"};
        double[][] dataTable = new double[4][3];

        for(int i = 0; i < 100; i++) {
            LoveLetter game = new LoveLetter();
            int[] data = game.playGame(agents);


            //Average score
            for(int d = 0; d <= 3; d++) {
                dataTable[d][0] = d;
                if(data[d] == 4) {
                    dataTable[d][1] += 1;
                }
                dataTable[d][2] += data[d];
            } 
        }

        for(int e = 0; e <= 3; e++){
            dataTable[e][1] = dataTable[e][1]/100;
            dataTable[e][2] = dataTable[e][2]/100;
        }

        System.out.print(Arrays.toString(columnName) +"\n");
        System.out.print(Arrays.deepToString(dataTable)+"\n");
    }

    public static void main(String[] args){
        Stats.getData();
    }
}