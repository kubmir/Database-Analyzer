package cz.muni.fi.bakalarka1.Demo;

import cz.muni.fi.bakalarka1.Database.SqlDb;
import cz.muni.fi.bakalarka1.Utils.ServiceFailureException;
import cz.muni.fi.bakalarka1.Utils.XSLTProcessor;
import java.sql.SQLException;

/**
 * Class which serve for demonstration of functions.
 * @author Miroslav Kubus
 */
public class Main {

    public static void main(String[] args) throws ServiceFailureException, SQLException {
        String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\Debug.db";
        //String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Debug_velka.db";
        SqlDb test = new SqlDb(pathToDB);
        test.testAccessDB();
        System.out.println("Done!");
    }
    
    public static void main1(String[] args) throws ServiceFailureException, SQLException {
        String pathToDrbo = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\port_587.drbo";
        String pathToHtml = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\database.html";
        String pathToXSLT = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\XSLTsablona.xsl";

        XSLTProcessor pro = new XSLTProcessor();
        pro.transformToHtml(pathToXSLT, pathToDrbo, pathToHtml);
        pro.openHtml(pathToHtml);
    }
    
}
