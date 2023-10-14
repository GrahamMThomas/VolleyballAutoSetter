# Purpose of this class is to support development within Windows as GPIO is not available
import time
import logging
from appsettings.app_name import APP_NAME
from lib.solenoid_controller.base_solenoid_controller import BaseSolenoidController


class TestSolenoidController(BaseSolenoidController):
    def __init__(self):
        self.logger = logging.getLogger(APP_NAME)
        self.logger.info("Test Solenoid Initialized!")

    def actuate(self) -> None:
        self.logger.info("Test Solenoid Actuated.")
        time.sleep(1)
        self.logger.info("Test Solenoid Retract.")

    def cleanup(self):
        self.logger.info("Test GPIO Cleaned Up.")
