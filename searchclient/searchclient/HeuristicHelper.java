package searchclient;

import javax.crypto.SealedObject;
import javax.swing.plaf.SeparatorUI;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by salik on 08-02-2017.
 */
public class HeuristicHelper {
    private static int MAX_ROW = StaticLevelItems.getInstance().getMAX_ROW();
    private static int MAX_COL = StaticLevelItems.getInstance().getMAX_COL();

    /**
     * This method returns the number of boxes not yet on their goal.
     * @param n Node which represent the state to calculate on.
     * @return int total number of boxes not on goal
     */
    public static int goalCount(Node n){
        int count = StaticLevelItems.getInstance().getNUM_GOALS();
        for (int row = 1; row < MAX_ROW - 1; row++) {
            for (int col = 1; col < MAX_COL - 1; col++) {
                char g = n.goals[row][col];
                char b = Character.toLowerCase(n.boxes[row][col]);
                if(g > 0 && b == g) count--;
            }
        }
        return count;
    }

    /**
     * This method returns the total manhattan distance of all boxes to all goals
     * @param n Node which represent the state to calculate on.
     * @return int total distance
     */
    public static int boxDistanceToGoal(Node n){
        HashMap<Character, HashSet<Point>>  goalMap = StaticLevelItems.getInstance().getGoalMap();
        int distance = 0;
        for (int row = 1; row < MAX_ROW - 1; row++) {
            for (int col = 1; col < MAX_COL - 1; col++) {
                char b = Character.toLowerCase(n.boxes[row][col]);
                if(b > 0) {
                    HashSet<Point> charSet = goalMap.get(b);
                    Iterator<Point> it = charSet.iterator();
                    int agentDistToBox = (Math.abs(n.agentCol-col) + Math.abs(n.agentRow-row))-1;//Manhattan distance: Agent to box subtract one because agent only has to be next to box to interact
//                    distance += agentDistToBox;
                    while(it.hasNext()){
                        Point goal = it.next();
                        int boxDistToGoal = Math.abs(goal.x-col) + Math.abs(goal.y-row);//Manhattan distance box to goal.
                        distance += boxDistToGoal;
                    }
                }
            }
        }
        return distance;

    }
}
