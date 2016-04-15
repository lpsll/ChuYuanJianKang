package com.htlc.cyjk.app.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Created by sks on 2016/1/4.
 */
public class DbManager {
    public static final String DATABASE_LAST_MODIFY = "database_last_modify";

    static final String DATABASE_NAME = "city_list.db";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "CREATE TABLE area ( _id integer primary key autoincrement, AREA_CODE integer, AREA_NAME varchar(20), TYPE integer, PARENT_ID integer);";


    private static DatabaseHelper databaseHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.wtf("DatabaseHelper", "Upgrading database from version " + oldVersion + "to " +
                    newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper.getWritableDatabase();
    }

    //    public static synchronized void initialize(Context context, SQLiteOpenHelper helper) {
//        if (instance == null) {
//            instance = new DatabaseManager();
//            mDatabaseHelper = helper;
//        }
//    }
//
//    public static synchronized DatabaseManager getInstance() {
//        if (instance == null) {
//            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
//                    " is not initialized, call initialize(..) method first.");
//        }
//
//        return instance;
//    }
//    public synchronized SQLiteDatabase getDatabase() {
//        return mDatabaseHelper.getWritableDatabase();
//    }


    private static final String assets_name = "city.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    private static final String pathStr = "data/data/com.htlc.cyjk";
    //数据库存储路径
    private static final String filePath = pathStr + "/" + assets_name;

    public static SQLiteDatabase getDatabase1(Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(pathStr);
            Log.i("test", "pathStr=" + path);
            if (path.mkdir()) {
                Log.i("test", "创建成功");
            } else {
                Log.i("test", "创建失败");
            }
            ;
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open(assets_name);
                Log.i("test", is + "");
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("test", "fos=" + fos);
                Log.i("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("test", "得到");
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return getDatabase1(context);
        }
    }
}

