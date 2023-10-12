import RPi.GPIO as GPIO
import time
import logging

from lib.solenoid_controller.base_solenoid_controller import BaseSolenoidController


class PiSolenoidController(BaseSolenoidController):
    def __init__(self, gpio_pin: int):
        self.gpio_pin = gpio_pin
        self.setup_gpio()

    def actuate(self) -> None:
        logging.info("Activating Solenoid!")

        GPIO.output(11, GPIO.HIGH)
        time.sleep(1)
        GPIO.output(11, GPIO.LOW)

    def cleanup(self) -> None:
        logging.info("GPIO Cleaned Up!")
        GPIO.cleanup()

    def setup_gpio(self) -> None:
        logging.info("GPIO Setup in OUT mode. Pin: " + str(self.gpio_pin))
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(self.gpio_pin, GPIO.OUT)
