package com.qingshan.util;
import com.qingshan.editor.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.qingshan.widget.ColorPickerView;

public class ColorPicker extends Dialog
{
	private EditText colorEditText;
	private ColorPickerView mColorView;
	private int mDefaultColor;
	private String mKey;
	private OnColorChangedListener mListener;
	private OnColorChangedListener mOnColorChangedListener = new OnColorChangedListener()
	{
		public void onColorChanged(String key, String color) {
			ColorPicker.this.colorEditText.setText(color);
		}
	};
	private String mTitle;
	private View.OnClickListener onCancelClickListener = new View.OnClickListener()
	{
		public void onClick(View v)
		{
			ColorPicker.this.dismiss();
		}
	};
	private View.OnClickListener onOkClickListener = new View.OnClickListener()
	{
		public void onClick(View v) {
			String color = ColorPicker.this.colorEditText.getText().toString().trim();
			if (!"".equals(color)) {
				try {
					Color.parseColor(color);
					ColorPicker.this.mListener.onColorChanged(ColorPicker.this.mKey, color);
					ColorPicker.this.dismiss();
				} catch (Exception e) {
					Toast.makeText(ColorPicker.this.getContext(), R.string.invalid_color, 1).show();
				}
			}
		}
	};

	public ColorPicker(Context context, OnColorChangedListener listener, String key, String title, int defaultColor) {
		super(context);
		this.mTitle = title;
        this.mListener = listener;
        this.mKey = key;
        this.mDefaultColor = defaultColor;
	}

	public static String getColor(int color) {
        return "#" + Integer.toHexString(color).substring(2);
    }

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        setTitle(this.mTitle);
        this.colorEditText = (EditText) findViewById(R.id.color_text);
        this.colorEditText.setText(getColor(this.mDefaultColor));
        this.mColorView = (ColorPickerView) findViewById(R.id.color_picker_view);
        this.mColorView.setColor(this.mDefaultColor);
        this.mColorView.setOnColorChangedListener(this.mOnColorChangedListener);
        Button cancel = (Button) findViewById(R.id.cancel);
        ((Button) findViewById(R.id.ok)).setOnClickListener(this.onOkClickListener);
        cancel.setOnClickListener(this.onCancelClickListener);
    }
	public static abstract interface OnColorChangedListener
	{
		public abstract void onColorChanged(String paramString1, String paramString2);
	}
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.util.ColorPicker
 * JD-Core Version:    0.6.0
 */
