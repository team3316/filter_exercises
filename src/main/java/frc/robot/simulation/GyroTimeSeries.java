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
@JsonPropertyOrder({"data"})
public class GyroTimeSeries {
    @JsonIgnore
    private double initTime = 0;
    @JsonIgnore
    private int index = 0;

    @JsonProperty("data")
    private GyroDataPoint[] data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("data")
    public GyroDataPoint[] getData() {
        return data;
    }
    
    @JsonProperty("data")
    public void setData(GyroDataPoint[] data) {
        this.data = data;
    }
    
    public void init(double initTime) {
        reset();
        this.initTime = initTime;
    }

    public void reset() {
        this.index = 0;
    }

    public GyroDataPoint getPoint(double time) {
        while (!isFinished() && data[index + 1].getTime() <= (time - initTime))
            index++;
        return data[index];
    }

    public Boolean isFinished() {
        return index == (data.length - 1);
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