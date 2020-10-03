package frc.robot.simulation;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GyroTimeSeriesFactory {
    public static GyroTimeSeries fromJSON(String filename) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(new File(filename), GyroTimeSeries.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}