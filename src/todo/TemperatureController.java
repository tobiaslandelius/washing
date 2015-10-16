package todo;

import done.AbstractWashingMachine;
import se.lth.cs.realtime.PeriodicThread;
import se.lth.cs.realtime.event.RTEventBuffer;

public class TemperatureController extends PeriodicThread {
	private AbstractWashingMachine mach;
	private double speed;
	private TemperatureEvent currentEvent;
	private RTEventBuffer programMailbox;

	// TODO: add suitable attributes

	public TemperatureController(AbstractWashingMachine mach, double speed) {
		super((long) (10000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		boolean tempReached = false;
		
		if (!mailbox.isEmpty() || currentEvent == null) {
			currentEvent = (TemperatureEvent) mailbox.fetch();
			if (currentEvent.getMode() == TemperatureEvent.TEMP_SET) {
				tempReached = false;
			}
		}

		if (currentEvent.getMode() == TemperatureEvent.TEMP_SET) {
			if (currentEvent.getTemperature() - 1.5 > mach.getTemperature()) {
				mach.setHeating(true);
			} else {
				if (!tempReached) {
					tempReached = true;
					programMailbox.doPost(new AckEvent(this));
				}
				mach.setHeating(false);
			}
		} else if (currentEvent.getMode() == TemperatureEvent.TEMP_IDLE) {
			mach.setHeating(false);
		}
	}

	public void addMailbox(RTEventBuffer mailbox) {
		programMailbox = mailbox;
	}
}
