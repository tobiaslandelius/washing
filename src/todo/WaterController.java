package todo;


import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEventBuffer;
import done.AbstractWashingMachine;


public class WaterController extends PeriodicThread {
	private double speed;
	private AbstractWashingMachine mach;
	private WaterEvent currentEvent;
	private RTEventBuffer programMailbox;

	// TODO: add suitable attributes

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (10000/speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		if (!mailbox.isEmpty() || currentEvent == null) {
			currentEvent = (WaterEvent) mailbox.fetch();
		}
		if (currentEvent.getMode() == WaterEvent.WATER_FILL) {
			if (mach.getWaterLevel() < currentEvent.getLevel()) {
				mach.setFill(true);
				
			} else {
				mach.setFill(false);
				currentEvent = null;
				programMailbox.doPost(new AckEvent(this));
			}
		} else if (currentEvent.getMode() == WaterEvent.WATER_DRAIN) {
			mach.setDrain(true);
			while(mach.getWaterLevel() > 0) {}
			mach.setDrain(false);
			programMailbox.doPost(new AckEvent(this));
		}
	}

	public void addMailbox(RTEventBuffer mailbox) {
		programMailbox = mailbox;
	}
}
