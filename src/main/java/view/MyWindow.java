package view;
/*
* 核心功能，蛇身转向【先确定预备点，再判断预备点】
* 功能2：【继续游戏：】游戏中，按esc键退出到菜单项【挂起线程，继续游戏再恢复线程】，情况2：当游戏未完成而直接退出时，
* */
import model.MyFood;
import model.MyPoint;
import model.MySnake;
import org.apache.log4j.Logger;
import util.MyFileWriteAndRead;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

//查看文件大小：ls -lh
public class MyWindow extends JFrame implements KeyListener{
    private final static Logger log = Logger.getLogger(MyWindow.class);
    private final int winWidth = 800;
    private final int winHeight = 600;//
    private final String winTitle = "贪吃蛇-[未实现功能：设置(速度？方块大小？),历史记录【把完成后的分数时间保存到文件，再读取？】]";
    private static final MyWindow window = new MyWindow();
    private final String notFinishedGameStateFileName = "temp";
    static {
    }
    //当切换面板时需要执行相关操作
    public void panelSwitch(){
        log.info("进行面板切换");
        if (GamePanel.getPanel().isCurrentPanel()){
            //主动获取焦点【http://blog.renren.com/share/252342204/12947836455】
            GamePanel.getPanel().grabFocus();
            //如果第二次进入需要对数据初始化
            //启动行走线程
            if (GamePanel.getPanel().isThreadStart()){//重新唤醒线程
                //重置已启动线程内实例信息，如果按下的是，开始游戏，需要重置游戏状态
                log.info("线程game,已经在挂起状态，需要唤醒：");
                if (MenuPanel.getPanel().isContinueGame()&&!GamePanel.getPanel().isGameOver())
                    GamePanel.getPanel().setMoveOver(false);
                synchronized (MySnake.getSnake().getArrayListSnake()){
                    MySnake.getSnake().getArrayListSnake().notifyAll();
                }
                return;
            }else {//如果还没有开启线程
                GamePanel.getThreadMove().start();//首次开启线程
                GamePanel.getPanel().setThreadStart(true);
            }
            //设置游戏激活标识
            GamePanel.getPanel().setGameOver(false);
            //参见【http://www.xuebuyuan.com/1627946.html】
            log.info("给game-panel添加按键响应");
            GamePanel.getPanel().addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    log.info("game按键："+e.getKeyCode());
                    int direction = 0;
                    boolean isKey = false;
                    if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
                        log.info("蛇向上");
                        isKey = true;
                        direction = MySnake.getSnake().getUp();
                    }else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN){
                        log.info("蛇向下");
                        isKey = true;
                        direction = MySnake.getSnake().getDown();
                    }else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
                        log.info("蛇向左");
                        isKey = true;
                        direction = MySnake.getSnake().getLeft();
                    }else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
                        log.info("蛇向右");
                        isKey = true;
                        direction = MySnake.getSnake().getRight();
                    }else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        log.info("暂停游戏，挂起");
                        GamePanel.getThreadMove().interrupt();
                        GamePanel.getPanel().setMoveOver(false);
                        GamePanel.getPanel().setGameOver(false);
                        MyWindow.getWindow().remove(GamePanel.getPanel());
                        MyWindow.getWindow().add(MenuPanel.getPanel().getjPanel());
                        getWindow().setTitle(winTitle);
                        MyWindow.getWindow().repaint();
                        MyWindow.getWindow().validate();

                    }else
                        log.info("程序用不到这个按键");
                    if (isKey && direction!=GamePanel.getPanel().getCurrentDirection()&&
                            //不能逆方向行走
                            !(direction == MySnake.getRight() && GamePanel.getPanel().getCurrentDirection() == MySnake.getLeft()
                            || direction == MySnake.getLeft() && GamePanel.getPanel().getCurrentDirection() == MySnake.getRight()
                            || direction == MySnake.getDown() && GamePanel.getPanel().getCurrentDirection() == MySnake.getUp()
                            || direction == MySnake.getUp() && GamePanel.getPanel().getCurrentDirection() == MySnake.getDown())
                            ){
                        GamePanel.getPanel().setChangeDirection(true);
                        GamePanel.getPanel().setCurrentDirection(direction);
                    }
                    //在线程里进行每帧绘制，而对蛇与食物位置改变的方法应该是一个同步方法
                }
            });
        }else {
            log.info("game-panel非当前面板：");
        }
    }
    private void init(){
        log.info("初始化主窗体");
        getContentPane().add(MenuPanel.getPanel().getjPanel());
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                log.info("窗口打开后");
            }
            /*当需要关闭窗口时：
            **
            * 检查game是否完全结束【？】
            *
            * */
            @Override
            public void windowClosing(WindowEvent e) {
                log.info("窗口正关闭");
                if (GamePanel.getPanel().isGameOver()){
                    //删除状态文件--不需要也是为了防止下次读入
                    File file = new File(MyFileWriteAndRead.getFileState());
                    if (file.exists())
                        file.delete();
                }else
                    MyFileWriteAndRead.writeCurrentState(MySnake.getSnake().getScore(),
                        MySnake.getSnake().getArrayListSnake(),MyFood.getFood(),
                            GamePanel.getPanel().getCurrentDirection());
            }

            @Override
            public void windowClosed(WindowEvent e) {
                log.info("窗口关闭完");
            }

            @Override
            public void windowIconified(WindowEvent e) {
                log.info("窗口最小化");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                log.info("窗口取消最小化");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                log.info("激活窗口");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                log.info("窗口失去焦点");
            }
        });
    }
    public String getWinTitle() {
        return winTitle;
    }

    private MyWindow() throws HeadlessException {
        setSize(winWidth,winHeight);
        setTitle(winTitle);
        getContentPane().setBackground(Color.BLACK);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        //获取焦点
        requestFocus();
        addKeyListener(this);
        setVisible(true);
    }

    public int getWinWidth() {
        return winWidth;
    }

    public int getWinHeight() {
        return winHeight;
    }

    public static MyWindow getWindow() {
        return window;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        log.info("按下"+e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        log.info("按下"+e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        log.info("弹起"+e.getKeyCode());
    }
}
