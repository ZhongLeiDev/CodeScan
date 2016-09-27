package com.zkc.barcodescan.activity;

import com.zkc.barcodescan.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class DialogEncodingActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_encoding);

		final String[] EncodeType = new String[] { "ASCII", "ISO-8859-1",
				"GB2312", "GBK", "UTF-8", "UTF-16" };

		Dialog alertDialog = new AlertDialog.Builder(this)
				.setTitle(
						this.getResources().getString(
								R.string.action_encoding_tip))
				.setItems(EncodeType, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(DialogEncodingActivity.this,
								EncodeType[which], Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(DialogEncodingActivity.this, ActivityQrcodeSetting.class);
						Bundle b = new Bundle();
						b.putString("str", EncodeType[which]);
						intent.putExtras(b);
						setResult(RESULT_OK, intent);	

						finish();
					}
				})
				.setNegativeButton(getResources().getString(R.string.popup_no),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).create();
		alertDialog.show();
	}
}