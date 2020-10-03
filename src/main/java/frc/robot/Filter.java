package frc.robot;

import edu.wpi.first.wpilibj.LinearFilter;

public class Filter {
    LinearFilter angleFilter, rateFilter1, rateFilter2;
    public Filter() {
        // TODO: Implement me!
    }

    public double[] calculate(double[] gyroData) {
        // TODO: Implement me!
        return new double[] {gyroData[0], gyroData[1]};
    }
}