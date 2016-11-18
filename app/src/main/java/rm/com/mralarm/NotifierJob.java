package rm.com.mralarm;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

/**
 * Created by alex
 */
final class NotifierJob extends Job {

  static final String TAG = "notifier_job_tag";

  @NonNull
  @Override
  protected Result onRunJob(Params params) {
    try {
      final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
      final Notification ntf = new NotificationCompat.Builder(getContext())
          .setSmallIcon(R.mipmap.ic_launcher)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setContentTitle("Yo")
          .build();

      notificationManager.cancelAll();
      notificationManager.notify(1488, ntf);

      return Result.SUCCESS;
    } finally {
      scheduleNotifier(false, getContext());
    }
  }

  static void scheduleNotifier(@NonNull Context context) {
    scheduleNotifier(true, context);
  }

  private static void scheduleNotifier(boolean updateCurrent, @NonNull Context context) {
    final NotificationSettings settings = new NotificationSettings(context);

    new JobRequest.Builder(TAG)
        .setExact(TimeUtils.nextDelay(settings.interval(), settings.from(), settings.to()))
        .setUpdateCurrent(updateCurrent)
        .build()
        .schedule();
  }
}
