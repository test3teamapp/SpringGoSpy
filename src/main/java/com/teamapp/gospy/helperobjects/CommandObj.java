package com.teamapp.gospy.helperobjects;

import com.redis.om.spring.annotations.Document;
import org.springframework.context.annotation.Bean;

import java.io.Serializable;
@Document
public class CommandObj implements Serializable {
    private String deviceId;
    private String command;

    public CommandObj() {
    }

    public CommandObj(String deviceId, String command) {
        this.deviceId = deviceId;
        this.command = command;
    }
// standard getters and setters

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
