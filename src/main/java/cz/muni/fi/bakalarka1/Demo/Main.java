package cz.muni.fi.bakalarka1.Demo;

import cz.muni.fi.bakalarka1.Database.SqlDb;
import cz.muni.fi.bakalarka1.Utils.ServiceFailureException;
import java.sql.SQLException;

/**
 * Class which serve for demonstration of functions.
 * @author Miroslav Kubus
 */
public class Main {

    public static void main(String[] args) throws ServiceFailureException, SQLException {
        String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\Debug.db";
        //String pathToDrbo = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\port_587.drbo";
        SqlDb test = new SqlDb(pathToDB);
        test.testAccessDB();
        System.out.println("Done!");
    }
}
