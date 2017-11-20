package com.qingshan.editor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;
import com.qingshan.colorschemes.ColorScheme;
import com.qingshan.util.ColorPicker;
//import com.jecelyin.util.ColorPicker.OnColorChangedListener;
import com.qingshan.util.TimeUtil;
import java.net.URLEncoder;

public class Options
extends PreferenceActivity
{
	private int category;
	/*private Preference.OnPreferenceChangeListener mOnHighlightChange = new Preference.OnPreferenceChangeListener()
	{
		public boolean onPreferenceChange(Preference paramAnonymousPreference, Object paramAnonymousObject)
		{
			PreferenceCategory localPreferenceCategory = (PreferenceCategory)Options.this.findPreference("custom_highlight_color");
			boolean bool;
			if (!paramAnonymousObject.toString().equals("true")) {
				bool = false;
			} else {
				bool = true;
			}
			localPreferenceCategory.setEnabled(bool);
			return true;
		}
	};*/
	private OnPreferenceChangeListener mOnHighlightChange = new OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference pref, Object val)
        {
            PreferenceCategory cate = (PreferenceCategory) findPreference("custom_highlight_color");
            //ListPreference colorscheme = (ListPreference) findPreference("hl_colorscheme");
            boolean isTrue = val.toString().equals("true");
            cate.setEnabled(isTrue ? true : false);
            //colorscheme.setEnabled(isTrue ? false : true);
            return true;
        }
    };
	private SharedPreferences mSP;
	
	public Options() {
        this.mOnHighlightChange = new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference pref, Object val) {
                ((PreferenceCategory) Options.this.findPreference("custom_highlight_color")).setEnabled(val.toString().equals("true"));
                return true;
            }
        };
    }
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.category = getIntent().getIntExtra("category", R.xml.options);
        addPreferencesFromResource(this.category);
        this.mSP = getPreferenceManager().getSharedPreferences();
        switch (this.category) {
            case R.xml.date_format /*2130968576*/:
                initDateFormat();
				break;
            case R.xml.editors /*2130968577*/:
                initEditors();
				break;
            case R.xml.help /*2130968578*/:
                initHelp();
				break;
            case R.xml.highlight /*2130968579*/:
                initHighlight();
				break;
            case R.xml.options /*2130968580*/:
                init();
				break;
            case R.xml.view /*2130968583*/:
                initView();
				break;
            default:
				break;
        }
    }
	//TabHost.setEditTextPref调用
	public static Typeface getFont(String font) {
        if ("Monospace".equals(font)) {
            return Typeface.MONOSPACE;
        }
        if ("Sans Serif".equals(font)) {
            return Typeface.SANS_SERIF;
        }
        if ("Serif".equals(font)) {
            return Typeface.SERIF;
        }
        return Typeface.DEFAULT;
	}

	private void init() {
        setOptionsPreference("opt_view", R.xml.view);
        setOptionsPreference("opt_editors", R.xml.editors);
        setOptionsPreference("opt_highlight", R.xml.highlight);
        setOptionsPreference("opt_search", R.xml.search);
        setOptionsPreference("opt_other", R.xml.other);
        setOptionsPreference("opt_help", R.xml.help);
        findPreference("donate").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Options.this.startActivity(Donate.getWebIntent());
					return true;
				}
			});
        findPreference("clear_history").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Options.this.getSharedPreferences(QSEditor.PREF_HISTORY, 0).edit().clear().commit();
					Toast.makeText(Options.this.getApplicationContext(), R.string.clear_history_ok, 1).show();
					return true;
				}
			});
    }

	private void initDateFormat() {
        ListPreference sys_format = (ListPreference) findPreference("sys_format");
        String[] fmVal = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        sys_format.setEntries(new String[]{TimeUtil.getDateByFormat(0), TimeUtil.getDateByFormat(1), TimeUtil.getDateByFormat(2), TimeUtil.getDateByFormat(3), TimeUtil.getDateByFormat(4), TimeUtil.getDateByFormat(5), TimeUtil.getDateByFormat(6), TimeUtil.getDateByFormat(7), TimeUtil.getDateByFormat(8), TimeUtil.getDateByFormat(9)});
        sys_format.setEntryValues(fmVal);
        if ("".equals(sys_format.getValue())) {
            sys_format.setValue(fmVal[0]);
        }
        ((EditTextPreference) findPreference("custom_date_format")).getEditText().setSingleLine();
    }

	private void initEditors() {
        setOptionsPreference("date_format", R.xml.date_format);
    }

	private void initHelp() {
        findPreference("about").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Options.this.startActivity(new Intent(Options.this, About.class));
					return true;
				}
			});
        findPreference("help").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Help.showHelp(Options.this);
					return true;
				}
			});
        findPreference("feedback").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Uri uri;
					try {
						uri = Uri.parse("http://www.jecelyin.com/920report.php?ver=" + URLEncoder.encode(QSEditor.version, "utf-8"));
					} catch (Exception e) {
						uri = Uri.parse("http://www.jecelyin.com/920report.php?var=badver");
					}
					Options.this.startActivity(new Intent("android.intent.action.VIEW", uri));
					return true;
				}
			});
        findPreference("project").setOnPreferenceClickListener(new OnPreferenceClickListener() {
				public boolean onPreferenceClick(Preference arg0) {
					Options.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.getlocalization.com/920TextEditor/")));
					return true;
				}
			});
    }

	private void initHighlight() {
        setHighlightEvent("hlc_font", ColorScheme.color_font);
        setHighlightEvent("hlc_backgroup", ColorScheme.color_backgroup);
        setHighlightEvent("hlc_string", ColorScheme.color_string);
        setHighlightEvent("hlc_keyword", ColorScheme.color_keyword);
        setHighlightEvent("hlc_comment", ColorScheme.color_comment);
        setHighlightEvent("hlc_tag", ColorScheme.color_tag);
        setHighlightEvent("hlc_attr_name", ColorScheme.color_attr_name);
        setHighlightEvent("hlc_function", ColorScheme.color_function);
        ((PreferenceCategory) findPreference("custom_highlight_color")).setEnabled(this.mSP.getBoolean("use_custom_hl_color", false));
        ((CheckBoxPreference) findPreference("use_custom_hl_color")).setOnPreferenceChangeListener(this.mOnHighlightChange);
        ListPreference csPref = (ListPreference) findPreference("hl_colorscheme");
        String[] csNames = ColorScheme.getSchemeNames();
        if (csNames == null) {
            csNames = new String[]{"Default"};
        }
        csPref.setEntries(csNames);
        csPref.setEntryValues(csNames);
    }

	private void initView() {
        ListPreference fontPf = (ListPreference) findPreference("font");
        String[] fonts = new String[]{"Normal", "Monospace", "Sans Serif", "Serif"};
        fontPf.setEntries(fonts);
        fontPf.setEntryValues(fonts);
        fontPf.setDefaultValue("Monospace");
        ListPreference fontSizePf = (ListPreference) findPreference("font_size");
        String[] font_size = new String[]{"10", "12", "13", "14", "16", "18", "20", "22", "24", "26", "28", "32"};
        fontSizePf.setEntries(font_size);
        fontSizePf.setEntryValues(font_size);
        fontSizePf.setDefaultValue("14");
    }

	private void setHighlightEvent(final String key, final String def) {
        Preference pref = findPreference(key);
        pref.setSummary(this.mSP.getString(key, def));
		pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				public boolean onPreferenceClick(Preference preference) {
					new ColorPicker(Options.this, new ColorListener(), preference.getKey(), preference.getTitle().toString(), Color.parseColor(preference.getSharedPreferences().getString(key, def))).show();
					return true;
				}
			});
	}

	private void setOptionsPreference(String paramString, final int paramInt)
	{
		findPreference(paramString).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
			{
				public boolean onPreferenceClick(Preference paramAnonymousPreference)
				{
					Intent localIntent = new Intent(Options.this, Options.class);
					localIntent.putExtra("category", paramInt);
					Options.this.startActivity(localIntent);
					return true;
				}
			});
	}

	

	private class ColorListener
    implements ColorPicker.OnColorChangedListener
	{
		private ColorListener() {}

		public void onColorChanged(String paramString1, String paramString2)
		{
			Preference localPreference = Options.this.findPreference(paramString1);
			localPreference.setSummary(paramString2);
			localPreference.getEditor().putString(paramString1, paramString2).commit();
		}
	}
	
}


/* Location:              C:\Documents and Settings\李忠全\桌面\9.4.jar!\com\jecelyin\editor\Options.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */
