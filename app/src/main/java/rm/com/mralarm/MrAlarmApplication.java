package rm.com.mralarm;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * Created by alex
 */

public class MrAlarmApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    JobManager.create(this).addJobCreator(new NotifierJobCreator());
  }
}
