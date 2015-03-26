package ioio.examples.ultrasonic;

import ioio.lib.api.PulseInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PulseInput.PulseMode;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.AbstractIOIOActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class IOIOUltrasonicSensorActivity extends AbstractIOIOActivity {
	/* ultrasonic sensor */
	private ProgressBar progressBar1_;
	private TextView textView2_;
	private int echoSeconds;
	private int echoDistanceCm;

	/**
	 * Called upon creation for initialization
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		/* ultrasonic sensor */
		progressBar1_ = (ProgressBar) findViewById(R.id.progressBar1);
		textView2_ = (TextView) findViewById(R.id.textView2);
	}

	/**
	 * Primary thread...runs until interrupted
	 */
	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
		/**
		 * define pin
		 */
		/* ultrasonic sensor */
		private DigitalOutput triggerPin_;
		private PulseInput echoPin_;

		/**
		 * Called every time a connection with IOIO has been established. (opens
		 * pins)
		 * 
		 * @throws ConnectionLostException
		 *             (if IOIO connection is lost)
		 */

		public void setup() throws ConnectionLostException {
			try {
				/* ultrasonic sensor */
				echoPin_ = ioio_.openPulseInput(6, PulseMode.POSITIVE);
				triggerPin_ = ioio_.openDigitalOutput(7);

			} catch (ConnectionLostException e) {
				throw e;
			}
		}

		/**
		 * Loop section
		 */

		public void loop() throws ConnectionLostException {
			try {
				// read HC-SR04 ultrasonic sensor
				triggerPin_.write(false);
				sleep(5);
				triggerPin_.write(true);
				sleep(1);
				triggerPin_.write(false);
				echoSeconds = (int) (echoPin_.getDuration() * 1000 * 1000);
				echoDistanceCm = echoSeconds / 29 / 2;
				/* update UI */
				updateViews();

				sleep(20);
			} catch (InterruptedException e) {
				ioio_.disconnect();

			} catch (ConnectionLostException e) {
				throw e;

			}
		}
	}

	/**
	 * A method to create our IOIO thread.
	 */

	@Override
	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
		return new IOIOThread();
	}

	private void updateViews() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textView2_.setText(String.valueOf(echoDistanceCm));
				progressBar1_.setProgress(echoDistanceCm);
			}
		});
	}
}
