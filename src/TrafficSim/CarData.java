/* Project name: CMSC335Project3
 * File name: Main.java
 * Author: Timothy Eckart
 * Date: 10 dec 2024
 * Purpose: This class is used Car data value to be stored into
 * the carTable
 *
 */

package TrafficSim;


public class CarData {
    private String carName;
    private String light1Time;
    private String light2Time;
    private String light3Time;

    // Constructor, Getters, and Setters
    public CarData(String carName, String light1Time, String light2Time, String light3Time) {
        this.carName = carName;
        this.light1Time = light1Time;
        this.light2Time = light2Time;
        this.light3Time = light3Time;
    }

    public String getCarName() { return carName; }
    public String getLight1Time() { return light1Time; }
    public String getLight2Time() { return light2Time; }
    public String getLight3Time() { return light3Time; }

    public void setLight1Time(String light1Time) { this.light1Time = light1Time; }
    public void setLight2Time(String light2Time) { this.light2Time = light2Time; }
    public void setLight3Time(String light3Time) { this.light3Time = light3Time; }
}




