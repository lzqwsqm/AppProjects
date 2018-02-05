package com.qingshan.widget;

import com.qingshan.editor.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.qingshan.editor.QSEditor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QSMenu extends Dialog
implements AdapterView.OnItemClickListener
{
	private GridView mGrid;
	private int[][] mMenuItemLists = new int[][]{
		new int[]{R.id.menu_reopen, R.string.reopen, R.drawable.menu_open}, 
		new int[]{R.id.menu_saveas, R.string.saveas, R.drawable.menu_saveas}, 
		new int[]{R.id.menu_highlight, R.string.highlight, R.drawable.menu_highlight}, 
		new int[]{R.id.menu_encoding, R.string.encoding, R.drawable.menu_encoding}, 
		new int[]{R.id.menu_search_replace, R.string.search_replace, R.drawable.menu_search}, 
		new int[]{R.id.menu_pipe, R.string.open_with, R.drawable.menu_openwith}, 
		new int[]{R.id.menu_preferences, R.string.preferences, R.drawable.menu_setting}, 
		new int[]{R.id.menu_exit, R.string.exit, R.drawable.menu_exit}
	};
	private ArrayList<Map<String, Object>> mMenuItems= new ArrayList();
	private OnMenuItemSelectedListener mOnMenuItemSelectedListener;
	private ViewGroup mViewGroup;

	public QSMenu(Context context) {
        super(context);
        /*this.mMenuItemLists = new int[][]{
			new int[]{R.id.menu_reopen, R.string.reopen, R.drawable.menu_open}, 
			new int[]{R.id.menu_saveas, R.string.saveas, R.drawable.menu_saveas}, 
			new int[]{R.id.menu_highlight, R.string.highlight, R.drawable.menu_highlight}, 
			new int[]{R.id.menu_encoding, R.string.encoding, R.drawable.menu_encoding}, 
			new int[]{R.id.menu_search_replace, R.string.search_replace, R.drawable.menu_search}, 
			new int[]{R.id.menu_pipe, R.string.open_with, R.drawable.menu_openwith}, 
			new int[]{R.id.menu_preferences, R.string.preferences, R.drawable.menu_setting}, 
			new int[]{R.id.menu_exit, R.string.exit, R.drawable.menu_exit}
			};
        this.mMenuItems = new ArrayList();*/
        init(context);
    }

	public QSMenu(Context context, int theme) {
        super(context, theme);
        /*this.mMenuItemLists = new int[][]{
			new int[]{R.id.menu_reopen, R.string.reopen, R.drawable.menu_open}, 
			new int[]{R.id.menu_saveas, R.string.saveas, R.drawable.menu_saveas}, 
			new int[]{R.id.menu_highlight, R.string.highlight, R.drawable.menu_highlight}, 
			new int[]{R.id.menu_encoding, R.string.encoding, R.drawable.menu_encoding}, 
			new int[]{R.id.menu_search_replace, R.string.search_replace, R.drawable.menu_search}, 
			new int[]{R.id.menu_pipe, R.string.open_with, R.drawable.menu_openwith}, 
			new int[]{R.id.menu_preferences, R.string.preferences, R.drawable.menu_setting}, 
			new int[]{R.id.menu_exit, R.string.exit, R.drawable.menu_exit}};
        this.mMenuItems = new ArrayList();*/
        init(context);
    }
	private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE/*1*/);
        this.mViewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.main_menu, null);
        this.mGrid = (GridView) this.mViewGroup.findViewById(R.id.menu_grid);
        ((TextView) this.mViewGroup.findViewById(R.id.menu_version)).setText(QSEditor.version);
        for (int[] item : this.mMenuItemLists) {
            HashMap<String, Object> map = new HashMap();
            map.put("id", Integer.valueOf(item[0]));
            //map.put(TestHandler.TEXT, context.getString(item[1]));
			map.put("text", context.getString(item[1]));
            map.put("icon", Integer.valueOf(item[2]));
            this.mMenuItems.add(map);
        }
        //this.mGrid.setAdapter(new SimpleAdapter(getContext(), this.mMenuItems, R.layout.main_menu_item, new String[]{TestHandler.TEXT, "icon"}, new int[]{R.id.main_menu_text, R.id.main_menu_icon}));
        this.mGrid.setAdapter(new SimpleAdapter(getContext(), this.mMenuItems, R.layout.main_menu_item, new String[]{"text", "icon"}, new int[]{R.id.main_menu_text, R.id.main_menu_icon}));
		this.mGrid.setOnItemClickListener(this);
    }
	
	public static abstract interface OnMenuItemSelectedListener
	{
		public abstract boolean onMenuItemSelected(int itemId, View view);
	}
	//
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        int itemId = ((Integer) ((Map) this.mMenuItems.get(position)).get("id")).intValue();
        dismiss();
        this.mOnMenuItemSelectedListener.onMenuItemSelected(itemId, view);
    }
	//QSEditor.onCreate调用
	public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener l)
	{
		this.mOnMenuItemSelectedListener = l;
	}
	
	
	
	protected void onCreate(Bundle mBundle) {
        super.onCreate(mBundle);
        setContentView(this.mViewGroup);
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.gravity = Gravity.CENTER|Gravity.BOTTOM/*80*/;
        attr.verticalMargin = 0.0f;
        attr.dimAmount = 0.0f;
        getWindow().setBackgroundDrawableResource(R.drawable.main_menu_bg);
        //按非窗口区域时，可以关闭窗口
		setCanceledOnTouchOutside(true);
    }
	
	

	protected void onStart()
	{
		super.onStart();
	}

	
	
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.JecMenu
 * JD-Core Version:    0.6.0
 */
