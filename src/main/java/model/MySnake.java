package model;

import view.GamePanel;

import java.util.ArrayList;

public class MySnake {
    private ArrayList<MyPoint> arrayListSnake;
    private final MyPoint beforehandPoint = new MyPoint();
    //这个形式主要为了switch中的case元素
    public static final int left = 0x000e1;
    public static final int right = 0x000e4;
    public static final int up = 0x000e2;
    public static final int down = 0x000e8;
    private static final MySnake snake = new MySnake();
    private int score = 0;

    public int getScore() {
        return score;
    }
    public void resetState(){
      arrayListSnake.clear();
      arrayListSnake.add(new MyPoint(GamePanel.getPanel().getBoxCol()/2,GamePanel.getPanel().getBoxRow()/2));
      arrayListSnake.add(new MyPoint(GamePanel.getPanel().getBoxCol()/2+1,GamePanel.getPanel().getBoxRow()/2));
      arrayListSnake.add(new MyPoint(GamePanel.getPanel().getBoxCol()/2+1,GamePanel.getPanel().getBoxRow()/2));
      score = 0;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public MyPoint getBeforehandPoint() {
        return beforehandPoint;
    }

    public static int getLeft() {
        return left;
    }

    public static int getRight() {
        return right;
    }

    public static int getUp() {
        return up;
    }

    public static int getDown() {
        return down;
    }

    public static MySnake getSnake() {
        return snake;
    }

    private MySnake() {
        arrayListSnake = new ArrayList<MyPoint>();
    }

    private MySnake(ArrayList<MyPoint> arrayListSnake) {
        this.arrayListSnake = arrayListSnake;
    }

    public ArrayList<MyPoint> getArrayListSnake() {
        return arrayListSnake;
    }

    public void setArrayListSnake(ArrayList<MyPoint> arrayListSnake) {
        this.arrayListSnake = arrayListSnake;
    }
}
