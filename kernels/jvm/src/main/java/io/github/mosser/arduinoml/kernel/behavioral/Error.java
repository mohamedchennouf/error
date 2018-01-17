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
    private Sensor sensor;
    private SIGNAL value;

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

