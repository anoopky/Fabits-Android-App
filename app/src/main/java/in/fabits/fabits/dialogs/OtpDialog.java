package in.fabits.fabits.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.helpers.Util;

import in.fabits.fabits.R;
import in.fabits.fabits.api.ApiUtil;
import in.fabits.fabits.api.Preferences;
import in.fabits.fabits.api.Utils;


public class OtpDialog {

    public void showDialog(final Activity activity, final int i) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        final EditText otp = (EditText) dialog.findViewById(R.id.otp);
        ImageView cross = (ImageView) dialog.findViewById(R.id.close);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0)
                    Utils.updateSetting(otp.getText().toString(), Preferences.S_OTP, activity.getBaseContext());
                else
                    Utils.updateSetting(otp.getText().toString(), Preferences.S_OTP, activity.getBaseContext(), i);
                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
