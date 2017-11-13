package model;

import org.apache.log4j.Logger;
import view.GamePanel;

import java.util.Random;

public class MyFood extends MyPoint {
    private int score = 1;
    private static int count = 0;
    private static final Logger logger = Logger.getLogger(MyPoint.class);
    //只生成一个food对象实例，如果生成新的，重置对象数据就可以了
    private static final MyFood food = new MyFood(0,0);
    private final Random random = new Random();
    //刷新食物位置
    public void flush(){
        int x = GamePanel.getPanel().getBoxCol(),
        y = GamePanel.getPanel().getBoxRow();
        int a,b,c;
        //不占用蛇身，在屏幕内
        while (true){
            a = random.nextInt(x);
            b = random.nextInt(y);
            c = 0;
            for (MyPoint p :MySnake.getSnake().getArrayListSnake()) {
                if (a == p.getX() && b == p.getX())
                    break;
                c++;
            }
            if (c == MySnake.getSnake().getArrayListSnake().size())
                break;
            logger.info("正在刷新新的食物点");
        }
        resetFoodxy(a,b);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Random getRandom() {
        return random;
    }

    public void resetFoodxy(int x, int y){
        setX(x);
        setY(y);
        score = (int) (Math.random()*2)+1;
        logger.info("重置food:"+count++);
    }
    private MyFood() {
        super();
        logger.info("生成第"+(++count)+"个food");
    }

    private MyFood(int x, int y) {
        super(x, y);
        logger.info("生成第"+(++count)+"个food");
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        MyFood.count = count;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static MyFood getFood() {
        return food;
    }
}
