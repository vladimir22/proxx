package com.vladimir22;

import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
Task from ex. Lohika (for Pandora project): Review Code
 */

public class HolidayHelper {

    /*
         It is a bad practice to use static fields for caching.
         Multiple PODs will have different instances of this class.
     */
    private static final Map<HolidayRequest, List<String>> LOCAL_CACHE = new HashMap<>();
    /*
        It is better to use interface of common cache client instead of concrete implementation.
    */
    private final IRedisConnector redisConnector;
    private final IHolidaysReader holidaysReader;

    /*
        Use Lombok annotations for fields instead of setters.
     */
    public HolidayHelper(IRedisConnector redisConnector, IHolidaysReader holidaysReader) {
        this.redisConnector = redisConnector;
        this.holidaysReader = holidaysReader;
    }

    public List<String> getHolidaysInCountry(HolidayRequest req) {
        List<String> cachedResponse = this.getFromCache(req); // this is a bad practice to use this. in code

        if (cachedResponse != null) {
            return cachedResponse;

        }
//        if (cachedResponse == null) {

        if (req.pathToFile != null) { // access to property of request object is a bad practice
            ((FileHolidayReader) this.holidaysReader).absolutePathToFile = req.pathToFile;
        }
        try {
            if (req.country.equals("*")) { // Use constants instead of magic values

                List<HolidayRecord> holidays = this.holidaysReader
                        .getHolidays()
                        .stream()
                        .filter(record -> record.holidayDate > req.startDate
                                || record.holidayDate < req.endDate).collect(Collectors.toList()); // Date comparison should be done with LocalDate

                cachedResponse = new ArrayList<>();
                for (HolidayRecord holiday : holidays) { // Unnecessary iteration
                    cachedResponse.add(holiday.holidayName);
                }
            } else {
                List<HolidayRecord> holidays = this.holidaysReader.getHolidays()
                        .stream()
                        .filter(record -> record.holidayDate > req.startDate
                                || record.holidayDate < req.endDate)
                        .filter(record -> record.country.equals(req.country)).collect(Collectors.toList());
                cachedResponse = new ArrayList<>();
                for (HolidayRecord holiday : holidays) {
                    cachedResponse.add(holiday.country + ' ' + holiday.holidayName);
                }
            }
        } catch (IOException exc) { // suppress exception is a bad practice
            System.out.println("oops file not found"); // Use logger instead of System.out
            return new ArrayList<>();
        }
        this.putToCache(req, cachedResponse); // this is a bad practice to use this. in code
//        }

        return cachedResponse;
    }

    private List<String> getFromCache(HolidayRequest req) {
        req.country = req.country.toUpperCase(); // Do not mutate DTO !!!

        if (this.redisConnector != null) { // use separate method for cache client
            return this.redisConnector.get_from_cache(req);
        } else {
            return LOCAL_CACHE.get(req);
        }
    }

    private void putToCache(HolidayRequest req, List<String> cachedResponse) { // e separate method for cache client
        if (this.redisConnector != null) {
            this.redisConnector.put_to_cache(req, cachedResponse);
        } else {
            LOCAL_CACHE.put(req, cachedResponse);
        }
    }
    // Implement method getCache() to hide implementation details of cache
}


class HolidayRequest {
    public String country;
    public int startDate;
    public int endDate;
    public String pathToFile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HolidayRequest that = (HolidayRequest) o;
        return startDate == that.startDate
                && endDate == that.endDate
                && Objects.equals(pathToFile, that.pathToFile); // equals should be calculated based on immutable fields
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, pathToFile); // Hash code should be calculated based on immutable fields
    }
}

class HolidayRecord {
    public String country;
    public int holidayDate; // Do not use into for date
    public String holidayName;
}

interface IRedisConnector {
    List<String> get_from_cache(HolidayRequest req);
    void put_to_cache(HolidayRequest req, List<String> value);
}

interface IHolidaysReader {
    List<HolidayRecord> getHolidays() throws IOException;
}

class FileHolidayReader implements IHolidaysReader {
    @Setter
    String absolutePathToFile;

    public FileHolidayReader(String pathToFile) {
        this.absolutePathToFile = pathToFile;
    }

    @Override
    public List<HolidayRecord> getHolidays() throws IOException {
        // implementation skipped for brevity. suppose its correct
        return new ArrayList<>();
    }
}