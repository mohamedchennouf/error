package main.groovy.groovuinoml.dsl;

import java.util.*;

import groovy.lang.Binding;
import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.Action;
import io.github.mosser.arduinoml.kernel.behavioral.Error;
import io.github.mosser.arduinoml.kernel.behavioral.State;
import io.github.mosser.arduinoml.kernel.behavioral.Transition;
import io.github.mosser.arduinoml.kernel.generator.ToWiring;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.Brick;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

public class GroovuinoMLModel {
	private List<Brick> bricks;
	private List<State> states;
	private List<Error> errors;
	private State initialState;
	
	private Binding binding;
	
	public GroovuinoMLModel(Binding binding) {
		this.bricks = new ArrayList<Brick>();
		this.states = new ArrayList<State>();
		this.errors = new ArrayList<>();
		this.binding = binding;
	}
	
	public void createSensor(String name, Integer pinNumber) {
		Sensor sensor = new Sensor();
		sensor.setName(name);
		sensor.setPin(pinNumber);
		this.bricks.add(sensor);
		this.binding.setVariable(name, sensor);
//		System.out.println("> sensor " + name + " on pin " + pinNumber);
	}

	public void setLedError(){
		Actuator errorLed = new Actuator();
		errorLed.setName("errorLed");
		errorLed.setPin(12);
		this.bricks.add(errorLed);
		this.binding.setVariable("errorLed", errorLed);
	}

	public void createError(Integer code, List<Action> actions, List<Sensor> sensor, List<SIGNAL> signal){
		Error err = new Error();
		err.setCode(code);
		err.setActions(actions);
		err.setSensors(sensor);
		err.setValues(signal);
		this.errors.add(err);

	}
	
	public void createActuator(String name, Integer pinNumber) {
		Actuator actuator = new Actuator();
		actuator.setName(name);
		actuator.setPin(pinNumber);
		this.bricks.add(actuator);
		this.binding.setVariable(name, actuator);
	}
	
	public void createState(String name, List<Action> actions) {
		State state = new State();
		state.setName(name);
		state.setActions(actions);
		this.states.add(state);
		this.binding.setVariable(name, state);
	}
	
	public void createTransition(State from, State to, Sensor sensor, SIGNAL value) {
		Transition transition = new Transition();
		transition.setNext(to);
		transition.setSensor(sensor);
		transition.setValue(value);
		from.setTransition(transition);
	}
	
	public void setInitialState(State state) {
		this.initialState = state;
	}

	public Error getError(Integer code){
		for(Error e : this.errors){
			if(e.getCode() == code){
				return e;
			}
		}
		return null;
	}
	public void addError(Error e){
		this.errors.add(e);
	}

	public void removeError(Error e){
		this.errors.remove(e);
	}
	@SuppressWarnings("rawtypes")
	public Object generateCode(String appName) {
		App app = new App();
		app.setName(appName);
		app.setBricks(this.bricks);
		app.setStates(this.states);
		app.setInitial(this.initialState);
		app.setErrors(this.errors);
		Visitor codeGenerator = new ToWiring();
		app.accept(codeGenerator);
		
		return codeGenerator.getResult();
	}
}
