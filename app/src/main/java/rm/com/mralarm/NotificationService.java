package rm.com.mralarm;

import android.app.IntentService;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import static rm.com.mralarm.TimeUtils.nextDelay;
import static rm.com.mralarm.TimeUtils.nextTime;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service isOn a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public final class NotificationService extends Service {

  private static Handler sServiceHandler;

  public static void rescheduleNotifications(
      final Context ctx,
      final int interval,
      final int from,
      final int to
  ) {
    if (sServiceHandler == null) return;

    sServiceHandler.removeCallbacksAndMessages(null);
    sServiceHandler.postAtTime(new Runnable() {
      @Override
      public void run() {
        showNotification(ctx);

        sServiceHandler.postDelayed(this, nextDelay(interval, from, to));
      }
    }, nextTime(interval, from, to));
  }

  public static void start(Context ctx) {
    ctx.startService(new Intent(ctx, NotificationService.class));
  }

  public static void stop(Context ctx) {
    ctx.stopService(new Intent(ctx, NotificationService.class));
  }

  private static void showNotification(Context ctx) {
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);
    Notification ntf = new NotificationCompat.Builder(ctx)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Yo")
        .build();

    notificationManager.cancelAll();
    notificationManager.notify(0, ntf);
  }

  @Override
  public void onCreate() {
    final NotificationSettings settings = new NotificationSettings(getApplicationContext());
    final HandlerThread thread = new HandlerThread("NotificationService", Process.THREAD_PRIORITY_LESS_FAVORABLE);
    thread.start();

    sServiceHandler = new Handler(thread.getLooper());

    rescheduleNotifications(
        this,
        settings.interval(),
        settings.from(),
        settings.to()
    );
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d("NotificationService", "onDestroy");
    sServiceHandler.removeCallbacksAndMessages(null);
    sServiceHandler = null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
