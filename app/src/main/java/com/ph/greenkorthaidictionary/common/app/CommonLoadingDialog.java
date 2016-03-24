package com.ph.greenkorthaidictionary.common.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.ph.greenkorthaidictionary.R;

/**
 * @author H1407050
 * 
 * @프로그램 설명 : 앱에서 공통으로 사용하는 로딩화면을 정의해놓은 클래스 
 */
public class CommonLoadingDialog extends Dialog {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
//		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//		lpWindow.dimAmount = 0.0f;
//		getWindow().setAttributes(lpWindow);
//		setContentView(R.layout.dlg_progress);

		// Dialog 배경을 투명 처리 해준다.
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dlg_progress);
	}
	

	public CommonLoadingDialog(Context context) {
		super(context, android.R.style.Theme_Holo_Light_NoActionBar);
		setCancelable(false);
	}

}
