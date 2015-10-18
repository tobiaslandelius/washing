package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;
import se.lth.cs.realtime.RTThread;
import se.lth.cs.realtime.event.RTEvent;
import se.lth.cs.realtime.event.RTEventBuffer;

public class TemperatureController extends PeriodicThread {

	private AbstractWashingMachine mach;
	private double speed;
	private TemperatureEvent currentEvent;

	private boolean stabilizing;
	private double targetTemp;
	private boolean ackSent;
	private RTThread sourceThread;

	// TODO: add suitable attributes

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		System.out.println("Tempcontrol perform()");
		currentEvent = (TemperatureEvent) mailbox.tryFetch();

		if (currentEvent != null) {
			if (currentEvent.getMode() == TemperatureEvent.TEMP_SET) {
				stabilizing = true;
				ackSent = false;
				targetTemp = currentEvent.getTemperature();
				sourceThread = (RTThread) currentEvent.getSource();
			} else if (currentEvent.getMode() == TemperatureEvent.TEMP_IDLE) {
				mach.setHeating(false);
				stabilizing = false;
				((RTThread) currentEvent.getSource()).putEvent(new AckEvent(this));
			}
		}

		if (stabilizing) {
			if (targetTemp - 1.5 > mach.getTemperature()) {
				mach.setHeating(true);
			} else {
				mach.setHeating(false);
				if (!ackSent) {
					sourceThread.putEvent(new AckEvent(this));
					ackSent = true;
				}
			}
		}
	}
}
