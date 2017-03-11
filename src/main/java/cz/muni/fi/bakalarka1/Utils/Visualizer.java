package cz.muni.fi.bakalarka1.Utils;

import java.sql.SQLException;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class Visualizer {
    
    public void toWeb() throws ServiceFailureException, SQLException {
        String pathToXML = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\myXmlTest.xml";
        String pathToHtml = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\database.html";
        String pathToXSLT = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\XSLTsablona.xsl";

        XSLTProcessor pro = new XSLTProcessor();
        pro.transformToHtml(pathToXSLT, pathToXML, pathToHtml);
        pro.openHtml(pathToHtml);
    }
}
