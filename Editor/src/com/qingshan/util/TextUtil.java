package com.qingshan.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TextUtil
{
	private static Object sLock = new Object();
	private static char[] sTemp = null;
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
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.TextUtil
 * JD-Core Version:    0.6.0
 */
