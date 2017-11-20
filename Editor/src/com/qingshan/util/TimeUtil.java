package com.qingshan.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil
{
	private static String format(Date paramDate)
	{
		return DateFormat.getDateTimeInstance().format(paramDate);
	}

	public static String getDate()
	{
		return format(new Date());
	}

	public static String getDate(long paramLong)
	{
		return format(new Date(paramLong));
	}

	public static String getDateByFormat(int paramInt)
	{
		Object localObject = new Date();
		switch (paramInt)
		{
			default: 
				localObject = ((Date)localObject).toString();
				break;
			case 0: 
				localObject = DateFormat.getDateTimeInstance().format((Date)localObject);
				break;
			case 1: 
				localObject = DateFormat.getDateInstance().format((Date)localObject);
				break;
			case 2: 
				localObject = new SimpleDateFormat("yyyyMMddHHmmss").format((Date)localObject);
				break;
			case 3: 
				localObject = DateFormat.getTimeInstance().format((Date)localObject);
				break;
			case 4: 
				localObject = DateFormat.getInstance().format((Date)localObject);
				break;
			case 5: 
				localObject = DateFormat.getDateTimeInstance(0, 0).format((Date)localObject);
				break;
			case 6: 
				localObject = DateFormat.getDateTimeInstance(1, 1).format((Date)localObject);
				break;
			case 7: 
				localObject = DateFormat.getDateTimeInstance(3, 3).format((Date)localObject);
				break;
			case 8: 
				localObject = DateFormat.getDateTimeInstance(2, 2).format((Date)localObject);
				break;
		}
		return (String)localObject;
	}

	public static String getDateByFormat(String paramString)
	{
		String localObject;
		try
		{
			 localObject = getDateByFormat(Integer.valueOf(paramString).intValue());
			//localObject = localObject;
		}
		catch (Exception localException)
		{
			for (;;)
			{
				Calendar localObject1 = Calendar.getInstance();
				localObject = paramString.replaceAll("yyyy", String.valueOf(((Calendar)localObject1).get(1))).replaceAll("MM", String.valueOf(((Calendar)localObject1).get(2))).replaceAll("dd", String.valueOf(((Calendar)localObject1).get(5))).replaceAll("HH", String.valueOf(((Calendar)localObject1).get(11))).replaceAll("mm", String.valueOf(((Calendar)localObject1).get(12))).replaceAll("ss", String.valueOf(((Calendar)localObject1).get(13)));
			}
		}
		return (String)localObject;
	}
}


/* Location:              C:\Documents and Settings\李忠全\桌面\9.4.jar!\com\jecelyin\util\TimeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */
