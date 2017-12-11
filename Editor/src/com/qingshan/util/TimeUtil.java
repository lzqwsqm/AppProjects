package com.qingshan.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil
{
	private static String format(Date d)
	{
		return DateFormat.getDateTimeInstance().format(d);
	}

	public static String getDate()
	{
		return format(new Date());
	}

	public static String getDate(long ts)
	{
		return format(new Date(ts));
	}

	
	public static String getDateByFormat(int format)
    {
        Date now = new Date();
        DateFormat df;

        switch(format)
        {
            case 0:
                df = DateFormat.getDateTimeInstance();
                return df.format(now);
            case 1:
                // 默认语言（汉语）下的默认风格（MEDIUM风格，比如：2008-6-16 20:54:53）
                df = DateFormat.getDateInstance();
                return df.format(now);
            case 2:
                df = new SimpleDateFormat("yyyyMMddHHmmss");
                return df.format(now);
            case 3:
                df = DateFormat.getTimeInstance();
                return df.format(now);
            case 4:
                df = DateFormat.getInstance();
                return df.format(now);
            case 5:
                df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
                return df.format(now);
            case 6:
                df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
                return df.format(now);
            case 7:
                df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                return df.format(now);
            case 8:
                df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
                return df.format(now);
            default:
                return now.toString();
        }
    }

	public static String getDateByFormat(String format)
	{
		//String localObject;
		try
		{
			 //localObject = getDateByFormat(Integer.valueOf(paramString).intValue());
			//localObject = localObject;
			return getDateByFormat(Integer.valueOf(format));
		}
		catch (Exception localException)
		{
			/*for (;;)
			{
				Calendar localObject1 = Calendar.getInstance();
				localObject = paramString.replaceAll("yyyy", String.valueOf(((Calendar)localObject1).get(1))).replaceAll("MM", String.valueOf(((Calendar)localObject1).get(2))).replaceAll("dd", String.valueOf(((Calendar)localObject1).get(5))).replaceAll("HH", String.valueOf(((Calendar)localObject1).get(11))).replaceAll("mm", String.valueOf(((Calendar)localObject1).get(12))).replaceAll("ss", String.valueOf(((Calendar)localObject1).get(13)));
			}*/
			//yyyy年MM月dd日_HH时mm分ss秒
            Calendar cal = Calendar.getInstance();
            format = format.replaceAll("yyyy", String.valueOf(cal.get(Calendar.YEAR)));
            format = format.replaceAll("MM", String.valueOf(cal.get(Calendar.MONTH)+1));
            format = format.replaceAll("dd", String.valueOf(cal.get(Calendar.DATE)));
            format = format.replaceAll("HH", String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
            format = format.replaceAll("mm", String.valueOf(cal.get(Calendar.MINUTE)));
            format = format.replaceAll("ss", String.valueOf(cal.get(Calendar.SECOND)));
            return format;
		}
		//return (String)localObject;
	}
	
}


/* Location:              C:\Documents and Settings\李忠全\桌面\9.4.jar!\com\jecelyin\util\TimeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */
