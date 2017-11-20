package com.qingshan.highlight;


import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import com.qingshan.colorschemes.ColorScheme;
import com.qingshan.editor.QSEditor;
import com.qingshan.util.FileUtil;
import com.qingshan.widget.ForegroundColorSpan;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import com.qingshan.editor.Searcher;

public class Highlight
{
	public static final int GROUP_ATTR_NAME_ID = 6;
	public static final int GROUP_COMMENT_ID = 2;
	public static final int GROUP_FUNCTION_ID = 5;
	public static final int GROUP_KEYWORD_ID = 4;
	public static final int GROUP_STRING_ID = 3;
	public static final int GROUP_TAG_ID = 1;
	private static final String TAG = "Highlight";
	private static int color_attr_name;
	private static int color_comment;
	private static int color_function;
	private static int color_keyword;
	private static int color_string;
	private static int color_tag;
	private static HashMap<String, String[]> langTab;
	private static boolean mEnabled;
	private static int mLimitFileSize;
	private static ArrayList<ForegroundColorSpan> mSpans;
	private static ArrayList<String[]> nameTab;
	private int mEndOffset = -1;
	private String mExt = "";
	private int mStartOffset = -1;
	private boolean mStop = true;
	private static Searcher mSearcher;
     
	static
	{
		//System.loadLibrary("highlight");
		mEnabled = true;
		mLimitFileSize = 0;
		mSpans = new ArrayList();
	}
	//初始化
	//QSEditText.init()调用
	public Highlight() {
        this.mExt = "";
        this.mEndOffset = -1;
        this.mStartOffset = -1;
        this.mStop = true;
    }
	//初始化
	//QSEditor.initEnv()调用
	public static void init()
	{
		loadLang();
		loadColorScheme();
	}
	public static String getNameByExt(String ext) {
        if (langTab == null) {
            loadLang();
            return "";
        }
        String[] info = (String[]) langTab.get(ext);
        if (info == null) {
            return "";
        }
        return info[0];
    }
	//QSEditor.init_highlight()调用
	public static void setEnabled(boolean paramBoolean)
	{
		mEnabled = paramBoolean;
	}
	//QSEditor.init_highlight()调用
	public static void setLimitFileSize(int paramInt)
	{
		mLimitFileSize = paramInt;
	}
	public static ArrayList<String[]> getLangList()
	{
		return nameTab;
	}
	//QSEditText.setPath调用
	public static int getLimitFileSize()
	{
		return mLimitFileSize;
	}

	
    //高亮着实现核心代码
    //QSEditText.drawView调用
	public boolean render(Editable mText, int startOffset, int endOffset) {
        if (!mEnabled || langTab == null) {
            return false;
        }
		//QSEditText.onTouchEvent调用redraw()控制
        if (this.mStop || "".equals(this.mExt)) {
            return false;
        }
        /*if (this.mStartOffset <= startOffset && this.mEndOffset >= endOffset) {
		 return false;
		 }*/
		//由菜单高亮着色控制
        String[] lang = (String[]) langTab.get(this.mExt);
        if (lang == null) {
            return false;
        }
        this.mStop = true;
        this.mStartOffset = startOffset;
        this.mEndOffset = endOffset;
        //截取字符串 关键代码
		int[] ret = jni_parse(mText.subSequence(0, endOffset).toString(), QSEditor.TEMP_PATH + File.separator + lang[GROUP_TAG_ID]);
        //int[] ret = new int[]{3,0,5};
		if (ret == null) {
            this.mStop = false;
            return false;
        }
        int len = ret.length;
        if (len < GROUP_TAG_ID || ((float) len) % 3.0f != 0.0f) {
            this.mStop = false;
            return false;
        }
        int index = 0;
        int bufLen = mSpans.size();
        Iterator it = mSpans.iterator();
        while (it.hasNext()) {
            mText.removeSpan((ForegroundColorSpan) it.next());
        }
        int i = 0;
        while (i < len) {
            int color;
            switch (ret[i]) {
                case GROUP_TAG_ID: //1
                    color = color_tag;
                    break;
                case GROUP_COMMENT_ID: //2
                    color = color_comment;
                    break;
                case GROUP_STRING_ID: //3
                    color = color_string;
                    break;
                case GROUP_KEYWORD_ID: //4
                    color = color_keyword;
                    break;
                case GROUP_FUNCTION_ID: //5
                    color = color_function;
                    break;
                case GROUP_ATTR_NAME_ID: //6
                    color = color_attr_name;
                    break;
                default:
                    Log.d(TAG, "\u83b7\u53d6\u989c\u8272group id\u5931\u8d25");
                    this.mStop = false;
                    return false;
            }
            i += GROUP_TAG_ID;
            int start = ret[i];
            i += GROUP_TAG_ID;
            int end = ret[i];
            if (end >= startOffset) {
                ForegroundColorSpan fcs;
                if (index >= bufLen) {
                    fcs = new ForegroundColorSpan(color);
                    mSpans.add(fcs);
                } else {
                    fcs = (ForegroundColorSpan) mSpans.get(index);
                    fcs.setColor(color);
                }
                index += GROUP_TAG_ID;
                try {
                    mText.setSpan(fcs, start, end, 33);
                } catch (Exception e) {
                }
            }
            i += GROUP_TAG_ID;
        }
        ret = null;
        this.mStop = false;
        return false;
    }

	//private static native int[] jni_parse(String paramString1, String paramString2);
	private static  int[] jni_parse(String str, String str2){
		if(str==null){
			return null;
		}
		//String st ="import";
		//st.indexOf(str,0);
		//mSearcher.setText(st);
		//int[] a1=mSearcher.find(str,0);
		
		
		
		
		
		int[] ai=new int[3];
		ai[0]=3;
		ai[1]=0;//a1[0];
		ai[2]=6;//a1[1];
		return ai;
	}
	
	/*
	 int __fastcall Java_com_jecelyin_highlight_Highlight_jni_1parse(int a1, int a2, int a3, int a4)
	 {
	 int v4; // r6@1
	 int v5; // r4@1
	 int v6; // r8@1
	 const char *v7; // r10@1
	 const char *v8; // r0@2
	 int v9; // r5@2
	 _DWORD *v10; // r9@3
	 int v12; // [sp+8h] [bp-28h]@3
	 char v13; // [sp+Fh] [bp-21h]@1

	 v13 = 0;
	 v4 = a3;
	 v5 = a1;
	 v6 = a4;
	 v7 = (const char *)(*(int (**)(void))(*(_DWORD *)a1 + 676))();
	 if ( !v7 )
	 goto LABEL_12;
	 v8 = (const char *)(*(int (__fastcall **)(int, int, char *))(*(_DWORD *)v5 + 676))(v5, v6, &v13);
	 v9 = (int)v8;
	 if ( !v8 )
	 {
	 (*(void (__fastcall **)(int, int, const char *))(*(_DWORD *)v5 + 680))(v5, v4, v7);
	 return v9;
	 }
	 v12 = 0;
	 v10 = read_syntax(v8, v7, &v12);
	 (*(void (__fastcall **)(int, int, const char *))(*(_DWORD *)v5 + 680))(v5, v4, v7);
	 (*(void (__fastcall **)(int, int, int))(*(_DWORD *)v5 + 680))(v5, v6, v9);
	 if ( v10 && v12 > 0 )
	 {
	 v9 = (*(int (__fastcall **)(int))(*(_DWORD *)v5 + 716))(v5);
	 if ( v9 )
	 {
	 (*(void (__fastcall **)(int, int, _DWORD, int))(*(_DWORD *)v5 + 844))(v5, v9, 0, v12);
	 free(v10);
	 }
	 }
	 else
	 {
	 LABEL_12:
	 v9 = 0;
	 }
	 return v9;
	 }
	 
	 void free(void *ptr)
	 {
	 free(ptr);
	 }
	 void free(void *ptr)
	 {
	 if ( ptr )
	 free(ptr);
	 }

	 read_syntax(const char *a1, const char *a2, _DWORD *a3)
	 {
	 const char *v3; // r4@1
	 const char *v4; // r5@1
	 _DWORD *v5; // r8@1
	 size_t v6; // r0@2
	 size_t v7; // r10@2
	 size_t v8; // r7@2
	 _BYTE *v9; // r11@2
	 int v10; // r3@3
	 _DWORD *v11; // r8@3
	 int v12; // r9@4
	 int v13; // r5@4
	 int v14; // r3@5
	 int v15; // r4@5
	 int v16; // r2@5
	 int v17; // r0@7
	 int v18; // r7@8
	 int v19; // r9@9
	 int v20; // r3@9
	 _DWORD *v21; // r5@11
	 int v22; // r7@11
	 int v23; // r3@12
	 int v24; // r6@12
	 int v25; // r4@12
	 signed int v26; // r3@14
	 int v27; // r1@15
	 signed int v28; // r4@15
	 int v29; // r5@15
	 int v30; // r2@16
	 int v31; // r3@16
	 int v32; // r3@17
	 _DWORD *v34; // [sp+Ch] [bp-2Ch]@0

	 v3 = a1;
	 v4 = a2;
	 v5 = a3;
	 if ( *a1 )
	 {
	 v6 = strlen(a2);
	 v7 = v6 + 1;
	 v8 = v6;
	 v9 = malloc(v6 + 1);
	 memset(v9, 0, v7);
	 memcpy(v9, v4, v8);
	 syntax_init();
	 load_syntax_conf(v3);
	 parse_file(v9);
	 if ( dword_7AB4 <= 0 )
	 {
	 printf("current_state.ga_len < 1");
	 }
	 else
	 {
	 v10 = 3 * dword_7AB4;
	 *v5 = 3 * dword_7AB4;
	 v11 = malloc(4 * v10);
	 v34 = v11;
	 if ( dword_7AB4 > 0 )
	 {
	 v12 = 0;
	 v13 = 0;
	 do
	 {
	 v14 = dword_7AC8 - 1;
	 v15 = dword_7AC4 + 16 * v13;
	 v16 = dword_7AD8 + 16 * (dword_7AC8 - 1);
	 while ( v14 >= 0 )
	 {
	 v17 = *(_DWORD *)(v16 + 8);
	 v16 -= 16;
	 --v14;
	 if ( v17 == *(_DWORD *)(v15 + 4) )
	 {
	 v18 = *(_DWORD *)(v16 + 28);
	 if ( v18 )
	 {
	 printf("%i -> ", *(_DWORD *)(v16 + 28));
	 echo((int)v9, *(_DWORD *)(v15 + 8), *(_DWORD *)(v15 + 12));
	 v11[v12] = v18;
	 v19 = v12 + 1;
	 v20 = v19 + 1;
	 v11[v19] = *(_DWORD *)(v15 + 8);
	 v12 = v19 + 2;
	 v11[v20] = *(_DWORD *)(v15 + 12);
	 goto LABEL_10;
	 }
	 break;
	 }
	 }
	 printf("to_id == 0");
	 LABEL_10:
	 ++v13;
	 }
	 while ( dword_7AB4 > v13 );
	 }
	 }
	 free(v9);
	 v21 = curbuf;
	 sub_35B0((int)(curbuf + 3));
	 sub_35B0((int)(v21 + 41));
	 v22 = v21[79];
	 if ( v22 - 1 >= 0 )
	 {
	 v23 = v21[83];
	 v24 = 0;
	 v25 = 32 * (v22 - 1);
	 do
	 {
	 free(*(void **)(v23 + v25));
	 ++v24;
	 free(*(void **)(v21[83] + v25 + 4));
	 free(*(void **)(v21[83] + v25 + 8));
	 free(*(void **)(v21[83] + v25 + 12));
	 free(*(void **)(v21[83] + v25 + 16));
	 free(*(void **)(v21[83] + v25 + 24));
	 free(*(void **)(v21[83] + v25 + 20));
	 v23 = v21[83];
	 *(_DWORD *)(v23 + v25 + 28) = 0;
	 v25 -= 32;
	 }
	 while ( v24 != v22 );
	 }
	 ga_clear((int)(v21 + 79));
	 free(v21);
	 ga_clear((int)&dword_7AB4);
	 v26 = dword_7AC8;
	 if ( dword_7AC8 > 0 )
	 {
	 v27 = dword_7AD8;
	 v28 = 1;
	 *(_DWORD *)(dword_7AD8 + 12) = 0;
	 *(_DWORD *)(v27 + 8) = 0;
	 v29 = 16;
	 if ( v26 > 1 )
	 {
	 do
	 {
	 v30 = dword_7AD8;
	 v31 = dword_7AD8 + v29;
	 *(_DWORD *)(v31 + 12) = 0;
	 *(_DWORD *)(v31 + 8) = 0;
	 if ( v28 > 6 )
	 {
	 free(*(void **)(v30 + 16 * v28));
	 free(*(void **)(dword_7AD8 + v29 + 4));
	 v32 = dword_7AC8;
	 }
	 else
	 {
	 v32 = dword_7AC8;
	 }
	 v29 = 16 * ++v28;
	 }
	 while ( v28 < v32 );
	 }
	 }
	 ga_clear((int)&dword_7AC8);
	 }
	 else
	 {
	 v34 = (_DWORD *)*a1;
	 printf("buffer is null");
	 }
	 return v34;
	 }
	 
	 int syntax_init()
	 {
	 _DWORD *v0; // r0@1

	 ga_clear((int)&dword_7AB4);
	 v0 = malloc(0x150u);
	 *v0 = 0;
	 curbuf = v0;
	 v0[1] = 0;
	 hash_init(v0 + 3);
	 hash_init((_DWORD *)(curbuf + 164));
	 ga_init(curbuf + 316);
	 sub_2334("ignore");
	 dword_7AC8 = 0;
	 sub_29CC("Tag");
	 dword_7AC8 = 1;
	 sub_29CC("Comment");
	 dword_7AC8 = 2;
	 sub_29CC("String");
	 dword_7AC8 = 3;
	 sub_29CC("Keyword");
	 dword_7AC8 = 4;
	 sub_29CC("Function");
	 dword_7AC8 = 5;
	 return sub_29CC("AttrName");
	 }
	 int __fastcall sub_29CC(_BYTE *a1)
	 {
	 int result; // r0@1
	 int v2; // r3@2

	 result = sub_2694(a1);
	 if ( result > 0 )
	 {
	 v2 = dword_7AD8 + 16 * (result - 1);
	 *(_DWORD *)(v2 + 12) = result;
	 *(_DWORD *)(v2 + 8) = result;
	 }
	 return result;
	 }

	 void __fastcall load_syntax_conf(const char *a1)
	 {
	 const char *v1; // r6@1
	 int *v2; // r5@1
	 const char *v3; // r0@2
	 const char *v4; // r8@2
	 const char *v5; // r9@2
	 int v6; // r3@3
	 bool v7; // zf@4
	 bool v8; // zf@7
	 int v9; // t1@11
	 const char *v10; // r0@14
	 char *v11; // r7@14
	 char *v12; // r4@14
	 int v13; // r10@14
	 int v14; // r11@14
	 char *v15; // r0@22
	 const char *v16; // r7@22
	 const char *v17; // r0@22
	 size_t v18; // r10@22
	 const char *v19; // r4@22
	 size_t v20; // r3@22
	 int v21; // t1@24
	 size_t v22; // r11@26
	 int v23; // ST08_4@26
	 void *v24; // r7@26
	 const char *v25; // r0@29
	 _BYTE *v26; // r10@33
	 int v27; // r2@33
	 void *v28; // r0@33
	 int v29; // r7@33
	 int v30; // r3@34
	 bool v31; // zf@34
	 int v32; // r3@38
	 int v33; // t1@38
	 bool v34; // zf@38
	 signed int v35; // r10@42
	 int v36; // r10@44
	 char *v37; // r7@48
	 size_t v38; // r0@49
	 char *v39; // r0@49
	 const char *v40; // r9@50
	 char *v41; // r10@50
	 int *v42; // r8@50
	 const char *v43; // r5@50
	 char *v44; // r4@50
	 int i; // r3@53
	 bool v46; // zf@54
	 bool v47; // zf@57
	 int v48; // r2@66
	 int v49; // r1@66
	 char *v50; // r7@70
	 int v51; // r3@70
	 int v52; // r4@72
	 int v53; // r3@74
	 bool v54; // zf@74
	 int v55; // t1@78
	 bool v56; // zf@78
	 const char *v57; // r11@81
	 bool v58; // zf@84
	 int v59; // t1@87
	 bool v60; // zf@88
	 char *v61; // r0@92
	 const char *v62; // r10@92
	 int v63; // r11@94
	 _BYTE *v64; // r0@105
	 _BYTE *v65; // r0@106
	 signed int v66; // r7@109
	 char *v67; // r11@112
	 char *v68; // r10@113
	 int v69; // r4@113
	 int v70; // r9@113
	 size_t v71; // r0@114
	 void *v72; // r0@114
	 const char *v73; // r6@114
	 char **v74; // r7@114
	 int v75; // r8@114
	 int v76; // r8@115
	 int v77; // r0@117
	 int v78; // r7@117
	 int v79; // r0@117
	 int v80; // r3@117
	 size_t v81; // r0@119
	 const char *v82; // r8@124
	 int v83; // lr@131
	 int v84; // r1@131
	 int v85; // r2@131
	 int v86; // r3@131
	 int v87; // r10@131
	 int v88; // r12@131
	 int v89; // r1@131
	 int v90; // r2@131
	 int v91; // r3@131
	 int v92; // r1@139
	 int v93; // r7@143
	 int v94; // r10@146
	 int v95; // lr@147
	 int v96; // r11@147
	 int v97; // r1@147
	 int v98; // r2@147
	 int v99; // r3@147
	 int v100; // r7@147
	 int v101; // r12@147
	 int v102; // r12@147
	 int v103; // r1@147
	 int v104; // r2@147
	 int v105; // r3@147
	 size_t n; // [sp+8h] [bp-68h]@14
	 const char *src; // [sp+Ch] [bp-64h]@22
	 char *srca; // [sp+Ch] [bp-64h]@70
	 char *srcb; // [sp+Ch] [bp-64h]@112
	 char *v110; // [sp+14h] [bp-5Ch]@49
	 char *v111; // [sp+14h] [bp-5Ch]@130
	 const char *v112; // [sp+18h] [bp-58h]@72
	 const char *v113; // [sp+1Ch] [bp-54h]@50
	 const char *v114; // [sp+1Ch] [bp-54h]@113
	 char *s; // [sp+24h] [bp-4Ch]@49
	 char *v116; // [sp+2Ch] [bp-44h]@113
	 char *v117; // [sp+30h] [bp-40h]@113
	 const char *v118; // [sp+34h] [bp-3Ch]@113
	 int v119; // [sp+38h] [bp-38h]@33
	 char *v120; // [sp+3Ch] [bp-34h]@29
	 char *s1; // [sp+40h] [bp-30h]@14
	 void *ptr; // [sp+44h] [bp-2Ch]@1

	 v1 = a1;
	 v2 = &GLOBAL_OFFSET_TABLE_;
	 if ( (signed int)read_file(a1, &ptr) > 0 )
	 {
	 v3 = (const char *)ptr;
	 v4 = "syn";
	 v5 = "HiLink";
	 LABEL_3:
	 v6 = *v3;
	 if ( !*v3 )
	 goto LABEL_20;
	 while ( 1 )
	 {
	 while ( 1 )
	 {
	 v7 = v6 == 32;
	 if ( v6 != 32 )
	 v7 = v6 == 9;
	 if ( !v7 )
	 {
	 v8 = v6 == 10;
	 if ( v6 != 10 )
	 v8 = v6 == 13;
	 if ( !v8 )
	 break;
	 }
	 v9 = (v3++)[1];
	 v6 = v9;
	 }
	 if ( !v6 )
	 goto LABEL_20;
	 if ( v6 == 35 )
	 goto LABEL_19;
	 v10 = get_word(v3, &s1);
	 v11 = s1;
	 v12 = (char *)v10;
	 v13 = strcmp(s1, v4) == 0;
	 v14 = strcmp(v11, v5) == 0;
	 n = strcmp(v11, "include") == 0;
	 free(v11);
	 if ( !(v14 | v13 | n) )
	 {
	 v3 = goto_next_line(v12);
	 goto LABEL_3;
	 }
	 if ( !v13 )
	 {
	 if ( v14 )
	 {
	 v3 = highlight_link(v12);
	 }
	 else
	 {
	 if ( n )
	 {
	 v15 = skipwhite(v12);
	 v16 = v15;
	 v17 = skiptowhite(v15);
	 v18 = v17 - v16;
	 v19 = v17;
	 src = strnsave(v16, v17 - v16);
	 v20 = (size_t)&v1[strlen(v1) - 1];
	 while ( (unsigned int)v1 < v20 )
	 {
	 v21 = *(_BYTE *)(v20-- - 1);
	 if ( v21 == 47 )
	 {
	 ++v20;
	 break;
	 }
	 }
	 v22 = v20 - (_DWORD)v1;
	 v23 = v18 + 1 + v20 - (_DWORD)v1;
	 v24 = malloc(v23);
	 memset(v24, 0, v23);
	 strncpy((char *)v24, v1, v22);
	 strncpy((char *)v24 + v22, src, v18);
	 free((void *)src);
	 load_syntax_conf(v24);
	 goto LABEL_27;
	 }
	 v3 = v12;
	 }
	 goto LABEL_19;
	 }
	 v25 = get_word(v12, &v120);
	 v24 = v120;
	 v19 = v25;
	 if ( !strcmp(v120, "case") )
	 {
	 sub_2334(v19);
	 v24 = v120;
	 goto LABEL_27;
	 }
	 if ( !strcmp((const char *)v24, "keyword") )
	 {
	 v37 = sub_22EC(v19, &v119);
	 if ( v37 )
	 {
	 v110 = (char *)sub_27C4(v19, v119 - (_DWORD)v19, 1);
	 v38 = strlen(v37);
	 v39 = (char *)malloc(v38 + 1);
	 s = v39;
	 if ( v39 )
	 {
	 v113 = v5;
	 v40 = v4;
	 v41 = v39;
	 v42 = v2;
	 v43 = v19;
	 v44 = 0;
	 while ( !ends_excmd((unsigned __int8)*v37) && !ends_excmd((unsigned __int8)*v37) )
	 {
	 for ( i = (unsigned __int8)*v37; *v37; i = (unsigned __int8)*v37 )
	 {
	 v46 = i == 9;
	 if ( i != 9 )
	 v46 = i == 32;
	 if ( v46 )
	 break;
	 v47 = i == 13;
	 if ( i != 13 )
	 v47 = i == 10;
	 if ( v47 )
	 break;
	 if ( i == 92 )
	 {
	 v48 = (unsigned __int8)v37[1];
	 v49 = (int)(v37++ + 1);
	 if ( v48 )
	 v37 = (char *)(v49 + 1);
	 else
	 LOBYTE(v48) = 92;
	 *v41++ = v48;
	 }
	 else
	 {
	 ++v37;
	 *v41++ = i;
	 }
	 }
	 *v41 = 0;
	 v37 = skipwhite(v37);
	 ++v44;
	 if ( !v37 )
	 break;
	 ++v41;
	 }
	 srcb = v44;
	 v67 = v44;
	 v19 = v43;
	 v2 = v42;
	 v4 = v40;
	 v5 = v113;
	 if ( !v67 )
	 goto LABEL_128;
	 v116 = (char *)v19;
	 v118 = v113;
	 v68 = s;
	 v69 = 0;
	 v117 = (char *)v1;
	 v114 = v4;
	 v70 = v2[39];
	 while ( 1 )
	 {
	 if ( *(_DWORD *)(*(_DWORD *)v70 + 8) )
	 v82 = str_tolower(v68);
	 else
	 v82 = v68;
	 v71 = strlen(v82);
	 v72 = malloc(v71 + 8);
	 v73 = (char *)v72 + 4;
	 v74 = (char **)v72;
	 strcpy((char *)v72 + 4, v82);
	 v75 = *(_DWORD *)v2[39];
	 *v74 = v110;
	 if ( *(_DWORD *)(v75 + 8) )
	 v76 = v75 + 164;
	 else
	 v76 = v75 + 12;
	 v77 = hash_hash((int)v73);
	 v78 = v77;
	 v79 = hash_lookup(v76, v73, v77);
	 v80 = *(_DWORD *)(v79 + 4);
	 if ( v80 && v80 != v2[36] )
	 {
	 *(_DWORD *)(v79 + 4) = v73;
	 v81 = strlen(v68);
	 if ( (char *)++v69 == srcb )
	 {
	 LABEL_127:
	 v19 = v116;
	 v1 = v117;
	 v4 = v114;
	 v5 = v118;
	 LABEL_128:
	 free(s);
	 v24 = v120;
	 goto LABEL_27;
	 }
	 }
	 else
	 {
	 hash_add_item(v76, v79, (int)v73, v78);
	 v81 = strlen(v68);
	 if ( (char *)++v69 == srcb )
	 goto LABEL_127;
	 }
	 v68 += v81 + 1;
	 }
	 }
	 }
	 LABEL_111:
	 v24 = v120;
	 goto LABEL_27;
	 }
	 if ( strcmp((const char *)v24, "region") )
	 {
	 if ( strcmp((const char *)v24, "match") )
	 goto LABEL_27;
	 v119 = 0;
	 v26 = sub_22EC(v19, &v119);
	 v27 = *(_DWORD *)v2[39];
	 *(_DWORD *)(v27 + 328) = 10;
	 *(_DWORD *)(v27 + 324) = 32;
	 v28 = malloc(0x20u);
	 v29 = (int)v28;
	 if ( v28 )
	 {
	 memset(v28, 0, 0x20u);
	 v30 = *v26;
	 v31 = v30 == 32;
	 if ( v30 != 32 )
	 v31 = v30 == 9;
	 if ( v31 )
	 {
	 do
	 {
	 v33 = (v26++)[1];
	 v32 = v33;
	 v34 = v33 == 32;
	 if ( v33 != 32 )
	 v34 = v32 == 9;
	 }
	 while ( v34 );
	 }
	 if ( sub_247C((int)v26, v29, 6) )
	 {
	 if ( *(_DWORD *)(v29 + 20) )
	 {
	 v36 = v2[39];
	 if ( ga_grow(*(_DWORD *)v36 + 316, 1) && (v111 = (char *)sub_27C4(v19, v119 - (_DWORD)v19, 1)) != 0 )
	 {
	 v83 = *(_DWORD *)v36;
	 v84 = *(_DWORD *)(v29 + 4);
	 v85 = *(_DWORD *)(v29 + 8);
	 v86 = *(_DWORD *)(v29 + 12);
	 v87 = 32 * *(_DWORD *)(*(_DWORD *)v36 + 316);
	 v88 = *(_DWORD *)(v83 + 332) + v87;
	 *(_DWORD *)v88 = *(_DWORD *)v29;
	 *(_DWORD *)(v88 + 4) = v84;
	 *(_DWORD *)(v88 + 8) = v85;
	 *(_DWORD *)(v88 + 12) = v86;
	 v88 += 16;
	 v89 = *(_DWORD *)(v29 + 20);
	 v90 = *(_DWORD *)(v29 + 24);
	 v91 = *(_DWORD *)(v29 + 28);
	 *(_DWORD *)v88 = *(_DWORD *)(v29 + 16);
	 *(_DWORD *)(v88 + 4) = v89;
	 *(_DWORD *)(v88 + 8) = v90;
	 *(_DWORD *)(v88 + 12) = v91;
	 *(_DWORD *)(*(_DWORD *)(v83 + 332) + v87 + 28) = v111;
	 ++*(_DWORD *)(v83 + 316);
	 free((void *)v29);
	 v24 = v120;
	 }
	 else
	 {
	 free((void *)v29);
	 v24 = v120;
	 }
	 goto LABEL_27;
	 }
	 v35 = 1;
	 }
	 else
	 {
	 v35 = 0;
	 }
	 free((void *)v29);
	 if ( v35 )
	 {
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 1742,
	 "E399: Not enough arguments: syntax region",
	 v19);
	 v24 = v120;
	 goto LABEL_27;
	 }
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 1744,
	 "e_invarg2: %s",
	 v19);
	 }
	 goto LABEL_111;
	 }
	 v119 = 0;
	 v50 = sub_22EC(v19, &v119);
	 v51 = *(_DWORD *)v2[39];
	 *(_DWORD *)(v51 + 328) = 10;
	 *(_DWORD *)(v51 + 324) = 32;
	 srca = (char *)malloc(0x20u);
	 if ( !srca )
	 goto LABEL_111;
	 memset(srca, 0, 0x20u);
	 if ( !v50 )
	 {
	 LABEL_109:
	 v66 = 0;
	 LABEL_137:
	 free(srca);
	 if ( v66 )
	 {
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 1656,
	 "E399: Not enough arguments: syntax region",
	 v19);
	 v24 = v120;
	 }
	 else
	 {
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 1658,
	 "e_invarg2: %s",
	 v19);
	 v24 = v120;
	 }
	 goto LABEL_27;
	 }
	 v112 = v19;
	 v52 = 0;
	 while ( 1 )
	 {
	 if ( ends_excmd((unsigned __int8)*v50) )
	 {
	 v92 = v52;
	 v19 = v112;
	 v57 = v50;
	 goto LABEL_140;
	 }
	 v53 = (unsigned __int8)*v50;
	 v54 = v53 == 32;
	 if ( v53 != 32 )
	 v54 = v53 == 9;
	 if ( v54 )
	 {
	 do
	 {
	 v55 = (unsigned __int8)(v50++)[1];
	 v53 = v55;
	 v56 = v55 == 32;
	 if ( v55 != 32 )
	 v56 = v53 == 9;
	 }
	 while ( v56 );
	 }
	 v57 = v50;
	 if ( v53 && v53 != 9 )
	 {
	 do
	 {
	 if ( v53 == 61 )
	 break;
	 v58 = v53 == 13;
	 if ( v53 != 13 )
	 v58 = v53 == 10;
	 if ( v58 )
	 break;
	 v59 = (unsigned __int8)(v50++)[1];
	 v53 = v59;
	 if ( !v59 )
	 break;
	 v60 = v53 == 9;
	 if ( v53 != 9 )
	 v60 = v53 == 32;
	 }
	 while ( !v60 );
	 }
	 v61 = strnsave_up(v57, v50 - v57);
	 v62 = v61;
	 if ( !v61 )
	 {
	 LABEL_108:
	 v19 = v112;
	 goto LABEL_109;
	 }
	 if ( !strcmp(v61, "TAG") )
	 {
	 v63 = 3;
	 goto LABEL_105;
	 }
	 if ( !strcmp(v62, "START") )
	 {
	 v63 = 0;
	 goto LABEL_105;
	 }
	 if ( !strcmp(v62, "END") )
	 {
	 v63 = 2;
	 goto LABEL_105;
	 }
	 if ( !strcmp(v62, "END2") )
	 {
	 v63 = 4;
	 goto LABEL_105;
	 }
	 if ( !strcmp(v62, "SKIP") )
	 {
	 v63 = 1;
	 goto LABEL_105;
	 }
	 if ( strcmp(v62, "HG") )
	 break;
	 v63 = 5;
	 LABEL_105:
	 free((void *)v62);
	 v64 = skipwhite(v50);
	 if ( *v64 != 61 )
	 {
	 v19 = v112;
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 1597,
	 "E398: Missing '=': %s",
	 v112);
	 goto LABEL_109;
	 }
	 v65 = skipwhite(v64 + 1);
	 if ( !*v65 )
	 {
	 v19 = v112;
	 LABEL_136:
	 v66 = 1;
	 goto LABEL_137;
	 }
	 v50 = sub_247C((int)v65, (int)srca, v63);
	 ++v52;
	 if ( !v50 )
	 goto LABEL_108;
	 }
	 v92 = v52;
	 v19 = v112;
	 LABEL_140:
	 if ( !v57 )
	 goto LABEL_109;
	 if ( !*(_DWORD *)srca || !*((_DWORD *)srca + 2) )
	 goto LABEL_136;
	 v93 = v2[39];
	 if ( ga_grow(*(_DWORD *)v93 + 316, v92) && (v94 = sub_27C4(v19, v119 - (_DWORD)v19, 1)) != 0 )
	 {
	 v95 = *(_DWORD *)v93;
	 v96 = *(_DWORD *)(*(_DWORD *)v93 + 332);
	 v97 = *((_DWORD *)srca + 1);
	 v98 = *((_DWORD *)srca + 2);
	 v99 = *((_DWORD *)srca + 3);
	 v100 = 32 * *(_DWORD *)(*(_DWORD *)v93 + 316);
	 v101 = v96 + v100;
	 *(_DWORD *)v101 = *(_DWORD *)srca;
	 *(_DWORD *)(v101 + 4) = v97;
	 *(_DWORD *)(v101 + 8) = v98;
	 *(_DWORD *)(v101 + 12) = v99;
	 v102 = v96 + v100 + 16;
	 v103 = *((_DWORD *)srca + 5);
	 v104 = *((_DWORD *)srca + 6);
	 v105 = *((_DWORD *)srca + 7);
	 *(_DWORD *)v102 = *((_DWORD *)srca + 4);
	 *(_DWORD *)(v102 + 4) = v103;
	 *(_DWORD *)(v102 + 8) = v104;
	 *(_DWORD *)(v102 + 12) = v105;
	 *(_DWORD *)(*(_DWORD *)(v95 + 332) + v100 + 28) = v94;
	 ++*(_DWORD *)(v95 + 316);
	 free(srca);
	 v24 = v120;
	 }
	 else
	 {
	 free(srca);
	 v24 = v120;
	 }
	 LABEL_27:
	 free(v24);
	 v3 = v19;
	 LABEL_19:
	 v3 = goto_next_line(v3);
	 v6 = *v3;
	 if ( !*v3 )
	 {
	 LABEL_20:
	 free(ptr);
	 return;
	 }
	 }
	 }
	 printf(
	 "\n== EMSG2 ===================\n%s(%d):\n## %s:\n%s",
	 "jni/highlight/src/syntax.c",
	 309,
	 "can't read syntax config file",
	 v1);
	 }

	 void __fastcall parse_file(_BYTE *a1)
	 {
	 _BYTE *v1; // r4@1
	 const char *v2; // r5@1
	 _DWORD *v3; // r12@1
	 int v4; // r6@1
	 int v5; // r7@2
	 int v6; // r12@2
	 int v7; // r10@2
	 int v8; // r8@2
	 int v9; // r4@2
	 char *v10; // lr@4
	 int v11; // r2@4
	 char *v12; // lr@4
	 int v13; // r3@4
	 int v14; // r1@5
	 int v15; // r0@5
	 bool v16; // zf@5
	 char *v17; // r9@13
	 int *v18; // r7@15
	 int v19; // r4@15
	 int *v20; // r6@16
	 int *v21; // t1@16
	 int v22; // r1@16
	 int v23; // r1@21
	 int v24; // r2@21
	 const char *v25; // r7@23
	 int v26; // r4@24
	 const char *i; // r0@25
	 int *v28; // r0@26
	 int v29; // r2@26
	 int v30; // t1@26
	 int *v31; // r1@33
	 int v32; // r0@39
	 size_t n; // [sp+10h] [bp-A0h]@23
	 int v34; // [sp+14h] [bp-9Ch]@21
	 int v35; // [sp+18h] [bp-98h]@21
	 int v36; // [sp+34h] [bp-7Ch]@2
	 int v37; // [sp+38h] [bp-78h]@2
	 int v38; // [sp+3Ch] [bp-74h]@2
	 int v39; // [sp+40h] [bp-70h]@2
	 int v40; // [sp+44h] [bp-6Ch]@2
	 int v41; // [sp+48h] [bp-68h]@2
	 int v42; // [sp+4Ch] [bp-64h]@2
	 int v43; // [sp+50h] [bp-60h]@2
	 int v44; // [sp+54h] [bp-5Ch]@2
	 int v45; // [sp+58h] [bp-58h]@2
	 int v46; // [sp+5Ch] [bp-54h]@2
	 int v47; // [sp+60h] [bp-50h]@2
	 int v48; // [sp+64h] [bp-4Ch]@2
	 int v49; // [sp+68h] [bp-48h]@2
	 int v50; // [sp+6Ch] [bp-44h]@2
	 int v51; // [sp+70h] [bp-40h]@2
	 int v52; // [sp+74h] [bp-3Ch]@2
	 int v53; // [sp+78h] [bp-38h]@2
	 int v54; // [sp+7Ch] [bp-34h]@2
	 int v55; // [sp+80h] [bp-30h]@2
	 int *v56; // [sp+84h] [bp-2Ch]@16
	 char v57; // [sp+88h] [bp-28h]@4

	 v1 = a1;
	 v2 = skipwhite(a1);
	 v3 = curbuf;
	 v4 = curbuf[79];
	 *v3 = &v2[*v3 - (_DWORD)v1];
	 dword_7ABC = 16;
	 dword_7AC0 = 3;
	 if ( v4 <= 0 )
	 goto LABEL_51;
	 v47 = 0;
	 v37 = 0;
	 v48 = 0;
	 v38 = 0;
	 v49 = 0;
	 v39 = 0;
	 v50 = 0;
	 v40 = 0;
	 v51 = 0;
	 v41 = 0;
	 v5 = dword_7AC8;
	 v52 = 0;
	 v42 = 0;
	 v53 = 0;
	 v43 = 0;
	 v6 = v3[83];
	 v54 = 0;
	 v44 = 0;
	 v7 = 0;
	 v55 = 0;
	 v45 = 0;
	 v8 = dword_7AD8 + 16 * (dword_7AC8 - 1);
	 v46 = 0;
	 v36 = 0;
	 v9 = 0;
	 do
	 {
	 if ( *(_DWORD *)(v6 + 16) )
	 {
	 v10 = &v57 + 4 * v7;
	 *((_DWORD *)v10 - 11) = v6;
	 v11 = v8;
	 v12 = v10 - 84;
	 v13 = v5;
	 while ( --v13 >= 0 )
	 {
	 v14 = *(_DWORD *)(v11 + 8);
	 v15 = *(_DWORD *)(v6 + 28);
	 v16 = v15 == v14;
	 if ( v15 == v14 )
	 v14 = *(_DWORD *)(v11 + 12);
	 v11 -= 16;
	 if ( v16 )
	 *(_DWORD *)v12 = v14;
	 }
	 ++v7;
	 }
	 ++v9;
	 v6 += 32;
	 }
	 while ( v9 != v4 );
	 if ( !v7 )
	 {
	 LABEL_51:
	 scan_syntax(v2, 0);
	 return;
	 }
	 v17 = (char *)malloc(0xFFu);
	 if ( !v17 )
	 {
	 printf("can't malloc split match memory!");
	 return;
	 }
	 LABEL_14:
	 if ( !*v2 )
	 goto LABEL_19;
	 while ( 2 )
	 {
	 v18 = &v45;
	 v19 = 0u;
	 do
	 {
	 v21 = (int *)v18[1];
	 ++v18;
	 v20 = v21;
	 v22 = *v21;
	 v56 = 0;
	 if ( sub_3A04((int)v2, v22, v17, 0, (int)&v56) == 1 )
	 {
	 v2 = (char *)v56 + (_DWORD)v2;
	 v23 = *curbuf;
	 v24 = (int)v56 + *curbuf;
	 *curbuf = v24;
	 v35 = v23;
	 v34 = *((_DWORD *)&v57 + v19 - 21);
	 if ( v34 == 1 )
	 sub_2620(1, v23, v24);
	 v25 = (const char *)v20[2];
	 n = strlen((const char *)v20[2]);
	 if ( !strcmp((const char *)v20[4], "other") )
	 {
	 v26 = *v2;
	 if ( *v2 )
	 {
	 for ( i = v25; strncmp(i, v2, n); i = (const char *)v20[2] )
	 {
	 v28 = (int *)utfc_ptr2len(v2);
	 v56 = v28;
	 v29 = *curbuf + 1;
	 *curbuf = v29;
	 v30 = *((_BYTE *)v28 + (_DWORD)v2);
	 v2 = (char *)v28 + (_DWORD)v2;
	 v26 = v30;
	 if ( !v30 )
	 goto LABEL_33;
	 }
	 if ( v26 )
	 {
	 if ( v34 != 1 )
	 goto LABEL_31;
	 LABEL_42:
	 sub_2620(v34, *curbuf, *curbuf + n);
	 goto LABEL_32;
	 }
	 v28 = &GLOBAL_OFFSET_TABLE_;
	 v29 = *curbuf;
	 }
	 else
	 {
	 v28 = (int *)156;
	 v29 = *curbuf;
	 }
	 LABEL_33:
	 v31 = &dword_7AB4;
	 if ( dword_7AB4 <= 0 )
	 v31 = (int *)-1;
	 else
	 v28 = &dword_7AB4;
	 if ( dword_7AB4 > 0 )
	 v31 = *(int **)(v28[4] + 16 * dword_7AB4 - 4);
	 if ( (int *)(v29 - 1) != v31 )
	 {
	 sub_2620(v34, v35, v29);
	 goto LABEL_14;
	 }
	 v32 = strncmp((const char *)v20[2], v2, n);
	 }
	 else
	 {
	 v2 = scan_syntax(v2, v25);
	 v32 = strncmp((const char *)v20[2], v2, n);
	 }
	 if ( !v32 )
	 {
	 if ( v34 == 1 )
	 goto LABEL_42;
	 LABEL_31:
	 sub_2620(v34, v35, *curbuf + n);
	 LABEL_32:
	 v2 += n;
	 *curbuf += n;
	 goto LABEL_14;
	 }
	 goto LABEL_14;
	 }
	 ++v19;
	 }
	 while ( v19 < v7 );
	 v56 = (int *)utfc_ptr2len(v2);
	 v2 = (char *)v56 + (_DWORD)v2;
	 ++*curbuf;
	 if ( *v2 )
	 continue;
	 break;
	 }
	 LABEL_19:
	 free(v17);
	 }

	 int __fastcall ga_clear(int a1)
	 {
	 int v1; // r4@1

	 v1 = a1;
	 free(*(void **)(a1 + 16));
	 return ga_init(v1);
	 }
	 int __fastcall ga_init(int result)
	 {
	 *(_DWORD *)result = 0;
	 *(_DWORD *)(result + 16) = 0;
	 *(_DWORD *)(result + 4) = 0;
	 return result;
	 }

	*/
	//TabHost.setEditTextPref调用
	public static void loadColorScheme()
	{
		color_tag = Color.parseColor(ColorScheme.color_tag);
		color_string = Color.parseColor(ColorScheme.color_string);
		color_keyword = Color.parseColor(ColorScheme.color_keyword);
		color_function = Color.parseColor(ColorScheme.color_function);
		color_comment = Color.parseColor(ColorScheme.color_comment);
		color_attr_name = Color.parseColor(ColorScheme.color_attr_name);
	}

	public static boolean loadLang() {
        String langfile = QSEditor.TEMP_PATH + "/lang.conf";
        if (!new File(langfile).isFile()) {
            return false;
        }
        langTab = new HashMap();
        nameTab = new ArrayList();
        try {
            String[] lines = new String(readFile(langfile), "utf-8").split("\n");
            int length = lines.length;
            for (int i = 0; i < length; i += GROUP_TAG_ID) {
                String line = lines[i].trim();
                if (!line.startsWith("#")) {
                    String[] cols = line.split(":");
                    String name = cols[0].trim();
                    String synfile = cols[GROUP_TAG_ID].trim();
                    String[] exts = cols[GROUP_COMMENT_ID].trim().split("\\s+");
                    ArrayList arrayList = nameTab;
                    Object[] obj = new String[GROUP_COMMENT_ID];
                    obj[0] = name;
                    obj[GROUP_TAG_ID] = exts[0];
                    arrayList.add(obj);
                    int length2 = exts.length;
                    for (int i2 = 0; i2 < length2; i2 += GROUP_TAG_ID) {
                        String ext = exts[i2];
                        HashMap hashMap = langTab;
                        Object[] obj2 = new String[GROUP_COMMENT_ID];
                        obj2[0] = name;
                        obj2[GROUP_TAG_ID] = synfile;
                        hashMap.put(ext, obj2);
                    }
                }
            }
            byte[] mByte = null;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

	public static String readFile(String file, String encoding) {
        try {
            return new String(readFile(file), encoding);
        } catch (Exception e) {
            try {
                return FileUtil.ReadFile(file, encoding);
            } catch (Exception e2) {
                return "";
            }
        }
    }

	public static byte[] readFile(String file)
	{
		//return read_file(paramString);
		return readFile(new File(file));
	}
	public static byte[] readFile(File filename) {
        try {
            
			return readFile(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
            return null;
        }
    }
	public static byte[] readFile(InputStream fis){
	
		try {
		int length = 0;
		length = fis.available();
		byte[] bs = new byte[length];
		fis.read(bs);
		return bs;
		} catch (IOException e) {
            return null;
        }
		
	}
	//private static native byte[] read_file(String paramString);

	/*
	 int __fastcall Java_com_jecelyin_highlight_Highlight_read_1file(int a1, int a2, int a3)
	 {
	 int v3; // r7@1
	 int v4; // r4@1
	 const char *v5; // r0@1
	 int v6; // r5@1
	 _BYTE *v7; // r6@2
	 int v9; // r0@5
	 int v10; // [sp+8h] [bp-20h]@2

	 v3 = a3;
	 v4 = a1;
	 v5 = (const char *)(*(int (**)(void))(*(_DWORD *)a1 + 676))();
	 v6 = (int)v5;
	 if ( v5 )
	 {
	 v10 = -1;
	 v7 = read_file3(v5, (__off_t *)&v10);
	 (*(void (__fastcall **)(int, int, int))(*(_DWORD *)v4 + 680))(v4, v3, v6);
	 if ( v10 == -1 )
	 {
	 v9 = (*(int (__fastcall **)(int, const char *))(*(_DWORD *)v4 + 24))(v4, "java/lang/Exception");
	 (*(void (__fastcall **)(int, int, const char *))(*(_DWORD *)v4 + 56))(v4, v9, "error: Can't read file.");
	 v6 = 0;
	 }
	 else
	 {
	 v6 = (*(int (__fastcall **)(int))(*(_DWORD *)v4 + 704))(v4);
	 (*(void (__fastcall **)(int, int, _DWORD, int))(*(_DWORD *)v4 + 832))(v4, v6, 0, v10);
	 free(v7);
	 }
	 }
	 return v6;
	 }

	 _BYTE *__fastcall read_file3(const char *a1, __off_t *a2)
	 {
	 __off_t *v2; // r7@1
	 char *v3; // r0@1
	 _BYTE *v4; // r6@2
	 _BYTE *result; // r0@3
	 int v6; // r0@5
	 int v7; // r8@5
	 __off_t v8; // r10@6
	 int v9; // [sp+4h] [bp-1024h]@1
	 int v10; // [sp+1004h] [bp-24h]@1

	 v2 = a2;
	 v10 = _stack_chk_guard;
	 v3 = realpath(a1, (char *)&v9);
	 if ( v3 && (v6 = open(v3, 0), v7 = v6, v6 >= 0) )
	 {
	 v8 = lseek(v6, 0, 2);
	 if ( v8 < 0 || lseek(v7, 0, 0) || (v4 = malloc(v8 + 2)) == 0 )
	 {
	 close(v7);
	 v4 = 0;
	 }
	 else if ( v8 == read(v7, v4, v8) )
	 {
	 close(v7);
	 v4[v8] = 10;
	 v4[v8 + 1] = 0;
	 *v2 = v8;
	 }
	 else
	 {
	 close(v7);
	 free(v4);
	 v4 = 0;
	 }
	 }
	 else
	 {
	 v4 = 0;
	 }
	 result = v4;
	 if ( v10 != _stack_chk_guard )
	 _stack_chk_fail((int)v4);
	 return result;
	 }
	 
	 int open(const char *file, int oflag, ...)
	 {
	 return open(file, oflag);
	 }

	 FILE *fopen(const char *filename, const char *modes)
	 {
	 return fopen(filename, modes);
	 }

	 char *realpath(const char *name, char *resolved)
	 {
	 return realpath(name, resolved);
	 }

	*/

	
	//QSEditText.setCurrentFileExt调用
	//QSEditText.onTouchEvent调用
	public void redraw()
	{
		this.mStop = false;
		this.mEndOffset = -1;
		this.mStartOffset = -1;
	}
	//QSEditText.setCurrentFileExt调用
	public void setSyntaxType(String paramString)
	{
		this.mExt = paramString;
	}
	

	
	//QSEditText.onTouchEvent调用
	public void stop()
	{
		this.mStop = true;
	}
	
}

/* Location:           C:\Documents and Settings\鏉庡繝鍏╘妗岄潰\鍙嶇紪璇慭ApkDecompiler\Output\com.jecelyin.editor_V12.9.25\classes_dex2jar.jar
 * Qualified Name:     com.jecelyin.highlight.Highlight
 * JD-Core Version:    0.6.0
 */
