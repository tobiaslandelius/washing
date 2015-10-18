package todo;

import done.AbstractWashingMachine;

public class WashingProgram0 extends WashingProgram {

	protected WashingProgram0(AbstractWashingMachine mach, double speed, TemperatureController tempController,
			WaterController waterController, SpinController spinController) {
		super(mach, speed, tempController, waterController, spinController);
	}

	@Override
	protected void wash() throws InterruptedException {
		throw new InterruptedException();
	}

}
