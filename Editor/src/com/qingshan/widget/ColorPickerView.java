package com.qingshan.widget;

//import I;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.qingshan.util.ColorPicker;
import com.qingshan.util.ColorPicker.OnColorChangedListener;
//ColorPicker.onCreate调用
public class ColorPickerView extends View
{
	private static final float CENTER_RADIUS_SCALE = 0.4F;
	private float center_radius;
	private Paint mCenterPaint;
	private int mColor1;
	private int mColor2;
	private int mColor3;
	private int mColor4 = 0xFFFFFFFF/*-1*/;
	private int[] mColors;
	private boolean mHighlightCenter;
	private Paint mLeftPaint;
	private ColorPicker.OnColorChangedListener mListener;
	private Paint mPaint;
	private float mRadius;
	private Paint mRightPaint;
	private boolean mTrackingCenter;

	public ColorPickerView(Context c) {
        super(c);
        //this.mColor4 = -1;
    }

	public ColorPickerView(Context context, AttributeSet attr) {
        super(context, attr);
        //this.mColor4 = -1;
        init();
    }

	private int ave(int s, int d, float p) {
        return Math.round(((float) (d - s)) * p) + s;
    }

	private void init() {
        mColors = new int[] {
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
            0xFFFFFF00, 0xFFFF0000
        };
        mColor1 = 0xFFFFFFFF;
        mColor2 = 0xFF000000;
        Shader s = new SweepGradient(0.0f, 0.0f, this.mColors, null);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG/*1*/);
        this.mPaint.setShader(s);
        this.mPaint.setStyle(Style.STROKE);
        this.mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG/*1*/);
        this.mCenterPaint.setStrokeWidth(5.0f);
        this.mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG/*1*/);
        this.mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG/*1*/);
    }
	
	private int interpColor(int[] colors, float unit, boolean isSlider) {
        if (unit <= 0.0f) {
            return colors[0];
        }
        if (unit >= 1.0f) {
            return colors[colors.length - 1];
        }
        float p;
        int c0;
        int c1;
        if (isSlider) {
            p = unit;
            c0 = colors[0];
            c1 = colors[colors.length - 1];
        } else {
            p = unit * ((float) (colors.length - 1));
            int i = (int) p;
            p -= (float) i;
            c0 = colors[i];
            c1 = colors[i + 1];
        }
        return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p));
	}
    @Override
	protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        float lr_width = ((float) width) * 0.2f;
        float space = ((float) width) * 0.03f;
        //半径
		float outer_radius = ((float) Math.min(width, height)) * 0.48f;
        float touch_feedback_ring = this.center_radius + (2.0f * this.mCenterPaint.getStrokeWidth());
        this.mRadius = (outer_radius + touch_feedback_ring) / 2.0f;
        //两边渐变
		this.mLeftPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) height, this.mColor1, this.mColor2, TileMode.CLAMP));
        canvas.drawRect(space, 0.0f, lr_width, (float) height, this.mLeftPaint);
        this.mRightPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) height, this.mColor3, this.mColor4, TileMode.CLAMP));
        canvas.drawRect(((float) width) - lr_width, 0.0f, ((float) width) - space, (float) height, this.mRightPaint);
		//转移坐标到中间
		canvas.translate((float) (width / 2), (float) (height / 2));
        this.mPaint.setStrokeWidth(outer_radius - touch_feedback_ring);
        //色盘
		canvas.drawCircle(0.0f, 0.0f, this.mRadius, this.mPaint);
        //中间圆心
		canvas.drawCircle(0.0f, 0.0f, this.center_radius, this.mCenterPaint);
        if (this.mTrackingCenter) {
            int c = this.mCenterPaint.getColor();
            this.mCenterPaint.setStyle(Style.STROKE);
            if (this.mHighlightCenter) {
                this.mCenterPaint.setAlpha(255);
            } else {
                this.mCenterPaint.setAlpha(128);
            }
            canvas.drawCircle(0.0f, 0.0f, this.center_radius + this.mCenterPaint.getStrokeWidth(), this.mCenterPaint);
            this.mCenterPaint.setStyle(Style.FILL);
            this.mCenterPaint.setColor(c);
        }
    }
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //窗口尺寸
		int size = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        //中间圆心的圆周率
		this.center_radius = (CENTER_RADIUS_SCALE * (((float) size) * 0.5f)) / 2.0f;
        setMeasuredDimension(size, (int) (((float) size) * 0.6f));
    }
	/*
	 .method public onTouchEvent(Landroid/view/MotionEvent;)Z
	 .locals 12
	 .param p1, "event"    # Landroid/view/MotionEvent;

	 .prologue
	 .line 169
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->getWidth()I

	 move-result v5

	 .line 170
	 .local v5, "width":I
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->getHeight()I

	 move-result v1

	 .line 171
	 .local v1, "height":I
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

	 move-result v8

	 div-int/lit8 v9, v5, 0x2

	 int-to-float v9, v9

	 sub-float v6, v8, v9

	 .line 172
	 .local v6, "x":F
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

	 move-result v8

	 div-int/lit8 v9, v1, 0x2

	 int-to-float v9, v9

	 sub-float v7, v8, v9

	 .line 174
	 .local v7, "y":F
	 invoke-static {v6, v7}, Landroid/graphics/PointF;->length(FF)F

	 move-result v8

	 iget v9, p0, Lcom/jecelyin/widget/ColorPickerView;->center_radius:F

	 cmpg-float v8, v8, v9

	 if-gtz v8, :cond_1

	 const/4 v2, 0x1

	 .line 176
	 .local v2, "inCenter":Z
	 :goto_0
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getAction()I

	 move-result v8

	 packed-switch v8, :pswitch_data_0

	 .line 233
	 :cond_0
	 :goto_1
	 const/4 v8, 0x1

	 return v8

	 .line 174
	 .end local v2    # "inCenter":Z
	 :cond_1
	 const/4 v2, 0x0

	 goto :goto_0

	 .line 178
	 .restart local v2    # "inCenter":Z
	 :pswitch_0
	 iput-boolean v2, p0, Lcom/jecelyin/widget/ColorPickerView;->mTrackingCenter:Z

	 .line 179
	 if-eqz v2, :cond_2

	 .line 180
	 const/4 v8, 0x1

	 iput-boolean v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mHighlightCenter:Z

	 .line 181
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->invalidate()V

	 goto :goto_1

	 .line 185
	 :cond_2
	 :pswitch_1
	 iget-boolean v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mTrackingCenter:Z

	 if-eqz v8, :cond_3

	 .line 186
	 iget-boolean v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mHighlightCenter:Z

	 if-eq v8, v2, :cond_0

	 .line 187
	 iput-boolean v2, p0, Lcom/jecelyin/widget/ColorPickerView;->mHighlightCenter:Z

	 .line 188
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->invalidate()V

	 goto :goto_1

	 .line 194
	 :cond_3
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

	 move-result v8

	 int-to-float v9, v5

	 const v10, 0x3e4ccccd    # 0.2f

	 mul-float/2addr v9, v10

	 cmpg-float v8, v8, v9

	 if-gtz v8, :cond_5

	 .line 196
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

	 move-result v8

	 int-to-float v9, v1

	 div-float v4, v8, v9

	 .line 197
	 .local v4, "unit":F
	 const/4 v8, 0x2

	 new-array v8, v8, [I

	 const/4 v9, 0x0

	 iget v10, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor1:I

	 aput v10, v8, v9

	 const/4 v9, 0x1

	 iget v10, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor2:I

	 aput v10, v8, v9

	 const/4 v9, 0x1

	 invoke-direct {p0, v8, v4, v9}, Lcom/jecelyin/widget/ColorPickerView;->interpColor([IFZ)I

	 move-result v3

	 .line 198
	 .local v3, "newcolor":I
	 iput v3, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor3:I

	 .line 214
	 :goto_2
	 iget-object v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mListener:Lcom/jecelyin/util/ColorPicker$OnColorChangedListener;

	 if-eqz v8, :cond_4

	 .line 215
	 iget-object v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mListener:Lcom/jecelyin/util/ColorPicker$OnColorChangedListener;

	 const-string v9, ""

	 invoke-static {v3}, Lcom/jecelyin/util/ColorPicker;->getColor(I)Ljava/lang/String;

	 move-result-object v10

	 invoke-interface {v8, v9, v10}, Lcom/jecelyin/util/ColorPicker$OnColorChangedListener;->onColorChanged(Ljava/lang/String;Ljava/lang/String;)V

	 .line 217
	 :cond_4
	 iget-object v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mCenterPaint:Landroid/graphics/Paint;

	 invoke-virtual {v8, v3}, Landroid/graphics/Paint;->setColor(I)V

	 .line 218
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->invalidate()V

	 goto :goto_1

	 .line 199
	 .end local v3    # "newcolor":I
	 .end local v4    # "unit":F
	 :cond_5
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getX()F

	 move-result v8

	 int-to-float v9, v5

	 int-to-float v10, v5

	 const v11, 0x3e4ccccd    # 0.2f

	 mul-float/2addr v10, v11

	 sub-float/2addr v9, v10

	 cmpl-float v8, v8, v9

	 if-ltz v8, :cond_6

	 .line 200
	 invoke-virtual {p1}, Landroid/view/MotionEvent;->getY()F

	 move-result v8

	 int-to-float v9, v1

	 div-float v4, v8, v9

	 .line 201
	 .restart local v4    # "unit":F
	 const/4 v8, 0x2

	 new-array v8, v8, [I

	 const/4 v9, 0x0

	 iget v10, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor3:I

	 aput v10, v8, v9

	 const/4 v9, 0x1

	 iget v10, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor4:I

	 aput v10, v8, v9

	 const/4 v9, 0x1

	 invoke-direct {p0, v8, v4, v9}, Lcom/jecelyin/widget/ColorPickerView;->interpColor([IFZ)I

	 move-result v3

	 .restart local v3    # "newcolor":I
	 goto :goto_2

	 .line 204
	 .end local v3    # "newcolor":I
	 .end local v4    # "unit":F
	 :cond_6
	 float-to-double v8, v7

	 float-to-double v10, v6

	 invoke-static {v8, v9, v10, v11}, Ljava/lang/Math;->atan2(DD)D

	 move-result-wide v8

	 double-to-float v0, v8

	 .line 206
	 .local v0, "angle":F
	 const v8, 0x40c90fdb

	 div-float v4, v0, v8

	 .line 207
	 .restart local v4    # "unit":F
	 const/4 v8, 0x0

	 cmpg-float v8, v4, v8

	 if-gez v8, :cond_7

	 .line 208
	 const/high16 v8, 0x3f800000    # 1.0f

	 add-float/2addr v4, v8

	 .line 210
	 :cond_7
	 iget-object v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mColors:[I

	 const/4 v9, 0x0

	 invoke-direct {p0, v8, v4, v9}, Lcom/jecelyin/widget/ColorPickerView;->interpColor([IFZ)I

	 move-result v3

	 .line 211
	 .restart local v3    # "newcolor":I
	 invoke-virtual {p0, v3}, Lcom/jecelyin/widget/ColorPickerView;->setColor(I)V

	 goto :goto_2

	 .line 222
	 .end local v0    # "angle":F
	 .end local v3    # "newcolor":I
	 .end local v4    # "unit":F
	 :pswitch_2
	 iget-boolean v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mTrackingCenter:Z

	 if-eqz v8, :cond_0

	 .line 223
	 if-eqz v2, :cond_8

	 .line 228
	 :cond_8
	 const/4 v8, 0x0

	 iput-boolean v8, p0, Lcom/jecelyin/widget/ColorPickerView;->mTrackingCenter:Z

	 .line 229
	 invoke-virtual {p0}, Lcom/jecelyin/widget/ColorPickerView;->invalidate()V

	 goto/16 :goto_1

	 .line 176
	 nop

	 :pswitch_data_0
	 .packed-switch 0x0
	 :pswitch_0
	 :pswitch_2
	 :pswitch_1
	 .end packed-switch
	 .end method

	 .method public setColor(I)V
	 .locals 1
	 .param p1, "color"    # I

	 .prologue
	 .line 159
	 iget-object v0, p0, Lcom/jecelyin/widget/ColorPickerView;->mCenterPaint:Landroid/graphics/Paint;

	 invoke-virtual {v0, p1}, Landroid/graphics/Paint;->setColor(I)V

	 .line 161
	 iput p1, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor1:I

	 .line 162
	 const/high16 v0, -0x1000000

	 iput v0, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor2:I

	 .line 163
	 iput p1, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor3:I

	 .line 164
	 const/4 v0, -0x1

	 iput v0, p0, Lcom/jecelyin/widget/ColorPickerView;->mColor4:I

	 .line 165
	 return-void
	 .end method
	*/
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        float x = event.getX() - width/2; // - width * 0.20F
        float y = event.getY() - height/2;

        boolean inCenter = PointF.length(x, y) <= center_radius;
        //Log.v("color", "x:"+x+" ex:"+event.getX()+" y:"+y+" center_radius:"+center_radius+" len:"+PointF.length(x, y));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTrackingCenter = inCenter;
                if (inCenter) {
                    mHighlightCenter = true;
                    invalidate();
                    break;
                }
            case MotionEvent.ACTION_MOVE:
                if (mTrackingCenter) {
                    if (mHighlightCenter != inCenter) {
                        mHighlightCenter = inCenter;
                        invalidate();
                    }
                } else {
                    float unit;
                    int newcolor;
                    //左边色盘
                    if(event.getX() <= width*0.2F)
                    {
                        unit = event.getY() / height;
                        newcolor = interpColor(new int[] {mColor1, mColor2}, unit, true);
                        mColor3 = newcolor;
                    } else if (event.getX() >= width-width*0.2F) { //右边色盘
                        unit = event.getY() / height;
                        newcolor = interpColor(new int[] {mColor3, mColor4}, unit, true);
                    } else {
                        //控制色盘范围
                        float angle = (float)java.lang.Math.atan2(y, x);
                        // need to turn angle [-PI ... PI] into unit [0....1]
                        unit = angle/(2*(float) Math.PI);
                        if (unit < 0) {
                            unit += 1;
                        }
                        newcolor = interpColor(mColors, unit, false);
                        setColor(newcolor);

                    }
                    if (mListener != null) {
                        mListener.onColorChanged("",ColorPicker.getColor(newcolor));
                    }
                    mCenterPaint.setColor(newcolor);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mTrackingCenter) {
                    if (inCenter) {
                        if (mListener != null) {
                            //mListener.onColorPicked(this, mCenterPaint.getColor());
                        }
                    }
                    mTrackingCenter = false;    // so we draw w/o halo
                    invalidate();
                }
                break;
        }
        return true;
    }
	/*public boolean onTouchEvent(MotionEvent paramMotionEvent)
	{
		int m = getWidth();
		int n = getHeight();
		float f4 = paramMotionEvent.getX() - m / 2;
		float f1 = paramMotionEvent.getY() - n / 2;
		boolean bool;
		if (PointF.length(f4, f1) > this.center_radius)
			bool = false;
		else
			bool = true;
		switch (paramMotionEvent.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				this.mTrackingCenter = bool;
				if (bool)break;
				break;
			case MotionEvent.ACTION_MOVE:
				//int k=0;
				if (!this.mTrackingCenter)
				{
					int k=0;
					if (paramMotionEvent.getX() > 0.2F * m)
					{
						if (paramMotionEvent.getX() < m - 0.2F * m)
						{
							f1 = (float)Math.atan2(f1, f4) / 6.283186F;
							if (f1 < 0.0F)
								f1 += 1.0F;
							int i = interpColor(this.mColors, f1, false);
							setColor(i);
						}
						else
						{
							float f3 = paramMotionEvent.getY() / n;
							int[] arrayOfInt1 = new int[2];
							arrayOfInt1[0] = this.mColor3;
							arrayOfInt1[1] = this.mColor4;
							int j = interpColor(arrayOfInt1, f3, true);
						}
					}
					else
					{
						float f2 = paramMotionEvent.getY() / n;
						int[] arrayOfInt2 = new int[2];
						arrayOfInt2[0] = this.mColor1;
						arrayOfInt2[1] = this.mColor2;
						k = interpColor(arrayOfInt2, f2, true);
						this.mColor3 = k;
					}
					if (this.mListener != null)
						this.mListener.onColorChanged("", ColorPicker.getColor(k));
					this.mCenterPaint.setColor(k);
					invalidate();
				}
				else if (this.mHighlightCenter != bool)
				{
					this.mHighlightCenter = bool;
					invalidate();
					//break label349;
					return true;
					}else{
					
					this.mHighlightCenter = true;
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (this.mTrackingCenter)
				{
					if (bool);
					this.mTrackingCenter = false;
					invalidate();
				}
				break;
		}
		label349: return true;
	}*/

	public void setColor(int color) {
        this.mCenterPaint.setColor(color);
        //左边色盘坐当前色到暗色
		this.mColor1 = color;
        this.mColor2 = 0xff000000/*-16777216*/;
        this.mColor3 = color;
        this.mColor4 = 0xFFFFFFFF/*-1*/;
    }
	
	public void setOnColorChangedListener(ColorPicker.OnColorChangedListener paramOnColorChangedListener)
	{
		this.mListener = paramOnColorChangedListener;
	}
}

/* Location:           C:\Documents and Settings\Administrator\桌面\反编译\APK反编译\ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.ColorPickerView
 * JD-Core Version:    0.6.0
 */
