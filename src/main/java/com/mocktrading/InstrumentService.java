package com.mocktrading;

import com.mocktrading.model.Instrument;
import java.util.*;
import java.util.stream.Collectors;

public class InstrumentService {
    private final Map<String, Instrument> instruments;
    private static final String ISIN_PATTERN = "^[A-Z]{2}[A-Z0-9]{9}\\d$";

    public InstrumentService() {
        this.instruments = new HashMap<>();
        initializeInstruments();
    }

    private void initializeInstruments() {
        instruments.put("US0378331005", new Instrument("US0378331005", "Apple Inc", "BOND"));
        instruments.put("US5949181045", new Instrument("US5949181045", "Microsoft Corp", "BOND"));
        instruments.put("US02079K3059", new Instrument("US02079K3059", "Alphabet Inc", "BOND"));
        instruments.put("US30303M1027", new Instrument("US30303M1027", "Meta Platforms", "BOND"));
        instruments.put("GB0002875804", new Instrument("GB0002875804", "HSBC Holdings", "BOND"));
    }

    public Instrument fetchInstrument(String isin) {
        if (!isValidIsin(isin)) {
            throw new IllegalArgumentException("Invalid ISIN format: " + isin);
        }
        Instrument instrument = instruments.get(isin);
        if (instrument == null) {
            throw new NoSuchElementException("Instrument not found for ISIN: " + isin);
        }
        return instrument;
    }

    public List<Instrument> searchInstruments(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return instruments.values().stream()
                .filter(instr -> instr.getName().toLowerCase().contains(lowerSearchTerm) ||
                               instr.getIsin().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public boolean isValidIsin(String isin) {
        return isin != null && isin.matches(ISIN_PATTERN);
    }

    public boolean instrumentExists(String isin) {
        return isValidIsin(isin) && instruments.containsKey(isin);
    }
}

