package com.example.wakey;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class NotifyService extends Service implements SensorEventListener {
	private NotificationManager nm;
	private SensorManager sensorManager;
	DevicePolicyManager devicePolicyManager;
	ComponentName adminComponent;
	String sxn, syn, szn;
	int t, c, f = 0;
	protected static final int REQUEST_ENABLE = 0;
	float xVal, yVal, zVal, tVal;
	private PowerManager.WakeLock wl;

	// private PowerManager.WakeLock off;
	// private PowerManager.WakeLock on;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
		wl.acquire();

		/*
		 * PowerManager pm = (PowerManager)
		 * getSystemService(Context.POWER_SERVICE); wl =
		 * pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNjfdhotDimScreen");
		 */

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		showNotification();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel the persistent notification.
		wl.release();
		nm.cancel(R.string.service_started);
		c = 1;

	}

	private void showNotification() {

		adminComponent = new ComponentName(NotifyService.this, Darclass.class);
		devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		// Dynamically Change the Text
		Resources res = getResources();
		CharSequence text = String.format(
				res.getString(R.string.service_started), sxn, syn, szn);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_launcher,
				text, System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.service_label),
				text, contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		nm.notify(R.string.service_started, notification);

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			xVal = event.values[0];
			yVal = event.values[1];
			zVal = event.values[2];

			sxn = "" + xVal;
			syn = "" + yVal;
			szn = "" + zVal;
			if (c != 1) {
				showNotification();

				if (yVal < 1 && yVal > 1) {
					// android.provider.Settings.System.putInt(getContentResolver(),
					// Settings.System.SCREEN_OFF_TIMEOUT, 1000);
					// devicePolicyManager.lockNow();
				}

				else {

					t = 0;

					// setWakeLockOn(wl);
					/*
					 * KeyguardManager manager = (KeyguardManager)
					 * this.getSystemService(Context.KEYGUARD_SERVICE);
					 * KeyguardLock lock = manager.newKeyguardLock("abc");
					 * lock.disableKeyguard();
					 * 
					 * Intent myIntent = new Intent(getBaseContext(),
					 * InvActivity.class);
					 * getApplication().startActivity(myIntent);
					 */
					// android.provider.Settings.System.putInt(getContentResolver(),
					// Settings.System.SCREEN_BRIGHTNESS, 0);

				}
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		devicePolicyManager.lockNow();

	}

	public void setWakeLockOf(WakeLock wakeLock) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock1");
		if ((wakeLock != null) && // we have a WakeLock
				(wakeLock.isHeld() == false)) {
			wakeLock.acquire();
		}
		// pm.userActivity(SystemClock.uptimeMillis(), true);

	}

	public void setWakeLockOn(WakeLock wakeLock) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyWakeLock");
		if ((wakeLock != null) && // we have a WakeLock
				(wakeLock.isHeld() == false)) {
			wakeLock.acquire();
			// pm.userActivity(SystemClock.uptimeMillis(), true);
		}
	}

	public void rmWakeLock(WakeLock wakeLock) {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock.release();
		// pm.userActivity(SystemClock.uptimeMillis(), true);
	}

}
