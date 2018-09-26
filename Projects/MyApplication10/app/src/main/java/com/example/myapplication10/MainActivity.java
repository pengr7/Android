package com.example.myapplication10;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

public class MainActivity extends Activity {
    TextureMapView mMapView = null;
    ToggleButton toggleButton = null;

    // managers
    SensorManager mSensorManager = null;
    LocationManager mLocationManager = null;

    // sensors
    Sensor mMagneticSensor = null;
    Sensor mAccelerometerSensor = null;

    // location provider
    String provider = LocationManager.GPS_PROVIDER;

    // whether the current location is required to be in center
    boolean centerRequired = true;

    // the number of shakes
    int shake = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (TextureMapView) findViewById(R.id.bmapView);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        // get managers
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // get sensors
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // register sensors
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);

        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.pointer), 100, 100, true);
        BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromBitmap(bitmap);
        mMapView.getMap().setMyLocationEnabled(true);
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, bitmapD);
        mMapView.getMap().setMyLocationConfigeration(config);

        // set location and direction
        setLocationAndDirection(getCurrentLocation(), 0);

        // toggle button listener
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                centerRequired = !centerRequired;
                if (centerRequired) {
                    toggleButton.setBackground(getResources()
                            .getDrawable(R.drawable.center_direction, null));
                } else {
                    toggleButton.setBackground(getResources()
                            .getDrawable(R.drawable.define_location, null));
                }
                setLocationAndDirection(getCurrentLocation(), 0);
            }
        });

        // map listener
        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    centerRequired = false;
                    toggleButton.setBackground(getResources()
                            .getDrawable(R.drawable.define_location, null));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

        // register sensors
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

        // unregister sensors
        mSensorManager.unregisterListener(mSensorEventListener);

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;
        mLocationManager.removeUpdates(mLocationListener);
    }

    // sensor event listener
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        float[] accValues = null;
        float[] magValues = null;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accValues = sensorEvent.values;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magValues = sensorEvent.values;
                    break;
                default:
                    break;
            }

            // get current direction
            if (accValues == null || magValues == null)
                return;
            float[] R = new float[9];
            float[] values = new float[3];
            SensorManager.getRotationMatrix(R, null, accValues, magValues);
            SensorManager.getOrientation(R, values);
            float direction = (float) Math.toDegrees(values[0]);

            // set location and direction
            setLocationAndDirection(getCurrentLocation(), direction);

            // shake
            for (int i = 0; i < 3; i++)
                if (Math.abs(accValues[i]) > 10) {
                    shake++;
                    if (shake >= 30) {
                        Toast.makeText(MainActivity.this, "Shake", Toast.LENGTH_SHORT).show();
                        shake = 0;
                    }
                }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {}
    };

    // location listener
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            setLocationAndDirection(location, 0);
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override
        public void onProviderEnabled(String s) {}
        @Override
        public void onProviderDisabled(String s) {}
    };

    // get current location
    Location getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return mLocationManager.getLastKnownLocation(provider);
        return mLocationManager.getLastKnownLocation(provider);
    }

    // set location and direction
    void setLocationAndDirection(Location location, float direction) {
        if (location == null)
            return;

        // convert location
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
        LatLng desLatLng = converter.convert();

        // set location data
        MyLocationData.Builder data = new MyLocationData.Builder();
        data.latitude(desLatLng.latitude);
        data.longitude(desLatLng.longitude);
        data.direction(direction);
        mMapView.getMap().setMyLocationData(data.build());

        // keep the location center
        if (centerRequired) {
            MapStatus mMapStatus = new MapStatus.Builder().target(desLatLng).build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            mMapView.getMap().setMapStatus(mMapStatusUpdate);
        }
    }
}
