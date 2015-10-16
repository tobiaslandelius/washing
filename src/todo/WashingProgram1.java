package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.event.RTEvent;

public class WashingProgram1 extends WashingProgram {

	protected WashingProgram1(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
		waterController.addMailbox(mailbox);
		tempController.addMailbox(mailbox);
		spinController.addMailbox(mailbox);
	}

	@Override
	protected void wash() throws InterruptedException {
		// TODO Auto-generated method stub

		// Close Hatch
		myMachine.setLock(true);

		// Add water
		myWaterController.putEvent(new WaterEvent(this, WaterEvent.WATER_FILL, .5));
		AckEvent ev = (AckEvent) mailbox.doFetch();

		// Switch on temp regulation
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_SET, 25));
		ev = (AckEvent) mailbox.doFetch();

		// Wait 30 min
		System.out.println("innan");
		Thread.sleep((long) (5 * 60 * 1000 / mySpeed));
		System.out.println("efter");

		// Switch off temp regulation
		myTempController.putEvent(new TemperatureEvent(this, TemperatureEvent.TEMP_IDLE, 0.0));

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
		

		System.out.println("Done!");
	}

}
