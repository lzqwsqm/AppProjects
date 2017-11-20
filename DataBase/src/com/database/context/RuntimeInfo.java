package com.database.context;



//import com.tomoney.finance.model.Audit;
import com.database.model.Param;
import com.database.MainActivity;
import com.database.WelcomeActivity;
import java.util.Vector;

public class RuntimeInfo {
    //public static Vector<Audit> auditlist;
    public static MainActivity main;
    public static Param param;
    public static String sql;
    public static long[] sum;
    public static WelcomeActivity welcome;

    static {
      //  auditlist = null;
        main = null;
        welcome = null;
        param = null;
	}
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.context.RuntimeInfo
 * JD-Core Version:    0.6.0
 */
