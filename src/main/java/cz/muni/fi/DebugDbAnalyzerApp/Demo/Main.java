package cz.muni.fi.DebugDbAnalyzerApp.Demo;

import cz.muni.fi.DebugDbAnalyzerApp.Database.DatabaseAccessManagerImpl;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.Visualizer;
import java.sql.SQLException;

/**
 * Class which serve for demonstration of functions.
 * @author Miroslav Kubus
 */
public class Main {

    public static void main(String[] args) throws ServiceFailureException, SQLException {
        String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\Debug.db";
        //String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Databázy\\Stredne velka debug.db";
        
        DatabaseAccessManagerImpl test = new DatabaseAccessManagerImpl(pathToDB);
        test.testAccessDB();

        Visualizer vis = new Visualizer();
        vis.toWeb();

        System.out.println("Done!");
    }
}
