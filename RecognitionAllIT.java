/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

/**
 *
 * @author Alexander
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private File plateFile;
    private String expectedPlate;

    public RecognitionAllIT(File plateFile, String expectedPlate) {
        this.plateFile = plateFile;
        this.expectedPlate = expectedPlate;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testDataCreator() throws FileNotFoundException, IOException {
        //Figure out how to get required info, it’s all in the existing tests
        //...
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));
        
        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();
        assertThat(properties.size(), greaterThan(0));
        
        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();
        
        Collection<Object[]> dataForOneImage = new ArrayList();
        for (File file : snapshots) {
            String name = file.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{file, plateExpected});
        }
        return dataForOneImage;
    }
    
    @Test
    public void testAllPlates() throws IOException, ParserConfigurationException, SAXException{
        CarSnapshot car = new CarSnapshot(new FileInputStream(plateFile));
        
        Intelligence intel = new Intelligence();
        String spz = intel.recognize(car);
        
        assertThat(expectedPlate, equalTo(spz));
    }

}
