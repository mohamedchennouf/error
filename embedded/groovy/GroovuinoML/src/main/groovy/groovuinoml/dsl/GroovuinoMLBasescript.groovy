package main.groovy.groovuinoml.dsl

import java.util.List;

import io.github.mosser.arduinoml.kernel.behavioral.Action
import io.github.mosser.arduinoml.kernel.behavioral.State
import io.github.mosser.arduinoml.kernel.behavioral.Error
import io.github.mosser.arduinoml.kernel.structural.Actuator
import io.github.mosser.arduinoml.kernel.structural.Sensor
import io.github.mosser.arduinoml.kernel.structural.SIGNAL

abstract class GroovuinoMLBasescript extends Script {
	// sensor "name" pin n
	def sensor(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n) },
		onPin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createSensor(name, n)}]
	}
	
	// actuator "name" pin n
	def actuator(String name) {
		[pin: { n -> ((GroovuinoMLBinding)this.getBinding()).getGroovuinoMLModel().createActuator(name, n) }]
	}

	// state "name" means actuator becomes signal [and actuator becomes signal]*n
	def state(String name) {
		List<Action> actions = new ArrayList<Action>()
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createState(name, actions)
		// recursive closure to allow multiple and statements
		def closure
		closure = { actuator -> 
			[becomes: { signal ->
				Action action = new Action()
				action.setActuator(actuator instanceof String ? (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable(actuator) : (Actuator)actuator)
				action.setValue(signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				actions.add(action)
				[and: closure]
			}]
		}

		[means: closure]
	}
	
	// initial state
	def initial(state) {
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setInitialState(state instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state) : (State)state)
	}

	def error(Integer code){
		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().setLedError()
		List<Action> actions = new ArrayList<Action>()
		List<Sensor> sensors = new ArrayList<Sensor>()
		List<SIGNAL> signals = new ArrayList<SIGNAL>()
		Action action = new Action();
		action.setActuator( (Actuator)((GroovuinoMLBinding)this.getBinding()).getVariable("errorLed"))
		action.setValue(SIGNAL.HIGH)
		actions.add(action);

		((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createError(code, actions, sensors, signals)
		//((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createError(code, actions)
		def closure
		closure = { sensor ->
			[becomes: { signal ->
						Error err = (Error) ((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().getError(code);
						((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().removeError(err)
						if(sensor instanceof  String){
							err.addSensor(((GroovuinoMLBinding) this.getBinding()).getVariable(sensor))
						}
						else{
							err.addSensor((Sensor) sensor)
						}
						if(signal instanceof  String){
							err.addValue(((GroovuinoMLBinding) this.getBinding()).getVariable(signal))
						}
						else{
							err.addValue((SIGNAL) signal)
						}
						((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().addError(err)
						//sensor instanceof String ? sensors.add((Sensor) ((GroovuinoMLBinding) this.getBinding()).getVariable(sensor)) : sensors.add((Sensor) sensor)
						//signal instanceof String ? signals.add((SIGNAL) ((GroovuinoMLBinding) this.getBinding()).getVariable(signal)) : signals.add((SIGNAL) signal)
				[and: closure]
			}]
		}
		[when: closure]
		//System.out.println(sensors.size() + " " + signals.size());
		//((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createError(code, actions, sensors, signals)

	}
	
	// from state1 to state2 when sensor becomes signal
	def from(state1) {
		[to: { state2 -> 
			[when: { sensor ->
				[becomes: { signal -> 
					((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().createTransition(
						state1 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state1) : (State)state1, 
						state2 instanceof String ? (State)((GroovuinoMLBinding)this.getBinding()).getVariable(state2) : (State)state2, 
						sensor instanceof String ? (Sensor)((GroovuinoMLBinding)this.getBinding()).getVariable(sensor) : (Sensor)sensor, 
						signal instanceof String ? (SIGNAL)((GroovuinoMLBinding)this.getBinding()).getVariable(signal) : (SIGNAL)signal)
				}]
			}]
		}]
	}
	
	// export name
	def export(String name) {
		println(((GroovuinoMLBinding) this.getBinding()).getGroovuinoMLModel().generateCode(name).toString())
	}
	
	// disable run method while running
	int count = 0
	abstract void scriptBody()
	def run() {
		if(count == 0) {
			count++
			scriptBody()
		} else {
			println "Run method is disabled"
		}
	}
}
