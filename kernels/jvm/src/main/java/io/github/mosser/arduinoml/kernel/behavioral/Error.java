package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.NamedElement;
import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthieu on 17/01/2018.
 */
public class Error implements Visitable {

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    private List<Action> actions = new ArrayList<Action>();
    private List<Sensor> sensors = new ArrayList<>();
    private List<SIGNAL> values = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor){
        this.sensors.add(sensor);
    }

    public void addValue(SIGNAL sig){
        this.values.add(sig);
    }

    public List<SIGNAL> getValues() {
        return values;
    }

    public void setValues(List<SIGNAL> values) {
        this.values = values;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

