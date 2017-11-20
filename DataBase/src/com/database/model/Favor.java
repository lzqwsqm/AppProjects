/*
 * 人情模块
 */
package com.database.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.database.util.Convertor;
import com.database.util.DBTool;
import com.database.util.MyDate;
import com.database.ListAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.List<Ljava.lang.String;>;

public class Favor
{
    public static final int AUDIT_PROJECT = 4;
    public static final int EVECTION = 0;
    public static final int PROJECT = 2;
    public String city;
    public String content = null;
    public MyDate date;
    public short flag;
    public int id;
    public String[] projectkm = null;
    public Date real_date;
    public byte[] stat = null;
    public long sum;


    /*
     * 创建表
     */
	public static void createDatabase(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE favor(id integer  PRIMARY KEY AUTOINCREMENT,sum integer,date integer,city varchar(32),flag smallint ,stat blob,content varchar(80),projectkm varchar(250));");
	}
	
	
	//--------项目显示----
	/*
	 * 获取行
	 * public Cursor query (String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) 
	 * selection 条件
	 * orderBy 排序
	 */
	public static Cursor getRows(String selection, String orderBy)
	{
		return DBTool.database.query("favor", getColumnString(), selection, null, null, null, orderBy);
	}
	
    /*
     * 获取列
     */
	public static String[] getColumnString()
	{
		String[] Column = new String[8];
		Column[0] = "id";
		Column[1] = "sum";
		Column[2] = "date";
		Column[3] = "city";
		Column[4] = "flag";
		Column[5] = "stat";
		Column[6] = "content";
		Column[7] = "projectkm";
		return Column;
	}
	
//--------新增项目----------
	
	/*
	 * 判断是否有项目
	 */
	public static boolean hasEvection(String selection)
	{
		Cursor localCursor = getRows(selection, null);
		boolean bool = false;
		if (localCursor.getCount() > 0)
			bool = true;
		localCursor.close();
		return bool;
	}
	/*
	 *插入(新增项目) 
	 * public long insert (String table, String nullColumnHack, ContentValues values) 
     *
	 */
	public static String insertEvection(int flag, String name, Date dateinput, String content)
	{
		String note;
		//如果名字不是空
		if (name.length() != 0)
		{
			ContentValues cv = new ContentValues();
			cv.put("sum", Integer.valueOf(0));
			cv.put("date", Long.valueOf(dateinput.getTime()));
			cv.put("city", name);
			cv.put("content", content);
			String km;
			if (flag != 0)
				km = getDefaultProjectKm();
			else
				km = "";
			cv.put("projectkm", km);
			cv.put("flag", Integer.valueOf(flag));
			cv.put("stat", new byte[296]);
			DBTool.database.insert("favor", "id", cv);
			note = "操作成功完成！";
		}
		else
		{
			note = "输入不允许为空！";
		}
		return note;
	}
	
	static String getDefaultProjectKm()
	{
		return "场地费,交通费,门票,物品购置,,,,,,,,,,,,,活动收费,赞助款,公司资助,会员缴费,,,,,,,,,,,,,";
	}
	
	/*
	 *查询行方法 
	 */
	public Favor(int id)
	{
		this.id = id;
		Cursor localCursor = DBTool.database.query("favor", getColumnString(), "id=" + id, null, null, null, null);
		localCursor.moveToFirst();
		reset(localCursor);
		localCursor.close();
	}
	void reset(Cursor paramCursor)
	{
		this.id = paramCursor.getInt(0);
		this.sum = paramCursor.getLong(1);
		this.real_date = new Date(paramCursor.getLong(2));
		this.date = new MyDate(this.real_date);
		this.city = paramCursor.getString(3);
		this.flag = paramCursor.getShort(4);
		this.stat = paramCursor.getBlob(5);
		this.content = paramCursor.getString(6);
	//	if (!isEvection())
		this.projectkm = Convertor.split(paramCursor.getString(7), ",");
	}
	
	//修改
	public String modify(String name, Date dateinput, String content)
	{
		String note;
		if (name.length() != 0)
		{
			this.city = name;
			this.content = content;
			if (!this.date.equals(dateinput))
			{
				this.real_date = dateinput;
				this.date = new MyDate(dateinput);
			}
			save();
			note = "操作成功完成！";
		}
		else
		{
			note = "输入不允许为空！";
		}
		return note;
	}
	
	/*
	 * 保存(更新)
	 * public int update (String table, ContentValues values, String whereClause, String[] whereArgs) 
	 * 
	 */
	void save()
	{
		ContentValues values = new ContentValues();
		values.put("sum", Long.valueOf(this.sum));
		values.put("date", Long.valueOf(this.real_date.getTime()));
		values.put("city", this.city);
		values.put("flag", Short.valueOf(this.flag));
		values.put("stat", this.stat);
		values.put("content", this.content);
	    StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < 32; i++)
			localStringBuffer.append(this.projectkm[i]).append(",");
		values.put("projectkm", localStringBuffer.toString());
		DBTool.database.update("favor", values, "id=" + this.id, null);
	}
	//删除
	public String delete()
	{
		String note;
		
		if (isFinished())
		{
			this.flag = (short)(-this.flag);
			save();
		    note = "操作成功完成！";
			return note;
		}
		else
		{
			Cursor localObject = DBTool.database.query("favoraudit", FavorAudit.getColumnString(), "eid=" + this.id, null, null, null, null);
			if (localObject.getCount() > 0)
			{    
			    localObject.close();
			    note = "未结束并且存在出差流水,不允许删除！";
				return note;
			}
			localObject.close();
			DBTool.database.execSQL("delete from favor where id=" + this.id);
		    note = "操作成功完成！";
			return note;
		}
		
	}
	
	public boolean isFinished()
	{
		boolean bool = true;
		if (this.flag % 2 != 1)
			bool = false;
		return bool;
	}
	
	/*
	 * 获得科目名字
	 */
	public String getKmName(int kmid)
	{
		String str;
		if (kmid != 1)
			str = this.projectkm[(kmid - 5)];
		else
			str = "项目结束";
		return str;
	}
	/*
	 * 
	 */
	public static List<String> getEvectionList(String selection, List<Integer> index)
	{
		index.clear();
		ArrayList localArrayList = new ArrayList();
		Cursor localCursor = getRows(selection, "id desc");
	
		for (localCursor.moveToFirst();!localCursor.isAfterLast();localCursor.moveToNext())
		{
			localArrayList.add(localCursor.getString(3));
			index.add(Integer.valueOf(localCursor.getInt(0)));
		
		}
		localCursor.close();
		return localArrayList;
	}
	
	public List<String> getEvectionAuditTypeList()
	{
		ArrayList localArrayList = new ArrayList();
		localArrayList.add("支出");
		localArrayList.add("收入");
		return localArrayList;
	}
	
	
	public List<String> getEvectionKmList(int paramInt, List<Integer> paramList)
	{
		paramList.clear();
		ArrayList localObject = new ArrayList();

		int i = 0;
		if (paramInt == 1)
			i = 16;
		for (int j = 0; j < 16; j++)
		{
			if (this.projectkm[(j + i)].length() <= 0)
				continue;
			localObject.add(this.projectkm[(j + i)]);
			paramList.add(Integer.valueOf(5 + (j + i)));
		}

		return localObject;
	}
	
	
	//记帐
	public String insertAudit(int kmid, long sum, int depositid, String content, Date paramDate)
    {
        String str;
        if (sum > 0L)
        {
		    if (!isIncome(kmid))
                str = insertOutcome(kmid, sum, depositid, content, paramDate);
            else
               str = insertIncome(kmid, sum, depositid, content, paramDate);
        }
        else
            str = "金额错误！";
        return str;
    }

	public static boolean isIncome(int kmid)
	{
		boolean bool;
		if (kmid <= 20)
			bool = false;
		else
			bool = true;
		return bool;
	}
	/*
	 * 收入
	 */
	public String insertIncome(int kmid, long sum, int depositid, String content, Date dateinput)
	{
		new Deposit(depositid).addSum(sum);
		addSum(kmid, sum);
		//写入收支并
		Audit.insert(297, sum, 0, dateinput, content + this.city);
		//获得ID
		int aid = 0;
		if (this.sum != 0L)
			aid = DBTool.getMaxId("audit");
		FavorAudit.insert(this.id, kmid, sum, depositid, aid, content, dateinput);
		save();
		return "操作成功完成！";
	}
    /*
     * 支出
     */
	public String insertOutcome(int kmid, long sum, int depositid, String content, Date dateinput)
	{
		Deposit localDeposit = new Deposit(depositid);
		String str;
		if (!localDeposit.isOverSum(sum))
		{
			localDeposit.addSum(-sum);
			addSum(kmid, sum);
		
			Audit.insert(266, sum, 0, dateinput, content + this.city);
			int aid = 0;
			if (this.sum != 0L)
				aid = DBTool.getMaxId("audit");
			FavorAudit.insert(this.id, kmid, sum, depositid, aid, content, dateinput);
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "余额不足！";
		}
		return str;
	}
	
	
	
	void addSum(int kmid, long sum)
	{
		if (!isIncome(kmid))
			this.sum -= sum;
		else
			this.sum = (sum + this.sum);
		Convertor.fromLong(sum + Convertor.toLong(this.stat, kmid * 8, 8), this.stat, kmid * 8);
	}
	
	
	
	public MyDate getEndDate()
    {
        Cursor localCursor = DBTool.database.query("evectionaudit", getColumnString(), "eid=" + this.id + " and kmid=" + 1, null, null, null, null);
        if (localCursor.getCount() == 0)
        {
            localCursor.close();
            localCursor = DBTool.database.query("evectionaudit", getColumnString(), "eid=" + this.id, null, null, null, "date desc");
            if (localCursor.getCount() == 0);
         }
         else
         {
             localCursor.moveToFirst();
             MyDate localFDate = new MyDate(new Date(localCursor.getLong(5)));
             localCursor.close();
             return localFDate;
         }
         localCursor.close();
         MyDate localFDate = MyDate.now();
         return localFDate;
    }
	
	public void fillProjectKmList(ListAdapter paramFinanceAdapter, List<Integer> paramList)
	{
		paramList.clear();
		paramFinanceAdapter.clear();
		int[] localObject = new int[2];
		localObject[0] = 60;
		localObject[1] = 260;
		
		paramFinanceAdapter.setLayout(localObject);
		paramList.add(Integer.valueOf(2147483394));
		
		ArrayList arraylist = new ArrayList();
		String[] arrayOfString1 = new String[1];
		arrayOfString1[0] = "科目名称";
		arraylist.add(arrayOfString1);
		
		int j;
	//	if (!isProject())
			j = 16;
	//	else
			j = 32;
		for (int i = 0; i < j; i++)
		{
		//	if (isProject())
				if (i != 0)
				{
					if (i == 16)
					{
						String[] arrayOfString2 = new String[1];
						arrayOfString2[0] = "收入";
						arraylist.add(arrayOfString2);
						paramList.add(Integer.valueOf(-1));
					}
				}
				else
				{
					String[] arrayOfString2 = new String[1];
					arrayOfString2[0] = "支出";
					arraylist.add(arrayOfString2);
					paramList.add(Integer.valueOf(-1));
				}
			if (this.projectkm[i].length() <= 0)
				continue;
			String[] arrayOfString2 = new String[1];
			arrayOfString2[0] = ("    " + this.projectkm[i]);
			arraylist.add(arrayOfString2);
			paramList.add(Integer.valueOf(i + 5));
		}
		paramFinanceAdapter.append(arraylist);
	}
	
	public String addProjectkm(int paramInt, String paramString)
	{
		String str;
		if (paramString.length() != 0)
		{
			if (paramString.length() <= 7)
			{
				int i = 0;
				if (paramInt == 1)
					i = 16;
				int j = 0;
				while (j < 16)
				{
					if (this.projectkm[(j + i)].length() != 0)
					{
						j++;
						continue;
					}
					this.projectkm[(j + i)] = paramString;
					save();
					str = "操作成功完成！";
					return str;
			//		break label92;
				}
				str = "科目数达到上限!";
			}
			else
			{
				str = "名称不能太长!";
			}
		}
		else
			str = "名称不能为空!";
		return str;
	}
	public String modifyProjectKm(int paramInt, String paramString)
	{
		String str;
		if (paramString.length() != 0)
		{
			if (paramString.length() <= 7)
			{
				this.projectkm[(paramInt - 5)] = paramString;
				save();
				str = "操作成功完成！";
			}
			else
			{
				str = "名称不能太长!";
			}
		}
		else
			str = "名称不能为空!";
		return str;
	}
	public void fillStatList(ListAdapter paramFinanceAdapter, List<Integer> paramList)
	{
		paramList.clear();
		paramList.add(Integer.valueOf(2147483394));
		paramFinanceAdapter.clear();
		ArrayList localArrayList = new ArrayList();
		String[] arrayOfString1 = new String[2];
		arrayOfString1[0] = "科目";
		arrayOfString1[1] = "金额";
		localArrayList.add(arrayOfString1);
		
		long l1 = 0L;
		for (int j = 5; j < 21; j++)
		{
			long l4 = getSum(j);
			if (l4 == 0L)
				continue;
			l1 += l4;
			String[] arrayOfString8 = new String[2];
			arrayOfString8[0] = getKmName(j);
			arrayOfString8[1] = Convertor.sumToString(l4);
			localArrayList.add(arrayOfString8);
		}
		
		if (l1 > 0L)
		{
		//	if (isAuditProject())
		//	{
		//		arrayOfString4 = new String[2];
		//		arrayOfString4[0] = "------";
		//		arrayOfString4[1] = "------------";
		//		localArrayList.add(arrayOfString4);
		//	}
			String[] arrayOfString4 = new String[2];
			arrayOfString4[0] = "支出合计";
			arrayOfString4[1] = Convertor.sumToString(l1);
			localArrayList.add(arrayOfString4);
		//	if (!isAuditProject())
		//	{
				String[] arrayOfString2 = new String[2];
				arrayOfString2[0] = "------";
				arrayOfString2[1] = "------------";
				localArrayList.add(arrayOfString2);
	//		}
		}
	//	if (!isEvection())
		{
	//		if (isProject())
			{
				long l2 = 0L;
				for (int k = 21; k < 37; k++)
				{
					long l5 = getSum(k);
					if (l5 == 0L)
						continue;
					l2 += l5;
					String[] arrayOfString7 = new String[2];
					arrayOfString7[0] = getKmName(k);
					arrayOfString7[1] = Convertor.sumToString(l5);
					localArrayList.add(arrayOfString7);
				}
				if (l2 > 0L)
				{
					String[] arrayOfString5 = new String[2];
					arrayOfString5[0] = "收入合计";
					arrayOfString5[1] = Convertor.sumToString(l2);
					localArrayList.add(arrayOfString5);
					String[] arrayOfString3 = new String[2];
					arrayOfString3[0] = "------";
					arrayOfString3[1] = "------------";
					localArrayList.add(arrayOfString3);
				}
			}
		}
	/*	else
			for (int i = 2; i < 5; i++)
			{
				long l3 = getSum(i);
				if (l3 == 0L)
					continue;
				String[] arrayOfString6 = new String[2];
				arrayOfString6[0] = getKmName(i);
				arrayOfString6[1] = Convertor.sumToString(l3);
				localArrayList.add(arrayOfString6);
			}*/
	//	if (!isAuditProject())
	//	{
			String[] localObject = new String[2];
			localObject[0] = "目前结余";
			localObject[1] = Convertor.sumToString(this.sum);
			localArrayList.add(localObject);
	//	}
		
		int[] localObject1 = new int[3];
		localObject1[0] = 40;
		localObject1[1] = 100;
		localObject1[2] = 180;
		paramFinanceAdapter.setLayout(localObject1);
		paramFinanceAdapter.append(localArrayList);
	}
	
	public long getSum(int paramInt)
	{
		return Convertor.toLong(this.stat, paramInt * 8, 8);
	}
	
	
	public String deleteProjectKm(int paramInt)
	{
		String str;
		if (!kmIsUsed(paramInt))
		{
			this.projectkm[(paramInt - 5)] = "";
			save();
			str = "操作成功完成！";
		}
		else
		{
			str = "科目被使用,不能删除!";
		}
		return str;
	}
	boolean kmIsUsed(int paramInt)
	{
		Cursor localCursor = EvectionAudit.getRows("eid=" + this.id + " and kmid=" + paramInt, null);
		boolean bool = false;
		if (localCursor.getCount() > 0)
			bool = true;
		localCursor.close();
		return bool;
	}
	public String finish(String paramString, Date paramDate)
	{
		String str;
//		if (!isAuditProject())
			str = evectionFinish(paramString, paramDate);
//		else
//			str = auditProjectFinish(paramString, paramDate);
		return str;
	}
	String evectionFinish(String paramString, Date paramDate)
	{
		String str1;
	//	if (getSum(2) == getSum(4))
	//	{
			long l;
			String str2;
			if (this.sum <= 0L)
			{
				if (this.sum < 0L)
				{
					l = -this.sum;
				//	if (!isEvection())
						str2 = "项目结余:";
				//	else
				//		str2 = "出差结余:";
					Audit.insert(273, l, 0, paramDate, str2 + this.city);
				}
			}
			else
			{
				l = this.sum;
			//	if (!isEvection())
					str2 = "项目结余:";
			//	else
			//		str2 = "出差结余:";
				Audit.insert(294, l, 0, paramDate, str2 + this.city);
			}
			int i = 0;
			if (this.sum != 0L)
				i = DBTool.getMaxId("audit");
			EvectionAudit.insert(this.id, 1, this.sum, 0, i, paramString, paramDate);
			this.flag = (short)(1 + this.flag);
			save();
			str1 = "操作成功完成！";
//		}
//		else
//		{
//			str1 = "借款没有还清,不能结束！";
//		}
		return str1;
	}
	
	}
/*



/*








  String auditProjectFinish(String paramString, Date paramDate)
  {
    this.flag = (1 + this.flag);
    save();
    return "操作成功完成！";
  }














 



  public String insertAuditProjectOutcome(int paramInt1, long paramLong, int paramInt2, String paramString, Date paramDate)
  {
    Deposit localDeposit = new Deposit(paramInt2);
    String str;
    if (!localDeposit.isOverSum(paramLong))
    {
      localDeposit.addSum(-paramLong);
      addSum(paramInt1, paramLong);
      int i = Audit.insertSystemAudit(265, paramLong, paramInt2, Virement.insert(34, paramLong, paramInt2, this.id, paramDate), paramDate, paramString);
      EvectionAudit.insert(this.id, paramInt1, paramLong, paramInt2, i, paramString, paramDate);
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不足！";
    }
    return str;
  }

  public String insertBorrow(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    new Deposit(paramInt).addSum(paramLong);
    addSum(2, paramLong);
    int i = Virement.insert(31, paramLong, paramInt, this.id, paramDate);
    Account.addAccountSum(5, paramLong);
    EvectionAudit.insert(this.id, 2, paramLong, paramInt, i, paramString, paramDate);
    save();
    return "操作成功完成！";
  }


  public String insertReimburse(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    new Deposit(paramInt).addSum(paramLong);
    addSum(3, paramLong);
    int i = Virement.insert(32, paramLong, paramInt, this.id, paramDate);
    EvectionAudit.insert(this.id, 3, paramLong, paramInt, i, paramString, paramDate);
    save();
    return "操作成功完成！";
  }

  public String insertReturn(long paramLong, int paramInt, String paramString, Date paramDate)
  {
    Deposit localDeposit = new Deposit(paramInt);
    String str;
    if (!localDeposit.isOverSum(paramLong))
    {
      localDeposit.addSum(-paramLong);
      addSum(4, paramLong);
      Account.addAccountSum(5, -paramLong);
      int i = Virement.insert(33, paramLong, paramInt, this.id, paramDate);
      EvectionAudit.insert(this.id, 4, paramLong, paramInt, i, paramString, paramDate);
      save();
      str = "操作成功完成！";
    }
    else
    {
      str = "余额不足！";
    }
    return str;
  }

  public boolean isAuditProject()
  {
    int i;
    if ((this.flag == 4) || (this.flag == 5) || (this.flag == -5))
      i = 1;
    else
      i = 0;
    return i;
  }

  public boolean isEvection()
  {
    int i = 1;
    if ((this.flag != 0) && (this.flag != i) && (this.flag != -1))
      i = 0;
    return i;
  }

 

  public boolean isProject()
  {
    int i;
    if ((this.flag == 2) || (this.flag == 3) || (this.flag == -3))
      i = 1;
    else
      i = 0;
    return i;
  }


  }





}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.model.Evection
 * JD-Core Version:    0.6.0
 */
