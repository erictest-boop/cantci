package edu.illinois.library.cantaloupe.processor.codec.jpeg2000;

import edu.illinois.library.cantaloupe.image.Rectangle;
import edu.illinois.library.cantaloupe.image.ScaleConstraint;
import edu.illinois.library.cantaloupe.image.iptc.DataSet;
import edu.illinois.library.cantaloupe.image.iptc.Reader;
import edu.illinois.library.cantaloupe.operation.ReductionFactor;
import edu.illinois.library.cantaloupe.operation.Scale;
import edu.illinois.library.cantaloupe.operation.ScaleByPercent;
import edu.illinois.library.cantaloupe.processor.SourceFormatException;
import edu.illinois.library.cantaloupe.test.BaseTest;
import edu.illinois.library.cantaloupe.test.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess") // a JMH test extends this
public class JPEG2000KakaduImageReaderTest extends BaseTest {

    private static final double DELTA = 0.0000001;

    private JPEG2000KakaduImageReader instance;

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        instance = new JPEG2000KakaduImageReader();
        instance.setSource(TestUtil.getImage("jp2"));
    }

    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
        instance.close();
    }

    @Test
    public void testGetHeight() throws Exception {
        assertEquals(56, instance.getHeight());
    }

    @Test
    public void testGetHeightWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getHeight());
    }

    @Test
    public void testGetIPTC() throws Exception {
        instance.setSource(TestUtil.getImage("jp2-iptc.jp2"));
        try (Reader reader = new Reader()) {
            reader.setSource(instance.getIPTC());
            List<DataSet> dataSets = reader.read();
            assertEquals(2, dataSets.size());
        }
    }

    @Test
    public void testGetIPTCWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getIPTC());
    }

    @Test
    public void testGetNumDecompositionLevels() throws Exception {
        assertEquals(5, instance.getNumDecompositionLevels());
    }

    @Test
    public void testGetNumDecompositionLevelsWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class,
                () -> instance.getNumDecompositionLevels());
    }

    @Test
    public void testGetTileHeight() throws Exception {
        assertEquals(56, instance.getTileHeight());
    }

    @Test
    public void testGetTileHeightWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getTileHeight());
    }

    @Test
    public void testGetTileWidth() throws Exception {
        assertEquals(64, instance.getTileWidth());
    }

    @Test
    public void testGetTileWidthWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getTileWidth());
    }

    @Test
    public void testGetWidth() throws Exception {
        assertEquals(64, instance.getWidth());
    }

    @Test
    public void testGetWidthWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getWidth());
    }

    @Test
    public void testGetXMP() throws Exception {
        instance.setSource(TestUtil.getImage("jp2-xmp.jp2"));
        String xmp = instance.getXMP();
        assertTrue(xmp.startsWith("<rdf:RDF"));
        assertTrue(xmp.endsWith("</rdf:RDF>"));
    }

    @Test
    public void testGetXMPWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));
        assertThrows(SourceFormatException.class, () -> instance.getXMP());
    }

    @Test
    public void testReadRegion() throws Exception {
        final Rectangle roi = new Rectangle(0, 0, 32, 28);
        final Scale scale = new ScaleByPercent(0.45);
        final ScaleConstraint scaleConstraint = new ScaleConstraint(1, 2);
        final ReductionFactor reductionFactor = new ReductionFactor();
        final double[] diffScales = new double[2];

        BufferedImage image = instance.readRegion(roi, scale, scaleConstraint,
                reductionFactor, diffScales);
        assertEquals(7, image.getWidth());
        assertEquals(6, image.getHeight());
        assertEquals(2, reductionFactor.factor);
        assertEquals(0.9, diffScales[0], DELTA);
        assertEquals(0.9, diffScales[1], DELTA);
    }

    @Test
    public void testReadRegionWithInvalidImage() throws Exception {
        instance.setSource(TestUtil.getImage("unknown"));

        final Rectangle roi = new Rectangle(0, 0, 32, 28);
        final Scale scale = new ScaleByPercent(0.45);
        final ScaleConstraint scaleConstraint = new ScaleConstraint(1, 2);
        final ReductionFactor reductionFactor = new ReductionFactor();
        final double[] diffScales = new double[2];

        assertThrows(SourceFormatException.class, () ->
                instance.readRegion(roi, scale, scaleConstraint,
                        reductionFactor, diffScales));
    }

}
