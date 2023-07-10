
import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Lab12_202003037_202003038_prog_v1 
{
	public static void main(String args[]) 
	{
		Connection c = null;
		try
		{
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MyDB","postgres", "mms10503");
			System.out.println("Opened database successfully");
			
			Lab12_202003037_202003038_prog_v1 p = new Lab12_202003037_202003038_prog_v1();

			p.setSearchPath(c);
			
			p.insertStudentTable(c);

			p.updateStudentTable(c);

                        p.queryStudentTable(c);

			p.deleteStudentTable(c);
			
			p.has_taken(c);
			
			c.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void setSearchPath(Connection c)
	{
		Statement stmt = null;
		try
		{
			stmt = c.createStatement();
			String sql = "SET search_path TO online_learning_platform;";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("Changed Search Path successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

	void insertStudentTable(Connection c)
	{
		PreparedStatement stmt = null;
		String sql = "INSERT INTO student VALUES (?, ?, ?, ?, ?, ?, ?)";
		try
		{
			Scanner in = new Scanner(System.in);

			stmt = c.prepareStatement(sql);
			stmt.setInt(1, 31);

			System.out.println("Enter first name:");
			String f = in.nextLine();
			stmt.setString(2, f);

                        System.out.println("Enter last name:");
			String l = in.nextLine();
			stmt.setString(3, l);

                        System.out.println("Enter Email ID:");
			String e = in.nextLine();
			stmt.setString(4, e);

                        System.out.println("Enter Gender(M/F/O):");
			String g = in.nextLine();
			stmt.setString(5, g);

			System.out.println("Enter city:");
			String t = in.nextLine();
			stmt.setString(6, t);

                        System.out.println("Enter password:");
			String p = in.nextLine();
			stmt.setString(7, p);
		
			int affectedRows = stmt.executeUpdate();
			stmt.close();
			System.out.println("Table Inserted successfully: Rows Affected: " + affectedRows);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void queryStudentTable(Connection c)
	{
		Statement stmt = null;
		try
		{
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT sid, fname || ' ' || lname AS name, gender, city FROM student");
			
			while(rs.next())
			{
				String name, gender, city;
				int sid;
				
				name = rs.getString("name");
				sid = rs.getInt("sid");
				gender = rs.getString("gender");
				city = rs.getString("city");
				
				System.out.println("StudentInfo: SID-" + sid + ", Name-" + name + " , Gender-" + gender + ", City-" + city);
			}
			
			stmt.close();
			System.out.println("Table Queried successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
   
   	void updateStudentTable(Connection c)
	{
		PreparedStatement stmt = null;
		String sql = "UPDATE student SET city = ? WHERE city = ?";
		try
		{
			stmt = c.prepareStatement(sql);
			stmt.setString(1, "Ahmedabad");
			stmt.setString(2, "ahemadabad");
			int affectedRows = stmt.executeUpdate();
			stmt.close();
			System.out.println("Table Updated successfully: Rows Updated: " + affectedRows);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

        void deleteStudentTable(Connection c)
	{
		PreparedStatement stmt = null;
		String sql = "DELETE FROM student where sid = ?";
		try
		{
			stmt = c.prepareStatement(sql);
			stmt.setInt(1, 31);
			int affectedRows = stmt.executeUpdate();
			stmt.close();
			System.out.println("Table deleted successfully: Rows Updated: " + affectedRows);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

   	void has_taken(Connection c)
	{
		CallableStatement stmt = null;
		try
		{
			String sql = "call has_taken(?, ?, ?)";
                        int sid=1;
                        String cid="MA101";

			stmt = c.prepareCall(sql);
			stmt.setInt(1, sid);
			stmt.setString(2, cid);
			stmt.setBoolean(3, true);
                        stmt.registerOutParameter(3, java.sql.Types.BOOLEAN);
			stmt.execute();
			
			boolean retval;
			retval = stmt.getBoolean(3);
			if (retval == true)
				System.out.println("Student with sid " + sid + " has taken course " + cid);
			else
				System.out.println("Student with sid " + sid + " has not taken course " + cid);
			stmt.close();
			System.out.println("Stored procedure Called successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

}