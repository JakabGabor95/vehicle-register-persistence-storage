package hu.ltk.jakabgabor.domain;

import hu.ltk.jakabgabor.interfaces.PersistenceInterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRegister implements PersistenceInterface {
    private Path storageFile = Paths.get("files/vehicle.csv");
    private List<Vehicle> vehicleList;

    @Override
    public void save(Vehicle vehicle) {
        writeToFile(vehicle);
    }

    @Override
    public Vehicle listVehicleByRegistrationNumber(String registrationNumber) {
        try {
            vehicleList = Files.readAllLines(storageFile).stream().map(storageString -> {
                String[] vehicleData = storageString.split(",");
                Vehicle newVehicle = new Vehicle(vehicleData[0], vehicleData[1], vehicleData[2],
                        Integer.parseInt(vehicleData[3]),VehicleType.valueOf(vehicleData[4]));
                return newVehicle;
            }).collect(Collectors.toList());
            return findByRegistrationNumber(vehicleList, registrationNumber);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(Vehicle vehicle) {
        try (BufferedWriter writer = Files.newBufferedWriter((storageFile))) {
                String vehicleString = "";
                vehicleString += vehicle.getMake() + ",";
                vehicleString += vehicle.getModel() + ",";
                vehicleString += vehicle.getVehicleType() + ",";
                vehicleString += vehicle.getRegistrationNumber() + ",";
                vehicleString += vehicle.getNumberOfSeats();
                writer.append(vehicleString);
                writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Vehicle findByRegistrationNumber(List<Vehicle> vehicleList, String registrationNumber) {
        return vehicleList.stream().filter(vehicle -> {
            return vehicle.getRegistrationNumber()
                    .equals(registrationNumber);
        }).findFirst().orElse(null);
    }
}
