# Purpose of this class is to support development within Windows as GPIO is not available
import time
import logging

from lib.solenoid_controller.base_solenoid_controller import BaseSolenoidController


class TestSolenoidController(BaseSolenoidController):
    def __init__(self):
        logging.info("Test Solenoid Initialized!")

    def actuate(self) -> None:
        logging.info("Test Solenoid Actuated!")
        time.sleep(1)

    def cleanup(self):
        logging.info("Test GPIO Cleaned Up!")
