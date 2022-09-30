package bank_pack;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;
import java.text.*;

public class Bank_methods {
	private String URL = "jdbc:postgresql://localhost:5432/dbase_bucket";
	private String uname = "postgres";
	private String password = "1217";
	
	/*Create a new Account*/
	public Long Create_account() throws SQLException, Exception
	{
		Long acc_no = null;
		Connection con1 = DriverManager.getConnection(URL, uname, password);
		Statement stmt = con1.createStatement();
		//System.out.println("connecton opened....");
		ResultSet rs;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your name : ");
		String name = sc.nextLine();
		System.out.println("Enter DOB : ");
		String sDate = sc.nextLine();
		Date dob = new SimpleDateFormat("dd-MM-yyyy").parse(sDate);
		java.sql.Date sql_date = new java.sql.Date(dob.getTime());
		System.out.println("Enter your ph.no : ");
		long phone = sc.nextLong();
		int flag = 0;
		long aadhaar;
		do
		{
			System.out.println("Enter Aadhaar no, : ");
			aadhaar = sc.nextLong();
			sc.nextLine();
			String sql_query1 = "SELECT Acc_no FROM postgre_bank WHERE aadhaar ="+aadhaar;
			rs = stmt.executeQuery(sql_query1);
			if(rs.isBeforeFirst()==false)
			{
				flag =0;
				continue;
			}
			else
			{
				System.out.println("Customer detail already exists....");
				flag = 1;
			}
				
		}while(flag == 1);
		System.out.println("Enter your Address : ");
		String address = sc.nextLine();
		System.out.println("Initial Deposit Amount : ");
		int d_amount = sc.nextInt();
		sc.nextLine();
		String uID;
		do
		{
		System.out.println("Enter user ID : ");
		uID = sc.nextLine();
		String sql_query2 = "SELECT Acc_no FROM postgre_bank WHERE userid =\'"+uID+"\'";
		rs = stmt.executeQuery(sql_query2);
		if(rs.isBeforeFirst()==false)
		{
			flag =0;
			continue;
		}
		else
		{
			System.out.println("Customer detail already exists....");
			flag = 1;
		}
		}while(flag ==1);
		
		PreparedStatement ps = con1.prepareStatement("INSERT INTO postgre_bank(Acc_no, name,dob, ph_no, aadhaar, address, balance, userid) "
				+ "VALUES(nextval('post_cust_seque'),?,?,?,?,?,?,?)");
		ps.setString(1, name);
		ps.setDate(2, sql_date);
		ps.setLong(3, phone);
		ps.setLong(4, aadhaar);
		ps.setString(5, address);
		ps.setInt(6, d_amount);
		ps.setString(7, uID);
		int rec = ps.executeUpdate();
		
		System.out.println("Record :"+rec+" inserted");
		String sql_query3 = "SELECT Acc_no FROM postgre_bank WHERE userid =\'"+uID+"\'";
		rs = stmt.executeQuery(sql_query3);
		rs.next();
		acc_no = rs.getLong(1);
		con1.close();
		return acc_no;
	}
	
	public void ExistingUser() throws SQLException
	{
		Connection con2 = DriverManager.getConnection(URL, uname, password);
		System.out.println("Enter userID/Acc_no :");
		Scanner sc = new Scanner(System.in);
		String intake = sc.nextLine();
		int flag =0;
		Long acc_no =(long) 0;
		try
		{
			acc_no = Long.parseLong(intake);
		}
		catch(NumberFormatException e)
		{
			flag =1;
		}
		Statement stmt2 = con2.createStatement();
		String sql_query4 = "SELECT * FROM postgre_bank WHERE userid =\'"+intake+"\' OR acc_no ="+acc_no;
		ResultSet rs2 = stmt2.executeQuery(sql_query4);
		if(rs2.next())
		{
			System.out.println("Hi...."+rs2.getString(2));
			
			int choice =0;
			do
			{
				System.out.println("1. Deposit Amount.");
				System.out.println("2. Withdraw Amount.");
				System.out.println("3. View Account Info.");
				System.out.println("4. LogOut.");
				System.out.println("Enter your choice.....");
				choice = sc.nextInt();
				switch(choice)
				{
				case 1:
				{
					String sql_query7 = "SELECT balance FROM postgre_bank WHERE acc_no ="+rs2.getLong(1);
					Statement stmt4 = con2.createStatement();
					ResultSet rs4 = stmt4.executeQuery(sql_query7);
					rs4.next();
					Long bal = rs4.getLong(1);
					//System.out.println("Before Balance :"+bal);
					System.out.println("Enter the amount to deposit : ");
					Long amount = sc.nextLong();
					bal = bal+amount;
					//System.out.println("Balance : "+amount);
					String sql_query8 = "UPDATE postgre_bank SET balance ="+bal+" WHERE acc_no ="+rs2.getLong(1);
					stmt4.executeUpdate(sql_query8);
					System.out.println("Account updated.....");
					break;
				}
				case 2:
				{
					String sql_query5 = "SELECT balance FROM postgre_bank WHERE acc_no ="+rs2.getLong(1);
					Statement stmt3 = con2.createStatement();
					ResultSet rs3 = stmt3.executeQuery(sql_query5);
					rs3.next();
					Long bal = rs3.getLong(1);
					//System.out.println("Before Balance :"+bal);
					System.out.println("Enter the amount to withdraw : ");
					Long amount = sc.nextLong();
					bal = bal-amount;
					//System.out.println("Balance :"+bal);
					if(bal>=0)
					{
						String sql_query6 = "UPDATE postgre_bank SET balance ="+bal+" WHERE acc_no ="+rs2.getLong(1);
						stmt3.executeUpdate(sql_query6);
						System.out.println("Account updated.....");
					}
					else
					{
						System.out.println("Insufficient Account balance......");
					}
					break;
				}
				case 3:
				{
					String sql_query0 = "SELECT * FROM postgre_bank WHERE acc_no = "+rs2.getLong(1);
					Statement stmt0 = con2.createStatement();
					ResultSet rs0 = stmt0.executeQuery(sql_query0);
					rs0.next();
					System.out.println("Account number : "+rs0.getLong(1));
					System.out.println("Name : "+rs0.getString(2));
					System.out.println("Date of Birth : "+rs0.getDate(3));
					System.out.println("Phone no. : "+rs0.getLong(4));
					String aadhaar = rs0.getString(5);
					System.out.println("Aadhaar : xxxx xxxx "+aadhaar.substring(aadhaar.length()-4));
					System.out.println("Address : "+rs0.getString(6));
					System.out.println("Account balance : "+rs0.getLong(7));
					rs0.close();
					stmt0.close();
					break;
				}
				case 4:
				{
					System.out.println("Bye....."+rs2.getString(2));
					con2.close();
					break;
				}
				default:
				{
					System.out.println("Invalid choice.....");
					break;
				}
				}
			}while(choice!=4);
		}
		else
		{
			System.out.println("Invalid userID/Acc_no ........");
		}
	}
	public void display()
	{
		System.out.println("User Info ..... ");
	}
}
