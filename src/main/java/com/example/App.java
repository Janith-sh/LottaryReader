package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    
    // Database configuration
    private static final String DB_HOST = "jdbc:mysql://13.234.242.165:3306/";
    private static final String DB_NAME = "a1"; // Using existing database
    private static final String TABLE_NAME = "newResult_71"; // Table name instead
    private static final String DB_USER = "iDevice";
    private static final String DB_PASS = "iDevice_123456";
    
    public static void main(String[] args) {
        
        try {
            // Step 1: Scrape lottery data
            System.out.println("Step 1: Scraping lottery data...");
            String url = "https://www.dlb.lk/result/6/";
            
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.5")
                    .header("Connection", "keep-alive")
                    .timeout(30000)
                    .get();
            
            System.out.println("Page Title: " + doc.title());
            System.out.println("================================\n");
            
            // Step 2: Create database and table
            System.out.println("Step 2: Setting up database...");
            setupDatabase();
            
            // Step 3: Parse and store lottery results
            System.out.println("\nStep 3: Parsing and storing Jayoda lottery results...");
            Connection conn = DriverManager.getConnection(DB_HOST + DB_NAME, DB_USER, DB_PASS);
            
            // Find all lottery result sections
            Elements lotteryDivs = doc.select("div");
            
            int jayodaCount = 0;
            for (Element element : lotteryDivs) {
                String text = element.text();
                
                // Only process Jayoda lottery
                if (text.contains("Jayoda Draw Number -")) {
                    parseLotteryResult(text, conn);
                    jayodaCount++;
                }
            }
            
            conn.close();
            System.out.println("\n================================");
            System.out.println("Jayoda lottery data successfully stored in database!");
            System.out.println("Total Jayoda records processed: " + jayodaCount);
            
        } catch (Exception e) {
            System.out.println("Error occurred:");
            e.printStackTrace();
        }
    }
    
    private static void setupDatabase() throws SQLException {
        // Connect to existing database
        Connection conn = DriverManager.getConnection(DB_HOST + DB_NAME, DB_USER, DB_PASS);
        Statement stmt = conn.createStatement();
        
        System.out.println("Connected to database '" + DB_NAME + "'");
        
        // Create lottery_results table with custom name
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "lottery_name VARCHAR(100) NOT NULL, " +
                "draw_number INT NOT NULL, " +
                "winning_numbers VARCHAR(200), " +
                "draw_date VARCHAR(50), " +
                "scraped_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE KEY unique_draw (lottery_name, draw_number)" +
                ")";
        
        stmt.executeUpdate(createTable);
        System.out.println("Table '" + TABLE_NAME + "' created/verified");
        
        stmt.close();
        conn.close();
    }
    
    private static void parseLotteryResult(String text, Connection conn) {
        try {
            // Pattern to extract Jayoda lottery information
            // Example: "Jayoda Draw Number - 2174 | 2025-Oct-29 Wednesday V 15 30 54 55"
            
            String lotteryName = "Jayoda";
            int drawNumber = 0;
            String drawDate = null;
            String winningNumbers = null;
            
            // Extract draw number
            Pattern drawPattern = Pattern.compile("Jayoda Draw Number - (\\d+)");
            Matcher drawMatcher = drawPattern.matcher(text);
            if (drawMatcher.find()) {
                drawNumber = Integer.parseInt(drawMatcher.group(1));
            } else {
                return; // Not a valid Jayoda entry
            }
            
            // Extract date
            Pattern datePattern = Pattern.compile("\\| (\\d{4}-\\w{3}-\\d{2} \\w+)");
            Matcher dateMatcher = datePattern.matcher(text);
            if (dateMatcher.find()) {
                drawDate = dateMatcher.group(1);
            }
            
            // Extract winning numbers (after the date, before "MORE")
            if (drawDate != null) {
                int dateEndIndex = text.indexOf(drawDate);
                if (dateEndIndex > 0) {
                    int moreIndex = text.indexOf("MORE", dateEndIndex);
                    if (moreIndex > 0) {
                        String numbersSection = text.substring(dateEndIndex + drawDate.length(), moreIndex).trim();
                        // Clean up the winning numbers
                        winningNumbers = numbersSection.replaceAll("\\s+", " ").trim();
                    }
                }
            }
            
            // Insert into database (or update if exists)
            if (drawNumber > 0 && winningNumbers != null && !winningNumbers.isEmpty()) {
                insertOrUpdateResult(conn, lotteryName, drawNumber, drawDate, winningNumbers);
                System.out.println("Stored: " + lotteryName + " #" + drawNumber + " | Date: " + drawDate + " | Numbers: " + winningNumbers);
            }
            
        } catch (Exception e) {
            System.out.println("Error parsing lottery result: " + e.getMessage());
        }
    }
    
    private static void insertOrUpdateResult(Connection conn, String lotteryName, int drawNumber, 
                                            String drawDate, String winningNumbers) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (lottery_name, draw_number, winning_numbers, draw_date) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE winning_numbers = VALUES(winning_numbers), draw_date = VALUES(draw_date)";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, lotteryName);
        pstmt.setInt(2, drawNumber);
        pstmt.setString(3, winningNumbers);
        pstmt.setString(4, drawDate);
        pstmt.executeUpdate();
        pstmt.close();
    }
}
