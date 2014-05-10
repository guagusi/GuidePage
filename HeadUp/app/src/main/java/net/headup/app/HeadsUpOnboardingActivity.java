package net.headup.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Description
 * Note
 * Created by 古阿古斯 on 14-5-8.
 */
public class HeadsUpOnboardingActivity extends Activity {

	private static final String TAG = HeadsUpOnboardingActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_headsup_onboarding);
	}

	
}
