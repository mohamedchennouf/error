sensor "button1" onPin 9
sensor "button2" onPin 10
actuator "led" pin 8

state "on" means "led" becomes "high"
state "off" means "led" becomes "low"

initial "off"

error 8 when "button1" becomes "high" and "button2" becomes "high"

from "on" to "off" when "button1" becomes "high"
from "off" to on when "button1" becomes "high"

export "Switch!"