package com.sonata;

import java.io.BufferedReader;

import java.io.FileReader;

import java.io.FileWriter;

import java.io.IOException;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Date;

import java.text.SimpleDateFormat;

public class Comparision {
	
	static private String HTMLTemplate = "<!doctype html> <html> <head> <title>Query Results</title> <style> .styled-table { border-collapse: collapse; margin: 25px 0; font-size: 0.9em; font-family: sans-serif; min-width: 400px; box-shadow: 0 0 20px rgba(0, 0, 0, 0.15); } .styled-table thead tr { background-color: #009879; color: #ffffff; text-align: left; } .styled-table th, .styled-table td { padding: 12px 15px; width: 250px;} .styled-table tbody tr { border-bottom: 1px solid #dddddd; } .styled-table tbody tr:nth-of-type(even) { background-color: #f3f3f3; } .styled-table tbody tr:last-of-type { border-bottom: 2px solid #009879; } .styled-table tbody tr.active-row { font-weight: bold; color: #009879; } </style> </head> <body> <div> <u style='color: blue;'><label>Test Result</label><label style='padding-left:75%;'>@@DATE@@</label></u></div> <div><table class='styled-table'> <thead> <tr> <th>ID</th> <th>Name</th> <th>Is Present In table</th> <th>Table Name</th> </tr> </thead> <tbody> @@ROW_DATA@@ </tbody> </table> </div> </body> </html>";
	
	public static void main(String[] args) {
		// Define variables for database connection
		String rowData="";
		String notepadLine=null;
		Connection connection = null;
		BufferedReader notepadReader = null;
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			DBManager dbManager = new DBManager();
			// Establish a database connection
			connection = dbManager.getConnection();
			// Read data from the Notepad file
			notepadReader = new BufferedReader(new FileReader("D:/TextPadReader/Notepad1.txt"));
			while ((notepadLine = notepadReader.readLine()) != null) {
				// Retrieve data from the database for comparison
				String sql = "SELECT id, sal, email_id, name FROM employees WHERE id = ? and upper(name)=?";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				System.out.println("notepadLine : " + notepadLine);
				
				String[] dataArr = notepadLine.split(",");
				System.out.println("dataArr[0] : " + dataArr[0]);
				
				preparedStatement.setString(1, dataArr[0]);
				preparedStatement.setString(2, dataArr[1].toUpperCase().trim());
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					// A match is found
					String id = resultSet.getString("id");
					String sal = resultSet.getString("sal");
					String email_id = resultSet.getString("email_id");
					String name = resultSet.getString("name");
					// Perform your comparison logic here
					System.out.println("Match found for: " + notepadLine);
					System.out.println("Database Data: " + id + ", " + sal + ", " + email_id + ", " + name);
					rowData = rowData + "<tr><td>"+resultSet.getString("id")+"</td><td>"+resultSet.getString("name")+"</td><td>true</td><td>employees</td></tr>";
				} else {
					// No match is found
					System.out.println("No match found for: " + notepadLine);
				}
				resultSet.close();
				preparedStatement.close();
			}
			notepadReader.close();
			
			
			notepadReader = new BufferedReader(new FileReader("D:/TextPadReader/Notepad1.txt"));
			notepadLine=null;
			while ((notepadLine = notepadReader.readLine()) != null) {
				// Retrieve data from the database for comparison
				String sql = "SELECT card_number, card_holder_name, available_bal, card_limit, used_bal FROM card_details WHERE card_number = ? and upper(card_holder_name)=?";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				System.out.println("notepadLine : " + notepadLine);
				
				String[] dataArr = notepadLine.split(",");
				System.out.println("dataArr[0] : " + dataArr[0]);
				
				preparedStatement.setString(1, dataArr[0]);
				preparedStatement.setString(2, dataArr[1].toUpperCase().trim());
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					// A match is found
					String cardNumber = resultSet.getString("card_number");
					String cardHolderName = resultSet.getString("card_holder_name");
					String availableBal = resultSet.getString("available_bal");
					String cardLimit = resultSet.getString("card_limit");
					String usedBal = resultSet.getString("used_bal");
					
					System.out.println("Match found for: " + notepadLine);
					System.out.println("Database Data: " + cardNumber + ", " + cardHolderName + ", " + availableBal + ", " + cardLimit  + ", " + usedBal);
					rowData = rowData + "<tr><td>"+cardNumber+"</td><td>"+cardHolderName+"</td><td>true</td><td>card_details</td></tr>";
				} else {
					// No match is found
					System.out.println("No match found for: " + notepadLine);
				}
				resultSet.close();
				preparedStatement.close();
			}
			notepadReader.close();
			
			
			
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			HTMLTemplate=HTMLTemplate.replace("@@DATE@@", formatter.format(date));
			HTMLTemplate=HTMLTemplate.replace("@@ROW_DATA@@", rowData);
			
			String resultFilePath= "D:/TextPadReader/Result/Result.html";
			FileWriter myWriter = new FileWriter(resultFilePath);
		    myWriter.write(HTMLTemplate);
		    myWriter.close();
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
