package org.appland.settlers.fuzzing;

import java.io.InputStream;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.appland.settlers.maps.MapFile;
import org.appland.settlers.maps.MapLoader;
import org.appland.settlers.maps.SettlersMapLoadingException;
import org.junit.Assume;
import org.junit.runner.RunWith;

@RunWith(JQF.class)

public class SettlersMapLoaderJfqDriver {

    @Fuzz /* JQF will generate inputs to this method */
    public void testRead(InputStream input) throws Exception {
        // Create parser
        MapLoader mapLoader = new MapLoader();

        try {
            MapFile mapFile = mapLoader.loadMapFromStream(input);
        } catch (SettlersMapLoadingException e) {
            e.printStackTrace();
            Assume.assumeNoException(e);
        }
    }
}
