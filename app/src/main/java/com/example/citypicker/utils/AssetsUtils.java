package com.example.citypicker.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 资产目录工具,可以在Application的时候就开始拷贝.把文件传到/data/data/包名/files/
 * Author     : 李大发
 * Date       : 2018/1/8 on 18:08
 * @version 1.0
 */

public class AssetsUtils {

    private static final String TAG = "AssetsUtils";
    /**
     * 把文件传到/data/data/files/
     * @param context
     * @param isCover 当本地已经存在相同文件的时候,是否覆盖
     * @param fileName
     */
    public static void copyFile2SdCard(Context context, boolean isCover, String fileName) {

        File file = new File(context.getFilesDir().getAbsolutePath(), fileName);
//        println(file.getPath());///data/data/com.kuchuanyun.test/files/BuSuanZi.txt
//        println(file.getAbsolutePath());///data/data/com.kuchuanyun.test/files/BuSuanZi.txt
//        try {
//            println(file.getCanonicalPath());///data/data/com.kuchuanyun.test/files/BuSuanZi.txt
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (file.exists()) {
            println(fileName+":文件已经存在!");
            if (isCover) {
                println(fileName + ":文件需要覆盖重写");
                println("文件删除成功? = " + String.valueOf(file.delete()));
            } else {//如果不覆盖已经存在的文件
                println(fileName + ":文件不需要覆盖重写");
                return;
            }
        }
        FileOutputStream fos = null;
        InputStream is = null;
        //1.获取资产管理器,从资产目录(asset)读取数据库文件, 然后写入到本地路径中
        //注意这种写法★★★★★★★★★★★★★★★★★★★★★★★★
        AssetManager assets = context.getAssets();
        //2.获取目录里的文件数组
        //遍历查找address.db文件

        try {
            //注意这种写法★★★★★★★★★★★★★★★★★★★★★★★★
            is = assets.open(fileName);
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] arr = new byte[1024*8];
            while ((len = is.read(arr)) != -1){
                fos.write(arr,0,len);
            }
        } catch (Exception e) {
            println(fileName+"文件copy过程异常");
            e.printStackTrace();
        } finally {
            try {
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                println("关流失败");
            }
        }
        println(fileName+":文件导入成功");
    }

    /**
     * 读取成String
     * @param fileName assets中文件的名称, 示例: china_city_data.json
     */
    public static synchronized String getString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 读取资源文件,返回流
     * @param context
     * @param fileName 资源文件中文件名称
     * @return
     */
    public static InputStream openFile(Context context, String fileName) {
        try {
            return context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取表情配置文件
     * @param context
     * @return
     */
    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<>();
            InputStream in = context.getResources().getAssets().open("emoji");//emoji是一个文件,不是文件夹
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void println(String message) {
//        if (MyApplication.instance.isDebugMode) {
//            System.out.println(TAG + ":" + message);
//        }
    }
}
