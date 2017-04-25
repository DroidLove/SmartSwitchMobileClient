package com.yoman.smartswitchclient;

/**
 * Created by jitesh on 3/2/17.
 */

public class ModelSwitch {

    String SwitchType, Switchname, SwitchNumber;
    boolean SwitchStatus;

    public ModelSwitch() {
    }

    public ModelSwitch(String switchType, String switchname, String switchNumber, boolean switchStatus) {
        SwitchType = switchType;
        Switchname = switchname;
        SwitchStatus = switchStatus;
        SwitchNumber = switchNumber;
    }

    public String getSwitchType() {
        return SwitchType;
    }

    public void setSwitchType(String switchType) {
        SwitchType = switchType;
    }

    public String getSwitchname() {
        return Switchname;
    }

    public void setSwitchname(String switchname) {
        Switchname = switchname;
    }

    public boolean getSwitchStatus() {
        return SwitchStatus;
    }

    public void setSwitchStatus(boolean switchStatus) {
        SwitchStatus = switchStatus;
    }

    public String getSwitchNumber() {
        return SwitchNumber;
    }

    public void setSwitchNumber(String switchNumber) {
        SwitchNumber = switchNumber;
    }

}
