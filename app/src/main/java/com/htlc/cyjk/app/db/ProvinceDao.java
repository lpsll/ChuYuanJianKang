package com.htlc.cyjk.app.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.htlc.cyjk.app.App;
import com.htlc.cyjk.app.bean.CityBean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/1/4.
 */
public class ProvinceDao {

    private static final String dbfile = "city.db";

    public ArrayList<CityBean> getProvinces(){
        SQLiteDatabase database = DbManager.getDatabase(App.app);
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {"1"};
        Cursor cursor = database.query("area", columns, "TYPE=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while(cursor.moveToNext()){
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        database.close();
        return beans;
    }

    public ArrayList<CityBean> getCities(int parentId){
        SQLiteDatabase database = DbManager.getDatabase(App.app);
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {parentId+""};
        Cursor cursor = database.query("area", columns, "PARENT_ID=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while(cursor.moveToNext()){
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        database.close();
        return beans;
    }

    public ArrayList<CityBean> getCounties(int parentId){
        SQLiteDatabase database = DbManager.getDatabase(App.app);
        String[] columns = {"AREA_CODE", "AREA_NAME", "TYPE", "PARENT_ID"};
        String[] where = {parentId+""};
        Cursor cursor = database.query("area", columns, "PARENT_ID=?", where, null, null, null);
        ArrayList<CityBean> beans = new ArrayList<CityBean>();
        while(cursor.moveToNext()){
            CityBean bean = new CityBean();
            bean.area_code = cursor.getInt(cursor.getColumnIndex("AREA_CODE"));
            bean.area_name = cursor.getString(cursor.getColumnIndex("AREA_NAME"));
            bean.type = cursor.getInt(cursor.getColumnIndex("TYPE"));
            bean.parent_id = cursor.getInt(cursor.getColumnIndex("PARENT_ID"));
            beans.add(bean);
        }
        database.close();
        return beans;
    }
}
