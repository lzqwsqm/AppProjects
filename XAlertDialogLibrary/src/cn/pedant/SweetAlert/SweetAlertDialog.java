package cn.pedant.SweetAlert;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.imagine.go.util.SeekBarUtil;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.xidian.xalertdialog.R;

public class SweetAlertDialog extends Dialog implements View.OnClickListener {
	private View mDialogView;
	private AnimationSet mModalInAnim;
	private AnimationSet mModalOutAnim;
	private Animation mOverlayOutAnim;
	private Animation mErrorInAnim;
	private AnimationSet mErrorXInAnim;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mSuccessBowAnim;
	private TextView mTitleTextView;
	private TextView mContentTextView;
	private String mTitleText;
	private String mContentText;
	private boolean mShowCancel;
	private boolean mShowContent;
	private String mCancelText;
	private String mConfirmText;
	private FrameLayout mErrorFrame;
	private FrameLayout mSuccessFrame;
	private FrameLayout mProgressFrame;
	private SuccessTickView mSuccessTick;
	private ImageView mErrorX;
	private View mSuccessLeftMask;
	private View mSuccessRightMask;
	private Drawable mCustomImgDrawable;
	private ImageView mCustomImage;
	private Button mConfirmButton;
	private Button mCancelButton;
	private ProgressHelper mProgressHelper;
	private FrameLayout mWarningFrame;

	private OnSweetClickListener mCancelClickListener;
	private OnSweetClickListener mConfirmClickListener;
	private boolean mCloseFromCancel;

	/* ����Ի��� */
	private FrameLayout mInputFrame;
	private EditText mEditText;

	/* �����Ի��� */
	private FrameLayout mSeekFrame;
	private SeekBar mSeekbar;
	private TextView mSeekText;

	/* �Զ���Ի��� . */
	private FrameLayout mCustomFrame;

	/* �Ի������� */
	public static final int NORMAL_TYPE = 0;
	public static final int ERROR_TYPE = 1;
	public static final int SUCCESS_TYPE = 2;
	public static final int WARNING_TYPE = 3;
	public static final int CUSTOM_IMAGE_TYPE = 4;
	public static final int PROGRESS_TYPE = 5;
	public static final int INPUT_TYPE = 6;
	public static final int SEEK_TYPE = 7;
	/* ��ǵ�ǰ�Ի������� */
	private int mAlertType;

	/* �Ի���Id. */
	private int mId;

	/**
	 * �ص��ӿ�
	 */
	public interface OnSweetClickListener {
		public void onClick(SweetAlertDialog sweetAlertDialog);
	}

	/**
	 * �������ص�
	 */
	private OnSweetSeekBarChangeListener onSweetSeekBarChangeListener;

	public interface OnSweetSeekBarChangeListener {
		public void onSeekBarChanged(int range);
	}

	// ---------------- ������ ----------------
	//
	public SweetAlertDialog(Context context) {
		this(context, NORMAL_TYPE);
	}

	public SweetAlertDialog(Context context, int alertType) {
		super(context, R.style.alert_dialog);
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		mProgressHelper = new ProgressHelper(context);
		mAlertType = alertType;

		// ��ʼ������
		mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.error_frame_in);
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.error_x_in);
		// 2.3.x system don't support alpha-animation on layer-list drawable
		// remove it from animation set
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			List<Animation> childAnims = mErrorXInAnim.getAnimations();
			int idx = 0;
			for (; idx < childAnims.size(); idx++) {
				if (childAnims.get(idx) instanceof AlphaAnimation) {
					break;
				}
			}
			if (idx < childAnims.size()) {
				childAnims.remove(idx);
			}
		}
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(),
				R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader
				.loadAnimation(getContext(), R.anim.success_mask_layout);
		mModalInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_in);
		mModalOutAnim = (AnimationSet) OptAnimationLoader.loadAnimation(
				getContext(), R.anim.modal_out);
		mModalOutAnim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mDialogView.setVisibility(View.GONE);
				mDialogView.post(new Runnable() {
					@Override
					public void run() {
						if (mCloseFromCancel) {
							SweetAlertDialog.super.cancel();
						} else {
							SweetAlertDialog.super.dismiss();
						}
					}
				});
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		// dialog overlay fade out
		mOverlayOutAnim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				WindowManager.LayoutParams wlp = getWindow().getAttributes();
				wlp.alpha = 1 - interpolatedTime;
				getWindow().setAttributes(wlp);
			}
		};
		mOverlayOutAnim.setDuration(120);
	}

	// ----------------- �������� ---------------------
	//
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);

		// ��ʼ����ͼ���
		mDialogView = getWindow().getDecorView().findViewById(
				android.R.id.content);
		mTitleTextView = (TextView) findViewById(R.id.title_text);
		mContentTextView = (TextView) findViewById(R.id.content_text);
		mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
		mErrorX = (ImageView) mErrorFrame.findViewById(R.id.error_x);
		mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
		mProgressFrame = (FrameLayout) findViewById(R.id.progress_dialog);
		mSuccessTick = (SuccessTickView) mSuccessFrame
				.findViewById(R.id.success_tick);
		mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
		mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
		mCustomImage = (ImageView) findViewById(R.id.custom_image);
		mWarningFrame = (FrameLayout) findViewById(R.id.warning_frame);
		mConfirmButton = (Button) findViewById(R.id.confirm_button);
		mCancelButton = (Button) findViewById(R.id.cancel_button);
		mProgressHelper
				.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));
		mConfirmButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);

		// ��ʼ����Ի���
		mInputFrame = (FrameLayout) findViewById(R.id.input_dialog);
		mEditText = (EditText) mInputFrame.findViewById(R.id.editText);

		// ��ʼ���϶����Ի���
		mSeekFrame = (FrameLayout) findViewById(R.id.seek_dialog);
		mSeekbar = (SeekBar) mSeekFrame.findViewById(R.id.seekbar);
		mSeekText = (TextView) mSeekFrame.findViewById(R.id.zoomText);

		setTitleText(mTitleText);
		setContentText(mContentText);
		setCancelText(mCancelText);
		setConfirmText(mConfirmText);
		changeAlertType(mAlertType, true);

		// ע�������
		registerViewListener();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private void restore() {
		mCustomImage.setVisibility(View.GONE);
		mErrorFrame.setVisibility(View.GONE);
		mSuccessFrame.setVisibility(View.GONE);
		mWarningFrame.setVisibility(View.GONE);
		mProgressFrame.setVisibility(View.GONE);
		mConfirmButton.setVisibility(View.VISIBLE);

		mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
		mErrorFrame.clearAnimation();
		mErrorX.clearAnimation();
		mSuccessTick.clearAnimation();
		mSuccessLeftMask.clearAnimation();
		mSuccessRightMask.clearAnimation();
	}

	private void playAnimation() {
		if (mAlertType == ERROR_TYPE) {
			mErrorFrame.startAnimation(mErrorInAnim);
			mErrorX.startAnimation(mErrorXInAnim);
		} else if (mAlertType == SUCCESS_TYPE) {
			mSuccessTick.startTickAnim();
			mSuccessRightMask.startAnimation(mSuccessBowAnim);
		}
	}

	private void changeAlertType(int alertType, boolean fromCreate) {
		mAlertType = alertType;
		// call after created views
		if (mDialogView != null) {
			if (!fromCreate) {
				// restore all of views state before switching alert type
				restore();
			}
			switch (mAlertType) {
			case ERROR_TYPE:
				mErrorFrame.setVisibility(View.VISIBLE);
				break;
			case SUCCESS_TYPE:
				mSuccessFrame.setVisibility(View.VISIBLE);
				// initial rotate layout of success mask
				mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet
						.getAnimations().get(0));
				mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet
						.getAnimations().get(1));
				break;
			case WARNING_TYPE:
				mConfirmButton
						.setBackgroundResource(R.drawable.red_button_background);
				mWarningFrame.setVisibility(View.VISIBLE);
				break;
			case CUSTOM_IMAGE_TYPE:
				setCustomImage(mCustomImgDrawable);
				break;
			case PROGRESS_TYPE:
				mProgressFrame.setVisibility(View.VISIBLE);
				mConfirmButton.setVisibility(View.GONE);
				mCancelButton.setVisibility(View.GONE);
				setCancelable(false);
				break;

			case INPUT_TYPE:
				// ����Ի���
				mInputFrame.setVisibility(View.VISIBLE);
				mConfirmButton.setVisibility(View.VISIBLE);
				break;
			case SEEK_TYPE:
				// �϶����Ի���
				mSeekFrame.setVisibility(View.VISIBLE);
				mConfirmButton.setVisibility(View.GONE);
				mCancelButton.setVisibility(View.GONE);
				break;
			}
			if (!fromCreate) {
				playAnimation();
			}
		}
	}

	/**
	 * ���ضԻ�������
	 * 
	 * @return
	 */
	public int getAlerType() {
		return mAlertType;
	}

	public void changeAlertType(int alertType) {
		changeAlertType(alertType, false);
	}

	public String getTitleText() {
		return mTitleText;
	}

	public SweetAlertDialog setTitleText(String text) {
		mTitleText = text;
		if (mTitleTextView != null && mTitleText != null) {
			mTitleTextView.setText(mTitleText);
		}
		return this;
	}

	public SweetAlertDialog setCustomImage(Drawable drawable) {
		mCustomImgDrawable = drawable;
		if (mCustomImage != null && mCustomImgDrawable != null) {
			mCustomImage.setVisibility(View.VISIBLE);
			mCustomImage.setImageDrawable(mCustomImgDrawable);
		}
		return this;
	}

	public SweetAlertDialog setCustomImage(int resourceId) {
		return setCustomImage(getContext().getResources().getDrawable(
				resourceId));
	}

	public String getContentText() {
		return mContentText;
	}

	public SweetAlertDialog setContentText(String text) {
		mContentText = text;
		if (mContentTextView != null && mContentText != null) {
			showContentText(true);
			mContentTextView.setText(mContentText);
		}
		return this;
	}

	public boolean isShowCancelButton() {
		return mShowCancel;
	}

	public SweetAlertDialog showCancelButton(boolean isShow) {
		mShowCancel = isShow;
		if (mCancelButton != null) {
			mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
		}
		return this;
	}

	public boolean isShowContentText() {
		return mShowContent;
	}

	public SweetAlertDialog showContentText(boolean isShow) {
		mShowContent = isShow;
		if (mContentTextView != null) {
			mContentTextView.setVisibility(mShowContent ? View.VISIBLE
					: View.GONE);
		}
		return this;
	}

	public String getCancelText() {
		return mCancelText;
	}

	public SweetAlertDialog setCancelText(String text) {
		mCancelText = text;
		if (mCancelButton != null && mCancelText != null) {
			showCancelButton(true);
			mCancelButton.setText(mCancelText);
		}
		return this;
	}

	public String getConfirmText() {
		return mConfirmText;
	}

	public SweetAlertDialog setConfirmText(String text) {
		mConfirmText = text;
		if (mConfirmButton != null && mConfirmText != null) {
			mConfirmButton.setText(mConfirmText);
		}
		return this;
	}

	public SweetAlertDialog setCancelClickListener(OnSweetClickListener listener) {
		mCancelClickListener = listener;
		return this;
	}

	public SweetAlertDialog setConfirmClickListener(
			OnSweetClickListener listener) {
		mConfirmClickListener = listener;
		return this;
	}

	protected void onStart() {
		mDialogView.startAnimation(mModalInAnim);
		playAnimation();
	}

	/**
	 * The real Dialog.cancel() will be invoked async-ly after the animation
	 * finishes.
	 */
	@Override
	public void cancel() {
		dismissWithAnimation(true);
	}

	/**
	 * The real Dialog.dismiss() will be invoked async-ly after the animation
	 * finishes.
	 */
	public void dismissWithAnimation() {
		dismissWithAnimation(false);
	}

	private void dismissWithAnimation(boolean fromCancel) {
		mCloseFromCancel = fromCancel;
		mConfirmButton.startAnimation(mOverlayOutAnim);
		mDialogView.startAnimation(mModalOutAnim);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.cancel_button) {
			if (mCancelClickListener != null) {
				mCancelClickListener.onClick(SweetAlertDialog.this);
			} else {
				dismissWithAnimation();
			}
		} else if (v.getId() == R.id.confirm_button) {
			if (mConfirmClickListener != null) {
				mConfirmClickListener.onClick(SweetAlertDialog.this);
			} else {
				dismissWithAnimation();
			}
		}
	}

	public ProgressHelper getProgressHelper() {
		return mProgressHelper;
	}

	// --------------------- �Զ��� ---------------------
	/**
	 * ��ʼ����ͼ������
	 */
	private void registerViewListener() {
		// �϶����Ի���������ʧ
		mSeekFrame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mConfirmButton.callOnClick();
			}
		});

		mSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int range = SeekBarUtil.calcRadius(progress);
				String distance = SeekBarUtil.formatDistance(range);
				mSeekText.setText(distance);
				if (null != onSweetSeekBarChangeListener) {
					onSweetSeekBarChangeListener.onSeekBarChanged(range);
				}
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int range = SeekBarUtil.calcRadius(seekBar.getProgress());
				String distance = SeekBarUtil.formatDistance(range);
				mSeekText.setText(distance);
				if (null != onSweetSeekBarChangeListener) {
					onSweetSeekBarChangeListener.onSeekBarChanged(range);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});

	}

	/**
	 * �������Ի����ı�
	 * 
	 * @return
	 */
	public String getInputText() {
		String text = mEditText.getText().toString().trim();
		return text;
	}

	/**
	 * �������õİ뾶ֵ
	 * 
	 * @return ��ǰֵ
	 */
	public int getRadius() {
		int progress = mSeekbar.getProgress();
		return SeekBarUtil.calcRadius(progress);
	}

	public void setProgress(int radius) {
		int progress = SeekBarUtil.calcProgress(radius);
		String radiusText = SeekBarUtil.formatDistance(radius);
		mSeekbar.setProgress(progress);
		mSeekText.setText(radiusText);
	}

	public void setOnSweetSeekBarChangeListener(
			OnSweetSeekBarChangeListener listener) {
		onSweetSeekBarChangeListener = listener;
	}

	/**
	 * �Ի���ʶ��
	 * 
	 * @param id
	 */
	public void setId(int id) {
		mId = id;
	}

	public int getId() {
		return mId;
	}

	/**
	 * ����ȷ�϶Ի���
	 * 
	 * @param context
	 * @param id
	 * @param txt
	 * @param listener
	 * @return
	 */
	public static SweetAlertDialog buildConfirmDialog(Context context, int id,
			String txt, OnSweetClickListener listener) {
		SweetAlertDialog dialog = new SweetAlertDialog(context);
		dialog.setContentText(txt);
		dialog.setConfirmClickListener(listener);
		dialog.setId(id);
		return dialog;
	}

}
