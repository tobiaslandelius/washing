package todo;

import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEventBuffer;
import done.AbstractWashingMachine;

public class SpinController extends PeriodicThread {

	private double speed;
	private AbstractWashingMachine mach;
	private SpinEvent currentEvent;

	private long spinTime;
	private static final int SPIN_LEFT = 1;
	private static final int SPIN_RIGHT = 2;
	private int direction;
	private int counter;
	
	private boolean spinning;
	private RTThread sourceThread;

	// TODO: add suitable attributes
	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000 / speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		currentEvent = (SpinEvent) mailbox.tryFetch();

		if (currentEvent != null) {
			if (currentEvent.getMode() == SpinEvent.SPIN_SLOW) {
				spinning = true;
				spinTime = currentEvent.getMillis();
				direction = SPIN_LEFT;
				counter = 0;
				mach.setSpin(direction);
				sourceThread = ((RTThread) currentEvent.getSource());
			} else if (currentEvent.getMode() == SpinEvent.SPIN_OFF) {
				mach.setSpin(SpinEvent.SPIN_OFF);
				spinning = false;
				((RTThread) currentEvent.getSource()).putEvent(new AckEvent(this));
			}
		}
		
		if (spinning) {
			if ((System.currentTimeMillis() - spinTime) > 60000 / speed) {
				direction = (direction == SPIN_LEFT) ? SPIN_RIGHT : SPIN_LEFT;
				spinTime = System.currentTimeMillis();
				mach.setSpin(direction);
				counter++;
			}
			if (counter == 5) {
				mach.setSpin(SpinEvent.SPIN_OFF);
				spinning = false;
				sourceThread.putEvent(new AckEvent(this));
			}
		}
	}
}
