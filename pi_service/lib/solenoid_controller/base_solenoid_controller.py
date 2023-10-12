from abc import ABC, abstractmethod


class BaseSolenoidController(ABC):
    @abstractmethod
    def actuate(self) -> None:
        pass

    @abstractmethod
    def cleanup(self) -> None:
        pass
