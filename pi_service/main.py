import RPi.GPIO as GPIO
import time


def main():
    print("Activating Volleyball Auto Setter!")
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(11, GPIO.OUT)
    GPIO.output(11, GPIO.HIGH)
    time.sleep(1)
    GPIO.output(11, GPIO.LOW)
    GPIO.cleanup()


if __name__ == "__main__":
    main()
