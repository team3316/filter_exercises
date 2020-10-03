package frc.robot.simulation;

import edu.wpi.first.hal.sim.AnalogGyroSim;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class GyroSimulator extends SendableChooser<String> {
    AnalogGyroSim gyroSim;
    GyroTimeSeries timeSeries;

    Thread m_simulationThread;

    public GyroSimulator(final int channel, final String[] filenames) {
        super();
        assert filenames.length > 0;
        
        this.setDefaultOption("0", filenames[0]);
        for (int i = 1; i < filenames.length; i++) {
            this.addOption(String.valueOf(i), filenames[i]);
        }

        gyroSim = new AnalogGyroSim(channel);
    }

    public void load() {
        reset();

        final String filename = this.getSelected();
        System.out.println("Loading time series: " + filename);
        timeSeries = GyroTimeSeriesFactory.fromJSON(filename);
    }

    public void run() {
        if (timeSeries == null) {
            System.out.println("Must load time series before running!");
            return;
        }

        reset();

        m_simulationThread = new SimulationThread();
        m_simulationThread.start();
    }

    public void reset() {
        if (isAlive()) {
            m_simulationThread.interrupt();
        }

        synchronized (gyroSim) {
            gyroSim.resetData();
        }

        if (timeSeries != null)
            timeSeries.reset();
    }

    public Boolean isAlive() {
        return m_simulationThread != null && m_simulationThread.isAlive();
    }

    private class SimulationThread extends Thread {
        @Override
        public void run() {
            synchronized (gyroSim) {
                System.out.println("Simulation started");
                timeSeries.init(Timer.getFPGATimestamp() * 100);

                while (!interrupted()) {
                    GyroDataPoint point = timeSeries.getPoint(Timer.getFPGATimestamp() * 100);
                    gyroSim.setAngle(Math.toDegrees(point.getAngle()));
                    gyroSim.setRate(Math.toDegrees(point.getRate()));

                    if (timeSeries.isFinished()) {
                        break;
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                gyroSim.setRate(0.0);
                System.out.println("Simulation " + (interrupted() ? "interrupted" : "ended"));
            }
        }
    }
}