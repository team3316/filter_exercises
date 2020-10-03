package frc.robot.simulation;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"time", "rate", "angle"})
public class GyroDataPoint {
    @JsonProperty("time")
    private double time; // in ms
    @JsonProperty("rate")
    private double rate;  // in radians per second
    @JsonProperty("angle")
    private double angle; // in radians
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("time")
    public double getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(double time) {
        this.time = time;
    }

    @JsonProperty("rate")
    public double getRate() {
        return rate;
    }

    @JsonProperty("rate")
    public void setRate(double rate) {
        this.rate = rate;
    }

    @JsonProperty("angle")
    public double getAngle() {
        return angle;
    }

    @JsonProperty("angle")
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}