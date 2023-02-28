package com.teamapp.gospy.helperobjects;

import com.teamapp.gospy.models.Location;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CommandReplyObj {
    @Nullable
    private String error;
    @Nullable
    private String name;
    @Nullable
    private String command;
    @Nullable
    private Location message;

    public CommandReplyObj() {
    }

    public CommandReplyObj(String ERROR) {
        this.error = ERROR;
    }

    public CommandReplyObj(String name, Location message) {
        this.name = name;
        this.message = message;
    }


    public CommandReplyObj(String name, String command, Location message) {
        this.name = name;
        this.command = command;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Location getMessage() {
        return message;
    }

    public void setMessage(Location message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String txt =  "CommandReplyObj{" +
                "error='" + error + '\'' +
                ", name='" + name + '\'' +
                ", command='" + command + '\'' +
                ", message=";
        if (message != null){
            txt = txt + message.toString();
        }else {
            txt = txt + message;
        }
        txt = txt + '}';
        return txt;
    }
}
