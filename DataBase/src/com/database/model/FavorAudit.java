/*
 * 人情流水
 */

package com.database.model;

import android.R.integer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.util.MyDate;
import java.util.Date;

public class FavorAudit
{
  public String content;
  public MyDate date;
  public int deposit_id;
  public int eid;
  public int id;
  public short kmid;
  public Date real_date;
  public long sum;
  public int vid;
  /*
   * 创建表
   */
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE favoraudit(id integer  PRIMARY KEY AUTOINCREMENT,eid int,kmid smallint,sum integer,deposit_id int ,date integer,content varchar(80) ,vid int);");
	}
	
	//--------项目显示----
	/*
	 * 获取行
	 *public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 

	 */
	public static Cursor getRows(String selection, String orderBy)
	{
		return DBTool.database.query("favoraudit", getColumnString(), selection, null, null, null, orderBy);
	}
	
	/*
     * 获取列
     */
	public static String[] getColumnString()
	{
		String[] column = new String[8];
		column[0] = "id";
		column[1] = "eid";
		column[2] = "kmid";
		column[3] = "sum";
		column[4] = "deposit_id";
		column[5] = "date";
		column[6] = "content";
		column[7] = "vid";
		return column;
	}
	
	
	
	
	
	public static boolean isIncome(int kmid)
	{
		boolean bool ;
		if ((kmid != 2) && (kmid != 3) && (kmid < 20))
			bool = false;
		else
			bool = true;
		return bool;

		
	//	return EvectionKm.isIncome(paramInt);
	}
	public FavorAudit(int paramInt)
	{
		this.id = paramInt;
		Cursor localCursor = DBTool.database.query("favoraudit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		this.eid = localCursor.getInt(1);
		this.kmid = localCursor.getShort(2);
		this.sum = localCursor.getLong(3);
		this.deposit_id = localCursor.getInt(4);
		this.real_date = new Date(localCursor.getLong(5));
		this.date = new MyDate(this.real_date);
		this.content = localCursor.getString(6);
		this.vid = localCursor.getInt(7);
		localCursor.close();
	}
	//记帐
	public static String insert(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, String paramString, Date paramDate)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("insert into ").append("favoraudit").append(" values(null,").append(paramInt1).append(",").append(paramInt2).append(",").append(paramLong).append(",").append(paramInt3).append(",").append(paramDate.getTime()).append(",'").append(paramString).append("',").append(paramInt4).append(");");
        DBTool.database.execSQL(localStringBuffer.toString());
        return "操作成功完成！";
    }
	
	/*
	 * 修改
	 */
	public String modify(int kmid, long sum, int deposit_id, String content, Date dateinput)
	{
		Favor localEvection = new Favor(this.eid);
		String localObject="";
		//if (this.kmid != 1)
		//{
		//	if (!localEvection.isFinished())
		//	{
				Deposit localDeposit = new Deposit(this.deposit_id);
				if (this.deposit_id == deposit_id)
				{
					if (this.sum != sum)
						if (!isIncome())
						{
							if (!localDeposit.isOverSum(sum - this.sum))
							{
								localDeposit.modifySum(this.sum - sum);
							}
							else
							{
								localObject = "余额不足！";
								return localObject;
						//		break label415;
							}
						}
						else if (!localDeposit.isOverSum(this.sum - sum))
						{
							localDeposit.modifySum(sum + -this.sum);
						}
						else
						{
							localObject = "余额不足！";
							return localObject;
					//		break label415;
						}
				}
				else
				{
					Deposit localObject1 = new Deposit(deposit_id);
					if (!isIncome())
					{
						if (!localObject1.isOverSum(sum))
						{
							localDeposit.restoreSum(this.sum);
							localObject1.addSum(-sum);
						}
						else
						{
							localObject = "余额不足！";
							return localObject;
					//		break label415;
						}
					}
					else
					{
						if (localDeposit.isOverSum(this.sum))
						{
						    localObject = "余额不足！";
					//		break label396;
						    return localObject;
						}
						localDeposit.restoreSum(-this.sum);
						localObject1.addSum(sum);
					}
				}
				if ((this.sum != sum) || (this.kmid != kmid))
				{
					localEvection.addSum(this.kmid, -this.sum);
					localEvection.addSum(kmid, sum);
				}
				localEvection.save();
				int akmid;
				if (!FavorAudit.isIncome(kmid))
                    akmid = 266;
                else
                	akmid = 297;
				new Audit(this.vid).modify(akmid, sum, deposit_id, dateinput,content);
				this.kmid = (short)kmid;
				this.sum = sum;
				this.deposit_id = deposit_id;
				this.content = content;
				if (!this.date.equals(dateinput))
				{
					this.real_date = dateinput;
					this.date = new MyDate(dateinput);
				}
				save();
				localObject = "操作成功完成！";
				return localObject;
			//	break label415;
			//	label396: localObject = "余额不足！";
		//	}
			//else
		//	{
		//		localObject = "出差已经结束,流水不能修改/删除！";
		//	}
		//}
		//else
		//	localObject = "出差结束流水只能删除,不能修改！";
	//	return localObject;
	}
	
	/*
	 * 删除流水
	 */
	public String delete()
	{
		String del; 
		Favor localObject = new Favor(this.eid);
	
		if (!localObject.isFinished())
			del = deleteEvectionAudit(localObject);
		else
			del = "出差已经结束,流水不能修改/删除！";
	
		return del;
	}
	
	public String deleteEvectionAudit(Favor paramEvection)
	{
		String del;
		Deposit localObject = new Deposit(this.deposit_id);
		//判断支出还是收入
		if (!isIncome())
		{
			localObject.restoreSum(this.sum);
		}
		else
		{
			if (localObject.isOverSum(this.sum))
			{
			    del = "余额不足！";
			    return del;
			}
			localObject.restoreSum(-this.sum);
		}
		
		paramEvection.addSum(this.kmid, -this.sum);
		paramEvection.save();
		
		if (this.vid > 0)
		    new Audit(this.vid).deleteWithForce();
		DBTool.database.execSQL("delete from favoraudit where id=" + this.id);
		del = "操作成功完成！";
		return del;

	}

	public String deleteFinishAudit(Evection paramEvection)
	{
		paramEvection.flag = (short)(-1 + paramEvection.flag);
		paramEvection.save();
		if (this.vid > 0)
	//		new Audit(this.vid).deleteWithForce();
		DBTool.database.execSQL("delete from evectionaudit where id=" + this.id);
		return "操作成功完成！";
	}
	
	public boolean isIncome()
	{
		return Favor.isIncome(this.kmid);
	}
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

	
	void save()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("Favoraudit").append(" set ").append("eid=").append(this.eid).append(",kmid=").append(this.kmid).append(",sum=").append(this.sum).append(",deposit_id=").append(this.deposit_id).append(",date=").append(this.real_date.getTime()).append(",content='").append(this.content).append("',vid=").append(this.vid).append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	public String getKmName()
	{
	    return new Favor(this.eid).getKmName(this.kmid);
	}
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
    }
/*

 
/* 
 








  public String deleteAuditProjectAudit(Evection paramEvection)
  {
    paramEvection.addSum(this.kmid, -this.sum);
    paramEvection.save();
    new Audit(this.vid).deleteWithForce();
    DBTool.database.execSQL("delete from evectionaudit where id=" + this.id);
    return "操作成功完成！";
  }


  






}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.EvectionAudit
 * JD-Core Version:    0.6.0
 */
