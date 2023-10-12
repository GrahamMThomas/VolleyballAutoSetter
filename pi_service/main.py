import os
import yaml
import logging
from lib.os_detector import is_raspberrypi

# Cannot import GPIO Library on windows
if is_raspberrypi():
    from lib.solenoid_controller.pi_solenoid_controller import PiSolenoidController
else:
    from lib.solenoid_controller.test_solenoid_controller import TestSolenoidController


def main():
    pass


def load_config() -> dict:
    with open("appsettings/appsettings.yml", "r") as f:
        return yaml.safe_load(f)


def setup_logging() -> None:
    logging.root.setLevel(logging.NOTSET)
    formatter = logging.Formatter(
        fmt="[%(levelname)s] %(message)s <%(module)s:%(lineno)d>"
    )
    stdout = logging.StreamHandler()
    stdout.formatter = formatter
    logging.root.addHandler(stdout)


if __name__ == "__main__":
    appsettings = load_config()
    setup_logging()

    if is_raspberrypi():
        solenoid_controller = PiSolenoidController(appsettings["solenoid_controller"])
    else:
        solenoid_controller = TestSolenoidController()

    main()
