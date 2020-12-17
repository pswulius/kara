package com.pete.swulius.device.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kara")
public class KaraProperties {

    public Integer logsPer = 1000;
    public Boolean run = false;
    public Integer maxLogs = 1000000;

    public Integer getLogsPer() {
        return logsPer;
    }

    public void setLogsPer(Integer logsPer) {
        this.logsPer = logsPer;
    }

    public Boolean getRun() {
        return run;
    }

    public void setRun(Boolean run) {
        this.run = run;
    }

    public Integer getMaxLogs() {
        return maxLogs;
    }

    public void setMaxLogs(Integer maxLogs) {
        this.maxLogs = maxLogs;
    }
}
