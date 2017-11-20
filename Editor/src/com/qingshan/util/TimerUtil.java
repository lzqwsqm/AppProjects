package com.qingshan.util;


import android.util.Log;

public class TimerUtil
{
	private static long endTime;
	private static long startTime;

	public static long getTime()
	{
		return System.currentTimeMillis();
	}

	public static void start()
	{
		startTime = System.currentTimeMillis();
	}

	public static void stop(String paramString)
	{
		endTime = System.currentTimeMillis();
		Log.d(paramString, String.valueOf((endTime - startTime) / 1000.0D) + " seconds");
	}
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.TimerUtil
 * JD-Core Version:    0.6.0
 */
