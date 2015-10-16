package todo;


import se.lth.cs.realtime.*;
import se.lth.cs.realtime.event.RTEventBuffer;
import done.AbstractWashingMachine;


public class SpinController extends PeriodicThread {
	private double speed;
	private AbstractWashingMachine mach;
	private SpinEvent currentEvent;
	private RTEventBuffer programMailbox;

	// TODO: add suitable attributes

	public SpinController(AbstractWashingMachine mach, double speed) {
		super((long) (1000/speed)); // TODO: replace with suitable period
		this.mach = mach;
		this.speed = speed;
	}

	public void perform() {
		if (!mailbox.isEmpty() || currentEvent == null) {
			currentEvent = (SpinEvent) mailbox.fetch();
		}
		
		if (currentEvent.getMode() == SpinEvent.SPIN_SLOW) {
			mach.setSpin(SpinEvent.SPIN_SLOW);
		}
	}
	
	public void addMailbox(RTEventBuffer mailbox) {
		programMailbox = mailbox;
	}
}
