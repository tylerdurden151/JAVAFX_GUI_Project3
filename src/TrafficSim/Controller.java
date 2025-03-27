/* Project name: 
 * File name: Main.java
 * Author: Timothy Eckart
 * Date: 10 dec 2024
 * Purpose: This is the main controller for the Scene.
 * Allows the user to perform multiple options to
 * Start, Stop, Pause, Resume, record, and add additional items
 * onto the simulator.
 *
 */

package TrafficSim;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import TrafficSim.CarData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This is the Controller class for the FXML files and prompts Traffic Simulator
 * selection.
 */
public class Controller {
    @FXML
    private ImageView car1, car2, car3;
    @FXML
    private Circle light1TopRed, light1TopYellow, light1TopGreen;
    @FXML
    private Circle light2TopRed, light2TopYellow, light2TopGreen;
    @FXML
    private Circle light3TopRed, light3TopYellow, light3TopGreen;

    @FXML
    private Label clockLabel;
    @FXML
    private TableView<CarData> carTable;

    @FXML
    private TableColumn<CarData, String> carNameColumn;
    @FXML
    private TableColumn<CarData, String> light1TimeColumn;
    @FXML
    private TableColumn<CarData, String> light2TimeColumn;
    @FXML
    private TableColumn<CarData, String> light3TimeColumn;

    private ObservableList<CarData> carDataList = FXCollections.observableArrayList();

    // Time tracking variables
    private long startTime = System.currentTimeMillis();
    private long car1Light1Time = 0, car1Light2Time = 0, car1Light3Time = 0;
    private long car2Light1Time = 0, car2Light2Time = 0, car2Light3Time = 0;
    private long car3Light1Time = 0, car3Light2Time = 0, car3Light3Time = 0;
    private Timeline carTimeline, lightTimeline;
    private boolean recordButtonPressed = false;
    private long car1LastLightTime = System.currentTimeMillis();
    private long car2LastLightTime = System.currentTimeMillis();
    private long car3LastLightTime = System.currentTimeMillis();
    private boolean car1IsMoving = false;
    private boolean car2IsMoving = false;
    private boolean car3IsMoving = false;
    private List<Timeline> extraCarTimelines = new ArrayList<>();

    @FXML
    private Button playButton, pauseButton, stopButton, resumeButton, recordButton;;
    @FXML
    private ImageView light1Buttom, light2Buttom, light3Buttom;
    @FXML
    private ImageView extraCar1, extraCar2, extraCar3, extraCar4, extraCar5, extraCar6;
    @FXML
    private Button addLightButton;

    @FXML
    private ImageView extraLight1Button;
    @FXML
    private Button addCarButton;

    @FXML
    private TableView<CarSpeedData> mphTable;
    @FXML
    private TableColumn<CarSpeedData, String> carIDColumn;
    @FXML
    private TableColumn<CarSpeedData, String> mphColumn;
    @FXML
    private ObservableList<CarSpeedData> mphDataList = FXCollections.observableArrayList();

    /** Method to handle Start Button, which starts the simulator
     * */
    @FXML
    public void handleStart() {
        startCarAnimation();
        startTrafficLightAnimation();
    }

    /** Method to handle Pause Button, which pauses the simulator
     * */
    @FXML
    public void handlePause() {
        if (carTimeline != null) {
            carTimeline.pause(); // Pause main car animations
        }
        if (lightTimeline != null) {
            lightTimeline.pause(); // Pause traffic light animations
        }
        for (Timeline timeline : extraCarTimelines) {
            timeline.pause(); // Pause extra car animations
        }
    }
    /** Method to handle Stop Button, which close the simulator
     * */
    @FXML
    public void handleStop() {
        System.exit(0); // Exits the program
    }
    /** Method to handle Resume Button, which resume the simulator from Pause
     * */

    @FXML
    public void handleResume() {
        if (carTimeline != null) {
            carTimeline.play(); // Resume main car animations
        }
        if (lightTimeline != null) {
            lightTimeline.play(); // Resume traffic light animations
        }
        for (Timeline timeline : extraCarTimelines) {
            timeline.play(); // Resume extra car animations
        }
    }

    /** Method to handle Record Button, which starts the second recording
     * */
    @FXML
    public void handleRecord() {
        // Reset all times
        car1Light1Time = car1Light2Time = car1Light3Time = 0;
        car2Light1Time = car2Light2Time = car2Light3Time = 0;
        car3Light1Time = car3Light2Time = car3Light3Time = 0;

        // Clear the table data
        carDataList.clear();
        carTable.setItems(carDataList);
        carTable.refresh();

        // Enable recording
        recordButtonPressed = true;

        // Restart the clock for new records
        startTime = System.currentTimeMillis();
    }
    /** Method to handle Add Cars Button
     * */
    @FXML
    private void handleAddCarsButton() {
        // Keep track of all cars (main and extra)
        List<ImageView> allCars = new ArrayList<>();
        allCars.add(car1);
        allCars.add(car2);
        allCars.add(car3);

        // Add extra cars sequentially
        if (!extraCar1.isVisible()) {
            extraCar1.setVisible(true);
            allCars.add(extraCar1);
            startExtraCarAnimation(extraCar1, allCars);
        } else if (!extraCar2.isVisible()) {
            extraCar2.setVisible(true);
            allCars.add(extraCar2);
            startExtraCarAnimation(extraCar2, allCars);
        } else if (!extraCar3.isVisible()) {
            extraCar3.setVisible(true);
            allCars.add(extraCar3);
            startExtraCarAnimation(extraCar3, allCars);
        } else if (!extraCar4.isVisible()) {
            extraCar4.setVisible(true);
            allCars.add(extraCar4);
            startExtraCarAnimation(extraCar4, allCars);
        } else if (!extraCar5.isVisible()) {
            extraCar5.setVisible(true);
            allCars.add(extraCar5);
            startExtraCarAnimation(extraCar5, allCars);
        } else if (!extraCar6.isVisible()) {
            extraCar6.setVisible(true);
            allCars.add(extraCar6);
            startExtraCarAnimation(extraCar6, allCars);
        }
    }
    /** Method to handle add Light Button, which add intersections
     * */
    @FXML
    private void handleAddLightButton() {
        if (!extraLight1Button.isVisible()) {
            extraLight1Button.setVisible(true); // Make the extra light visible
        }
    }

    /** Method to handle the setting of all lights
     * */
    @FXML
    public void initialize() {
        // Set initial visibility for Light 1
        light1TopRed.setVisible(true);
        light1TopYellow.setVisible(false);
        light1TopGreen.setVisible(false);

        // Set initial visibility for Light 2
        light2TopRed.setVisible(false);
        light2TopYellow.setVisible(false);
        light2TopGreen.setVisible(true);

        // Set initial visibility for Light 3
        light3TopRed.setVisible(true);
        light3TopYellow.setVisible(false);
        light3TopGreen.setVisible(false);
        // Bind columns to CarData properties
        carNameColumn.setCellValueFactory(new PropertyValueFactory<>("carName"));
        light1TimeColumn.setCellValueFactory(new PropertyValueFactory<>("light1Time"));
        light2TimeColumn.setCellValueFactory(new PropertyValueFactory<>("light2Time"));
        light3TimeColumn.setCellValueFactory(new PropertyValueFactory<>("light3Time"));
        carTable.setItems(carDataList);
        // Start the clock if applicable
        startClock();
        carIDColumn.setCellValueFactory(new PropertyValueFactory<>("carID"));
        mphColumn.setCellValueFactory(new PropertyValueFactory<>("mph"));
        // Add initial car speed data
        mphDataList.add(new CarSpeedData("Car 1", "0 MPH"));
        mphDataList.add(new CarSpeedData("Car 2", "0 MPH"));
        mphDataList.add(new CarSpeedData("Car 3", "0 MPH"));
        mphTable.setItems(mphDataList);
    }
    /** Method to handle the current time clock
     * */
    private void startClock() {
        // Define the time format (HH:MM:SS)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Create a Timeline to update the clock every second
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Get the current time
            String currentTime = LocalTime.now().format(timeFormatter);
            // Update the clockLabel with the current time
            clockLabel.setText(currentTime);
        }));

        // Set the Timeline to run indefinitely
        clock.setCycleCount(Timeline.INDEFINITE);
        // Start the clock
        clock.play();
    }
    /** Method to handle the movement animation of car1, car2, & car3
     * to stop at traffic lights
     * */
    private void startCarAnimation() {
        double[] car1PrevX = {car1.getLayoutX()};
        long[] car1PrevTime = {System.currentTimeMillis()};

        double[] car2PrevX = {car2.getLayoutX()};
        long[] car2PrevTime = {System.currentTimeMillis()};

        double[] car3PrevX = {car3.getLayoutX()};
        long[] car3PrevTime = {System.currentTimeMillis()};
        carTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // Car 1 logic

            moveCar(car1,
                    light1TopRed, light1TopYellow, light1TopGreen, 8.0, 28.0, 60.0,
                    light2TopRed, light2TopYellow, light2TopGreen, 235.0, 262.0, 305.0,
                    light3TopRed, light3TopYellow, light3TopGreen, 446.0, 480.0, 548.0);
            updateCarSpeed(mphDataList.get(0), car1, car1PrevX, car1PrevTime);
            checkCarPassages(car1, "Car 1", 8, 235, 446, car1LastLightTime, 1);

            // Car 2 logic
            moveCar(car2,
                    light1TopRed, light1TopYellow, light1TopGreen, 8.0, 28.0, 60.0,
                    light2TopRed, light2TopYellow, light2TopGreen, 235.0, 262.0, 305.0,
                    light3TopRed, light3TopYellow, light3TopGreen, 446.0, 480.0, 548.0);
            updateCarSpeed(mphDataList.get(1), car2, car2PrevX, car2PrevTime);
            checkCarPassages(car2, "Car 2", 8, 235, 446, car2LastLightTime, 2);

            // Car 3 logic
            moveCar(car3,
                    light1TopRed, light1TopYellow, light1TopGreen, 8.0, 28.0, 60.0,
                    light2TopRed, light2TopYellow, light2TopGreen, 235.0, 262.0, 305.0,
                    light3TopRed, light3TopYellow, light3TopGreen, 446.0, 480.0, 548.0);
            updateCarSpeed(mphDataList.get(2), car3, car3PrevX, car3PrevTime);
            checkCarPassages(car3, "Car 3", 8, 235, 446, car3LastLightTime, 3);
            mphTable.refresh();
        }));
        carTimeline.setCycleCount(Timeline.INDEFINITE);
        carTimeline.play();
    }
    /** Method to handle the movement animation of extra cars
     * to stop at traffic lights
     * */
    private void startExtraCarAnimation(ImageView extraCar, List<ImageView> allCars) {
        Timeline extraCarTimeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            // (Existing logic for moving extra cars)
            double currentX = extraCar.getLayoutX();
            double nearestCarX = -1;

            // Find the nearest car ahead on the same lane
            for (ImageView car : allCars) {
                if (car != extraCar && car.getLayoutY() == extraCar.getLayoutY() && car.getLayoutX() < currentX) {
                    if (nearestCarX == -1 || car.getLayoutX() > nearestCarX) {
                        nearestCarX = car.getLayoutX();
                    }
                }
            }

            boolean shouldStop = false;
            boolean shouldSlowDown = false;
            double nearestStopX = Double.MAX_VALUE;

            // Evaluate traffic light 1
            if (currentX <= 60.0 && currentX >= 8.0) {
                if (light1TopRed.isVisible()) {
                    shouldStop = true;
                    nearestStopX = Math.min(nearestStopX, 8.0);
                } else if (light1TopYellow.isVisible()) {
                    shouldSlowDown = true;
                }
            }

            // Evaluate traffic light 2
            if (currentX <= 305.0 && currentX >= 235.0) {
                if (light2TopRed.isVisible()) {
                    shouldStop = true;
                    nearestStopX = Math.min(nearestStopX, 235.0);
                } else if (light2TopYellow.isVisible()) {
                    shouldSlowDown = true;
                }
            }

            // Evaluate traffic light 3
            if (currentX <= 548.0 && currentX >= 446.0) {
                if (light3TopRed.isVisible()) {
                    shouldStop = true;
                    nearestStopX = Math.min(nearestStopX, 446.0);
                } else if (light3TopYellow.isVisible()) {
                    shouldSlowDown = true;
                }
            }

            // Apply movement logic based on traffic light and nearest car
            if (shouldStop || (nearestCarX != -1 && currentX - nearestCarX <= extraCar.getFitWidth() + 10)) {
                extraCar.setLayoutX(currentX); // Stop
            } else if (shouldSlowDown) {
                extraCar.setLayoutX(currentX - 1); // Slow down
            } else {
                extraCar.setLayoutX(currentX - 2); // Normal movement
            }

            if (extraCar.getLayoutX() < -extraCar.getFitWidth()) {
                extraCar.setLayoutX(600); // Reset position
            }
        }));
        extraCarTimeline.setCycleCount(Timeline.INDEFINITE);
        extraCarTimeline.play();

        // Add the timeline to the list
        extraCarTimelines.add(extraCarTimeline);
    }


    /** Method to handle the calculate the speed of the vehicles
     * */
    private double calculateSpeed(ImageView car, double prevX, long prevTime) {
        long currentTime = System.currentTimeMillis();
        double distance = prevX - car.getLayoutX(); // Calculate distance moved
        double timeElapsed = (currentTime - prevTime) / 1000.0; // Convert to seconds

        if (timeElapsed > 0) {
            double speedInMph = (distance / timeElapsed) * 2.237; // Convert from m/s to MPH
            double scalingFactor = 0.5; // Reduce the speed by 50%
            return speedInMph * scalingFactor;
        }
        return 0; // Return 0 if no time has elapsed
    }

    /** Method to handle the updating the paramenters for mphTable
     * */

    private void updateCarSpeed(CarSpeedData carData, ImageView car, double[] prevX, long[] prevTime) {
        double currentSpeed = calculateSpeed(car, prevX[0], prevTime[0]);
        carData.setMph(String.format("%.1f MPH", currentSpeed)); // Update the speed
        prevX[0] = car.getLayoutX();
        prevTime[0] = System.currentTimeMillis();
    }


    /** Method to handle the second values to the carTable
     * */
    private void checkCarPassages(ImageView car, String carName, double light1X, double light2X, double light3X, long lastLightTime, int carIndex) {
        if (!recordButtonPressed) {
            return; // Do nothing if recording is not active
        }

        long currentTime = (System.currentTimeMillis() - startTime) / 1000; // Time in seconds

        // Update the appropriate car's times
        switch (carIndex) {
            case 1: // Car 1
                if (car.getLayoutX() >= light1X - 5 && car.getLayoutX() <= light1X + 5 && car1Light1Time == 0) {
                    car1Light1Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light2X - 5 && car.getLayoutX() <= light2X + 5 && car1Light2Time == 0) {
                    car1Light2Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light3X - 5 && car.getLayoutX() <= light3X + 5 && car1Light3Time == 0) {
                    car1Light3Time = currentTime;
                    updateTable();
                }
                break;
            case 2: // Car 2
                if (car.getLayoutX() >= light1X - 5 && car.getLayoutX() <= light1X + 5 && car2Light1Time == 0) {
                    car2Light1Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light2X - 5 && car.getLayoutX() <= light2X + 5 && car2Light2Time == 0) {
                    car2Light2Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light3X - 5 && car.getLayoutX() <= light3X + 5 && car2Light3Time == 0) {
                    car2Light3Time = currentTime;
                    updateTable();
                }
                break;
            case 3: // Car 3
                if (car.getLayoutX() >= light1X - 5 && car.getLayoutX() <= light1X + 5 && car3Light1Time == 0) {
                    car3Light1Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light2X - 5 && car.getLayoutX() <= light2X + 5 && car3Light2Time == 0) {
                    car3Light2Time = currentTime;
                    updateTable();
                }
                if (car.getLayoutX() >= light3X - 5 && car.getLayoutX() <= light3X + 5 && car3Light3Time == 0) {
                    car3Light3Time = currentTime;
                    updateTable();
                }
                break;
        }
    }



    /** Method to handle the values sorted into the carTable
     * */
    private void updateTable() {
        carDataList.clear(); // Clear the current data

        // Add updated data for all cars
        carDataList.add(new CarData("Car 1",
                car1Light1Time > 0 ? car1Light1Time + "s" : "",
                car1Light2Time > 0 ? car1Light2Time + "s" : "",
                car1Light3Time > 0 ? car1Light3Time + "s" : ""));
        carDataList.add(new CarData("Car 2",
                car2Light1Time > 0 ? car2Light1Time + "s" : "",
                car2Light2Time > 0 ? car2Light2Time + "s" : "",
                car2Light3Time > 0 ? car2Light3Time + "s" : ""));
        carDataList.add(new CarData("Car 3",
                car3Light1Time > 0 ? car3Light1Time + "s" : "",
                car3Light2Time > 0 ? car3Light2Time + "s" : "",
                car3Light3Time > 0 ? car3Light3Time + "s" : ""));

        // Refresh the table
        carTable.setItems(carDataList);
        carTable.refresh();
    }
    private void moveCar(ImageView car,
                         Circle light1Red, Circle light1Yellow, Circle light1Green, double stopX1, double slowDownStartX1, double slowDownEndX1,
                         Circle light2Red, Circle light2Yellow, Circle light2Green, double stopX2, double slowDownStartX2, double slowDownEndX2,
                         Circle light3Red, Circle light3Yellow, Circle light3Green, double stopX3, double slowDownStartX3, double slowDownEndX3) {
        boolean shouldStop = false;
        boolean shouldSlowDown = false;
        double nearestStopX = Double.MAX_VALUE;

        // Evaluate light 1
        if (car.getLayoutX() >= stopX1 && car.getLayoutX() <= slowDownEndX1) {
            if (light1Red.isVisible()) {
                shouldStop = true;
                nearestStopX = Math.min(nearestStopX, stopX1);
            } else if (light1Yellow.isVisible()) {
                shouldSlowDown = true;
            }
        }

        // Evaluate light 2
        if (car.getLayoutX() >= stopX2 && car.getLayoutX() <= slowDownEndX2) {
            if (light2Red.isVisible()) {
                shouldStop = true;
                nearestStopX = Math.min(nearestStopX, stopX2);
            } else if (light2Yellow.isVisible()) {
                shouldSlowDown = true;
            }
        }

        // Evaluate light 3
        if (car.getLayoutX() >= stopX3 && car.getLayoutX() <= slowDownEndX3) {
            if (light3Red.isVisible()) {
                shouldStop = true;
                nearestStopX = Math.min(nearestStopX, stopX3);
            } else if (light3Yellow.isVisible()) {
                shouldSlowDown = true;
            }
        }

        // Apply the most restrictive behavior
        if (shouldStop) {
            // Gradually move toward the stop position
            if (car.getLayoutX() > nearestStopX) {
                car.setLayoutX(car.getLayoutX() - 1); // Move slowly to stop position
            } else {
                return; // Stop completely when at the stop position
            }
        } else if (shouldSlowDown) {
            // Slow down the car
            car.setLayoutX(car.getLayoutX() - 1);
        } else {
            // Move normally
            car.setLayoutX(car.getLayoutX() - 2);
        }

        // Reset the car when it goes off-screen (left edge)
        if (car.getLayoutX() < -car.getFitWidth()) {
            car.setLayoutX(600); // Reset to starting position
        }




    }

    /** Method to handle the Animation of the Traffic Light
     * */
    private void startTrafficLightAnimation() {
        lightTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            toggleTrafficLights();
        }));
        lightTimeline.setCycleCount(Timeline.INDEFINITE);
        lightTimeline.play();
    }
    /** Method to handle the order of the Traffic Lights that appear
     * */
    private void toggleTrafficLights() {
        // Light 1 sequence: Red → Green → Yellow → Red
        if (light1TopRed.isVisible()) {
            light1TopRed.setVisible(false);
            light1TopGreen.setVisible(true);
        } else if (light1TopGreen.isVisible()) {
            light1TopGreen.setVisible(false);
            light1TopYellow.setVisible(true);
        } else if (light1TopYellow.isVisible()) {
            light1TopYellow.setVisible(false);
            light1TopRed.setVisible(true);
        }

        // Light 2 sequence: Red → Green → Yellow → Red
        if (light2TopRed.isVisible()) {
            light2TopRed.setVisible(false);
            light2TopGreen.setVisible(true);
        } else if (light2TopGreen.isVisible()) {
            light2TopGreen.setVisible(false);
            light2TopYellow.setVisible(true);
        } else if (light2TopYellow.isVisible()) {
            light2TopYellow.setVisible(false);
            light2TopRed.setVisible(true);
        }

        // Light 3 sequence: Red → Green → Yellow → Red
        if (light3TopRed.isVisible()) {
            light3TopRed.setVisible(false);
            light3TopGreen.setVisible(true);
        } else if (light3TopGreen.isVisible()) {
            light3TopGreen.setVisible(false);
            light3TopYellow.setVisible(true);
        } else if (light3TopYellow.isVisible()) {
            light3TopYellow.setVisible(false);
            light3TopRed.setVisible(true);
        }
    }

}


