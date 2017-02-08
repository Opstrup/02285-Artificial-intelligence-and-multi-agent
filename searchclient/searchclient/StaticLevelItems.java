package searchclient;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by salik on 03-02-2017.
 */
public class StaticLevelItems {
    private static StaticLevelItems instance;
    private boolean[][] walls;// = new boolean[MAX_ROW][MAX_COL];
    private char[][] goals;// = new char[MAX_ROW][MAX_COL];
    private HashMap<Character, HashSet<Point>> goalMap;
    private int MAX_ROW;
    private int MAX_COL;
    private int NUM_GOALS;

    private StaticLevelItems(int MAX_ROW, int MAX_COL){
        walls = new boolean[MAX_ROW][MAX_COL];
        goals = new char[MAX_ROW][MAX_COL];
        this.MAX_ROW = MAX_ROW;
        this.MAX_COL = MAX_COL;
        NUM_GOALS = 0;
        goalMap = new HashMap<>();
    }
    public static StaticLevelItems createInstance(int MAX_ROW, int MAX_COL) {
        if(instance == null) {
            instance = new StaticLevelItems(MAX_ROW, MAX_COL);
        }
        return instance;
    }
    public static StaticLevelItems getInstance(){
        return instance;
    }
    public void setWall(boolean wall, int row, int col){
        walls[row][col] = wall;
    }
    public void setGoal(char goal, int row, int col){
        goals[row][col] = goal;
        if (goalMap.containsKey(goal)){
            HashSet<Point> charSet = goalMap.get(goal);
            charSet.add(new Point(col, row));
        }else{
            HashSet<Point> charSet = new HashSet<>();
            charSet.add(new Point(col, row));
            goalMap.put(goal, charSet);
        }
        NUM_GOALS++;
    }
    public char[][] getGoals(){
        return goals;
    }
    public int getNUM_GOALS(){
        return NUM_GOALS;
    }
    public HashMap<Character, HashSet<Point>> getGoalMap(){
        return goalMap;
    }
    public boolean[][] getWalls(){
        return walls;
    }
    public int getMAX_ROW(){
        return MAX_ROW;
    }

    public int getMAX_COL() {
        return MAX_COL;
    }
}
