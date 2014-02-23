
package com.example.preferencecategory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {

	private final String[] colorName = {
			"ORANGE", "WHITE", "BLACK" };
	private final int[] colorResource = {
			R.color.orange, R.color.white, R.color.black };
	private Button colorButton, settingButton;
	private final String DEFAULT_TEXT_COLOR = "prefColor";
	private boolean colorSettingChanged = false;
	private TextView txtColors;
	private Context context;
	private SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		// initialize user interface
		initUI();
		// initialize preferences
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		sharedPref.registerOnSharedPreferenceChangeListener(this);
	}

	private void initUI() {
		txtColors = (TextView) findViewById(R.id.txtColor);
		colorButton = (Button) findViewById(R.id.colorButton);
		settingButton = (Button) findViewById(R.id.settingButton);

		colorButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this);
				build.setTitle("Text Color");
				build.setSingleChoiceItems(colorName, -1, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int choice) {
						setPreferences(choice);
						dialog.dismiss();
					}
				});

				AlertDialog alert = build.create();
				alert.show();

			}
		});// colorButton End

		settingButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent inSetting = new Intent(context, SettingsActivity.class);
				startActivity(inSetting);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (colorSettingChanged) {
			LoadPreferences();
			colorSettingChanged = false;
		}
	}

	private void setPreferences(int choice) {
		SharedPreferences.Editor mEditor = sharedPref.edit();
		mEditor.putString(DEFAULT_TEXT_COLOR, "" + choice);
		mEditor.commit();
		Toast.makeText(context, "" + colorName[choice] + " set ...", Toast.LENGTH_SHORT).show();
		txtColors.setTextColor(getResources().getColor(colorResource[choice]));
	}

	private void LoadPreferences() {
		int colorId = Integer.valueOf(sharedPref.getString(DEFAULT_TEXT_COLOR, "-1"));
		if (colorId != -1)
			txtColors.setTextColor(getResources().getColor(colorResource[colorId]));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences shrdPref, String val) {
		if (val.equals("prefColor")) {
			colorSettingChanged = true;
			Log.d("::debug::", "prefName " + val);
			Log.d("::debug::", "value :" + shrdPref.getString(val, ""));
		}
	}
}
