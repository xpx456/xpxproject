// IGpioService.aidl
package android.os;

// Declare any non-default types here with import statements

interface IGpioService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
         int gpioWrite(int gpio, int value);
         int gpioRead(int gpio);
         int gpioDirection(int gpio, int direction, int value);
         int gpioRegKeyEvent(int gpio);
         int gpioUnregKeyEvent(int gpio);
         int gpioGetNumber();
}
