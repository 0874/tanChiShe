package view;
/*标题栏高度30，但在linux下不占用window高度
*如何避免所有重绘，而只画其中一部分【1.截图，设置背景2.】
* 重绘指定区域【http://bbs.csdn.net/topics/220032015】repaint(x,y,width,height)
* */
import model.MyFood;
import model.MyPoint;
import model.MySnake;
import org.apache.log4j.Logger;
//import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{
    //主要是需要动态绘图
    private static final GamePanel panel = new GamePanel();
    private final int boxWidth = 25*2;
    private final int boxRow = MyWindow.getWindow().getWinHeight()/boxWidth;
    private final int boxCol = MyWindow.getWindow().getWinWidth()/boxWidth;
    private final ArrayList<MyPoint> pointArrayList = MySnake.getSnake().getArrayListSnake();
    private final MyPoint pointFood = MyFood.getFood();
    //上一轮的最后一个，
    private final MyPoint lastPoint = new MyPoint(-1,-1);
    private final static Logger log = Logger.getLogger(GamePanel.class);
    private boolean currentPanel = false;
    //是否改变行动方向，由按键控制
    private boolean changeDirection = false;
    private int currentDirection = MySnake.getLeft();
    private boolean firstPaint = true;
    //返回主菜单中断行走线程
    private boolean moveOver = false;
    private static final Thread threadMove = new Thread(panel);
    //游戏是否完成标识[当开启线程时和重置状态时，此变量设为false
    private boolean gameOver = true;
    //线程是否已经开启
    private boolean threadStart = false;
    static {
    }

    public boolean isThreadStart() {
        return threadStart;
    }

    public void setThreadStart(boolean threadStart) {
        this.threadStart = threadStart;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public static Thread getThreadMove() {
        return threadMove;
    }
    public boolean isMoveOver() {
        return moveOver;
    }

    public void setMoveOver(boolean moveOver) {
        this.moveOver = moveOver;
    }

    public boolean isFirstPaint() {
        return firstPaint;
    }

    public void setFirstPaint(boolean firstPaint) {
        this.firstPaint = firstPaint;
    }

    public MyPoint getLastPoint() {
        return lastPoint;
    }

    public boolean isChangeDirection() {
        return changeDirection;
    }

    public void setChangeDirection(boolean changeDirection) {
        this.changeDirection = changeDirection;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public boolean isCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(boolean currentPanel) {
        this.currentPanel = currentPanel;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxRow() {
        return boxRow;
    }

    public ArrayList<MyPoint> getPointArrayList() {
        return pointArrayList;
    }

    public MyPoint getPointFood() {
        return pointFood;
    }

    public static Logger getLog() {
        return log;
    }

    public int getBoxCol() {
        return boxCol;
    }

    //绘图产生动画-----
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(Color.MAGENTA);
            graphics2D.drawLine(0, 0, 800, 600);
            graphics2D.drawLine(0, 600, 800, 0);
            //绘制网格
            graphics2D.setColor(Color.BLUE);
            int i;
            for (i = 1; i < boxCol; i++)
                graphics2D.drawLine(i * boxWidth, 0, i * boxWidth, 600);
            for (i = 1; i < boxRow; i++)
                graphics2D.drawLine(0, i * boxWidth, 800, i * boxWidth);
        paintDynamic(graphics2D);
    }
    //绘制蛇体，食物，其余动态效果
    private void paintDynamic(Graphics2D graphics2D){
        Stroke stroke = new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,
               0,null,0 );
        graphics2D.setStroke(stroke);
        for (MyPoint point:pointArrayList) {
            graphics2D.setColor(Color.MAGENTA);
            graphics2D.fillRect(point.getX() * boxWidth, point.getY() * boxWidth, boxWidth, boxWidth);
            graphics2D.setColor(Color.YELLOW);
            graphics2D.drawRect(point.getX() * boxWidth, point.getY() * boxWidth, boxWidth, boxWidth);
        }
        if (MyFood.getFood().getScore() == 2)
            graphics2D.setColor(Color.pink);
        graphics2D.fillRect(pointFood.getX()*boxWidth,pointFood.getY()*boxWidth,boxWidth,boxWidth);
        graphics2D.setColor(Color.MAGENTA);
        graphics2D.drawRect(pointFood.getX()*boxWidth,pointFood.getY()*boxWidth,boxWidth,boxWidth);
        //覆盖上一次的最后一个，是否需要【吃到食物就不需要】
        if (!(lastPoint.getX() == -1 && lastPoint.getX() == -1)){
            graphics2D.setColor(new Color(0x33,0xcc,0xcc));//面板背景色
            graphics2D.fillRect(lastPoint.getX()*boxWidth,lastPoint.getY()*boxWidth,boxWidth,boxWidth);
            graphics2D.setColor(Color.BLUE);
            graphics2D.drawRect(lastPoint.getX()*boxWidth,lastPoint.getY()*boxWidth,boxWidth,boxWidth);
        }
    }

    @Override
    public void print(Graphics g) {
        super.print(g);
    }
//坐标为x(col),y(row)方向
    private void init(){
        pointArrayList.add(new MyPoint(boxCol/2,boxRow/2));
        pointArrayList.add(new MyPoint(boxCol/2+1,boxRow/2));
        pointArrayList.add(new MyPoint(boxCol/2+2,boxRow/2));
    }
    public static GamePanel getPanel() {
        return panel;
    }

    private GamePanel() {
        super();
        setBackground(new Color(0x33,0xcc,0xcc));
        init();
    }

    @Override
    public void run() {
        log.info("move 线程启动。");
        MyWindow.getWindow().setTitle("得分："+MySnake.getSnake().getScore());
        while (true){
            synchronized (pointArrayList) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    log.info("行走发生异常：" + e.getLocalizedMessage());
                    try {
                        log.info("挂起move线程");
                        pointArrayList.wait();
                        //唤醒线程后从哪开始执行
                        log.info("线程move被唤醒，开始执行");
                        //唤醒线程之前需要重置food snake 状态，或者是唤醒后第一执行
                        if (!moveOver)//如果移动没有结束，【也就是这局游戏没有结束】
                            continue;
                        MySnake.getSnake().resetState();
                        GamePanel.getPanel().setCurrentDirection(MySnake.getLeft());
                        GamePanel.getPanel().setChangeDirection(false);
                    } catch (InterruptedException e1) {
                        log.info("挂起.的线程发生异常："+e1.getLocalizedMessage());
                    }
                }
            }
            log.info("I am going...");
            MyPoint point = new MyPoint();
            //是否有转向
            if (changeDirection){
                changeDirection = false;
            }else {
                //好像没必要分开，转不转向，都不影响预先点的生成
            }
            point.setY(pointArrayList.get(0).getY());
            point.setX(pointArrayList.get(0).getX());
            switch (currentDirection){
                case MySnake.left:point.setX(point.getX()-1);break;
                case MySnake.down:point.setY(point.getY()+1);break;
                case MySnake.right:point.setX(point.getX()+1);break;
                case MySnake.up:point.setY(point.getY()-1);break;
                default:JOptionPane.showMessageDialog(this,"出现了未知方向");
            }
            MyPoint  pointFood = MyFood.getFood();
            //如果撞到自己
            for (MyPoint myp : pointArrayList) {
                if (myp.getY() == point.getY() && myp.getX() == point.getX()){
                    gameOver();
                }
            }
            //给最后一个赋值
            lastPoint.setX(pointArrayList.get(pointArrayList.size()-1).getX());
            lastPoint.setY(pointArrayList.get(pointArrayList.size()-1).getY());
            //吃到食物[改变数组长度，并使lastPoint为-1,-1（表示不覆盖）]
            if (point.getX() == pointFood.getX() && point.getY() == pointFood.getY()){
                log.info("吃到食物。");
                //插入头节点
                pointArrayList.add(0,point);
                lastPoint.setY(-1);
                lastPoint.setX(-1);
                MySnake.getSnake().setScore(MySnake.getSnake().getScore()+MyFood.getFood().getScore());
                MyFood.getFood().flush();
                MyWindow.getWindow().setTitle("得分："+MySnake.getSnake().getScore());
            }else if (point.getX()<0||point.getX()>=boxCol||point.getY()<0||point.getY()>=boxRow){
               gameOver();
            }else {//正常移动，整体移位
                int x,y;
                for (MyPoint myPoint:pointArrayList){
                    x = myPoint.getX();
                    y = myPoint.getY();
                    myPoint.setY(point.getY());
                    myPoint.setX(point.getX());
                    point.setX(x);
                    point.setY(y);
                }
            }
            log.info("蛇长度："+pointArrayList.size()+"头位置："+pointArrayList.get(0).getX()+','+pointArrayList.get(0).getY()+
            "食物位置："+pointFood.toString()+"总分:"+MySnake.getSnake().getScore());
            paintMove();
        }
    }

    public  void  gameOver(){
        JOptionPane.showMessageDialog(this,"game over\n" +
                "得分："+MySnake.getSnake().getScore());
        moveOver = true;
        //游戏处于结束状态，设置结束标识
        gameOver = true;
        threadMove.interrupt();
        /*根据jdk的void notifyAll()的描述，“解除那些在该对象上调用wait()方法的线程的阻塞状态。
        该方法只能在同步方法或同步块内部调用。
        如果当前线程不是对象所得持有者，该方法抛出一个java.lang.IllegalMonitorStateException 异常”
        * */
        /*try {
            threadMove.wait();
        } catch (InterruptedException e) {
            log.info("挂起："+e.getLocalizedMessage());
        }*/
        MyWindow.getWindow().remove(this);
        MyWindow.getWindow().add(MenuPanel.getPanel().getjPanel());
        MyWindow.getWindow().setTitle(MyWindow.getWindow().getWinTitle());
        MyWindow.getWindow().repaint();
        MyWindow.getWindow().validate();
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    //行走：
    public void paintMove(){
        repaint();
        //重绘蛇身上轮最后一点
       /* repaint(lastPoint.getX(),lastPoint.getY(),boxWidth,boxWidth);
        repaint(pointArrayList.get(0).getX());*/
        /*//给最后一个赋值
        lastPoint.setX(pointArrayList.get(pointArrayList.size()-1).getX());
        lastPoint.setY(pointArrayList.get(pointArrayList.size()-1).getY());*/
    }
}
