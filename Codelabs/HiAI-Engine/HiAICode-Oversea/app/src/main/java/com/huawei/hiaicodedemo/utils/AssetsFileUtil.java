package com.huawei.hiaicodedemo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AssetsFileUtil {

    public static Bitmap getBitmapByFilePath(Context context,String filePath){
        try{
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(filePath);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<Bitmap> getBitmapListByDirPath(Context context,String dirPath){
        List<Bitmap> list = new ArrayList<Bitmap>();
        try{
            AssetManager assetManager = context.getResources().getAssets();
            String [] photos = assetManager.list(dirPath);
            for(String photo : photos){
                if(isFile(photo)){
                    Bitmap bitmap = getBitmapByFilePath(context,dirPath + "/" + photo);
                    list.add(bitmap);
                }else {
                    List<Bitmap> childList = getBitmapListByDirPath(context,dirPath + "/" + photo);
                    list.addAll(childList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getFileNameListByDirPath(Context context,String dirPath){
        List<String> list = new ArrayList<String>();
        try{
            AssetManager assetManager = context.getResources().getAssets();
            String [] photos = assetManager.list(dirPath);
            for(String photo : photos){
                if(isFile(photo)){
                    list.add(dirPath + "/" + photo);
                }else {
                    List<String> childList = getFileNameListByDirPath(context,dirPath + "/" + photo);
                    list.addAll(childList);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isFile(String fileName){
        if(fileName.contains(".")){
            return true;
        }else {
            return false;
        }
    }

}
