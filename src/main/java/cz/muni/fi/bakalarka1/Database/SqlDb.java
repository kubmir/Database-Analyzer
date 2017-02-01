package cz.muni.fi.bakalarka1.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class which provide access to database defined by absolute path.
 * @author Miroslav Kubus
 */
public class SqlDb {
        
    /**
     * Method which load sqlite jdbc driver's class file into the memory.
     */
    public void registerJDBCDriver() {
        try {
            Class.forName("org.sqlite.JDBC"); //Ak zmenim driver a budem mat problem tak nutne pridat dependencies do pom.xml
        } 
        catch (ClassNotFoundException ex) {
            System.err.println("Error: problem while loading driver class!");
            System.err.println("Exception: " + ex.toString());
            System.exit(1);
        }
    } 
    
    /**
     * Method which establish connection to database file
     * @param pathToDB path to chosen database file
     * @return connection to database file defined by pathToDB. Null in case of error
     */
    public Connection establishDBConnection(String pathToDB) {
        String urlPath = this.modifySlashes(pathToDB);
        
        if(urlPath != null) {
            try {
                Connection con = DriverManager.getConnection("jdbc:sqlite:/" + urlPath); //username a password??
                return con;
            } 
            catch (SQLException ex) {
                System.err.println("Error: unable to establish database connection!");
                System.err.println(ex);
                return null;
            }
        } else {
            System.err.println("Error: Invalid file path. After modification url is NULL!");
            return null;
        }
    }
    
    /**
     * Method which access all cars in table car in database
     * @param pathToDB represents absolute path to .db file
     */
    public void testAccessDB(String pathToDB) {
    
        try(Connection con = this.establishDBConnection(pathToDB);
            PreparedStatement statement = con.prepareStatement("SELECT * FROM car")) {
            
            ResultSet rs = statement.executeQuery();
            int i = 1;
            while(rs.next()) {
                System.out.println("Car number " + i);
                System.out.println("Producer: " + rs.getString(rs.findColumn("producer")));
                System.out.println("Model: " + rs.getString(rs.findColumn("model")));
                System.out.println("Year: " + rs.getInt(rs.findColumn("year")));
                System.out.println("SPZ: " + rs.getString(rs.findColumn("licencePlate")));
                System.out.println();
                i++;
            }
        }
        catch(SQLException ex) {
            System.err.println("Error: SQL error while accessing database!");
            System.err.println(ex);
        } 
    }

    /**
     * This method change file path to url path - WINDOWS: replace \ with /
     * @param stringToModify represents path to .db file
     * @return string where all \ are replaced with /
     */
    private String modifySlashes(String stringToModify) {
        return stringToModify.replace(File.separator, "/");
    }    
}