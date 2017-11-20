package com.qingshan.editor;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

public class EncodingList
{
	public static final String[] list;
	private DialogInterface.OnClickListener mClickEvent = new DialogInterface.OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which) {
			EncodingList.this.mJecEditor.setEncoding(EncodingList.list[which]);
		}
	};
	private QSEditor mJecEditor;

	static
	{
		String[] arrayOfString = new String[66];
		arrayOfString[0] = "UTF-8";
		arrayOfString[1] = "GBK";
		arrayOfString[2] = "ARMSCII-8";
		arrayOfString[3] = "BIG5";
		arrayOfString[4] = "BIG5-HKSCS";
		arrayOfString[5] = "CP866";
		arrayOfString[6] = "CP932";
		arrayOfString[7] = "EUC-JP";
		arrayOfString[8] = "EUC-JP-MS";
		arrayOfString[9] = "EUC-KR";
		arrayOfString[10] = "EUC-TW";
		arrayOfString[11] = "GB18030";
		arrayOfString[12] = "GB2312";
		arrayOfString[13] = "GBK";
		arrayOfString[14] = "Georgian";
		arrayOfString[15] = "HZ";
		arrayOfString[16] = "IBM850";
		arrayOfString[17] = "IBM852";
		arrayOfString[18] = "IBM855";
		arrayOfString[19] = "IBM857";
		arrayOfString[20] = "IBM862";
		arrayOfString[21] = "IBM864";
		arrayOfString[22] = "ISO-2022-JP";
		arrayOfString[23] = "ISO-2022-KR";
		arrayOfString[24] = "ISO-8859-1";
		arrayOfString[25] = "ISO-8859-10";
		arrayOfString[26] = "ISO-8859-13";
		arrayOfString[27] = "ISO-8859-14";
		arrayOfString[28] = "ISO-8859-15";
		arrayOfString[29] = "ISO-8859-16";
		arrayOfString[30] = "ISO-8859-2";
		arrayOfString[31] = "ISO-8859-3";
		arrayOfString[32] = "ISO-8859-4";
		arrayOfString[33] = "ISO-8859-5";
		arrayOfString[34] = "ISO-8859-6";
		arrayOfString[35] = "ISO-8859-7";
		arrayOfString[36] = "ISO-8859-8";
		arrayOfString[37] = "ISO-8859-8-I";
		arrayOfString[38] = "ISO-8859-9";
		arrayOfString[39] = "ISO-IR-111";
		arrayOfString[40] = "JOHAB";
		arrayOfString[41] = "KOI8-R";
		arrayOfString[42] = "KOI8R";
		arrayOfString[43] = "KOI8U";
		arrayOfString[44] = "SHIFT_JIS";
		arrayOfString[45] = "TCVN";
		arrayOfString[46] = "TIS-620";
		arrayOfString[47] = "UCS-2";
		arrayOfString[48] = "UCS-4";
		arrayOfString[49] = "UHC";
		arrayOfString[50] = "UTF-7";
		arrayOfString[51] = "UTF-8";
		arrayOfString[52] = "UTF-16";
		arrayOfString[53] = "UTF-16BE";
		arrayOfString[54] = "UTF-16LE";
		arrayOfString[55] = "UTF-32";
		arrayOfString[56] = "VISCII";
		arrayOfString[57] = "WINDOWS-1250";
		arrayOfString[58] = "WINDOWS-1251";
		arrayOfString[59] = "WINDOWS-1252";
		arrayOfString[60] = "WINDOWS-1253";
		arrayOfString[61] = "WINDOWS-1254";
		arrayOfString[62] = "WINDOWS-1255";
		arrayOfString[63] = "WINDOWS-1256";
		arrayOfString[64] = "WINDOWS-1257";
		arrayOfString[65] = "WINDOWS-1258";
		list = arrayOfString;
	}

	public EncodingList(QSEditor mJecEditor)
	{
		this.mJecEditor = mJecEditor;
        new Builder(mJecEditor).setTitle(R.string.encoding).setAdapter(new DialogListAdapter(mJecEditor, R.layout.dialog_list_row, Arrays.asList(list)), this.mClickEvent).show();
	}

	private class DialogListAdapter extends ArrayAdapter<String> {

        private class ViewHolder {
            public TextView text;

            private ViewHolder() {
            }
        }

        public DialogListAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(EncodingList.this.mJecEditor, R.layout.dialog_list_row, null);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.textView1);
                view.setTag(holder);
            }
            holder.text.setText((String) getItem(position));
            return view;
        }
    }
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.editor.EncodingList
 * JD-Core Version:    0.6.0
 */
