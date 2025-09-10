package org.example.model;

import java.util.Map;

public class AnalyticsDashboardDTO {
    private long totalBookings;
    private double totalRevenue; // Assuming a fixed price for now
    private Map<String, Long> popularTurfs;
    private Map<String, Long> popularSports;
    private Map<String, Long> peakTimeUsage; // Key: "09 AM", Value: count
    private Map<String, Long> bookingsPerDay; // Key: "2025-09-10", Value: count

    // Getters and Setters for all fields
    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public Map<String, Long> getPopularTurfs() { return popularTurfs; }
    public void setPopularTurfs(Map<String, Long> popularTurfs) { this.popularTurfs = popularTurfs; }
    public Map<String, Long> getPopularSports() { return popularSports; }
    public void setPopularSports(Map<String, Long> popularSports) { this.popularSports = popularSports; }
    public Map<String, Long> getPeakTimeUsage() { return peakTimeUsage; }
    public void setPeakTimeUsage(Map<String, Long> peakTimeUsage) { this.peakTimeUsage = peakTimeUsage; }
    public Map<String, Long> getBookingsPerDay() { return bookingsPerDay; }
    public void setBookingsPerDay(Map<String, Long> bookingsPerDay) { this.bookingsPerDay = bookingsPerDay; }
}