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

	/*public static BufferedReader execute(String cmd) {
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
            return null;
        } catch (Exception e9) {
            e3 = e9;
            e3.printStackTrace();
            return null;
        }
    }*/
	/**
     * 返回执行完成的结果
     * @param cmd 命令内容
     * @return
     */
    public static BufferedReader execute(String cmd)
    {
        BufferedReader reader = null; //errReader = null;
        try
        {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            //os.writeBytes("mount -oremount,rw /dev/block/mtdblock3 /system\n");
            //os.writeBytes("busybox cp /data/data/com.koushikdutta.superuser/su /system/bin/su\n");
            os.writeBytes(cmd+"\n");
            os.writeBytes("exit\n");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String err = (new BufferedReader(new InputStreamReader(process.getErrorStream()))).readLine();
            os.flush();

            if(process.waitFor() != 0 || (!"".equals(err) && null != err))
            {
                Log.e("QSTERoot", err);
                return null;
            }
            return reader;
        }catch (IOException e)
        {
            e.printStackTrace();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	//FileUtil.writeFile调用
	public static String getCmdPath(String path)
	{
		return path.replace(" ", "\\ ").replace("'", "\\'");
	}
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.LinuxShell
 * JD-Core Version:    0.6.0
 */
