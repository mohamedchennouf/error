sensor "button" onPin 9
actuator "led" pin 12
actuator "buzzer" pin 11

state "ledstate" means "led" becomes "high"
state "ledstate" means "buzzer" becomes "low"
state "buzzstate" means "buzzer" becomes "high"
state "buzzstate" means "led" becomes "low"
state "off" means led becomes low
state "off" means "buzzer" becomes low

initial "off"

from "ledstate" to "buzzstate" when "button" becomes "high"
from "buzzstate" to "off" when "button" becomes "high"
from off to ledstate when button becomes high

export "Switch!"