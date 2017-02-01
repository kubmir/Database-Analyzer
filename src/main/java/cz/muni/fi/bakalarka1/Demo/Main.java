package cz.muni.fi.bakalarka1.Demo;

import cz.muni.fi.bakalarka1.Database.SqlDb;
import java.sql.SQLException;

/**
 * Class which serve for demonstration of functions.
 * @author Miroslav Kubus
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\testDB.db";
        SqlDb test = new SqlDb();
        test.registerJDBCDriver();
        test.testAccessDB(pathToDB);
        System.out.println("Done!");
    }
}
