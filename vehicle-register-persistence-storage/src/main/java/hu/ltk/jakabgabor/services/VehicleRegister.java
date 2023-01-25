package hu.ltk.jakabgabor.services;

import hu.ltk.jakabgabor.entities.Vehicle;
import hu.ltk.jakabgabor.enums.VehicleType;
import hu.ltk.jakabgabor.interfaces.PersistenceInterface;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleRegister implements PersistenceInterface {
    private Path vehicleFile = Paths.get("files/vehicle.csv");
    private List<Vehicle> vehicleList;

    @Override
    public void save(Vehicle vehicle) {
        writeToFile(vehicle);
    }

    @Override
    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) {
        try {
            vehicleList = Files.readAllLines(vehicleFile).stream().map(storageString -> {
                String[] vehicleData = storageString.split(",");
                Vehicle newVehicle = new Vehicle(vehicleData[0], vehicleData[1], vehicleData[2],
                        Integer.parseInt(vehicleData[3]), VehicleType.valueOf(vehicleData[4]));
                return newVehicle;
            }).collect(Collectors.toList());
            return findByRegistrationNumber(vehicleList, registrationNumber);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(Vehicle vehicle) {
        try (BufferedWriter writer = Files.newBufferedWriter((vehicleFile), StandardOpenOption.APPEND)) {
                String vehicleString = "";
                vehicleString += vehicle.getRegistrationNumber() + ",";
                vehicleString += vehicle.getMake() + ",";
                vehicleString += vehicle.getModel() + ",";
                vehicleString += vehicle.getNumberOfSeats() + ",";
                vehicleString += vehicle.getVehicleType();
                writer.write(vehicleString);
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
