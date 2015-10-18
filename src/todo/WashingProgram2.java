package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.RTThread;

public class WashingProgram2 extends WashingProgram {

	
	protected WashingProgram2(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	@Override
	protected void wash() throws InterruptedException {
		// Close Hatch
				myMachine.setLock(true);

				// Add water
				myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, .5));
				AckEvent ev = (AckEvent) mailbox.doFetch();
				
				// Switch on temp regulation
				myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 40));
				ev = (AckEvent) mailbox.doFetch();
				
				Thread.sleep((long) (15*60*1000 / mySpeed));
				
				// Switch off temp regulation
				myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0.0));
				ev = (AckEvent) mailbox.doFetch();
				
				// Drain water
				myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
				ev = (AckEvent) mailbox.doFetch();
				
				// Add water
				myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, .5));
				ev = (AckEvent) mailbox.doFetch();

				// Switch on temp regulation
				myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 30));
				ev = (AckEvent) mailbox.doFetch();

				// Wait 30 min
				Thread.sleep((long) (5 * 60 * 1000 / mySpeed));
				System.out.println("Sleep done...");

				// Switch off temp regulation
				myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0.0));
				ev = (AckEvent) mailbox.doFetch();
				
				// Drain water
				myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
				ev = (AckEvent) mailbox.doFetch();

				// Rinse 5 times
				for (int i = 0; i < 0; i++) {
					myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, 0.5));
					ev = (AckEvent) mailbox.doFetch();
					Thread.sleep((long) (5 * 60 * 1000 / mySpeed));
					myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_DRAIN, 0.0));
					ev = (AckEvent) mailbox.doFetch();
				}
				
				mySpinController.putEvent(new SpinEvent(this, SpinEvent.SPIN_SLOW));
				ev = (AckEvent) mailbox.doFetch();
			
				myMachine.setLock(false);
				System.out.println("Done!");
	}

}
