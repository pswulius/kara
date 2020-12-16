package com.pete.swulius.device.model;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("city_states")
public class CityState {

    @PrimaryKeyColumn(name = "city", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String city;

    @PrimaryKeyColumn(name = "state", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String state;

    private Integer population;
    private Integer density;
    private String timezone;

    public CityState() {

    }

    @Override
    public String toString() {
        return "CityState{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", population=" + population +
                ", density=" + density +
                ", timezone='" + timezone + '\'' +
                '}';
    }

    public CityState(String aCommaSeperatedLine) {
        try {
            String[] split = aCommaSeperatedLine.split(",");
            city = split[0];
            state = split[1];
            population = Integer.parseInt(split[2]);
            density = Integer.parseInt(split[3]);
            timezone = split[4];
        } catch (Exception e) {
            System.out.println("ERROR with line: " + aCommaSeperatedLine);
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getDensity() {
        return density;
    }

    public void setDensity(Integer density) {
        this.density = density;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
