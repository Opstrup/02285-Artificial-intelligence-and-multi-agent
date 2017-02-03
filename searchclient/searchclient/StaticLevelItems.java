package searchclient;

/**
 * Created by salik on 03-02-2017.
 */
public class StaticLevelItems {
    private static StaticLevelItems instance;
    private boolean[][] walls;// = new boolean[MAX_ROW][MAX_COL];
    private char[][] goals;// = new char[MAX_ROW][MAX_COL];
    private StaticLevelItems(int MAX_ROW, int MAX_COL){
        walls = new boolean[MAX_ROW][MAX_COL];
        goals = new char[MAX_ROW][MAX_COL];
    }
    public static StaticLevelItems getInstance(int MAX_ROW, int MAX_COL) {
        if(instance == null) {
            instance = new StaticLevelItems(MAX_ROW, MAX_COL);
        }
        return instance;
    }
    public void setWall(boolean wall, int row, int col){
        walls[row][col] = wall;
    }
    public void setGoal(char goal, int row, int col){
        goals[row][col] = goal;
    }
    public char[][] getGoals(){
        return goals;
    }
    public boolean[][] getWalls(){
        return walls;
    }
}
