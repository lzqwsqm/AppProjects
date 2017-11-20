/*
 * 数据库工具
 */
package com.database.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
/*import com.tomoney.finance.context.RuntimeInfo;
 import com.tomoney.finance.model.Asset;
 import com.tomoney.finance.model.Audit;
 import com.tomoney.finance.model.Bank;
 import com.tomoney.finance.model.Bond;
 import com.tomoney.finance.model.Credit;
 import com.tomoney.finance.model.Deposit;
 import com.tomoney.finance.model.Favor;
 import com.tomoney.finance.model.FavorType;
 import com.tomoney.finance.model.Feedback;
 import com.tomoney.finance.model.Friend;
 import com.tomoney.finance.model.FriendType;
 import com.tomoney.finance.model.Funds;
 import com.tomoney.finance.model.Insurance;
 import com.tomoney.finance.model.InterestRate;
 import com.tomoney.finance.model.InvestAccount;
 import com.tomoney.finance.model.InvestType;
 import com.tomoney.finance.model.KM;
 import com.tomoney.finance.model.Member;
 import com.tomoney.finance.model.Param;
 import com.tomoney.finance.model.Report;
 import com.tomoney.finance.model.Safe;
 import com.tomoney.finance.model.SafeType;
 import com.tomoney.finance.model.Stock;
 import com.tomoney.finance.model.Virement;
 import com.tomoney.finance.view.MainActivity;*/
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class DBTool
{
	private static final String DATABASE_NAME = "/mnt/sdcard/DataBase/lzq.db";
	public static final String SINA_HOST = "http://k266.sinaapp.com";
	public static SQLiteDatabase database = null;
	public static final int[] key_ret;

	static {
        database = null;
        int[] iArr = new int[16];
        iArr[0] = 14;
        iArr[1] = 4;
        iArr[2] = 2;
        iArr[3] = 6;
        iArr[4] = 12;
        iArr[5] = 10;
        iArr[6] = 8;
        iArr[7] = 15;
        iArr[8] = 1;
        iArr[9] = 9;
        iArr[10] = 7;
        iArr[11] = 5;
        iArr[13] = 11;
        iArr[14] = 3;
        iArr[15] = 13;
        key_ret = iArr;
    }

	//--------系统初始化--------
	//WelcomeActivity.entry调用
    //判断文件是否存在
    public static boolean isFileExist(String filename) {
        try {
            if (new File(filename).exists()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.v("isFileExist", e.toString());
            return false;
        }
    }

    //WelcomeActivity.entry调用
	public static String getDatabaseFilename(Context context)
    {
		return  DATABASE_NAME;
   
    }

	
    //创建数据库
	//WelcomeActivity.entry调用
    public static SQLiteDatabase getDatabase(Context context)
	{
		return new DatabaseHelper(context, DATABASE_NAME).getWritableDatabase();
	}

    public static int getMaxId(String tablename) {
        Cursor cur = database.query(tablename, new String[]{"max(id)"}, null, null, null, null, null);
        cur.moveToFirst();
        int nextid = cur.getInt(0);
        cur.close();
        return nextid;
    }



}


/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.util.DBTool
 * JD-Core Version:    0.6.0
 */

