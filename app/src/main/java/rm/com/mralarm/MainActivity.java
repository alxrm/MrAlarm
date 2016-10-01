package rm.com.mralarm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

import static rm.com.mralarm.TimeUtils.FROM_TIMES;
import static rm.com.mralarm.TimeUtils.INTERVALS;
import static rm.com.mralarm.TimeUtils.TO_TIMES;

public final class MainActivity extends AppCompatActivity {

  final static Handler UI_HANDLER = new Handler(Looper.getMainLooper());

  @BindView(R.id.send_from)
  Spinner from;

  @BindView(R.id.send_to)
  Spinner to;

  @BindView(R.id.send_interval)
  Spinner interval;

  @BindView(R.id.send_toggle)
  CheckBox should;

  NotificationSettings settings;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    settings = new NotificationSettings(this);

    from.setAdapter(new ArrayAdapter<>(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        FROM_TIMES.keySet().toArray())
    );

    to.setAdapter(new ArrayAdapter<>(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        TO_TIMES.keySet().toArray())
    );

    interval.setAdapter(new ArrayAdapter<>(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        INTERVALS.keySet().toArray())
    );

    from.setSelection(settings.fromIndex());
    to.setSelection(settings.toIndex());
    interval.setSelection(settings.intervalIndex());

    should.setChecked(settings.isOn());

    from.setEnabled(should.isChecked());
    to.setEnabled(should.isChecked());
    interval.setEnabled(should.isChecked());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    reschedule();
  }

  @OnItemSelected(R.id.send_from)
  void onFromSelected(int pos) {
    settings.setFrom(pos);
  }

  @OnItemSelected(R.id.send_to)
  void onToSelected(int pos) {
    settings.setTo(pos);
  }

  @OnItemSelected(R.id.send_interval)
  void onIntervalSelected(int pos) {
    settings.setInterval(pos);
  }

  @OnCheckedChanged(R.id.send_toggle)
  void onNotifierToggle(final boolean shouldSend) {
    if (shouldSend) NotificationService.start(this);
    else NotificationService.stop(this);

    settings.setOn(shouldSend);

    UI_HANDLER.postDelayed(new Runnable() {
      @Override
      public void run() {
        from.setEnabled(shouldSend);
        to.setEnabled(shouldSend);
        interval.setEnabled(shouldSend);
      }
    }, 100);
  }

  private void reschedule() {
    NotificationService.rescheduleNotifications(
        this,
        settings.interval(),
        settings.from(),
        settings.to()
    );
  }
}
