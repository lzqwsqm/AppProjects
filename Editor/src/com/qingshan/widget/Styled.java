package com.qingshan.widget;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;

public class Styled
{
	//QSEditText.drawText调用
	public static float drawText(Canvas canvas, CharSequence text, int start, int end, int direction, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        if (direction >= 0) {
            direction = 1;
        } else {
            direction = -1;
        }
        return drawText(canvas, text, start, end, direction, false, x, top, y, bottom, paint, workPaint, needWidth);
    }

	static float drawText(Canvas canvas, CharSequence text, int start, int end, int dir, boolean runIsRtl, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        if ((dir != -1 || runIsRtl) && (!runIsRtl || dir != 1)) {
            return drawDirectionalRun(canvas, text, start, end, dir, runIsRtl, x, top, y, bottom, null, paint, workPaint, needWidth);
        }
        float ch = drawDirectionalRun(null, text, start, end, 1, false, 0.0f, 0, 0, 0, null, paint, workPaint, true) * ((float) dir);
        drawDirectionalRun(canvas, text, start, end, -dir, runIsRtl, x + ch, top, y, bottom, null, paint, workPaint, true);
        return ch;
    }
	private static float drawDirectionalRun(Canvas canvas, CharSequence text, int start, int end, int dir, boolean runIsRtl, float x, int top, int y, int bottom, FontMetricsInt fmi, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        if (text instanceof Spanned) {
            Class<?> division;
            float ox = x;
            int minAscent = 0;
            int maxDescent = 0;
            int minTop = 0;
            int maxBottom = 0;
            Spanned sp = (Spanned) text;
            if (canvas == null) {
                division = MetricAffectingSpan.class;
            } else {
                division = CharacterStyle.class;
            }
            int i = start;
            while (i < end) {
                int next = sp.nextSpanTransition(i, end, division);
                boolean z = needWidth || next != end;
                x += drawUniformRun(canvas, sp, i, next, dir, runIsRtl, x, top, y, bottom, fmi, paint, workPaint, z);
                if (fmi != null) {
                    if (fmi.ascent < minAscent) {
                        minAscent = fmi.ascent;
                    }
                    if (fmi.descent > maxDescent) {
                        maxDescent = fmi.descent;
                    }
                    if (fmi.top < minTop) {
                        minTop = fmi.top;
                    }
                    if (fmi.bottom > maxBottom) {
                        maxBottom = fmi.bottom;
                    }
                }
                i = next;
            }
            if (fmi != null) {
                if (start == end) {
                    paint.getFontMetricsInt(fmi);
                } else {
                    fmi.ascent = minAscent;
                    fmi.descent = maxDescent;
                    fmi.top = minTop;
                    fmi.bottom = maxBottom;
                }
            }
            return x - ox;
        }
        float ret = 0.0f;
        if (runIsRtl) {
            CharSequence tmp = TextUtils.getReverse(text, start, end);
            int tmpend = end - start;
            if (canvas != null || needWidth) {
                ret = paint.measureText(tmp, 0, tmpend);
            }
            if (canvas != null) {
                canvas.drawText(tmp, 0, tmpend, x - ret, (float) y, paint);
            }
        } else {
            if (needWidth) {
                ret = paint.measureText(text, start, end);
            }
            if (canvas != null) {
                canvas.drawText(text, start, end, x, (float) y, paint);
            }
        }
        if (fmi != null) {
            paint.getFontMetricsInt(fmi);
        }
        return ((float) dir) * ret;
    }

	

	private static float drawUniformRun(Canvas canvas, Spanned text, int start, int end, int dir, boolean runIsRtl, float x, int top, int y, int bottom, FontMetricsInt fmi, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        boolean haveWidth = false;
        float ret = 0.0f;
        CharacterStyle[] spans = (CharacterStyle[]) text.getSpans(start, end, CharacterStyle.class);
        ReplacementSpan replacement = null;
        paint.bgColor = 0;
        paint.baselineShift = 0;
        workPaint.set(paint);
        if (spans.length > 0) {
            for (CharacterStyle span : spans) {
                if (span instanceof ReplacementSpan) {
                    replacement = (ReplacementSpan) span;
                } else {
                    span.updateDrawState(workPaint);
                }
            }
        }
        if (replacement == null) {
            CharSequence tmp;
            int tmpstart;
            int tmpend;
            if (runIsRtl) {
                tmp = TextUtils.getReverse(text, start, end);
                tmpstart = 0;
                tmpend = end - start;
            } else {
                tmp = text;
                tmpstart = start;
                tmpend = end;
            }
            if (fmi != null) {
                workPaint.getFontMetricsInt(fmi);
            }
            if (canvas != null) {
                if (workPaint.bgColor != 0) {
                    int c = workPaint.getColor();
                    Style s = workPaint.getStyle();
                    workPaint.setColor(workPaint.bgColor);
                    workPaint.setStyle(Style.FILL);
                    if (null == null) {
                        ret = workPaint.measureText(tmp, tmpstart, tmpend);
                        haveWidth = true;
                    }
                    if (dir == -1) {
                        canvas.drawRect(x - ret, (float) top, x, (float) bottom, workPaint);
                    } else {
                        canvas.drawRect(x, (float) top, x + ret, (float) bottom, workPaint);
                    }
                    workPaint.setStyle(s);
                    workPaint.setColor(c);
                }
                if (dir == -1) {
                    if (!haveWidth) {
                        ret = workPaint.measureText(tmp, tmpstart, tmpend);
                    }
                    canvas.drawText(tmp, tmpstart, tmpend, x - ret, (float) (workPaint.baselineShift + y), workPaint);
                } else {
                    if (needWidth && !haveWidth) {
                        ret = workPaint.measureText(tmp, tmpstart, tmpend);
                    }
                    canvas.drawText(tmp, tmpstart, tmpend, x, (float) (workPaint.baselineShift + y), workPaint);
                }
            } else if (needWidth && null == null) {
                ret = workPaint.measureText(tmp, tmpstart, tmpend);
            }
        } else {
            ret = (float) replacement.getSize(workPaint, text, start, end, fmi);
            if (canvas != null) {
                if (dir == -1) {
                    replacement.draw(canvas, text, start, end, x - ret, top, y, bottom, workPaint);
                } else {
                    replacement.draw(canvas, text, start, end, x, top, y, bottom, workPaint);
                }
            }
        }
        if (dir == -1) {
            return -ret;
        }
        return ret;
    }

	public static int getTextWidths(TextPaint paint, TextPaint workPaint, Spanned text, int start, int end, float[] widths, FontMetricsInt fmi) {
        int i;
        MetricAffectingSpan[] spans = (MetricAffectingSpan[]) text.getSpans(start, end, MetricAffectingSpan.class);
        ReplacementSpan replacement = null;
        workPaint.set(paint);
        for (MetricAffectingSpan span : spans) {
            if (span instanceof ReplacementSpan) {
                replacement = (ReplacementSpan) span;
            } else {
                span.updateMeasureState(workPaint);
            }
        }
        if (replacement == null) {
            workPaint.getFontMetricsInt(fmi);
            return workPaint.getTextWidths(text, start, end, widths);
        }
        int wid = replacement.getSize(workPaint, text, start, end, fmi);
        if (end > start) {
            widths[0] = (float) wid;
            for (i = start + 1; i < end; i++) {
                widths[i - start] = 0.0f;
            }
        }
        return end - start;
    }

	public static float measureText(TextPaint paint, TextPaint workPaint, CharSequence text, int start, int end, FontMetricsInt fmi) {
        return drawDirectionalRun(null, text, start, end, 1, false, 0.0f, 0, 0, 0, fmi, paint, workPaint, true);
	}
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.widget.Styled
 * JD-Core Version:    0.6.0
 */
