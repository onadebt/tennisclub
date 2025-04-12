package cz.inqool.tennisclub.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private boolean dataInit;

    public boolean isDataInit() {
        return dataInit;
    }

    public void setDataInit(boolean dataInit) {
        this.dataInit = dataInit;
    }
}
