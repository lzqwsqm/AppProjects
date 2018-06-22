package com.qingshan.editor;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.AssetManager;
import com.qingshan.util.FileUtil;

public class Help
{
	private static void popupWindow(final Context mContext, int title, String file)
	{
		try
		{
			String str = FileUtil.ReadFile(mContext.getAssets().open(file), "utf-8");
			Builder localBuilder = new Builder(mContext);
			localBuilder.setIcon(0).setTitle(title).setMessage(str).setPositiveButton(17039370, null);
			localBuilder.setNegativeButton(2131230791, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						mContext.startActivity(Donate.getWebIntent());
					}
				});
			localBuilder.show();
		
		}
		catch (Exception localException)
		{
		
		}
	}
	/*private static void popupWindow(Context mContext, int title, String file) {
        try {
            String text = FileUtil.ReadFile(mContext.getAssets().open(file), "utf-8");
            Builder builder = new Builder(mContext);
            builder.setIcon(0).setTitle(title).setMessage(text).setPositiveButton(17039370, null);
            builder.setNegativeButton(R.string.donate, new AnonymousClass1(mContext));
            builder.show();
        } catch (Exception e) {
        }
    }*/

	public static void showChangesLog(Context mContext) {
        popupWindow(mContext, R.string.changelog, "CHANGES");
    }

	public static void showHelp(Context mContext) {
        popupWindow(mContext, R.string.help, "HELP");
    }
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.Help
 * JD-Core Version:    0.6.0
 */
