import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.Polygon;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

import oracle.sql.STRUCT;

public class xquery {

	private static final String URL = "jdbc:oracle:thin:localhost:1521:orcl";
	private static final String ID = "scott";
	private static final String PASSWORD = "tiger";

	public HW3() {

	}

	public static Connection connectDB() {
		try {
			System.out.println("Searching For Oracle's JDBC-ODBC Driver ... ");
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			System.out.print("Driver Loaded");
			System.out.println("Connecting To The Database");
			Connection conn = DriverManager.getConnection(URL, ID, PASSWORD);
			System.out.println("Successfully Connected !!");
			return conn;
		} catch (Exception err) {
			System.out.println("ERROR ! " + err.toString());
			err.printStackTrace();
			System.exit(-1);
			return null;
		}
	}

	public static void main(String[] args) throws SQLException 
	{ 
		int i=0,j=-0;
		String id="",tile="",aut="",prc="",is="",pd="";
		String rid="",btile="",rat="",rev="",rdes="",rd="";
		String insBook="",insRev="";
		Connection conn = connectDB();
		Statement stmt = null;
		stmt = conn.createStatement();
		
		try {
			 
			File fXmlFile = new File("book.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
	
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Book");
			
			for (i = 0; i < nList.getLength(); i++) 
			{
				 
				Node nNode = nList.item(i);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
		 
					Element eElement = (Element) nNode;
		 
					id=eElement.getElementsByTagName("ID").item(0).getTextContent();
					tile=eElement.getElementsByTagName("Title").item(0).getTextContent();
					aut=eElement.getElementsByTagName("Author").item(0).getTextContent();
					prc=eElement.getElementsByTagName("Price").item(0).getTextContent();
					is=eElement.getElementsByTagName("ISBN").item(0).getTextContent();
					pd=eElement.getElementsByTagName("Publish_Date").item(0).getTextContent();
					pd.replaceAll("\\s","");
					// create insert string and insert the row 
					
					insBook="INSERT INTO BOOK VALUES(XMLTYPE('<BOOKS><Book><ID>";
					insBook+=id;
					insBook+="</ID><Title>";
					tile = tile.replace("'", "''");
					insBook+=tile;
					insBook+="</Title><Author>";
					insBook+=aut;
					insBook+="</Author><Price>";
					insBook+=prc;
					insBook+="</Price><ISBN>";
					insBook+=is;
					insBook+="</ISBN><Publish_Date>";
					insBook+=pd;
					insBook+="</Publish_Date></Book></BOOKS>'))";	
					
					try
					{
						stmt.executeUpdate(insBook);
						System.out.println("1 Row Inserted Into Table BOOK");
					}
					catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
					   }catch(Exception e){
					      //Handle errors for Class.forName
					      e.printStackTrace();
					   }
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
	}
		
		try {
			 
			File fXmlFile = new File("review.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
	
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Review");
			
			for (j = 0; j < nList.getLength(); j++) 
			{
				 
				Node nNode = nList.item(j);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
		 
					Element eElement = (Element) nNode;
		 
					rid=eElement.getElementsByTagName("Review_ID").item(0).getTextContent();
					btile=eElement.getElementsByTagName("Book_Title").item(0).getTextContent();
					rat=eElement.getElementsByTagName("Rating").item(0).getTextContent();
					rev=eElement.getElementsByTagName("Reviewer").item(0).getTextContent();
					rdes=eElement.getElementsByTagName("Review_Description").item(0).getTextContent();
					rd=eElement.getElementsByTagName("Review_Date").item(0).getTextContent();
					
					// create insert string and insert the row
					insRev="INSERT INTO REVIEW VALUES(XMLTYPE('<REVIEWS><Review><Review_ID>";
					insRev+=rid;
					insRev+="</Review_ID><Book_Title>";
					btile = btile.replace("'", "''");
					insRev+=btile;
					insRev+="</Book_Title><Rating>";
					insRev+=rat;
					insRev+="</Rating><Reviewer>";
					insRev+=rev;
					insRev+="</Reviewer><Review_Description>";
					rdes = rdes.replace("'", "''");
					insRev+=rdes;
					insRev+="</Review_Description><Review_Date>";
					insRev+=rd;
					insRev+="</Review_Date></Review></REVIEWS>'))";
					try
					{
						stmt.executeUpdate(insRev);
						System.out.println("1 Row Inserted Into Table REVIEW ");
					}
					catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
					   }catch(Exception e){
					      //Handle errors for Class.forName
					      e.printStackTrace();
					   }
					
					
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
	}
		
}
}
