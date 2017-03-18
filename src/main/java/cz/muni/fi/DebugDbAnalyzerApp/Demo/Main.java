package cz.muni.fi.DebugDbAnalyzerApp.Demo;

import cz.muni.fi.DebugDbAnalyzerApp.Database.DatabaseAccessManagerImpl;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.Visualizer;
import java.io.File;
import java.sql.SQLException;

/**
 * Class which serve for demonstration of functions.
 * @author Miroslav Kubus
 */
public class Main {

    public static void main(String[] args) throws ServiceFailureException, SQLException {
        //String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\Debug.db";
        String pathToDB = "C:\\Users\\Miroslav Kubus\\Desktop\\Neni zavedena vrstva\\Debug.db";
        String pathToDbFolder = pathToDB.substring(0, pathToDB.lastIndexOf(File.separator));

        DatabaseAccessManagerImpl test = new DatabaseAccessManagerImpl(pathToDB, pathToDbFolder);
        test.testAccessDB();

        Visualizer vis = new Visualizer();
        vis.toWeb(pathToDbFolder);

        System.out.println("Done!");
    }
}
