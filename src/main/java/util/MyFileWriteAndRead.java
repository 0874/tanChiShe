package util;
//https://www.cnblogs.com/StanLong/p/6906814.html【字符和二进制的相互转换】


import model.MyFood;
import model.MyPoint;
import model.MySnake;
import org.apache.log4j.Logger;
import org.junit.Test;
import view.GamePanel;

import java.io.*;
import java.util.ArrayList;

//通过Integer.toBinaryString的方式将原有字符串转为2进制字符串
public class MyFileWriteAndRead {
    private final static String fileState = "state.tcs";
    private final static Logger log = Logger.getLogger(MyFileWriteAndRead.class);
    //从状态文件中读取-信息并配置到相应对象中
    public static boolean readStateFromFile(){
        File file = new File(fileState);
        if (!file.exists())
            return false;
        int va = 0;
        try {
            DataInputStream  dataInputStream = new DataInputStream(new FileInputStream(file));
            MySnake.getSnake().setScore(dataInputStream.readInt());
            GamePanel.getPanel().setCurrentDirection(dataInputStream.readInt());
            log.info("从文件获取的方向是："+GamePanel.getPanel().getCurrentDirection());
            MyFood.getFood().setX(dataInputStream.readInt());
            MyFood.getFood().setY(dataInputStream.readInt());
            //读取蛇身
            MySnake.getSnake().getArrayListSnake().clear();
            while (true){
                MyPoint point = new MyPoint();
                point.setX(dataInputStream.readInt());
                point.setY(dataInputStream.readInt());
                MySnake.getSnake().getArrayListSnake().add(point);
                va++;
            }
        } catch (FileNotFoundException e) {
            log.info("要读取的文件没有找到;"+e.getLocalizedMessage());
            return false;
        } catch (IOException e) {
            log.info("不能从状态文件中读取数据:"+e.getMessage());
            if (va >= 3) {
                log.info("已将文件读完：");
                return true;
            }
            return false;
        }
    }

    //原先通过字符转二进制字符串的方式，有点问题，，直接使用data-output-stream
    //写入格式字符串【】，，，也不是太多数据，按顺序写入，按顺序读出
    public static void writeCurrentState(int score,//分数
                                         ArrayList<MyPoint> snakeList,//蛇身位置
    //食物位置
                                         MyPoint food,
                                         //运动方向
                                         int direction
    ){
        log.info("写入文件的方向："+direction);
        File file = new File(fileState);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("不能创建游戏状态保存文件："+e.getMessage());
                return;
            }
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            dataOutputStream.writeInt(score);
            dataOutputStream.writeInt(direction);
            dataOutputStream.writeInt(food.getX());
            dataOutputStream.writeInt(food.getY());
            for (MyPoint point :snakeList) {
                dataOutputStream.writeInt(point.getX());
                dataOutputStream.writeInt(point.getY());
            }
        } catch (FileNotFoundException e) {
            log.error("没有找到状态保存文件："+e.getLocalizedMessage());
            return;
        } catch (IOException e) {
            log.error("数据不能写入文件："+e.getMessage());
        }
    }

    public static Logger getLog() {
        return log;
    }

    public static String getFileState() {
        return fileState;
    }

    //string-builder是非同步的，要快一点比string-buffer
    public static StringBuilder getBinaryString(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = string.toCharArray();
        for (char c : chars) {
            String str = Integer.toBinaryString(c);
            stringBuilder.append(str.length() + "\n" + str);
//            System.out.println(Integer.toBinaryString(c)+"对应于："+getCharFromBinaryString(Integer.toBinaryString(c)));
        }
        return stringBuilder;
    }

    public static char getCharFromBinaryString(String string) {
        char[] te = string.toCharArray();
        int[] result = new int[te.length];
        for (int i = 0; i < te.length; i++)
            result[i] = te[i] - 48;
        int sum = 0;
        for (int i = 0; i < result.length; i++)
            sum += result[result.length - 1 - i] << i;
        return (char) sum;
    }

    //写入文件及读取方式,,,由于转换得到的二进制字符串长度不一样，应该在每个二进制字符串前加上长度
    public static StringBuilder getBinaryStringForMyFile(String str) {
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder = getBinaryString(str);
    }
    ///相同的代码在t()中就能读出正确字符串，但在这里只能读出乱码
    //原先存入时需要手动加入\n换行符，也是为了读取时方便分行
    public static ArrayList<String> getStringLineFromBinaryFile(File file) {
        ArrayList<String> arrayList = new ArrayList<>();
        //读取文件内容，如果没有的话，需要先创建并写入指定格式
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            //这个怎么读取的是乱码
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            int len = 0;
            char[] buffer = new char[15];//汉子也就15位
            char[] str_t = new char[1024];
            int i=0;
            while (true) {
                String as = null;
                if (len == 0) {
                   as = bufferedReader.readLine();

                    if (as == null)
                        break;
                } else {
                    //读取一个原意字符：一行二进制字符串
                    int in = inputStreamReader.read(buffer, 0, len);

                    len = 0;
                    System.out.println("从文件读取长度："+in+""+buffer.toString()+"截取："+String.valueOf(buffer,0,len)+"转换字符："+str_t[i]);
                    //转换得到原意字符
                    str_t[i] = getCharFromBinaryString(String.valueOf(buffer,0,len));
                    i++;
                    continue;
                }
                //并不能读取\n符号，只是认为是没有，string.length == 0
                //其实可以不用\n符，如果文件以追加方式写入，那么每一次写入都会自己换行，在每一行字符串结束时并不会添加下一段的长度
                if (as.length() == 0) {
                    //原意一行字符串读取完毕：进行转换
                    len = 0;
                    arrayList.add(String.valueOf(str_t,0,i));
                    i = 0;
                    continue;
                }
                len = Integer.parseInt(as);
                if (as == null)
                    break;
                System.out.println(as);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    @Test
    //是否字符转为二进制字符串长度一样
    public void t() {
    /*    String a=null;
        //选择不添加\n原因：每次调用write写入都会重新起头
        System.out.println(a=getBinaryString("今天天气晴朗1234asadd..,;;-(+''").toString());*/
        File file = new File("t.mfwar");
        //读取文件内容，如果没有的话，需要先创建并写入指定格式
        try {
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            /*BufferedInputStream bufferedInputStream = new BufferedInputStream();*/

            int len = 0;
            char[] buffer = new char[15];//汉子也就15位
            while (true) {

                String as = null;
                if (len == 0) {
                    as = bufferedReader.readLine();
                    if (as == null)
                        break;
                } else {
                    bufferedReader.read(buffer, 0, len);
                    System.out.println(String.valueOf(buffer, 0, len));
                    len = 0;
                    continue;
                }
                //并不能读取\n符号，只是认为是没有，string.length == 0
                //其实可以不用\n符，如果文件以追加方式写入，那么每一次写入都会自己换行，在每一行字符串结束时并不会添加下一段的长度
                if (as.length() == 0) {
                    System.out.println("遇到行分割符：");
                    len = 0;
                    continue;
                }
                len = Integer.parseInt(as);
                if (as == null)
                    break;
                System.out.println(as);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /*  if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(a.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    @Test
    public void t_read_2str(){
        System.out.println("读取结果"+getStringLineFromBinaryFile(new File("t.mfwar")).toString());
    }
}
