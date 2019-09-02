package in.fabits.fabits;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by pi on 4/22/17.
 */

public class MyUploadService  extends GcmTaskService {
    @Override
    public int onRunTask(TaskParams params) {

        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
