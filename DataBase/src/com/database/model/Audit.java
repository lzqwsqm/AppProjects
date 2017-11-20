package com.database.model;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import com.tomoney.finance.context.RuntimeInfo;
//import com.tomoney.finance.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
//import com.tomoney.finance.view.FinanceAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class Audit
{
	public String content = null;
	public MyDate date = null;
	public short deposit_id = 0;
	public  int id = 0;
	public short kmid = 0;
	public int member;
	public Date real_date = null;
	public long sum = 0L;
	public int vid = 0;
	
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE audit(id integer PRIMARY KEY AUTOINCREMENT,kmid smallint,sum int ,vid int,deposit_id smallint,date integer, content varchar(80),member smallint);");
	}
	
	//显示
	public static Cursor getRows(String paramString1, String paramString2)
	{
		return DBTool.database.query("audit", getColumnString(), paramString1, null, null, null, paramString2);
	}
	/*
     * 获取列
     */
	public static String[] getColumnString()
	{
		String[] column = new String[8];
		column[0] = "id";
		column[1] = "kmid";
		column[2] = "sum";
		column[3] = "vid";
		column[4] = "deposit_id";
		column[5] = "date";
		column[6] = "content";
		column[7] = "member";
		return column;
	}
	/*
     * 获取科目
     */
	public static String getKm2Name(int paramInt1, int paramInt2)
	{
		String kmname = "";
		KM km = new KM(paramInt1);
		//if (paramInt1 != 265)
		//{
		//	if ((paramInt1 != 266) && (paramInt1 != 297))
				kmname = km.name;
		//	else
		//		kmname = new FavorAudit(FavorAudit.getIdByAuditId(paramInt2)).getKmName();
		//}
//		else
	//		localObject = new EvectionAudit(EvectionAudit.getIdByAuditId(paramInt2)).getKmName();
		return kmname;
	}
	
	
	
	/*
	 * 记帐
	 */
	public static String insert(int kmid, long sum, int deposit_id, Date dateinput, String content)
	{
		
		String str2;
		if (sum > 0L)
		{
			int vid = 0;
			
			if (deposit_id > 0)
			{
				Deposit localDeposit = new Deposit(deposit_id);
		//		String str1;
				if (kmid < 275)
				{
					if (!localDeposit.isOverSum(sum))
					{
						localDeposit.addSum(-sum);
						vid = Virement.insert(1, sum, deposit_id, 0, dateinput);
					}
					else
					{
						str2 = "余额不足！";
						return str2;
				//		break label126;
					}
				}
				else
				{
					localDeposit.addSum(sum);
					vid = Virement.insert(2, sum, deposit_id, 0, dateinput);
				}
			}
			//写入
			insertRow(kmid, sum, vid, deposit_id, dateinput, content, 1);
			//写入并修改报表
			Report.modifyReportSum(new MyDate(dateinput), kmid, sum);
			str2 = "操作成功完成！";
		}
		else
		{
			str2 = "金额错误！";
		}
		return str2;
	}
	
	static void insertRow(int paramInt1, long paramLong, int paramInt2, int paramInt3, Date paramDate, String paramString, int paramInt4)
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("insert into ").append("audit").append(" values(null,").append(paramInt1).append(",").append(paramLong).append(",").append(paramInt2).append(",").append(paramInt3).append(",").append(paramDate.getTime()).append(",'").append(paramString).append("',").append(paramInt4).append(");");
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	//
	/*
	 *修改 
	 */
	public String modify(int kmid, long sum, int deposit_id, Date dateinput, String content)
	{
		String note;
		if (sum > 0L)
		{
			KM km = new KM(this.kmid);
			if (this.vid > 0)
				if (km.pid != 1)
				{
					Deposit deposit = new Deposit(deposit_id);
					if (this.deposit_id == deposit_id)
					{
						if (this.sum != sum)
							if (!deposit.isOverSum(sum - this.sum))
							{
								deposit.modifySum(this.sum - sum);
							}
							else
							{
								note = "余额不足！";
								return note;
							}
					}
					else if (!deposit.isOverSum(sum))
					{
						deposit.addSum(-sum);
						new Deposit(this.deposit_id).restoreSum(this.sum);
					}
					else
					{
						note = "余额不足！";
						return note;
					}
				}
				else
				{
					Deposit deposit1 = new Deposit(this.deposit_id);
					if (this.deposit_id == deposit_id)
					{
						if (this.sum != sum)
							if (!deposit1.isOverSum(this.sum - sum))
							{
								deposit1.modifySum(sum + -this.sum);
							}
							else
							{
								note = "余额不足！";
								return note;
							}
					}
					else
					{
						if (deposit1.isOverSum(this.sum))
						{
							note = "余额不足！";
							return note;
						}
						deposit1.restoreSum(-this.sum);
						new Deposit(deposit_id).addSum(sum);
					}
				}
			MyDate localObject = new MyDate(dateinput);
			if ((this.date.getYear() == (localObject.getYear()) && (this.date.getMonth() == localObject.getMonth())))
			{
				if (this.kmid == kmid)
				{
					if (this.sum != sum)
						Report.modifyReportSum(this.date, this.kmid, sum - this.sum);
				}
				else
				{
					Report.modifyReportSum(this.date, this.kmid, -this.sum);
					Report.modifyReportSum(this.date, kmid, sum);
				}
			}
			else
			{
				Report.modifyReportSum(this.date, this.kmid, -this.sum);
				Report.modifyReportSum(localObject, kmid, sum);
			}
			if (this.vid > 0)
			{
				Virement localObject1 = new Virement(this.vid);
				localObject1.modify(sum + localObject1.sum - this.sum, deposit_id, dateinput);
			}
			this.kmid = (short)kmid;
			this.sum = sum;
			this.deposit_id = (short)deposit_id;
			if (!this.date.equals(dateinput))
			{
				this.real_date = dateinput;
				this.date = new MyDate(dateinput);
			}
			this.content = content;
			save();
			note = "操作成功完成！";
			return note;
			//label482: note = "余额不足！";
		}
		else
		{
			note = "金额错误！";
		}
		 return note;
	}
	public void save()
	{
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("update ").append("audit").append(" set kmid=").append(this.kmid).append(",sum=").append(this.sum).append(",vid=").append(this.vid).append(",deposit_id=").append(this.deposit_id).append(",date=").append(this.real_date.getTime()).append(",content='").append(this.content).append("' where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
	}
	/////
	
	public String delete()
	{
		String str="";
		if (this.vid >= 0)
		{
			if ((this.kmid != 273) && (this.kmid != 294) && (this.kmid != 265))
			{
				if ((this.kmid != 274) && (this.kmid != 293) && (this.kmid != 268) && (this.kmid != 271) && (this.kmid != 292))
				{
					if ((this.kmid != 270) && (this.kmid != 295))
					{
						if ((this.kmid != 266) && (this.kmid != 297))
							str = deleteWithForce();
						else
						{
							str = FavorAudit.deleteByAuditId(this.id);
						}
					}
					else
					{
				//		str = InvestAudit.deleteByAuditId(this.id);
					}
				}
				else
				{
					str = CreditAudit.deleteByAuditId(this.id);
				}
			}
			else
			{
				str = EvectionAudit.deleteByAuditId(this.id);
			}
		}
		else
		{
			str = deleteAuditPosOrder();
		}
		return str;
	}
	public String deleteAuditPosOrder()
	{
		Audit localAudit = new Audit(-this.vid);
		Object localObject;
		if (localAudit.sum > this.sum)
		{
			localObject = new Deposit(localAudit.deposit_id);
			((Deposit)localObject).modifySum(this.sum);
			((Deposit)localObject).save();
			localAudit.sum -= this.sum;
			localAudit.save();
			new Virement(localAudit.vid).modify(localAudit.sum);
			Report.modifyReportSum(localAudit.date, this.kmid, -this.sum);
			DBTool.database.execSQL("delete from audit  where id=" + this.id);
			localObject = "操作成功完成！";
		}
		else
		{
			localObject = localAudit.deleteAuditPos();
		}
		return (String)localObject;
	}
	public String deleteAuditPos()
	{
		Deposit localDeposit = new Deposit(this.deposit_id);
		localDeposit.restoreSum(this.sum);
		localDeposit.save();
		Cursor localCursor = getRows("vid=-" + this.id, null);
		localCursor.moveToFirst();
		Report localReport1 = new Report(2, this.date);
		Report localReport2 = new Report(1, this.date);
		while (!localCursor.isAfterLast())
		{
			int i = localCursor.getShort(1);
			long l = localCursor.getLong(2);
			localReport1.addKmSum(i, -l);
			localReport2.addKmSum(i, -l);
			localCursor.moveToNext();
		}
		localReport1.save();
		localReport2.save();
		localCursor.close();
		new Virement(this.vid).delete();
		DBTool.database.execSQL("delete from audit  where vid=-" + this.id);
		DBTool.database.execSQL("delete from audit  where id=" + this.id);
		return "操作成功完成！";
	}
	public static Audit getAuditByVid(int paramInt)
	{
		Audit localObject = null;
		
		Cursor localCursor = DBTool.database.query("audit", getColumnString(), "vid=" + paramInt, null, null, null, null);
		if (localCursor.getCount() != 0)
		{
			localCursor.moveToFirst();
			Audit localObject1 = new Audit();
			localObject1.reset(localCursor);
			localCursor.close();
			localObject = localObject1;
		}
		else
		{
			localCursor.close();
		}
		return localObject;
	}
	public Audit()
	  {
	  }
	
	public String getKm1Name()
	{
		return getKm1Name(this.kmid);
	}
	public static String getKm1Name(int paramInt)
	{
		Object localObject = new KM(paramInt);
		if ((paramInt != 265) && (paramInt != 266) && (paramInt != 297) && (paramInt != 268))
			localObject = new KM(((KM)localObject).pid).name;
		else
			localObject = ((KM)localObject).name;
		return (String)localObject;
	}
	public String getKm2Name()
	{
		return getKm2Name(this.kmid, this.id);
	}

	public static int insertSystemAudit(int paramInt1, long paramLong, int paramInt2, int paramInt3, Date paramDate, String paramString)
	{
		insertRow(paramInt1, paramLong, paramInt3, paramInt2, paramDate, paramString, 1);
		int i = DBTool.getMaxId("audit");
		Report.modifyReportSum(new MyDate(paramDate), paramInt1, paramLong);
		return i;
	}
	
	//首页显示
	public static long getOutcomeAfter(long paramLong)
	{
		long l = 0L;
		Cursor localCursor = getRows("kmid<275 and date>=" + paramLong, null);
		localCursor.moveToFirst();
		while (!localCursor.isAfterLast())
		{
			l += localCursor.getLong(2);
			localCursor.moveToNext();
		}
		return l;
	}
	public static long getIncomeAfter(long paramLong)
	{
		long l = 0L;
		Cursor localCursor = getRows("kmid>275 and date>=" + paramLong, null);
		localCursor.moveToFirst();
		while (!localCursor.isAfterLast())
		{
			l += localCursor.getLong(2);
			localCursor.moveToNext();
		}
		return l;
	}
	
	
	
	public Audit(int paramInt)
	{
		Cursor localCursor = DBTool.database.query("audit", getColumnString(), "id=" + paramInt, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	public void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.kmid = paramCursor.getShort(1);
		this.sum = paramCursor.getLong(2);
		this.vid = paramCursor.getInt(3);
		this.deposit_id = paramCursor.getShort(4);
		this.real_date = new Date(paramCursor.getLong(5));
		paramCursor.getLong(5);
		this.date = new MyDate(this.real_date);
		this.content = paramCursor.getString(6);
		this.member = paramCursor.getShort(7);
	}
	
	public String deleteWithForce()
	{
		KM localObject = new KM(this.kmid);
		String note;
		if (this.vid > 0)
		{
			Deposit localDeposit = new Deposit(this.deposit_id);
			if (((KM)localObject).pid != 1)
			{
				localDeposit.restoreSum(this.sum);
			}
			else
			{
				if (localDeposit.isOverSum(this.sum))
				{	
					note = "余额不足！";
					return note;
				}
				else
				{
				    localDeposit.restoreSum(-this.sum);
			    }
			}
		}
		Report.modifyReportSum(this.date, this.kmid, -this.sum);
		if (this.vid > 0)
		{
			Virement virement = new Virement(this.vid);
			if (virement.sum > this.sum)
				virement.modify(virement.sum - this.sum);
			else
				virement.deleteRow();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("delete from ").append("audit").append(" where id=").append(this.id);
		DBTool.database.execSQL(buffer.toString());
		note = "操作成功完成！";
	
		 return note;
	}
	
	public String deleteWithoutVirement()
	{
		Report.modifyReportSum(this.date, this.kmid, -this.sum);
		StringBuffer localStringBuffer = new StringBuffer();
		localStringBuffer.append("delete from ").append("audit").append(" where id=").append(this.id);
		DBTool.database.execSQL(localStringBuffer.toString());
		return "操作成功完成！";
	}
	
	}
	/*	public Audit()
	{
	}



	public Audit(int paramInt, long paramLong, String paramString)
	{
		this.kmid = (short)paramInt;
		this.sum = paramLong;
		this.content = paramString;
	}



	public static void fillListOfKmStat(FinanceAdapter paramFinanceAdapter, Cursor paramCursor, List<Integer> paramList)
	{
		paramList.clear();
		paramFinanceAdapter.clear();
		paramList.add(Integer.valueOf(2147483394));
		ArrayList localArrayList = new ArrayList();
		Object localObject = new String[2];
		localObject[0] = "科目";
		localObject[1] = "余额";
		localArrayList.add(localObject);
		localObject = RuntimeInfo.sum;
		if (RuntimeInfo.sum == null)
		{
			localObject = new long[302];
			while (!paramCursor.isAfterLast())
			{
				int j = paramCursor.getShort(1);
				long l = paramCursor.getLong(2);
				if (j >= 275)
				{
					if (j < 301)
					{
						localObject[j] = (l + localObject[j]);
						localObject[1] = (l + localObject[1]);
					}
				}
				else
				{
					localObject[j] = (l + localObject[j]);
					j = new KM(j).pid;
					localObject[j] = (l + localObject[j]);
					localObject[2] = (l + localObject[2]);
				}
				paramCursor.moveToNext();
			}
			RuntimeInfo.sum = (J)localObject;
		}
		KM localKM = new KM();
		for (int i = 0; i < 2; i++)
		{
			localKM.reset(i + 1);
			String[] arrayOfString;
			if (localObject[(i + 1)] <= 0L)
			{
				arrayOfString = new String[2];
				arrayOfString[0] = ("-" + localKM.name);
				arrayOfString[1] = Convertor.sumToString(localObject[(i + 1)]);
				localArrayList.add(arrayOfString);
			}
			else
			{
				arrayOfString = new String[2];
				arrayOfString[0] = ("+" + localKM.name);
				arrayOfString[1] = Convertor.sumToString(localObject[(i + 1)]);
				localArrayList.add(arrayOfString);
			}
			paramList.add(Integer.valueOf(localKM.id));
		}
		localObject = new int[3];
		localObject[0] = 40;
		localObject[1] = 120;
		localObject[2] = 160;
		paramFinanceAdapter.setLayout(localObject);
		paramFinanceAdapter.append(localArrayList);
	}

	

	




	public static String insertAuditPos(Vector<Audit> paramVector, long paramLong, int paramInt, Date paramDate, String paramString)
	{
		Object localObject = new Deposit(paramInt);
		if (!((Deposit)localObject).isOverSum(paramLong))
		{
			((Deposit)localObject).addSum(-paramLong);
			insertRow(320, paramLong, Virement.insert(1, paramLong, paramInt, 0, paramDate), paramInt, paramDate, paramString, 1);
			int i = DBTool.getMaxId("audit");
			for (int j = 0; j < paramVector.size(); j++)
			{
				localObject = (Audit)paramVector.get(j);
				insertSystemAudit(((Audit)localObject).kmid, ((Audit)localObject).sum, paramInt, -i, paramDate, ((Audit)localObject).content);
			}
			localObject = "操作成功完成！";
		}
		else
		{
			localObject = "余额不足！";
		}
		return (String)localObject;
	}

	public static int insertAuditWithoutReport(int paramInt1, long paramLong, int paramInt2, int paramInt3, Date paramDate, String paramString)
	{
		insertRow(paramInt1, paramLong, paramInt3, paramInt2, paramDate, paramString, 1);
		return DBTool.getMaxId("audit");
	}





	
	
	


	

	public String insertAuditPosOrder(int paramInt1, int paramInt2, String paramString)
	{
		Object localObject;
		if (paramInt2 > 0)
		{
			localObject = new Deposit(this.deposit_id);
			if (!((Deposit)localObject).isOverSum(paramInt2))
			{
				((Deposit)localObject).addSum(-paramInt2);
				insertSystemAudit(paramInt1, paramInt2, this.deposit_id, -this.id, this.real_date, paramString);
				this.sum += paramInt2;
				save();
				new Virement(this.vid).modify(this.sum);
				localObject = "操作成功完成！";
			}
			else
			{
				localObject = "余额不足!";
			}
		}
		else
		{
			localObject = "金额不对!";
		}
		return (String)localObject;
	}


	public String modifyAuditPos(long paramLong, int paramInt, Date paramDate, String paramString)
	{
		if (paramInt == this.deposit_id)
		{
			if (paramLong != this.sum)
			{
				localObject = new Deposit(this.deposit_id);
				if (!((Deposit)localObject).isOverSum(paramLong - this.sum))
				{
					((Deposit)localObject).modifySum(this.sum - paramLong);
					((Deposit)localObject).save();
				}
				else
				{
					localObject = "余额不足!";
					break label484;
				}
			}
		}
		else
		{
			localObject = new Deposit(paramInt);
			if (((Deposit)localObject).isOverSum(paramLong))
				break label479;
			((Deposit)localObject).addSum(-paramLong);
			((Deposit)localObject).save();
			localObject = new Deposit(this.deposit_id);
			((Deposit)localObject).restoreSum(this.sum);
			((Deposit)localObject).save();
		}
		Object localObject = new FDate(paramDate);
		if ((this.date.getYear() != ((FDate)localObject).getYear()) || (this.date.getMonth() != ((FDate)localObject).getMonth()))
		{
			Cursor localCursor = getRows("vid=-" + this.id, null);
			localCursor.moveToFirst();
			Report localReport3 = new Report(2, this.date);
			Report localReport1 = new Report(1, this.date);
			Report localReport2 = new Report(2, (FDate)localObject);
			Report localReport4 = new Report(1, (FDate)localObject);
			while (!localCursor.isAfterLast())
			{
				int i = localCursor.getShort(1);
				long l = localCursor.getLong(2);
				localReport3.addKmSum(i, -l);
				localReport1.addKmSum(i, -l);
				localReport2.addKmSum(i, l);
				localReport4.addKmSum(i, l);
				localCursor.moveToNext();
			}
			localReport3.save();
			localReport1.save();
			localReport2.save();
			localReport4.save();
			localCursor.close();
		}
		DBTool.database.execSQL("update audit set deposit_id=" + paramInt + ",date=" + paramDate.getTime() + " where vid=-" + this.id);
		Virement localVirement = new Virement(this.vid);
		localVirement.modify(localVirement.sum, paramInt, paramDate);
		this.sum = paramLong;
		this.deposit_id = (short)paramInt;
		this.real_date = paramDate;
		this.date = ((FDate)localObject);
		this.content = paramString;
		save();
		localObject = "操作成功完成！";
		break label484;
		label479: localObject = "余额不足!";
		label484: return (String)localObject;
	}

	public String modifyAuditPosOrder(int paramInt, long paramLong, String paramString)
	{
		Object localObject;
		if (paramLong > 0L)
		{
			localObject = new Audit(-this.vid);
			if (this.sum != paramLong)
			{
				Deposit localDeposit = new Deposit(((Audit)localObject).deposit_id);
				if (!localDeposit.isOverSum(paramLong - this.sum))
				{
					localDeposit.modifySum(this.sum - paramLong);
					((Audit)localObject).sum += paramLong - this.sum;
					((Audit)localObject).save();
					new Virement(((Audit)localObject).vid).modify(paramLong);
				}
			}
			else
			{
				if ((this.kmid != paramInt) || (this.sum != paramLong))
				{
					Report.modifyReportSum(((Audit)localObject).date, this.kmid, -this.sum);
					Report.modifyReportSum(((Audit)localObject).date, paramInt, paramLong);
				}
				this.sum = paramLong;
				this.kmid = paramInt;
				this.content = paramString;
				save();
				localObject = "操作成功完成！";
				break label193;
			}
			localObject = "余额不足!";
		}
		else
		{
			localObject = "金额不对!";
		}
		label193: return (String)localObject;
	}



	
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Audit
 * JD-Core Version:    0.6.0
 */
