package com.qingshan.colorschemes;

import android.content.SharedPreferences;
import com.qingshan.editor.QSEditor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class ColorScheme
{
	private static final String ATTR_NAME_COLOR = "#FF0000";
	private static final String BACKGROUP_COLOR = "#ffffff";
	private static final String COMMENT_COLOR = "#3F7F5F";
	private static final String FONT_COLOR = "#000000";
	private static final String FUNCTION_COLOR = "#000080";
	private static final String KEYWORD_COLOR = "#000088";
	private static final String STRING_COLOR = "#008800";
	private static final String TAG_COLOR = "#800080";
	public static String color_attr_name;
	public static String color_backgroup;
	public static String color_comment;
	public static String color_font = "#000000";
	public static String color_function;
	public static String color_keyword;
	public static String color_string;
	public static String color_tag;
	private static String[] schemeNames;
	private static ArrayList<SchemeTable> schemeTables;

	static
	{
		color_backgroup = "#ffffff";
		color_string = "#008800";
		color_keyword = "#000088";
		color_comment = "#3F7F5F";
		color_tag = "#800080";
		color_attr_name = "#FF0000";
		color_function = "#000080";
		schemeTables = new ArrayList();
	}

	
	public static String[] getSchemeNames()
	{
		if (schemeNames == null)
			loadAllScheme();
		return schemeNames;
	}
	//TabHost.setEditTextPref调用
	public static void set(SharedPreferences mSharedPreferences) {
        loadAllScheme();
        String cs = mSharedPreferences.getString("hl_colorscheme", "");
        if (!cs.equals("")) {
            Iterator it = schemeTables.iterator();
            while (it.hasNext()) {
                SchemeTable st = (SchemeTable) it.next();
                if (cs.equals(st.colors_name)) {
                    color_font = st.font;
                    color_backgroup = st.backgroup;
                    color_string = st.string;
                    color_keyword = st.keyword;
                    color_comment = st.comment;
                    color_tag = st.tag;
                    color_attr_name = st.attr_name;
                    color_function = st.function;
                    return;
                }
            }
        }
        if (mSharedPreferences.getBoolean("use_custom_hl_color", false)) {
            color_font = mSharedPreferences.getString("hlc_font", color_font);
            color_backgroup = mSharedPreferences.getString("hlc_backgroup", color_backgroup);
            color_string = mSharedPreferences.getString("hlc_string", color_string);
            color_keyword = mSharedPreferences.getString("hlc_keyword", color_keyword);
            color_comment = mSharedPreferences.getString("hlc_comment", color_comment);
            color_tag = mSharedPreferences.getString("hlc_tag", color_tag);
            color_attr_name = mSharedPreferences.getString("hlc_attr_name", color_attr_name);
            color_function = mSharedPreferences.getString("hlc_function", color_function);
            return;
        }
        color_font = FONT_COLOR;
        color_backgroup = BACKGROUP_COLOR;
        color_string = STRING_COLOR;
        color_keyword = KEYWORD_COLOR;
        color_comment = COMMENT_COLOR;
        color_tag = TAG_COLOR;
        color_attr_name = ATTR_NAME_COLOR;
        color_function = FUNCTION_COLOR;
    }
	public static void loadAllScheme() {
        if (schemeTables.size() <= 0) {
            File files = new File(QSEditor.TEMP_PATH + "/colors");
            if (files.isDirectory()) {
                for (File f : files.listFiles(new FilenameFilter() {
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".conf")) {
								return true;
							}
							return false;
						}
					})) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        SchemeTable st = new SchemeTable();
                        while (true) {
                            String line = br.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] sp = line.split("=");
                            if (sp.length == 2) {
                                String key = sp[0].trim();
                                String val = sp[1].trim();
                                if ("colors_name".equals(key)) {
                                    st.colors_name = val;
                                } else if ("backgroup".equals(key)) {
                                    st.backgroup = val;
                                } else if ("string".equals(key)) {
                                    st.string = val;
                                } else if ("font".equals(key)) {
                                    st.font = val;
                                } else if ("comment".equals(key)) {
                                    st.comment = val;
                                } else if ("keyword".equals(key)) {
                                    st.keyword = val;
                                } else if ("tag".equals(key)) {
                                    st.tag = val;
                                } else if ("attr_name".equals(key)) {
                                    st.attr_name = val;
                                } else if ("function".equals(key)) {
                                    st.function = val;
                                }
                            }
                        }
                        schemeTables.add(st);
                    } catch (Exception e) {
                    }
                }
                if (schemeTables.size() > 0) {
                    schemeNames = new String[schemeTables.size()];
                    int i = 0;
                    Iterator it = schemeTables.iterator();
                    while (it.hasNext()) {
                        schemeNames[i] = ((SchemeTable) it.next()).colors_name;
                        i++;
                    }
                }
            }
        }
    }

	
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.colorschemes.ColorScheme
 * JD-Core Version:    0.6.0
 */
