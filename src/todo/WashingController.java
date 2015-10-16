package todo;

import done.*;

public class WashingController implements ButtonListener {
	private double theSpeed;
	private AbstractWashingMachine theMachine;
	private TemperatureController tempControl;
	private SpinController spinControl;
	private WaterController waterControl;

	// TODO: add suitable attributes

	public WashingController(AbstractWashingMachine theMachine, double theSpeed) {
		// TODO: implement this constructor
		this.theMachine = theMachine;
		this.theSpeed = theSpeed;
		tempControl = new TemperatureController(theMachine, theSpeed);
		spinControl = new SpinController(theMachine, theSpeed);
		waterControl = new WaterController(theMachine, theSpeed);
		tempControl.start();
		spinControl.start();
		waterControl.start();

	}

	public void processButton(int theButton) {
		// TODO: implement this method
		WashingProgram wp = null;
		switch (theButton) {
		case 1:
			wp = new WashingProgram1(theMachine, theSpeed, tempControl, waterControl, spinControl);
			break;
		case 3:
			wp = new WashingProgram3(theMachine, theSpeed, tempControl, waterControl, spinControl);
			break;
		}
		wp.start();
	}
}
