// RepondWeather.java
package com.example.airapp;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepondWeather {
    @SerializedName("attributes")
    private AttributeWeather attributeWeather;

    public AttributeWeather getAttributeWeather() {
        return attributeWeather;
    }

    public static class AttributeWeather {
        @SerializedName("data")
        private DataWeather dataWeather;

        public DataWeather getDataWeather() {
            return dataWeather;
        }

        public static class DataWeather {
            @SerializedName("value")
            private ValueWeather valueWeather;

            public ValueWeather getValueWeather() {
                return valueWeather;
            }

            public static class ValueWeather {
                @SerializedName("main")
                private MainWeather mainWeather;
                public MainWeather getMainWeather() {
                    return mainWeather;
                }
                public static class MainWeather {
                    @SerializedName("temp")
                    private double temperature;

                    @SerializedName("humidity")
                    private int humidity;

                    public double getTemperature() {
                        return temperature;
                    }

                    public void setTemperature(double temperature) {
                        this.temperature = temperature;
                    }

                    public int getHumidity() {
                        return humidity;
                    }

                    public void setHumidity(int humidity) {
                        this.humidity = humidity;
                    }

                    @NonNull
                    @Override
                    public String toString() {
                        return "repond" + this.getHumidity() + this.getTemperature();
                    }
                }
                @SerializedName("wind")
                private WindSuperIdol windSuperIdol;

                public WindSuperIdol getWindSuperIdol() {
                    return windSuperIdol;
                }
                public static class WindSuperIdol{
                    @SerializedName("deg")
                    private double deg;

                    @SerializedName("speed")
                    private double speed;
                    public double getDeg() {
                        return deg;
                    }

                    public void setDeg(double deg) {
                        this.deg = deg;
                    }

                    public double getSpeed() {
                        return speed;
                    }

                    public void setSpeed(int speed) {
                        this.speed = speed;
                    }
                }
                @SerializedName("weather")
                private List<WeatherSuperIdol> weatherSuperIdol;

                public List<WeatherSuperIdol> getWeatherSuperIdol() {
                    return weatherSuperIdol;
                }
                public static class WeatherSuperIdol{
                    @SerializedName("main")
                    private String main;
                    @SerializedName("description")
                    private String description;
                    public String getMainWeatherSuperIdol() {
                        return main;
                    }

                    public void setMainWeatherSuperIdol(String main) {
                        this.main = main;
                    }

                    public String getDescription() {
                        return description;
                    }

                    public void setDescription(String description) {
                        this.description = description;
                    }
                }
            }
        }
    }
}
