package com.patientmonitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:thresholds.properties")
@ConfigurationProperties(prefix = "threshold")
public class MedicalThresholdProperties {

    private Border systolic = new Border();
    private Border diastolic = new Border();
    private Border pulse = new Border();
    private Border temperature = new Border();
    private Border glucose = new Border();
    private Border oxygen = new Border();

    public Border getSystolic() {
        return systolic;
    }

    public void setSystolic(Border systolic) {
        this.systolic = systolic;
    }

    public Border getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Border diastolic) {
        this.diastolic = diastolic;
    }

    public Border getPulse() {
        return pulse;
    }

    public void setPulse(Border pulse) {
        this.pulse = pulse;
    }

    public Border getTemperature() {
        return temperature;
    }

    public void setTemperature(Border temperature) {
        this.temperature = temperature;
    }

    public Border getGlucose() {
        return glucose;
    }

    public void setGlucose(Border glucose) {
        this.glucose = glucose;
    }

    public Border getOxygen() {
        return oxygen;
    }

    public void setOxygen(Border oxygen) {
        this.oxygen = oxygen;
    }

    public static class Border {
        private double low;
        private double high;

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public boolean isOutside(Number value) {
            if (value == null) {
                return false;
            }

            double current = value.doubleValue();
            return current < low || current > high;
        }
    }
}