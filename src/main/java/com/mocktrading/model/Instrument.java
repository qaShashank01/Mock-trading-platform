package com.mocktrading.model;

public class Instrument {
    private String isin;
    private String name;
    private String type;

    public Instrument(String isin, String name, String type) {
        this.isin = isin;
        this.name = name;
        this.type = type;
    }

    public String getIsin() {
        return isin;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Instrument{isin='%s', name='%s', type='%s'}", isin, name, type);
    }
}

