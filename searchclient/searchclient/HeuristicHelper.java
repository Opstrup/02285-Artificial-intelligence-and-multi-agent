package searchclient;

/**
 * Created by salik on 08-02-2017.
 */
public class HeuristicHelper {
    private static int MAX_ROW = StaticLevelItems.getInstance().getMAX_ROW();
    private static int MAX_COL = StaticLevelItems.getInstance().getMAX_COL();

//    public static int closestBoxDistance(){
//
//    }
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
}
