package com.appchallengers.appchallengers.helpers.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.view.Gravity;
import android.view.WindowManager;
import com.appchallengers.appchallengers.R;


public class NotificationToast {

    public static NotificationToast notificationToast = null;
    public static Object object = new Object();

    private NotificationToast() {
    }

    public static NotificationToast getInstance() {
        if (notificationToast == null) {
            synchronized (object) {
                if (notificationToast == null) {
                    return notificationToast = new NotificationToast();
                }
            }
        }
        return notificationToast;
    }

    public void Show_Toast(Context context, int text, int image) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.notification_toast);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes();
        window.setGravity(Gravity.CENTER);
        String mMesaage = context.getResources().getString(text);
        // set the custom dialog components - text, image and button
        TextView mtext = (TextView) dialog.findViewById(R.id.notification_toast_textview);
        mtext.setText(mMesaage);
        ImageView mimage = (ImageView) dialog.findViewById(R.id.notification_toast_image);
        mimage.setImageResource(image);
        dialog.show();

    }
}
