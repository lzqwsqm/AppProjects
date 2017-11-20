package com.database;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.database.context.RuntimeInfo;
import com.database.model.Account;
import com.database.model.KM;
import com.database.model.Report;

class ChartCanvas extends View
{
    Context context;
    int function = 0;
    int height = 0;
    int width = 0;
    //构造方法
    public ChartCanvas(Context context, int width, int height, int function)
    {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;
        this.function = function;
        setBackgroundColor(Color.WHITE);//-1
    }
    //自定义view必须实现onDraw
    public void onDraw(Canvas canvas)
    {
        switch (this.function)
        {
            case 111:
                zcfzt(canvas);
                break;
            case 139:
            //  szdbt(paramCanvas);
                break;
            case 140:
            case 141:
           //   zcfxt(paramCanvas);
                break;
            case 142:
            //  zcbht(paramCanvas);
                break;
        }
    }
    void zcfzt(Canvas canvas)
    {
        String[] names = new String[6];
        long[] sums = new long[6];
    
        for (int i = 1; i <= 5; i++)
        {
            Account account = new Account(i);
            names[(i - 1)] = account.name;
            sums[(i - 1)] = account.sum;
        }
        names[5] = "投资";
        sums[5] = new Account(10).sum;
        drawPieForZcfzt(canvas, this.width, -20 + this.height, names, sums);
    }
  
    public static void drawPieForZcfzt(Canvas canvas, int width, int height, String[] names, long[] sums)
    {
    	//创建画笔
        Paint paint = new Paint();
        //设置画笔大小
        paint.setTextSize(UIAdapter.getTextSize());
        //
        int[] arrayOfInt2 = new int[sums.length];
        int[] arrayOfInt1 = new int[sums.length];
        //文字大小
        int textsize = 13 * UIAdapter.getTextSize() / 10;
    
        long l = 0L;
    
        for (int n = 0; n < sums.length; n++)
            l += sums[n];
    
        if ((sums.length != 0) && (l != 0L))
        {
            if (l > 0L)
            {
                for (int i1 = 0; i1 < sums.length; i1++)
                {
                    arrayOfInt2[i1] = (int)(3600L * sums[i1] / l);
                    int n;
                    if (arrayOfInt2[i1] % 10 < 5)
                        n = arrayOfInt2[i1] / 10;
                    else
                        n = 1 + arrayOfInt2[i1] / 10;
                    arrayOfInt2[i1] = n;
                }
                
                if (l > sums[4])
                    l -= sums[4];
                for (int i1 = 0; i1 < arrayOfInt1.length; i1++)
                {
                    arrayOfInt1[i1] = (int)(1000L * sums[i1] / l);
                    int n;
                    if (arrayOfInt1[i1] % 10 < 5)
                        n = arrayOfInt1[i1] / 10;
                    else
                        n = 1 + arrayOfInt1[i1] / 10;
                    arrayOfInt1[i1] = n;
                }
        
          
           }
           //宽
           int w = width / 2;
           //高
           int h = 1 + (-1 + arrayOfInt2.length) / 2;
           
           if (-10 + (height - textsize * h) < height / 2)
               h = height / 2 / 20;
           
           int m = 5;
           int k = width - 10;
           
           h = -10 + (height - textsize * h);
           
           int i2 = 0;
           if ((height - width <= w / 2) || (h <= k))
           {
               if ((width - height > w / 2) && (k > h))
               {
                   i2 = 1;
                   h = height - 5;
                   k = h;
                   m = (-50 + (width - k - w)) / 2;
                   if (m < 5)
                       m = 5;
               }
           }
           else
           {
        	//   k = height - (h - k);
               h = k;
           }
           //颜色
           int[] arrayOfInt3 = ChartView.color;
           
           if (i2 != 0)
               for (int n1 = 0; n1 < arrayOfInt2.length; n1++)
               {
        	       paint.setColor(arrayOfInt3[n1]);
        	       paint.setStyle(Paint.Style.FILL);
                   //画圆
        	       canvas.drawArc(new RectF(textsize + (k + (m + 5)), w * textsize + textsize * 6 / 13, textsize + (k + (m + (5 + textsize * 5 / 13))), w * textsize + textsize * 11 / 13), 0.0F, 360.0F, true, paint);
                   paint.setColor(-16777216);
                   canvas.drawText(names[n1] + "(" + arrayOfInt1[n1] + "%)", k + (m + (5 + textsize * 2)), textsize + w * textsize, paint);
               }
           //
           for ( i2 = 0; i2 < arrayOfInt2.length; i2++)
           {
    	       paint.setColor(arrayOfInt3[i2]);
    	       paint.setStyle(Paint.Style.FILL);
               canvas.drawArc(new RectF(10 + w * (i2 % 2), 5 + h + textsize * (1 + i2 / 2) - textsize * 7 / 13, 10 + textsize * 5 / 13 + w * (i2 % 2), 5 + h + textsize * (1 + i2 / 2) - textsize * 2 / 13), 0.0F, 360.0F, true, paint);
               paint.setColor(-16777216);
               canvas.drawText(names[i2] + "(" + arrayOfInt1[i2] + "%)", textsize + 5 + w * (i2 % 2), 5 + h + textsize * (1 + i2 / 2), paint);
           }
           drawPieChart(canvas, paint, 5, 5, k, h, arrayOfInt2);
        }
        else
        {
            int j = width - 10;
            textsize = -10 + (height - textsize * 2);
            paint.setColor(-7829368);
            paint.setStyle(Paint.Style.FILL);
            //画方框
            canvas.drawRect(new Rect(5, 5, j, textsize), paint);
            paint.setColor(-16777216);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(new Rect(5, 5, j, textsize), paint);
            paint.setColor(-65536);
            canvas.drawText("没有数据!", 5 + j / 2, 5 + textsize / 2, paint);
        }
    }
    static void drawPieChart(Canvas canvas, Paint paint, int left, int top, int right, int bottom, int[] paramArrayOfInt)
    {
        int l = left + right * 5 / 100;
   
        int r = right - right * 10 / 100;
        int b = bottom - bottom * 10 / 100;
        
        if (b > r * 2 / 3)
        {
            b = r * 2 / 3;
      
        }
        
        if (b + 20 > bottom)
            b = bottom - 20;
        int t = top + (20 + (bottom - b - 20) / 2);
        int[] arrayOfInt2 = ChartView.color;
        int[] arrayOfInt1 = ChartView.color1;
        paint.setColor(-3355444);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(left, top, left + right, top + bottom), paint);
        paint.setColor(-7829368);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(new Rect(left, top, left + right, top + bottom), paint);
        for (int m = 0; m < 20; m++)
        {
            int n = 90;
            for (int k = 0; k < paramArrayOfInt.length; k++)
            {
                if (m >= 19)
                	paint.setColor(arrayOfInt2[k]);
                else
                	paint.setColor(arrayOfInt1[k]);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawArc(new RectF(l, t - m, l + r, b + (t - m)), n, -paramArrayOfInt[k], true, paint);
  
                n -= paramArrayOfInt[k];
            }
        }
    }
}  
    
 /*   static void drawBarChart(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int[] arrayOfInt2 = ChartView.color;
      int[] arrayOfInt1 = ChartView.color1;
      int[] arrayOfInt3 = ChartView.color2;
      int k = 3 * (paramInt3 * 3 / 4 / 3);
      int j = k / 3;
      Path localPath = new Path();
      paramPaint.setStyle(Paint.Style.FILL);
      int i;
      if (paramInt4 < j * 2)
        i = paramInt2 - j - paramInt4 / 2;
      else
        i = paramInt2 - paramInt4;
      paramPaint.setColor(arrayOfInt2[0]);
      localPath.reset();
      localPath.moveTo(paramInt1, i + j);
      localPath.lineTo(paramInt1 + j, i);
      localPath.lineTo(paramInt1 + j * 2, i + j);
      localPath.close();
      paramCanvas.drawPath(localPath, paramPaint);
      paramPaint.setColor(arrayOfInt2[0]);
      localPath.reset();
      localPath.moveTo(paramInt1 + k, i);
      localPath.lineTo(paramInt1 + j, i);
      localPath.lineTo(paramInt1 + j * 2, i + j);
      localPath.close();
      paramCanvas.drawPath(localPath, paramPaint);
      if (paramInt4 < j * 2)
      {
        paramPaint.setColor(arrayOfInt1[0]);
        paramCanvas.drawRect(paramInt1, i + j, paramInt1 + j * 2, i + j + paramInt4 / 2, paramPaint);
        paramPaint.setColor(arrayOfInt3[0]);
        localPath.reset();
        localPath.moveTo(paramInt1 + k, i);
        localPath.lineTo(paramInt1 + j * 2, i + j);
        localPath.lineTo(paramInt1 + k, i + paramInt4 / 2);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt3[0]);
        localPath.reset();
        localPath.moveTo(paramInt1 + k, i + paramInt4 / 2);
        localPath.lineTo(paramInt1 + j * 2, i + j);
        localPath.lineTo(paramInt1 + j * 2, i + j + paramInt4 / 2);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
      }
      else
      {
        paramPaint.setColor(arrayOfInt1[0]);
        paramCanvas.drawRect(paramInt1, i + j, paramInt1 + j * 2, i + paramInt4, paramPaint);
        paramPaint.setColor(arrayOfInt3[0]);
        paramCanvas.drawRect(paramInt1 + j * 2, i + j, j + (paramInt1 + j * 2), paramInt4 + (i + j) - j * 2, paramPaint);
        paramPaint.setColor(arrayOfInt3[0]);
        localPath.reset();
        localPath.moveTo(paramInt1 + k, i);
        localPath.lineTo(paramInt1 + j * 2, i + j);
        localPath.lineTo(paramInt1 + k, i + j);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt3[0]);
        localPath.reset();
        localPath.moveTo(paramInt1 + k, i + paramInt4 - j);
        localPath.lineTo(paramInt1 + j * 2, i + paramInt4 - j);
        localPath.lineTo(paramInt1 + j * 2, i + paramInt4);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
      }
    }
 /* public static void drawBar(Context paramContext, Canvas paramCanvas, int paramInt1, int paramInt2, String[] paramArrayOfString, String paramString, long[] paramArrayOfLong)
  {
    Paint localPaint = new Paint();
    localPaint.setTextSize(UIAdapter.getTextSize());
    long l1 = 0L;
    Drawable localDrawable1 = paramArrayOfLong.length;
    for (int i = 0; ; i++)
    {
      int m = paramArrayOfLong.length;
      if (i >= m)
        break;
      if (paramArrayOfLong[i] <= l1)
        continue;
      l1 = paramArrayOfLong[i];
    }
    int i1 = 0;
    String str;
    if (l1 >= 100L)
    {
      str = "拾元";
      while (l1 >= 100L)
      {
        l1 /= 10L;
        i1++;
      }
      if (i1 != 2)
      {
        if (i1 != 3)
        {
          if (i1 != 4)
          {
            if (i1 != 5)
            {
              if (i1 == 6)
                str = "百万";
            }
            else
              str = "拾万";
          }
          else
            str = "万元";
        }
        else
          str = "千元";
      }
      else
        str = "百元";
    }
    else
    {
      str = "元";
    }
    long l2 = l1 + 1L;
    if (paramInt2 >= 211)
      j = 7;
    else
      j = 5;
    while (l2 % j != 0L)
      l2 += 1L;
    Drawable localDrawable2 = (int)l2 / j;
    Drawable localDrawable3 = j + 1;
    long l3 = localDrawable2 * localDrawable3;
    int k = paramInt1 - 20;
    int j = paramInt2 - 2 * UIAdapter.getTextSize() - 20;
    Drawable localDrawable4 = j / localDrawable3;
    int i2 = (paramInt1 - 35) / 6;
    localPaint.setColor(-52);
    localPaint.setStyle(Paint.Style.FILL);
    paramCanvas.drawRect(new Rect(35, 3, 35 + (k - 5), j + 20), localPaint);
    for (Object localObject1 = 0; ; localObject1++)
    {
      Object localObject2 = localDrawable3 + 1;
      if (localObject1 >= localObject2)
        break;
      localPaint.setColor(-13487566);
      paramCanvas.drawText(l3 - localObject1 * localDrawable2, 5, 5 + (20 + localObject1 * localDrawable4), localPaint);
      if (localObject1 == localDrawable3)
        continue;
      for (int i3 = 0; i3 < i2; i3++)
        paramCanvas.drawLine(35 + i3 * 6, 20 + localObject1 * localDrawable4, 3 + (35 + i3 * 6), 20 + localObject1 * localDrawable4, localPaint);
    }
    for (localDrawable2 = 0; localDrawable2 < i1; localDrawable2++)
      l3 *= 10L;
    i1 = k / localDrawable1;
    localDrawable2 = paramInt1 / 4;
    if (i1 > localDrawable2)
      i1 = paramInt1 / 4;
    localDrawable2 = 0;
    if (i1 < 5)
    {
      i1 = 5;
      localDrawable2 = localDrawable1 - k / i1;
    }
    localDrawable3 = i1 * 2 / 3;
    if (localDrawable3 < 5)
      localDrawable3 = i1 - 2;
    Drawable localDrawable6 = paramContext.getResources().getDrawable(2130837507);
    Drawable localDrawable5 = paramContext.getResources().getDrawable(2130837534);
    localObject1 = WelcomeActivity.drawableToBitmap(localDrawable6);
    Bitmap localBitmap = WelcomeActivity.drawableToBitmap(localDrawable5);
    for (localDrawable6 = localDrawable2; localDrawable6 < localDrawable1; localDrawable6++)
    {
      int i5 = (int)(paramArrayOfLong[localDrawable6] * j / l3);
      if ((localDrawable6 != localDrawable2) && (paramArrayOfLong[localDrawable6] > paramArrayOfLong[(localDrawable6 - 1)]))
      {
        int i4 = (int)(paramArrayOfLong[(localDrawable6 - 1)] * j / l3);
        paramCanvas.drawBitmap(localBitmap, new Rect(0, 0, localDrawable5.getIntrinsicWidth(), localDrawable5.getIntrinsicHeight()), new Rect(35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 / 2, 20 + j - i4, 35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 * 3 / 2, 20 + j), localPaint);
        paramCanvas.drawBitmap((Bitmap)localObject1, new Rect(0, 0, localDrawable5.getIntrinsicWidth(), localDrawable5.getIntrinsicHeight()), new Rect(35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 / 2, 20 + j - i5, 35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 * 3 / 2, 20 + j - i4), localPaint);
      }
      else
      {
        paramCanvas.drawBitmap(localBitmap, new Rect(0, 0, localDrawable5.getIntrinsicWidth(), localDrawable5.getIntrinsicHeight()), new Rect(35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 / 2, 20 + j - i5, 35 + i1 * (localDrawable6 - localDrawable2) + localDrawable3 * 3 / 2, 20 + j), localPaint);
      }
    }
    localPaint.setColor(-13421773);
    paramCanvas.drawLine(35, 0, 35, j + 20, localPaint);
    paramCanvas.drawLine(35, 20 + j, -3 + (35 + k), 20 + j, localPaint);
    int n = (-1 + (localDrawable1 - localDrawable2)) / 2;
    localPaint.setColor(-16777216);
    localPaint.setTextAlign(Paint.Align.RIGHT);
    if (n > 0)
    {
      paramCanvas.drawText(paramArrayOfString[n], 35 + i1 * (n - localDrawable2) + i1 / 2, 20 + j + 4 * UIAdapter.getTextSize() / 3, localPaint);
      paramCanvas.drawRect(-2 + (35 + i1 * (n - localDrawable2) + i1 / (4 + paramArrayOfLong.length)), 20 + j, 2 + (35 + i1 * (n - localDrawable2) + i1 / (4 + paramArrayOfLong.length)), 5 + (20 + j), localPaint);
    }
    localDrawable1 -= 1;
    paramCanvas.drawText(paramArrayOfString[localDrawable1], 35 + i1 * (localDrawable1 - localDrawable2) + i1 / 2, 20 + j + 4 * UIAdapter.getTextSize() / 3, localPaint);
    paramCanvas.drawRect(-2 + (35 + i1 * (localDrawable1 - localDrawable2) + i1 / (4 + paramArrayOfLong.length)), 20 + j, 2 + (35 + i1 * (localDrawable1 - localDrawable2) + i1 / (4 + paramArrayOfLong.length)), 5 + (20 + j), localPaint);
    localPaint.setTextAlign(Paint.Align.LEFT);
    float f = j + 20 + 4 * UIAdapter.getTextSize() / 3;
    paramCanvas.drawText(str, 2.0F, f, localPaint);
    paramCanvas.drawLine(35, 20 + j, -3 + (35 + k), 20 + j, localPaint);
  }

  

  public static void drawBarGroup(Context paramContext, Canvas paramCanvas, int paramInt1, int paramInt2, String[][] paramArrayOfString, long[][] paramArrayOfLong)
  {
    int[] arrayOfInt = ChartView.color;
    Paint localPaint = new Paint();
    localPaint.setTextSize(UIAdapter.getTextSize());
    long l1 = 0L;
    int i = paramArrayOfLong[0].length;
    for (int j = 0; ; j++)
    {
      int k = paramArrayOfLong.length;
      if (j >= k)
        break;
      for (int i4 = 0; ; i4++)
      {
        k = paramArrayOfLong[j].length;
        if (i4 >= k)
          break;
        if (paramArrayOfLong[j][i4] <= l1)
          continue;
        l1 = paramArrayOfLong[j][i4];
      }
    }
    int i5 = 0;
    String str;
    if (l1 >= 100L)
    {
      str = "拾元";
      while (l1 >= 100L)
      {
        l1 /= 10L;
        i5++;
      }
      if (i5 != 2)
      {
        if (i5 != 3)
        {
          if (i5 != 4)
          {
            if (i5 != 5)
            {
              if (i5 == 6)
                str = "百万";
            }
            else
              str = "拾万";
          }
          else
            str = "万元";
        }
        else
          str = "千元";
      }
      else
        str = "百元";
    }
    else
    {
      str = "元";
    }
    l1 += 1L;
    if (paramInt2 >= 211)
      j = 7;
    else
      j = 5;
    while (l1 % j != 0L)
      l1 += 1L;
    int i6 = (int)l1 / j;
    int i7 = j + 1;
    long l2 = i6 * i7;
    int i3 = paramInt1 - 35;
    j = i3 / i;
    if (j % (4 + paramArrayOfLong.length) == 0)
      j--;
    int i2 = paramInt1 / 5;
    if (j > i2)
      j = paramInt1 / 5;
    i2 = paramInt2 - 3 * UIAdapter.getTextSize() - 20;
    int i8 = i2 / i7;
    localPaint.setColor(-69);
    localPaint.setStyle(Paint.Style.FILL);
    paramCanvas.drawRect(new RectF(35, 3.0F, 35 + (i3 - 5), i2 + 20), localPaint);
    localPaint.setColor(-13487566);
    paramCanvas.drawLine(35, 0, 35, i2 + 20, localPaint);
    int i9 = (paramInt1 - 35) / 6;
    for (int i10 = 0; i10 < i7; i10++)
    {
      localPaint.setColor(-16777216);
      paramCanvas.drawText(l2 - i10 * i6, 10, 5 + (20 + i10 * i8), localPaint);
      localPaint.setColor(-13487566);
      for (int i11 = 0; i11 < i9; i11++)
        paramCanvas.drawLine(35 + i11 * 6, 20 + i10 * i8, 3 + (35 + i11 * 6), 20 + i10 * i8, localPaint);
    }
    for (i6 = 0; i6 < i5; i6++)
      l2 *= 10L;
    Drawable localDrawable2 = paramContext.getResources().getDrawable(2130837507);
    Drawable localDrawable1 = paramContext.getResources().getDrawable(2130837534);
    Bitmap localBitmap1 = WelcomeActivity.drawableToBitmap(localDrawable2);
    Bitmap localBitmap2 = WelcomeActivity.drawableToBitmap(localDrawable1);
    i6 = j * 2 / (1 + 2 * paramArrayOfLong.length);
    for (i5 = 0; i5 < i; i5++)
      for (i7 = 0; ; i7++)
      {
        int i12 = paramArrayOfLong.length;
        if (i7 >= i12)
          break;
        if (paramArrayOfLong[i7][i5] <= 0L)
          continue;
        i12 = (int)(paramArrayOfLong[i7][i5] * i2 / l2);
        if (i7 != 0)
          paramCanvas.drawBitmap(localBitmap1, new Rect(0, 0, localDrawable2.getIntrinsicWidth(), localDrawable2.getIntrinsicHeight()), new Rect(35 + j * (i5 - 0) + i6 * i7 + i6 / 2, 20 + i2 - i12, 35 + j * (i5 - 0) + i6 * (i7 + 1) + i6 / 2, 20 + i2), localPaint);
        else
          paramCanvas.drawBitmap(localBitmap2, new Rect(0, 0, localDrawable1.getIntrinsicWidth(), localDrawable1.getIntrinsicHeight()), new Rect(35 + j * (i5 - 0) + i6 * i7 + i6 / 2, 20 + i2 - i12, 35 + j * (i5 - 0) + i6 * (i7 + 1) + i6 / 2, 20 + i2), localPaint);
      }
    localPaint.setColor(-13421773);
    paramCanvas.drawLine(35, 20 + i2, -3 + (35 + i3), 20 + i2, localPaint);
    float f = i2 + 20 + 4 * UIAdapter.getTextSize() / 3;
    paramCanvas.drawText(str, 5.0F, f, localPaint);
    paramCanvas.drawText("0", 15, 3 + (i2 + 20), localPaint);
    paramCanvas.drawLine(35, 20 + i2, -3 + (35 + i3), 20 + i2, localPaint);
    i3 = (-1 + (i - 0)) / 2;
    localPaint.setColor(-16777216);
    localPaint.setTextAlign(Paint.Align.LEFT);
    int m = 6 + (int)localPaint.measureText(str);
    int n;
    if (i3 > 0)
    {
      m += 4;
      if (m < 35 + j * (i3 - 0) - localPaint.measureText(paramArrayOfString[1][i3]) / 2.0F)
        m = (int)(35 + j * (i3 - 0) - localPaint.measureText(paramArrayOfString[1][i3]) / 2.0F);
      paramCanvas.drawText(paramArrayOfString[1][i3], m, 20 + i2 + 4 * UIAdapter.getTextSize() / 3, localPaint);
      paramCanvas.drawRect(-2 + (35 + j * (i3 - 0) + j / (4 + paramArrayOfLong.length)), 20 + i2, 2 + (35 + j * (i3 - 0) + j / (4 + paramArrayOfLong.length)), 5 + (20 + i2), localPaint);
      m += (int)localPaint.measureText(paramArrayOfString[1][i3]);
    }
    i -= 1;
    n += 4;
    if (n < 35 + j * (i - 0) - localPaint.measureText(paramArrayOfString[1][i]) / 2.0F)
      n = (int)(35 + j * (i - 0) - localPaint.measureText(paramArrayOfString[1][i]) / 2.0F);
    if (n + localPaint.measureText(paramArrayOfString[1][i]) > paramInt1)
      n = (int)(paramInt1 - localPaint.measureText(paramArrayOfString[1][i]) - 4.0F);
    paramCanvas.drawText(paramArrayOfString[1][i], n, 20 + i2 + 4 * UIAdapter.getTextSize() / 3, localPaint);
    paramCanvas.drawRect(-2 + (35 + j * (i - 0) + j / (4 + paramArrayOfLong.length)), 20 + i2, 2 + (35 + j * (i - 0) + j / (4 + paramArrayOfLong.length)), 5 + (20 + i2), localPaint);
    localPaint.setTextAlign(Paint.Align.LEFT);
    i = (paramInt1 - 80 * paramArrayOfString[0].length) / 2;
    for (j = 0; ; j++)
    {
      int i1 = paramArrayOfString[0].length;
      if (j >= i1)
        break;
      localPaint.setColor(arrayOfInt[j]);
      localPaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawArc(new RectF(i + j * 80, paramInt2 - UIAdapter.getTextSize(), i + j * 80 + 2 * UIAdapter.getTextSize() / 3, paramInt2 - UIAdapter.getTextSize() / 3), 0.0F, 360.0F, true, localPaint);
      localPaint.setColor(-16777216);
      paramCanvas.drawText(paramArrayOfString[0][j], 20 + (i + j * 80), paramInt2 - UIAdapter.getTextSize() / 3, localPaint);
    }
  }

  static void drawBarGroupChart(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
  {
    int[] arrayOfInt1 = ChartView.color;
    int[] arrayOfInt3 = ChartView.color1;
    int[] arrayOfInt2 = ChartView.color2;
    int j = paramArrayOfInt.length;
    int i = (paramInt3 - 1) / (j + 4) * (j + 4) / (j + 4);
    Path localPath = new Path();
    paramPaint.setStyle(Paint.Style.FILL);
    for (int k = j - 1; k >= 0; k--)
    {
      if (paramArrayOfInt[k] == 0)
        continue;
      int m;
      if (paramArrayOfInt[k] < i * (j + 5))
        m = paramInt2 - i * (j + 3) - 2 * paramArrayOfInt[k] / 5;
      else
        m = paramInt2 - paramArrayOfInt[k];
      paramPaint.setColor(arrayOfInt3[k]);
      localPath.reset();
      localPath.moveTo(paramInt1 + 2 * (i * k), m + i * (1 + (j - k)));
      localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
      localPath.lineTo(paramInt1 + i * (2 + k * 2), m + i * (j - k));
      localPath.close();
      paramCanvas.drawPath(localPath, paramPaint);
      paramPaint.setColor(arrayOfInt3[k]);
      localPath.reset();
      localPath.moveTo(paramInt1 + i * (3 + k * 2), m + i * (2 + (j - k)));
      localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
      localPath.lineTo(paramInt1 + i * (2 + k * 2), m + i * (j - k));
      localPath.close();
      paramCanvas.drawPath(localPath, paramPaint);
      if (paramArrayOfInt[k] >= i * (j + 5))
      {
        paramPaint.setColor(arrayOfInt2[k]);
        paramCanvas.drawRect(paramInt1 + i * (k * 2), m + i * (3 + (j - k)), i + (paramInt1 + i * (k * 2)), m + i * (3 + (j - k)) + paramArrayOfInt[k] - i * (j + 5), paramPaint);
        paramPaint.setColor(arrayOfInt2[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (k * 2), m + i * (1 + (j - k)));
        localPath.lineTo(paramInt1 + i * (k * 2), m + i * (3 + (j - k)));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt2[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (k * 2), paramInt2 - i * (k + 2));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), paramInt2 - i * (k + 2));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), paramInt2 - i * k);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt1[k]);
        paramCanvas.drawRect(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)), paramInt1 + i * (1 + k * 2) + i * 2, m + i * (3 + (j - k)) + paramArrayOfInt[k] - i * (j + 4), paramPaint);
        paramPaint.setColor(arrayOfInt1[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (3 + k * 2), m + i * (2 + (j - k)));
        localPath.lineTo(paramInt1 + i * (3 + k * 2), m + i * (3 + (j - k)));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt1[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (3 + k * 2), paramInt2 - i * (k + 1));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), paramInt2 - i * (k + 1));
        localPath.lineTo(paramInt1 + i * (1 + k * 2), paramInt2 - i * k);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
      }
      else
      {
        paramPaint.setColor(arrayOfInt2[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (k * 2), m + i * (1 + (j - k)));
        localPath.lineTo(paramInt1 + i * (k * 2), m + i * (1 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt2[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.lineTo(paramInt1 + i * (k * 2), m + i * (1 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt1[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (3 + k * 2), m + i * (2 + (j - k)));
        localPath.lineTo(paramInt1 + i * (3 + k * 2), m + i * (2 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
        paramPaint.setColor(arrayOfInt1[k]);
        localPath.reset();
        localPath.moveTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.lineTo(paramInt1 + i * (3 + k * 2), m + i * (2 + (j - k)) + 2 * paramArrayOfInt[k] / 5);
        localPath.lineTo(paramInt1 + i * (1 + k * 2), m + i * (3 + (j - k)));
        localPath.close();
        paramCanvas.drawPath(localPath, paramPaint);
      }
    }
  }

  
  
  public void drawPie(Canvas paramCanvas, int paramInt1, int paramInt2, String[] paramArrayOfString, long[] paramArrayOfLong)
  {
    int i = UIAdapter.getTextSize();
    Paint localPaint = new Paint();
    localPaint.setTextSize(UIAdapter.getTextSize());
    long l = 0L;
    for (int j = 0; j < paramArrayOfLong.length; j++)
      l += paramArrayOfLong[j];
    if ((paramArrayOfLong.length != 0) && (l != 0L))
    {
      int[] arrayOfInt2 = new int[paramArrayOfLong.length];
      int[] arrayOfInt1 = new int[paramArrayOfLong.length];
      if (l > 0L)
      {
        for (i1 = 0; i1 < paramArrayOfLong.length; i1++)
        {
          arrayOfInt2[i1] = (int)(3600L * paramArrayOfLong[i1] / l);
          if (arrayOfInt2[i1] % 10 < 5)
            n = arrayOfInt2[i1] / 10;
          else
            n = 1 + arrayOfInt2[i1] / 10;
          arrayOfInt2[i1] = n;
        }
        for (n = 0; n < arrayOfInt1.length; n++)
        {
          arrayOfInt1[n] = (int)(1000L * paramArrayOfLong[n] / l);
          if (arrayOfInt1[n] % 10 < 5)
            i1 = arrayOfInt1[n] / 10;
          else
            i1 = 1 + arrayOfInt1[n] / 10;
          arrayOfInt1[n] = i1;
        }
        l = 0L;
        for (n = 0; n < arrayOfInt2.length; n++)
          l += arrayOfInt2[n];
        n = -1 + arrayOfInt2.length;
        while (n >= 0)
        {
          if (arrayOfInt2[n] <= 0)
          {
            n--;
            continue;
          }
          arrayOfInt2[n] = (int)(arrayOfInt2[n] + (360L - l));
        }
        l = 0L;
        for (n = 0; n < arrayOfInt1.length; n++)
          l += arrayOfInt1[n];
        n = -1 + arrayOfInt1.length;
        while (n >= 0)
        {
          if (arrayOfInt1[n] <= 0)
          {
            n--;
            continue;
          }
          arrayOfInt1[n] = (int)(arrayOfInt1[n] + (100L - l));
        }
      }
      int[] arrayOfInt3 = ChartView.color;
      int i2 = paramInt1 / 150;
      if ((i2 != 1) || (paramInt1 <= 150))
      {
        if (i2 <= 1)
          i2 = 1;
      }
      else
      {
        i2 = 2;
        (paramInt1 / 2);
      }
      int i1 = paramInt1 / i2;
      int i3 = 3;
      if (-10 + (paramInt2 - i * i3) < paramInt2 / 2)
        i3 = paramInt2 / 2 / 20;
      int n = 5;
      int m = paramInt1 - 10;
      i3 = -10 + (paramInt2 - i * i3);
      int i4 = 0;
      if ((paramInt2 - paramInt1 <= i1 / 2) || (i3 <= m))
      {
        if ((paramInt1 - paramInt2 > i1 / 2) && (m > i3))
        {
          i4 = 1;
          i3 = paramInt2 - 5;
          m = i3;
          n = (-50 + (paramInt1 - m - i1)) / 2;
          if (n < 5)
            n = 5;
        }
      }
      else
      {
        (paramInt2 - (i3 - m));
        i3 = m;
      }
      if (i4 != 0)
        for (i1 = 0; i1 < arrayOfInt2.length; i1++)
        {
          localPaint.setColor(arrayOfInt3[i1]);
          localPaint.setStyle(Paint.Style.FILL);
          paramCanvas.drawArc(new RectF(i + (m + (n + 5)), i1 * i + i * 6 / 13, i + (m + (n + (5 + i * 5 / 13))), i1 * i + i * 11 / 13), 0.0F, 360.0F, true, localPaint);
          localPaint.setColor(-16777216);
          paramCanvas.drawText(paramArrayOfString[i1] + "(" + arrayOfInt1[i1] + "%)", m + (n + (5 + i * 2)), i + i1 * i, localPaint);
        }
      for (i4 = 0; i4 < arrayOfInt2.length; i4++)
      {
        localPaint.setColor(arrayOfInt3[i4]);
        localPaint.setStyle(Paint.Style.FILL);
        paramCanvas.drawArc(new RectF(10 + i1 * (i4 % i2), 5 + i3 + i * (1 + i4 / i2) - i * 7 / 13, 10 + i * 5 / 13 + i1 * (i4 % i2), 5 + i3 + i * (1 + i4 / i2) - i * 2 / 13), 0.0F, 360.0F, true, localPaint);
        localPaint.setColor(-16777216);
        paramCanvas.drawText(paramArrayOfString[i4] + "(" + arrayOfInt1[i4] + "%)", i + 5 + i1 * (i4 % i2), 5 + i3 + i * (1 + i4 / i2), localPaint);
      }
      drawPieChart(paramCanvas, localPaint, n, 5, m, i3, arrayOfInt2);
    }
    else
    {
      int k = paramInt1 - 10;
      i = -10 + (paramInt2 - i * 2);
      localPaint.setColor(-7829368);
      localPaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawRect(new Rect(5, 5, k, i), localPaint);
      localPaint.setColor(-16777216);
      localPaint.setStyle(Paint.Style.STROKE);
      paramCanvas.drawRect(new Rect(5, 5, k, i), localPaint);
      localPaint.setColor(-65536);
      paramCanvas.drawText("没有数据!", 5 + k / 2, 5 + i / 2, localPaint);
    }
  }

  public int getFunction()
  {
    return this.function;
  }

  
  public void setFunction(int paramInt)
  {
    this.function = paramInt;
  }

  void szdbt(Canvas paramCanvas)
  {
    Cursor localCursor = Report.getRows("type=2", "date desc");
    long[][] arrayOfLong = new long[2][];
    int i = this.width / 30;
    if (i > localCursor.getCount())
      i = localCursor.getCount();
    arrayOfLong[0] = new long[i];
    arrayOfLong[1] = new long[i];
    String[][] arrayOfString; = new String[2][];
    arrayOfString;[0] = new String[2];
    arrayOfString;[1] = new String[i];
    arrayOfString;[0][0] = "收入";
    arrayOfString;[0][1] = "支出";
    Report localReport = new Report();
    localCursor.moveToFirst();
    i -= 1;
    while ((!localCursor.isAfterLast()) && (i >= 0))
    {
      localReport.reset(localCursor);
      arrayOfLong[0][i] = (localReport.getKmSum(1) / 100L);
      arrayOfLong[1][i] = (localReport.getKmSum(2) / 100L);
      arrayOfString;[1][i] = localReport.getDateString();
      localCursor.moveToNext();
      i--;
    }
    localCursor.close();
    drawBarGroup(this.context, paramCanvas, this.width, this.height, arrayOfString;, arrayOfLong);
  }

  void zcbht(Canvas paramCanvas)
  {
    Cursor localCursor = Report.getRows("type=2", "date desc");
    long[][] arrayOfLong = new long[2][];
    int i = this.width / 36;
    if (i > localCursor.getCount())
      i = localCursor.getCount();
    arrayOfLong[0] = new long[i];
    arrayOfLong[1] = new long[i];
    String[][] arrayOfString; = new String[2][];
    arrayOfString;[0] = new String[2];
    arrayOfString;[1] = new String[i];
    arrayOfString;[0][0] = "总资产";
    arrayOfString;[0][1] = "净资产";
    Object localObject = new Report();
    localCursor.moveToFirst();
    for (int j = i - 1; (!localCursor.isAfterLast()) && (j >= 0); j--)
    {
      ((Report)localObject).reset(localCursor);
      arrayOfLong[0][j] = (((Report)localObject).getAccountSum(1) + ((Report)localObject).getAccountSum(2) + ((Report)localObject).getAccountSum(3) + ((Report)localObject).getAccountSum(4) + ((Report)localObject).getAccountSum(10) + ((Report)localObject).getAccountSum(7));
      arrayOfLong[1][j] = (arrayOfLong[0][j] - ((Report)localObject).getAccountSum(5));
      [J local[J = arrayOfLong[0];
      local[J[j] /= 100L;
      local[J = arrayOfLong[1];
      local[J[j] /= 100L;
      arrayOfString;[1][j] = ((Report)localObject).getDateString();
      localCursor.moveToNext();
    }
    arrayOfLong[0][(i - 1)] = (Account.getAccountSum(1) + Account.getAccountSum(2) + Account.getAccountSum(3) + Account.getAccountSum(4) + Account.getAccountSum(10) + Account.getAccountSum(7));
    arrayOfLong[1][(i - 1)] = (arrayOfLong[0][(i - 1)] - Account.getAccountSum(5));
    localObject = arrayOfLong[0];
    j = i - 1;
    localObject[j] /= 100L;
    localObject = arrayOfLong[1];
    i -= 1;
    localObject[i] /= 100L;
    localCursor.close();
    drawBar(this.context, paramCanvas, this.width, this.height, arrayOfString;[1], "收支对比图", arrayOfLong[0]);
  }

  void zcfxt(Canvas paramCanvas)
  {
    String[] arrayOfString = new String[16];
    long[] arrayOfLong = new long[16];
    int i = 0;
    int k = Integer.parseInt(RuntimeInfo.sql);
    if (k % 13 != 0)
      localObject2 = new Report(2, k);
    else
      localObject2 = new Report(1, k);
    if (this.function != 140)
    {
      if (this.function == 141)
        RuntimeInfo.main.setTitle(((Report)localObject2).getDateString() + "收入分析");
    }
    else
      RuntimeInfo.main.setTitle(((Report)localObject2).getDateString() + "支出分析");
    KM localKM = new KM();
    Object localObject1 = null;
    if (this.function != 140)
    {
      if (this.function == 141)
        localObject1 = KM.getRows("flag=1 and pid=1", "");
    }
    else
      localObject1 = KM.getRows("flag=1 and pid=2", "");
    ((Cursor)localObject1).moveToFirst();
    while (!((Cursor)localObject1).isAfterLast())
    {
      localKM.reset((Cursor)localObject1);
      int n = localKM.id;
      long l = ((Report)localObject2).getKmSum(n);
      if (l > 0L)
      {
        arrayOfString[i] = localKM.name;
        int i3 = i + 1;
        arrayOfLong[i] = l;
        i = i3;
      }
      ((Cursor)localObject1).moveToNext();
    }
    ((Cursor)localObject1).close();
    ((long[])null);
    int m = 0;
    ((String[])null);
    int i2;
    if (i <= 6)
    {
      localObject2 = new long[i];
      localObject1 = new String[i];
      for (i1 = 0; i1 < i; i1++)
      {
        for (i2 = 0; i2 < i; i2++)
        {
          if (arrayOfLong[i2] <= localObject2[i1])
            continue;
          localObject2[i1] = arrayOfLong[i2];
          m = i2;
        }
        arrayOfLong[m] = 0L;
        localObject1[i1] = arrayOfString[m];
      }
    }
    Object localObject2 = new long[6];
    localObject1 = new String[6];
    for (int i1 = 0; i1 < 5; i1++)
    {
      for (i2 = 0; i2 < i; i2++)
      {
        if (arrayOfLong[i2] <= localObject2[i1])
          continue;
        localObject2[i1] = arrayOfLong[i2];
        m = i2;
      }
      arrayOfLong[m] = 0L;
      localObject1[i1] = arrayOfString[m];
    }
    for (int j = 0; j < i; j++)
      localObject2[5] += arrayOfLong[j];
    localObject1[5] = "其他";
    drawPie(paramCanvas, this.width, -20 + this.height, localObject1, localObject2);
  }

  void zcfzt(Canvas paramCanvas)
  {
    String[] arrayOfString = new String[6];
    long[] arrayOfLong = new long[6];
    for (int i = 1; i <= 5; i++)
    {
      Account localAccount = new Account(i);
      arrayOfString[(i - 1)] = localAccount.name;
      arrayOfLong[(i - 1)] = localAccount.sum;
    }
    arrayOfString[5] = "投资";
    arrayOfLong[5] = new Account(10).sum;
    drawPieForZcfzt(paramCanvas, this.width, -20 + this.height, arrayOfString, arrayOfLong);
  }
  */
//}

/* Location:           D:\备份文件\手机备份\可用文件\反编译\APK反编译\ApkDecompiler\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.view.ChartCanvas
 * JD-Core Version:    0.6.0
 */
