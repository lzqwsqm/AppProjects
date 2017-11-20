package com.qingshan.util;
import com.qingshan.editor.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;

class FileListAdapter extends ArrayAdapter<File>
{
	private ViewHolder holder;
	private Bitmap mIcon_apk;
	private Bitmap mIcon_audio;
	private Bitmap mIcon_file;
	private Bitmap mIcon_folder;
	private Bitmap mIcon_image;
	private Bitmap mIcon_video;
	private LayoutInflater mInflater;
	private String[] type_app;
	private String[] type_audio;
	private String[] type_image;
	private String[] type_video;

	public FileListAdapter(Context context, int Resource, List<File> objects) {
        super(context, Resource, objects);
        this.type_audio = new String[]{".m4a", ".mp3", ".wma", ".mid", ".xmf", ".ogg", ".wav"};
        this.type_video = new String[]{".3gp", ".mp4", ".avi", ".rm", ".rmvb"};
        this.type_image = new String[]{".jpg", ".gif", ".png", ".bmp", ".jpeg"};
        this.type_app = new String[]{".apk"};
        this.mInflater = LayoutInflater.from(context);
        Resources res = context.getResources();
        this.mIcon_folder = BitmapFactory.decodeResource(res, R.drawable.folder);
        this.mIcon_file = BitmapFactory.decodeResource(res, R.drawable.file);
        this.mIcon_image = BitmapFactory.decodeResource(res, R.drawable.image);
        this.mIcon_audio = BitmapFactory.decodeResource(res, R.drawable.audio);
        this.mIcon_video = BitmapFactory.decodeResource(res, R.drawable.video);
        this.mIcon_apk = BitmapFactory.decodeResource(res, R.drawable.apk);
    }

	
	private String getMimeType(String file) {
        int i = 0;
        for (String ext : this.type_audio) {
            if (file.endsWith(ext)) {
                return "audio";
            }
        }
        for (String ext : this.type_video) {
            if (file.endsWith(ext)) {
                return "video";
            }
        }
        for (String ext : this.type_image) {
            if (file.endsWith(ext)) {
                return "image";
            }
        }
        /*String[] strArr = this.type_app;
        int length = strArr.length;
        while (i < length) {
            if (file.endsWith(strArr[i])) {
                return "app";
            }
            i++;
        }*/
		for(String ext:type_app)
        {
            if(file.endsWith(ext))
                return "app";
        }
        return "";
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
        File file = (File) getItem(position);
        String title = file.getName();
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.file_list, null);
            this.holder = new ViewHolder();
            this.holder.f_title = (TextView) convertView.findViewById(R.id.f_title);
            this.holder.f_text = (TextView) convertView.findViewById(R.id.f_text);
            this.holder.f_icon = (ImageView) convertView.findViewById(R.id.f_icon);
            convertView.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        String f_type = getMimeType(file.getName());
        this.holder.f_title.setText(title);
        if (file.isDirectory()) {
            this.holder.f_icon.setImageBitmap(this.mIcon_folder);
        } else
		{
			if ("image".equals(f_type)) {
            this.holder.f_icon.setImageBitmap(this.mIcon_image);
        } else if ("audio".equals(f_type)) {
            this.holder.f_icon.setImageBitmap(this.mIcon_audio);
        } else if ("video".equals(f_type)) {
            this.holder.f_icon.setImageBitmap(this.mIcon_video);
        } else if ("apk".equals(f_type)) {
            this.holder.f_icon.setImageBitmap(this.mIcon_apk);
        } else {
            this.holder.f_icon.setImageBitmap(this.mIcon_file);
        }
		}
        StringBuilder textStringBuilder = new StringBuilder();
        if (!"..".equals(title)) {
            textStringBuilder.append(TimeUtil.getDate(file.lastModified())).append("   ");
        }
        if (file.length() > 0) {
            textStringBuilder.append(FileUtil.byteCountToDisplaySize(file.length()));
        }
        this.holder.f_text.setText(textStringBuilder.toString());
        return convertView;
    }

	private class ViewHolder
	{
		public ImageView f_icon;
		public TextView f_text;
		public TextView f_title;

		private ViewHolder()
		{
		}
	}
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.FileListAdapter
 * JD-Core Version:    0.6.0
 */
