package t1113;

import org.junit.Test;

import java.io.*;

public class File2JZ {
    @Test
    public void t1(){
        File file = new File("game-no-over.tcs");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件fail");
            }
        }
        //写入字节
        OutputStream outputStream = null;
        byte[] bytes = new byte[1024];
        try (OutputStream outputStream1=new FileOutputStream(file)){
            outputStream1.write("abcd".getBytes());
        }catch (FileNotFoundException e){
            System.out.println(e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
    }
    @Test
    public void t2(){
        try{
            DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("game-no-over.tcs")));
//            dataOutputStream.write("qwert".getBytes());
            char[] str = "aaa".toCharArray();
            System.out.println(Integer.toBinaryString(str[0]));
            //直接转2进制
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
