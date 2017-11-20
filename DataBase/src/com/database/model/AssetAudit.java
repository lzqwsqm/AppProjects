package com.database.model;

//资产流水

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.database.ListAdapter;
import com.database.MainActivity;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssetAudit
{
	public String content;
	public MyDate date;
	public int deposit_id;
	public int eid;
	public int id;
	public short kmid;
	public Date real_date;
	public float sum;
	public float nub;
	public int vid;
    
	//创建表
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE assetaudit(id integer  PRIMARY KEY AUTOINCREMENT,eid int,kmid smallint,nub float,sum float,deposit_id int ,date integer,content varchar(80) ,vid int);");
	}
    //显示
	
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("assetaudit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[9];
		arrayOfString[0] = "id";
		arrayOfString[1] = "eid";
		arrayOfString[2] = "kmid";
		arrayOfString[3] = "nub";
		arrayOfString[4] = "sum";
		arrayOfString[5] = "deposit_id";
		arrayOfString[6] = "date";
		arrayOfString[7] = "content";
		arrayOfString[8] = "vid";
		return arrayOfString;
	}
	
	public static boolean isIncome(int kmid)
	{
		boolean bool ;
		if ((kmid != 2) && (kmid != 3) && (kmid < 20))
			bool = true;
		else
			bool = false;
		return bool;
	}
    //记帐
	public static String insert(int eid, int kmid,float nub, float sum, int depositid,  Date dateinput,String content,int vid)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("insert into ").append("assetaudit").append(" values(null,").append(eid).append(",").append(kmid).append(",").append(nub).append(",").append(sum).append(",").append(depositid).append(",").append(dateinput.getTime()).append(",'").append(content).append("',").append(vid).append(");");
        DBTool.database.execSQL(localStringBuffer.toString());
        return "操作成功完成！";
    }
	
	
	public AssetAudit(int paramInt)
	{
		this.id = paramInt;
		Cursor localCursor = DBTool.database.query("assetaudit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		this.eid = localCursor.getInt(1);
		this.kmid = localCursor.getShort(2);
		this.nub = localCursor.getFloat(3);
		this.sum = localCursor.getFloat(4);
		this.deposit_id = localCursor.getInt(5);
		this.real_date = new Date(localCursor.getLong(6));
		this.date = new MyDate(this.real_date);
		this.content = localCursor.getString(7);
		this.vid = localCursor.getInt(8);
		localCursor.close();
	}
	
	public boolean isIncome()
	{
		return Asset.isIncome(this.kmid);
	}
	
	public String modify(int kmid,float nub, float sum, int despositid, String content, Date dateinput)
	{
		Asset localEvection = new Asset(this.eid);
		
		String localObject;
		//判断km
		if (this.kmid != 1)
		{
			//判断是否结束
			if (!localEvection.isFinished())
			{
				if ((this.nub !=nub) || (this.kmid != kmid))
				{
					localEvection.addSum(this.kmid,-this.nub, -this.sum);
			        localEvection.addSum(kmid,nub, sum);
				}
				
				localEvection.save();
		
				this.kmid = (short)kmid;
				this.nub = nub;
				this.sum = sum;
				this.deposit_id = despositid;
				this.content = content;
				
				if (!this.date.equals(dateinput))
				{
					this.real_date = dateinput;
					this.date = new MyDate(dateinput);
				}
				save();
				localObject = "操作成功完成！";
				return localObject;
	
			}
			else
			{
				localObject = "出差已经结束,流水不能修改/删除！";
			}
		}
		else
			localObject = "出差结束流水只能删除,不能修改！";
		return localObject;
	}
	
	void save()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("assetaudit").append(" set ").append("eid=").append(this.eid).append(",kmid=").append(this.kmid).append(",nub=").append(this.nub).append(",sum=").append(this.sum).append(",deposit_id=").append(this.deposit_id).append(",date=").append(this.real_date.getTime()).append(",content='").append(this.content).append("',vid=").append(this.vid).append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	
	public String delete()
	{
		String del; 
		Asset localObject = new Asset(this.eid);
		if (this.kmid != 1)
		{
			if (!localObject.isFinished())
			{
				del = deleteAssetAudit(localObject);

			}
			else
				del = "出差已经结束,流水不能修改/删除！";
		}
		else
			del = deleteFinishAudit(localObject);
		return del;
	}

	public String deleteAssetAudit(Asset paramEvection)
	{
		String del;
		paramEvection.addSum(this.kmid,-this.nub, -this.sum);
		paramEvection.save();
		DBTool.database.execSQL("delete from assetaudit where id=" + this.id);
		del = "操作成功完成！";
		return del;

	}

	public String deleteFinishAudit(Asset paramEvection)
	{
		paramEvection.flag = (short)(-1 + paramEvection.flag);
		paramEvection.save();
	    DBTool.database.execSQL("delete from evectionaudit where id=" + this.id);
		return "操作成功完成！";
	}
	
	
}
/*







	public boolean canBeModified()
	{
	    boolean bool  = false;
		if ((this.kmid != 1) && (!new Evection(this.eid).isFinished()))
			bool = true;
		return bool;
	}
	public static int getIdByAuditId(int paramInt)
	{
		int i = 0;
		Cursor localCursor = getRows("vid=" + paramInt, null);
		if (localCursor.getCount() != 0)
		{
			localCursor.moveToFirst();
			i = localCursor.getInt(i);
			localCursor.close();
		}
		else
		{
			localCursor.close();
		}
		return i;
	}


}
	
	
	
/*

 
/* 
 public static String deleteByAuditId(int paramInt)
  {
    int i = getIdByAuditId(paramInt);
    String str;
    if (i != 0)
      str = new EvectionAudit(i).delete();
    else
      str = "出差结束流水不存在!";
    return str;
  }








  public String deleteAuditProjectAudit(Evection paramEvection)
  {
    paramEvection.addSum(this.kmid, -this.sum);
    paramEvection.save();
    new Audit(this.vid).deleteWithForce();
    DBTool.database.execSQL("delete from evectionaudit where id=" + this.id);
    return "操作成功完成！";
  }


  public String getKmName()
  {
    return new Evection(this.eid).getKmName(this.kmid);
  }






}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.EvectionAudit
 * JD-Core Version:    0.6.0
 */

