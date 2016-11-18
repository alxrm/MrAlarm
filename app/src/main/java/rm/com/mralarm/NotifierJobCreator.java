package rm.com.mralarm;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by alex
 */
final class NotifierJobCreator implements JobCreator {

  @Override
  public Job create(String tag) {
    if (tag.equals(NotifierJob.TAG)) return new NotifierJob();

    return null;
  }
}
