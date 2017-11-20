/*
 * 项目流水
 */
package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.DBTool;
import com.database.util.MyDate;
import java.util.Date;

public class EvectionAudit
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

	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE evectionaudit(id integer  PRIMARY KEY AUTOINCREMENT,eid int,kmid smallint,sum integer,deposit_id int ,date integer,content varchar(80) ,vid int);");
	}
	
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("evectionaudit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	
	
	public static String[] getColumnString()
	{
		String[] arrayOfString = new String[8];
		arrayOfString[0] = "id";
		arrayOfString[1] = "eid";
		arrayOfString[2] = "kmid";
		arrayOfString[3] = "sum";
		arrayOfString[4] = "deposit_id";
		arrayOfString[5] = "date";
		arrayOfString[6] = "content";
		arrayOfString[7] = "vid";
		return arrayOfString;
	}
	
	public static boolean isIncome(int paramInt)
	{
		boolean bool ;
		if ((paramInt != 2) && (paramInt != 3) && (paramInt < 20))
			bool = false;
		else
			bool = true;
		return bool;

		
	//	return EvectionKm.isIncome(paramInt);
	}
	public static String insert(int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4, String paramString, Date paramDate)
    {
        StringBuffer localStringBuffer = new StringBuffer();
        localStringBuffer.append("insert into ").append("evectionaudit").append(" values(null,").append(paramInt1).append(",").append(paramInt2).append(",").append(paramLong).append(",").append(paramInt3).append(",").append(paramDate.getTime()).append(",'").append(paramString).append("',").append(paramInt4).append(");");
        DBTool.database.execSQL(localStringBuffer.toString());
        return "操作成功完成！";
    }

	public EvectionAudit(int paramInt)
	{
		this.id = paramInt;
		Cursor localCursor = DBTool.database.query("evectionaudit", getColumnString(), "id=" + paramInt, null, null, null, null);
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
	public String delete()
	{
		String del; 
		Evection localObject = new Evection(this.eid);
		if (this.kmid != 1)
		{
			if (!localObject.isFinished())
			{
		//		if (!((Evection)localObject).isAuditProject())
					del = deleteEvectionAudit(localObject);
	//			else
		//			localObject = deleteAuditProjectAudit((Evection)localObject);
			}
			else
				del = "出差已经结束,流水不能修改/删除！";
		//	return del;
		}
		else
			del = deleteFinishAudit(localObject);
		return del;
	}
	
	public String deleteEvectionAudit(Evection paramEvection)
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
		
		if (this.kmid != 2)
		{
			if (this.kmid == 4)
				Account.addAccountSum(5, this.sum);
		}
		else
			Account.addAccountSum(5, -this.sum);
		
		Virement.deleteRow(this.vid);
		DBTool.database.execSQL("delete from evectionaudit where id=" + this.id);
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
		return Evection.isIncome(this.kmid);
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

	public String modify(int paramInt1, long paramLong, int paramInt2, String paramString, Date paramDate)
	{
		Evection localEvection = new Evection(this.eid);
		String localObject;
		if (this.kmid != 1)
		{
			if (!localEvection.isFinished())
			{
				Deposit localDeposit = new Deposit(this.deposit_id);
				if (this.deposit_id == paramInt2)
				{
					if (this.sum != paramLong)
						if (!isIncome())
						{
							if (!localDeposit.isOverSum(paramLong - this.sum))
							{
								localDeposit.modifySum(this.sum - paramLong);
							}
							else
							{
								localObject = "余额不足！";
								return localObject;
						//		break label415;
							}
						}
						else if (!localDeposit.isOverSum(this.sum - paramLong))
						{
							localDeposit.modifySum(paramLong + -this.sum);
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
					Deposit localObject1 = new Deposit(paramInt2);
					if (!isIncome())
					{
						if (!localObject1.isOverSum(paramLong))
						{
							localDeposit.restoreSum(this.sum);
							localObject1.addSum(-paramLong);
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
						localObject1.addSum(paramLong);
					}
				}
				if ((this.sum != paramLong) || (this.kmid != paramInt1))
				{
					localEvection.addSum(this.kmid, -this.sum);
					localEvection.addSum(paramInt1, paramLong);
				}
				localEvection.save();
				if ((paramInt1 != 2) || (this.sum == paramLong))
				{
					if ((paramInt1 == 4) && (this.sum != paramLong))
						Account.addAccountSum(5, this.sum - paramLong);
				}
				else
					Account.addAccountSum(5, paramLong - this.sum);
				new Virement(this.vid).modify(paramLong, paramInt2, paramDate);
				this.kmid = (short)paramInt1;
				this.sum = paramLong;
				this.deposit_id = paramInt2;
				this.content = paramString;
				if (!this.date.equals(paramDate))
				{
					this.real_date = paramDate;
					this.date = new MyDate(paramDate);
				}
				save();
				localObject = "操作成功完成！";
				return localObject;
			//	break label415;
			//	label396: localObject = "余额不足！";
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
		localStringBuffer.append("update ").append("evectionaudit").append(" set ").append("eid=").append(this.eid).append(",kmid=").append(this.kmid).append(",sum=").append(this.sum).append(",deposit_id=").append(this.deposit_id).append(",date=").append(this.real_date.getTime()).append(",content='").append(this.content).append("',vid=").append(this.vid).append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
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


  public String getKmName()
  {
    return new Evection(this.eid).getKmName(this.kmid);
  }






}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.EvectionAudit
 * JD-Core Version:    0.6.0
 */
