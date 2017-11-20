package com.qingshan.util;



import com.stericson.RootTools.RootTools;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxShell
{
	public static boolean canRoot() {
        return RootTools.isRootAvailable() || RootTools.isBusyboxAvailable();
    }

	public static BufferedReader execute(String cmd) {
        BufferedReader bufferedReader;
        IOException e;
        InterruptedException e2;
        Exception e3;
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(new StringBuilder(String.valueOf(cmd)).append("\n").toString());
            os.writeBytes("exit\n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                String err = new BufferedReader(new InputStreamReader(process.getErrorStream())).readLine();
                os.flush();
                if (process.waitFor() == 0 && ("".equals(err) || err == null)) {
                    return reader;
                }
                Log.e("TERoot", err);
                bufferedReader = reader;
                return null;
            } catch (IOException e4) {
                e = e4;
                bufferedReader = reader;
                e.printStackTrace();
                return null;
            } catch (InterruptedException e5) {
                e2 = e5;
                bufferedReader = reader;
                e2.printStackTrace();
                return null;
            } catch (Exception e6) {
                e3 = e6;
                bufferedReader = reader;
                e3.printStackTrace();
                return null;
            }
        } catch (IOException e7) {
            e = e7;
            e.printStackTrace();
            return null;
        /*} catch (InterruptedException e8) {
            e2 = e8;
            e2.printStackTrace();
            return null;*/
        } catch (Exception e9) {
            e3 = e9;
            e3.printStackTrace();
            return null;
        }
    }
	
	//FileUtil.writeFile调用
	public static String getCmdPath(String paramString)
	{
		return paramString.replace(" ", "\\ ").replace("'", "\\'");
	}
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.LinuxShell
 * JD-Core Version:    0.6.0
 */
