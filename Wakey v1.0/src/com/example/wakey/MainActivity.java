package com.example.wakey;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		SensorEventListener {
	// Declarations
	private SensorManager sensorManager;
	DevicePolicyManager devicePolicyManager;
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	ComponentName adminComponent;
	protected static final int REQUEST_ENABLE = 0;
	int t;
	TextView x;
	TextView y;
	TextView z;
	TextView d;
	String sx, sy, sz, dn, dy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		x = (TextView) findViewById(R.id.textView2);
		y = (TextView) findViewById(R.id.textView3);
		z = (TextView) findViewById(R.id.textView4);
		d = (TextView) findViewById(R.id.textView5);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_NORMAL);

		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.activity_drawer_list, mPlanetTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		return super.onPrepareOptionsMenu(menu);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = inflater.inflate(R.layout.fragment_disp, container,
					false);

			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String planet = getResources()
					.getStringArray(R.array.planets_array)[i];
			/*
			 * 
			 * int imageId =
			 * getResources().getIdentifier(planet.toLowerCase(Locale
			 * .getDefault()), "drawable", getActivity().getPackageName());
			 * ((ImageView)
			 * rootView.findViewById(R.id.image)).setImageResource(imageId);
			 * getActivity().setTitle(planet);
			 */
			return rootView;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		switch (item.getItemId()) {

		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return true;

		case R.id.action_settings:
			break;

		case R.id.action_notification: {

			// Get Device Administrator Permission
			adminComponent = new ComponentName(MainActivity.this,
					Darclass.class);
			devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			if (!devicePolicyManager.isAdminActive(adminComponent)) {
				Intent intent = new Intent(
						DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						adminComponent);
				startActivityForResult(intent, REQUEST_ENABLE);
			}

			if (item.isChecked()) {
				item.setChecked(false);
				Toast.makeText(this, "Hide Notification!", Toast.LENGTH_LONG)
						.show();
				stopService(new Intent(MainActivity.this, NotifyService.class));
				return true;
			} else {
				item.setChecked(true);
				Toast.makeText(this, "Show Notification!", Toast.LENGTH_LONG)
						.show();
				startService(new Intent(MainActivity.this, NotifyService.class));
				return true;
			}
		}

		case R.id.action_display:
			Intent intent = new Intent(MainActivity.this, DispActivity.class);
			startActivity(intent);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		// Sensor Logic
		if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {

			float xVal = event.values[0];
			float yVal = event.values[1];
			float zVal = event.values[2];

			float rVal = (float) Math.sqrt((xVal * xVal) + (yVal * yVal)
					+ (zVal * zVal));
			float fin = SensorManager.GRAVITY_EARTH - rVal;

			sx = "X Value : <font color = '#800080'> " + xVal + "</font>";
			sy = "Y Value : <font color = '#800080'> " + fin + "</font>";
			sz = "Z Value : <font color = '#800080'> " + zVal + "</font>";
			dy = "The Phone is on the Table";
			dn = "The Phone is not on the Table";

			x.setText(Html.fromHtml(sx));
			y.setText(Html.fromHtml(sy));
			z.setText(Html.fromHtml(sz));

			if (yVal < 0.5 && yVal > -0.5) {
				d.setText(Html.fromHtml(dy));
			} else {
				d.setText(Html.fromHtml(dn));
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
