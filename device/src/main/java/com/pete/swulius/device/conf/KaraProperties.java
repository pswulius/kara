package com.pete.swulius.device.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kara")
public class KaraProperties {

    public Integer logsPer = 1000;
    public Boolean run = true;
    public Integer maxLogs = 1000000;
}
