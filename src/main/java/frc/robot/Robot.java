/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.simulation.GyroSimulator;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final int kChannel = 1;
  private final String[] kFilenames = {"input_1.json", "input_2.json", "input_3.json", "input_4.json"};
  private final String kTabName = "Filters Exercise";

  private AnalogGyro m_gyro = new AnalogGyro(kChannel);
  private NetworkTableEntry m_gyroAngleEntry, m_gyroRateEntry;
  private GyroSimulator m_gyroSimulator = new GyroSimulator(kChannel, kFilenames);
  private Filter m_filter = new Filter();
  private NetworkTableEntry m_load, m_run;
  private Boolean m_runToggle = false;
  private Boolean m_aliveToggle = false;
  

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    initShuffleboard();
  }

  private void initShuffleboard() {
    ShuffleboardTab tab = Shuffleboard.getTab(kTabName);
    
    tab.add("Simulator", m_gyroSimulator)
      .withWidget(BuiltInWidgets.kSplitButtonChooser)
      .withSize(2, 1)
      .withPosition(0, 0);
    
    m_gyroAngleEntry = tab.add("Gyro Angle", new double[] { 0, 0 })
      .withWidget(BuiltInWidgets.kGraph)
      .withPosition(0, 1)
      .withSize(3, 3)
      .withProperties(Map.of("Visible time", 30))
      .getEntry();
  
    m_gyroRateEntry = tab.add("Gyro Rate", new double[] { 0, 0 })
      .withWidget(BuiltInWidgets.kGraph)
      .withPosition(3, 1)
      .withSize(3, 3)
      .withProperties(Map.of("Visible time", 30))
      .getEntry();

    ShuffleboardLayout commands = tab.getLayout("Commands", BuiltInLayouts.kGrid)
      .withSize(2, 1)
      .withPosition(2, 0)
      .withProperties(Map.of("Label position", "HIDDEN", "Number of columns", 2, "Number of rows", 1));

    m_load = commands.add("Load", false)
      .withWidget(BuiltInWidgets.kToggleButton)
      .getEntry();

    m_run = commands.add("Run", false)
      .withWidget(BuiltInWidgets.kToggleButton)
      .getEntry();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    handleBottons();

    double[] rawGyroData = new double[] { m_gyro.getAngle(), m_gyro.getRate() };
    double[] filteredGyroData = m_filter.calculate(rawGyroData);

    m_gyroAngleEntry.setDoubleArray(new double[] { rawGyroData[0], filteredGyroData[0] });
    m_gyroRateEntry.setDoubleArray(new double[] { rawGyroData[1], filteredGyroData[1] });
  }

  private void handleBottons() {
    if (m_load.getBoolean(false)) {
      m_gyroSimulator.load();
      m_load.setBoolean(false);
    }

    Boolean _run = m_run.getBoolean(false);
    if (_run && !m_runToggle) {  // Run button was toggled on, start simulation.
      m_gyroSimulator.run();
    } else if (!_run && m_runToggle) {  // Run button was toggled off, reset simulation.
      m_gyroSimulator.reset();
    }
    m_runToggle = _run;

    
    Boolean _alive = m_gyroSimulator.isAlive();
    if (!_alive && m_aliveToggle) {  // Simulator finished, toggle run botton off.
      m_runToggle = false;
      m_run.setBoolean(false);
    }
    m_aliveToggle = _alive;

  }
}
