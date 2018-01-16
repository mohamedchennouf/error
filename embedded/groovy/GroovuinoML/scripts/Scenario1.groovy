sensor "button" onPin 9
actuator "led" pin 12
actuator "buzzer" pin 11

state "on" means "led" becomes "high"
state "on" means "buzzer" becomes "high"
state "off" means led becomes low
state "off" means "buzzer" becomes low

initial "off"

from "on" to "off" when "button" becomes "high"
from off to on when button becomes high

export "Switch!"