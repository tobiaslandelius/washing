package todo;

import done.*;

public class WashingController implements ButtonListener {
	private double theSpeed;
	private AbstractWashingMachine theMachine;
	private TemperatureController tempControl;
	private SpinController spinControl;
	private WaterController waterControl;
	
	private WashingProgram currentProgram;

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
		System.out.println("Temp: " +tempControl.getPriority());
		System.out.println("Spin: " +spinControl.getPriority());
		System.out.println("Water: " +waterControl.getPriority());

	}

	public void processButton(int theButton) {
		// TODO: implement this method
		switch (theButton) {
		case 1:
			currentProgram = new WashingProgram1(theMachine, theSpeed, tempControl, waterControl, spinControl);
			currentProgram.start();
			break;
		case 2:
			currentProgram = new WashingProgram2(theMachine, theSpeed, tempControl, waterControl, spinControl);
			currentProgram.start();
			break;
		case 3:
			currentProgram = new WashingProgram3(theMachine, theSpeed, tempControl, waterControl, spinControl);
			currentProgram.start();
			break;
		case 0:
//			tempControl.terminate();
//			spinControl.interrupt();
//			waterControl.interrupt();
//			currentProgram.interrupt();
			
			WashingProgram wp = new WashingProgram0(theMachine, theSpeed, tempControl, waterControl, spinControl);
			currentProgram.terminate();
//			wp.start();
			break;
		}
	}
}
