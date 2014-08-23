package fortyonepost.com.ag;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.widget.TextView;

public class AccessGyroscope extends Activity implements SensorEventListener
{
	 private static final int SERVERPORT = 4444;

	// TextView
	private TextView tv;
	private TextView textView;
	private TextView test;
	//the Sensor Manager
	private SensorManager sManager;
	private Sensor mSensor;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //get the TextView from the layout file
        tv = (TextView) findViewById(R.id.tv);
        textView = (TextView) findViewById(R.id.textView1);
        test = (TextView) findViewById(R.id.textView2);
        
        //get a hook to the sensor service
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        //runTcpServer();
        
        
    }
    
    //when this Activity starts
    @Override
	protected void onResume() 
	{
		super.onResume();
		/*register the sensor listener to listen to the gyroscope sensor, use the 
		 * callbacks defined in this class, and gather the sensor information as  
		 * quick as possible*/
		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
		sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_FASTEST);
		
		//
       // runTcpServer();
		
	}

    //When this Activity isn't visible anymore
	@Override
	protected void onStop() 
	{
		//unregister the sensor listener
		sManager.unregisterListener(this);
		super.onStop();
	}

	// connect wifi
	private void runTcpServer() {
	    ServerSocket ss = null;
	    try {
	        ss = new ServerSocket(SERVERPORT);
	        Log.d("TcpServer", ss.getInetAddress()+"");
	        //ss.setSoTimeout(10000);
	        //accept connections
	        Socket s = ss.accept();
	        Log.i("TcpServer", "Receiving");
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	        //receive a message
	        Log.i("TcpServer", in.readLine());
	        final String incomingMsg = in.readLine() + System.getProperty("line.separator");
	        Log.i("TcpServer", "received: " + incomingMsg);
	        runOnUiThread(new Runnable() {
	            public void run() {
	            	test.append("received: " + incomingMsg);
	            }
	        });

	        s.close();
	    } catch (InterruptedIOException e) {
	        //if timeout occurs
	        e.printStackTrace();
	        Log.e("TcpServer", ""+e);
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("TcpServer", ""+e);
	    } finally {
	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                Log.e("TcpServer", ""+e);
	            }
	        }
	    }
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) 
	{
		//Do nothing
	}
public void writeToCsvGy(String x,String y,String z) throws IOException {
		
	Calendar c = Calendar.getInstance(); 
	File folder = new File(Environment.getExternalStorageDirectory() + "/TollCulator");
	boolean success = true;
	if (!folder.exists()) {
	    success = folder.mkdir();
	}
	if (success) {
	    // Do something on success
	
		
	
		String csv = "/storage/sdcard0/project/GyroscopeValue.csv";
		FileWriter file_writer = new FileWriter(csv,true);
  		 
  			 
  
  				
		String s= c.get(Calendar.YEAR)+","+c.get(Calendar.MONTH)+","+c.get(Calendar.DATE)+","+c.get(Calendar.HOUR)+","+c.get(Calendar.MINUTE)+","+c.get(Calendar.SECOND)+","+ c.get(Calendar.MILLISECOND)+","+x + ","+y+","+z+"\n";
  				
  				file_writer.append(s);
  				file_writer.close();
	}
  				
  			
	}
public void writeToCsv(String x,String y,String z) throws IOException{
	Calendar c = Calendar.getInstance(); 
	File folder = new File(Environment.getExternalStorageDirectory() + "/project");
	boolean success = true;
	if (!folder.exists()) {
	    success = folder.mkdir();
	}
	if (success) {
	    // Do something on success
		String csv = "/storage/sdcard0/project/AccelerometerValue.csv";
		FileWriter file_writer = new FileWriter(csv,true);;
			 
				 

					
					String s= c.get(Calendar.YEAR)+","+c.get(Calendar.MONTH)+","+c.get(Calendar.DATE)+","+c.get(Calendar.HOUR)+","+c.get(Calendar.MINUTE)+","+c.get(Calendar.SECOND)+","+ c.get(Calendar.MILLISECOND)+","+x + ","+y+","+z+"\n";
					
					file_writer.append(s);
					file_writer.close();
		
	} 
	
	
	
}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		//runTcpServer();// test
		//if sensor is unreliable, return void
		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
		{
			return;
		}
		
		Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //TODO: get values
        	
        	//else it will output the Roll, Pitch and Yawn values
    		textView.setText("Acceleration X  :"+ Float.toString(event.values[0]) +"\n"+
    						   "Acceleration Y  :"+ Float.toString(event.values[1]) +"\n"+
    						   "Acceleration Z :"+ Float.toString(event.values[2]));
    		
    		
    		
    		try {
				writeToCsv(Float.toString(event.values[0]),Float.toString(event.values[1]),Float.toString(event.values[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
        }else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //TODO: get values
        	//else it will output the Roll, Pitch and Yawn values
    		tv.setText("Orientation X (Roll) :"+ Float.toString(event.values[0]) +"\n"+
    				   "Orientation Y (Pitch) :"+ Float.toString(event.values[1]) +"\n"+
    				   "Orientation Z (Yaw) :"+ Float.toString(event.values[2]));
    		
    		try {
				writeToCsvGy(Float.toString(event.values[0]),Float.toString(event.values[1]),Float.toString(event.values[2]));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		
	}
}