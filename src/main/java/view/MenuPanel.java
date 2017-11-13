package view;
//由于加载log4j配置文件的方法，这个工程现在的jdk为1.6
import org.apache.log4j.Logger;
import util.MyFileWriteAndRead;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
//import java.util.logging.Logger;

public class MenuPanel {
    private static final Logger log = Logger.getLogger(MenuPanel.class);
    private static final MenuPanel panel = new MenuPanel();
    private JPanel jPanel;
    private JButton jButtonStart;
    private JButton jButtonContinue;
    private JButton jButtonHelp;
    private JButton jButtonSet;
    private JButton jButtonHistory;

    //如果内存中没有正在运行的游戏实例，就从文件中加载，如果有的话就恢复线程
    private boolean firstExeContinue = true;
    //按下那个按钮
    private boolean startGame = false;
    private boolean continueGame = false;
    static {
        panel.init();
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isContinueGame() {
        return continueGame;
    }

    public void setContinueGame(boolean continueGame) {
        this.continueGame = continueGame;
    }

    public boolean isFirstExeContinue() {
        return firstExeContinue;
    }

    public void setFirstExeContinue(boolean firstExeContinue) {
        this.firstExeContinue = firstExeContinue;
    }

    private void init(){

        //帮助---
        jButtonHelp.addActionListener(e -> {
            JOptionPane.showMessageDialog(jPanel,"使用wasd或者方向键控制，游戏中esc键中途退出\n" +
                    "目前实现的功能有：【开始游戏，继续游戏(重启程序支持)】");
        });

        //添加事件响应
        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame = true;
                MyWindow.getWindow().remove(MenuPanel.getPanel().getjPanel());
                MyWindow.getWindow().add(GamePanel.getPanel());
                /*//这个方法从1.7才支持
                MyWindow.getWindow().revalidate();*/
                MyWindow.getWindow().repaint();
                MyWindow.getWindow().validate();
                GamePanel.getPanel().setCurrentPanel(true);
                //对面板切换状态及相应的操作
                //开始就表示重置游戏状态，即便上次的没有结束，也会被重置
                GamePanel.getPanel().setGameOver(true);
                GamePanel.getPanel().setMoveOver(true);
                MyWindow.getWindow().panelSwitch();
            }
        });
        //继续游戏【1.从文件中加载来的；2.已存在内存中的】
        jButtonContinue.addActionListener((e)->{
            continueGame = true;
            GamePanel.getPanel().setChangeDirection(true);
            MyWindow.getWindow().remove(MenuPanel.getPanel().getjPanel());
            MyWindow.getWindow().add(GamePanel.getPanel());
            MyWindow.getWindow().repaint();
            MyWindow.getWindow().validate();
            GamePanel.getPanel().setCurrentPanel(true);
            //如果线程没有启动，先查看是否有状态配置文件，
            if (!GamePanel.getPanel().isThreadStart()){
                if (MyFileWriteAndRead.readStateFromFile()){
                    log.info("成功读取状态文件，并赋值与相应实例，");
                }else
                    log.info("读取配置文件失败");

            }else {
                log.info("内存中已有实例运行");
                MyWindow.getWindow().panelSwitch();
            }
            MyWindow.getWindow().panelSwitch();
        });
    }
    public static MenuPanel getPanel() {
        return panel;
    }

    public static Logger getLog() {
        return log;
    }

    private MenuPanel() {
        log.info("构造菜单面板单例.");
        jPanel = new JPanel();
        jPanel.setBackground(Color.BLUE);
        //参照【https://www.cnblogs.com/fnlingnzb-learner/p/6008572.html】
        BoxLayout boxLayout = new BoxLayout(jPanel,BoxLayout.Y_AXIS);
        jPanel.setLayout(boxLayout);
        //开始游戏
        jPanel.add(Box.createVerticalStrut(20));
        jButtonStart = new JButton("开始游戏");
        jPanel.add(jButtonStart);
        //继续游戏
        jPanel.add(Box.createVerticalStrut(15));
        jButtonContinue = new JButton("继续游戏");
        jPanel.add(jButtonContinue);
        //历史记录
        jPanel.add(Box.createVerticalStrut(15));
        jButtonHistory = new JButton("历史记录");
        jPanel.add(jButtonHistory);
        //设置
        jPanel.add(Box.createVerticalStrut(15));
        jButtonSet = new JButton("设置");
        jPanel.add(jButtonSet);
        //帮助
        jPanel.add(Box.createVerticalStrut(15));
        jButtonHelp = new JButton("帮助");
        jPanel.add(jButtonHelp);
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }

    public JButton getjButtonStart() {
        return jButtonStart;
    }

    public void setjButtonStart(JButton jButtonStart) {
        this.jButtonStart = jButtonStart;
    }

    public JButton getjButtonContinue() {
        return jButtonContinue;
    }

    public void setjButtonContinue(JButton jButtonContinue) {
        this.jButtonContinue = jButtonContinue;
    }

    public JButton getjButtonHelp() {
        return jButtonHelp;
    }

    public void setjButtonHelp(JButton jButtonHelp) {
        this.jButtonHelp = jButtonHelp;
    }

    public JButton getjButtonSet() {
        return jButtonSet;
    }

    public void setjButtonSet(JButton jButtonSet) {
        this.jButtonSet = jButtonSet;
    }

    public JButton getjButtonHistory() {
        return jButtonHistory;
    }

    public void setjButtonHistory(JButton jButtonHistory) {
        this.jButtonHistory = jButtonHistory;
    }
}
