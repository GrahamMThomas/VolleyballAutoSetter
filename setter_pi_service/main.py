import logging

import atexit
import sys
from appsettings.app_name import APP_NAME
from flask import Flask
import yaml
from lib.os_detector import is_raspberrypi
from lib.solenoid_controller.base_solenoid_controller import BaseSolenoidController

# Cannot import GPIO Library on windows
if is_raspberrypi():
    from lib.solenoid_controller.pi_solenoid_controller import PiSolenoidController
else:
    from lib.solenoid_controller.test_solenoid_controller import TestSolenoidController


# Endpoints

app = Flask(APP_NAME)


@app.route("/health")
def get_health():
    return "OK"


@app.route("/actuate")
def actuate():
    solenoid_controller.actuate()
    return "Done."


@atexit.register
def do_cleanup():
    solenoid_controller.cleanup()


# Startup
def load_config() -> dict:
    with open("appsettings/appsettings.yml", "r") as f:
        return yaml.safe_load(f)


def setup_logging() -> None:
    formatter = logging.Formatter(
        fmt="[%(levelname)s] %(message)s <%(module)s:%(lineno)d>"
    )
    stdout = logging.StreamHandler(stream=sys.stdout)
    stdout.formatter = formatter

    logger = logging.getLogger(APP_NAME)
    logger.setLevel(logging.DEBUG)
    logger.addHandler(stdout)
    logger.info("Logging Setup!")


if __name__ == "__main__":
    appsettings = load_config()
    setup_logging()

    if is_raspberrypi():
        solenoid_controller = PiSolenoidController(appsettings["solenoid_controller"])
    else:
        solenoid_controller = TestSolenoidController()

    app.run(host=appsettings["host"], port=appsettings["port"])
