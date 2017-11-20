package com.database.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.List;


public class AssetKm {
	
	public short flag = 0;
    public int id ;
    public short keyflag = 0;
    public String name = null;
    public short pid = 0;
    public short rank = 0;
    //创建表
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE assetkm(id integer PRIMARY KEY AUTOINCREMENT, name varchar(16),pid smallint ,flag smallint,keyflag smallint,rank smallint);");
		initData(db);
	}
	//初始化
	static void initData(SQLiteDatabase db)
	{
		db.execSQL("insert into assetkm values(null,'类一',0,1,1,1)");
		db.execSQL("insert into assetkm values(null,'类二',0,1,1,1)");
	}
	
	
	//显示
	public static void fillList(ListAdapter listadapter, List<Integer> index)
	{
        listadapter.clear();
        index.clear();
        index.add(Integer.valueOf(2147483394));
        
        ArrayList listdata = new ArrayList();
        //标题
	    String[] title = new String[1];
        title[0] = "科目名称";
        listdata.add(title);
        
        int[] width_layout = new int[2];
        width_layout[0] = 50;
        width_layout[1] = 200;
        listadapter.setLayout(width_layout);
        
        Cursor cursor = getRows(" flag=1 and pid=0", "");	
        
        
   
		for ( cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext())
        {
            String[] tp = new String[1];
            tp[0] = cursor.getString(1);
            listdata.add(tp);
            index.add(Integer.valueOf(cursor.getInt(0)));
        } 
        cursor.close();
        listadapter.append(listdata);
        
    }
	
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("assetkm", getColumnString(), paramString1, null, null, null, paramString2);
	}
	
	public static String[] getColumnString()
	{
		String[] column = new String[6];
		column[0] = "id";
		column[1] = "name";
		column[2] = "pid";
		column[3] = "flag";
		column[4] = "keyflag";
		column[5] = "rank";
		return column;
	
	}
	//////////////////
	//新增类别
	public static String addKm(String name)
    {
		String str;
		if (name.length() != 0)
        {
			DBTool.database.execSQL("insert into assetkm values(null,'"+name+"',0,1,1,1)");
			str = "操作成功完成！";
        }
		else 
		{
			str = "名字不允许为空！";
		}
		
		return str;
    }
	
	//修改
	public AssetKm(int id)
	{
		reset(id);
	}
	public void reset(int id)
	{
		Cursor localCursor = DBTool.database.query("assetkm", getColumnString(), "id=" + id, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.name = paramCursor.getString(1);
		this.pid = paramCursor.getShort(2);
		this.flag = paramCursor.getShort(3);
		this.keyflag = paramCursor.getShort(4);
		this.rank = paramCursor.getShort(5);
	}
	//修改
	public String modify(String name)
	{
		String str;
		if (name.length() != 0)
		{
			DBTool.database.execSQL("update assetkm set name='" + name + "' where id=" + this.id);
			str = "操作成功完成！";
		}
		else
		{
			str = "名字不允许为空！";
		}
		return str;
	}
	//删除
	public String delete()
	{
		String str;
		Cursor cursor = DBTool.database.query("asset", Asset.getColumnString(), "type=" + this.id, null, null, null, null);
		if (cursor.getCount() <= 0)
		{
			cursor.close();
			DBTool.database.execSQL("delete from assetkm where id=" + this.id);
			str = "操作成功完成！";
		}
		else
		{
			cursor.close();
			str = "存在该类别流水,不允许删除！";
		}
		return str;
	}
	
	//获得类型列表
	public static List<String> getAssetTypeList(int paramInt, List<Integer> index)
	{
		ArrayList localArrayList = new ArrayList();
		String condition = " flag=1 and pid=" + paramInt;
		Cursor localCursor = getRows(condition, "id");
		
		for (localCursor.moveToFirst(); !localCursor.isAfterLast();	localCursor.moveToNext())
		{
			localArrayList.add(localCursor.getString(1));
			index.add(Integer.valueOf(localCursor.getInt(0)));
	    }
		localCursor.close();
		return localArrayList;
	}
	
	
	
	
	
	
	

}
