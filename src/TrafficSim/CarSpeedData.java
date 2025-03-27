/* Project name: 
 * File name: Main.java
 * Author: Timothy Eckart
 * Date: 10 dec 2024
 * Purpose: This class is used Car data value to be stored into
 * the mphTable
 *
 */

package TrafficSim;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarSpeedData {
    private final StringProperty carID;
    private final StringProperty mph;

    public CarSpeedData(String carID, String mph) {
        this.carID = new SimpleStringProperty(carID);
        this.mph = new SimpleStringProperty(mph);
    }

    public String getCarID() {
        return carID.get();
    }

    public void setCarID(String carID) {
        this.carID.set(carID);
    }

    public StringProperty carIDProperty() {
        return carID;
    }

    public String getMph() {
        return mph.get();
    }

    public void setMph(String mph) {
        this.mph.set(mph);
    }

    public StringProperty mphProperty() {
        return mph;
    }
}
