package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEventBuffer;
import done.AbstractWashingMachine;

public class WaterController extends PeriodicThread {
	private double speed;
	private AbstractWashingMachine mach;
	private WaterEvent currentEvent;
	
	private boolean filling;
	private double targetWaterLevel;
	private RTThread sourceThread;
	
	private boolean draining;

	// TODO: add suitable attributes

	public WaterController(AbstractWashingMachine mach, double speed) {
		super((long) (10000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		currentEvent = (WaterEvent) mailbox.tryFetch();

		if (currentEvent != null ) {
			if (currentEvent.getMode() == WaterEvent.WATER_FILL) {
				filling = true;
				mach.setFill(true);
				targetWaterLevel = currentEvent.getLevel();
				sourceThread = ((RTThread) currentEvent.getSource());
			} else if (currentEvent.getMode() == WaterEvent.WATER_DRAIN) {
				draining = true;
				mach.setDrain(true);
			} else if (currentEvent.getMode() == WaterEvent.WATER_IDLE) {
				mach.setFill(false);
				mach.setDrain(false);
				filling = false;
				draining = false;
				((RTThread) currentEvent.getSource()).putEvent(new AckEvent(this));
			}
		}

		if (filling) {
			if (mach.getWaterLevel() > targetWaterLevel) {
				filling = false;
				mach.setFill(false);
				sourceThread.putEvent(new AckEvent(this));
			}
		}
		
		if (draining) {
			if (mach.getWaterLevel() == 0) {
				draining = false;
				mach.setDrain(false);
				sourceThread.putEvent(new AckEvent(this));
			}
		}
	}
}
