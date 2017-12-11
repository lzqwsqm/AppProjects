package com.qingshan.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TextUtil
{
	private static Object sLock = new Object();
	private static char[] sTemp = null;
	/**
     * <p>Counts how many times the substring appears in the larger String.</p>
     *
     * <p>A <code>null</code> or empty ("") String input returns <code>0</code>.</p>
     *
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param sub  the substring to count, may be null
     * @return the number of occurrences, 0 if either String is <code>null</code>
     */
	//QSEditText.drawView调用
	public static int countMatches(CharSequence str, char sub, int start, int end) {
        if (str.length() == 0) {
            return 0;
        }
        int count = 0;
        for (int index = start; index <= end; index++) {
            if (str.charAt(index) == sub) {
                count++;
            }
        }
        return count;
    }
	
    
	/*public static String MD5(String paramString1, String paramString2)
	{
		Object localObject = null;
		StringBuffer localStringBuffer;
		try
		{
			localObject = MessageDigest.getInstance("MD5");
			localObject = localObject;
			((MessageDigest)localObject).reset();
		}
		catch (NoSuchAlgorithmException localUnsupportedEncodingException)
		{
			try
			{
				((MessageDigest)localObject).update(paramString1.getBytes(paramString2));
				localObject = ((MessageDigest)localObject).digest();
				localStringBuffer = new StringBuffer();
				int i = 0;
				if (i >= localObject.length)
				{
					return localStringBuffer.toString();
					localNoSuchAlgorithmException = localNoSuchAlgorithmException;
					localNoSuchAlgorithmException.printStackTrace();
				}
			}
			catch (UnsupportedEncodingException localUnsupportedEncodingException)
			{
				while (true)
					localUnsupportedEncodingException.printStackTrace();
			}
		}
		if (Integer.toHexString(0xFF & localObject[localUnsupportedEncodingException]).length() == 1)
			localStringBuffer.append("0").append(Integer.toHexString(0xFF & localObject[localUnsupportedEncodingException]));
		while (true)
		{
			localUnsupportedEncodingException++;
			break;
			localStringBuffer.append(Integer.toHexString(0xFF & localObject[localUnsupportedEncodingException]));
		}
	}*/
	public static String MD5(String str, String encoding)
    {
        MessageDigest messageDigest = null;


		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		messageDigest.reset();
		try
		{
			messageDigest.update(str.getBytes(encoding));
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }
	

	/*public static char[] obtain(int paramInt)
	{
		synchronized (sLock)
		{
			char[] arrayOfChar = sTemp;
			sTemp = null;
			if ((arrayOfChar == null) || (arrayOfChar.length < paramInt))
				arrayOfChar = new char[ArrayUtil.idealCharArraySize(paramInt)];
			return arrayOfChar;
		}
	}
	*/
	
    
    /*public static char[] obtain(int len) {
        char[] buf;

        synchronized (sLock) {
            buf = sTemp;
            sTemp = null;
        }

        if (buf == null || buf.length < len)
            buf = new char[ArrayUtil.idealCharArraySize(len)];

        return buf;
    }*/

    /*public static void printStacks(String msg)
    {
        Exception e = new Exception("PRINT STACKS: "+msg+" ======================================================");
        e.printStackTrace();
    }*/
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.TextUtil
 * JD-Core Version:    0.6.0
 */
