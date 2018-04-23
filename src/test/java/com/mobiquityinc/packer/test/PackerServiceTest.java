package com.mobiquityinc.packer.test;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.packer.Packer;
import com.mobiquityinc.service.IPackerService;
import com.mobiquityinc.service.PackerService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PackerServiceTest {

    private final static Logger logger = Logger.getLogger(PackerServiceTest.class);

    private IPackerService packerService;

    @BeforeAll
    public void loadServiceContext() {
        packerService = new PackerService();
    }

    @Test
    public void isLoadingInputFileHandlingFileNotFoundException() {

        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile("fileDoNotExits.txt");
        } catch (APIException e) {
            logger.error(e.getMessage());
            assertNotNull(e);
        }
        assertNull(packages);
    }

    @Test
    public void isLoadingInputFileValidatingInputDataFormat() {

        File file = new File(this.getClass().getResource("/inputTestInvalidData.txt").getFile());
        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile(file.getPath());
        } catch (APIException e) {
            logger.error(e.getMessage());
            assertNotNull(e);
        }
        assertNull(packages);
    }

    @Test
    public void isLoadingInputFileValidatingWeightData() {

        File file = new File(this.getClass().getResource("/inputTestInvalidWeightData.txt").getFile());
        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile(file.getPath());
        } catch (APIException e) {
            logger.error(e.getMessage());
            assertNotNull(e);
        }
        assertNull(packages);
    }

    @Test
    public void isLoadingInputFileValidatingCostData() {

        File file = new File(this.getClass().getResource("/inputTestInvalidCostData.txt").getFile());
        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile(file.getPath());
        } catch (APIException e) {
            logger.error(e.getMessage());
            assertNotNull(e);
        }
        assertNull(packages);
    }

    @Test
    public void isLoadingInputFileValidatingLimitNumberOfItems() {

        File file = new File(this.getClass().getResource("/inputTestInvalidNumberOfItems.txt").getFile());
        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile(file.getPath());
        } catch (APIException e) {
            logger.error(e.getMessage());
            assertNotNull(e);
        }
        assertNull(packages);
    }

    @Test
    public void isLoadingInputFile() {

        File file = new File(this.getClass().getResource("/inputTest.txt").getFile());
        List<Package> packages = null;
        packages = packerService.loadInputFile(file.getPath());
        assertEquals(packages.size(), 4);
        assertEquals(packages.get(0).getItems().size(), 6);
        assertEquals(packages.get(1).getItems().size(), 1);
        assertEquals(packages.get(2).getItems().size(), 9);
        assertEquals(packages.get(3).getItems().size(), 9);
    }

    @Test
    public void isPacking() {

        File file = new File(this.getClass().getResource("/inputTest.txt").getFile());
        String[] args = {file.getPath()};
        String packages = Packer.pack(args);
        String expectedResult = "4\n" +
                                "-\n" +
                                "2,7\n" +
                                "8,9\n";
        assertEquals(packages.equals(expectedResult), Boolean.TRUE);
    }

}