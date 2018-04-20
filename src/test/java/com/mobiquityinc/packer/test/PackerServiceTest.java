package com.mobiquityinc.packer.test;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.service.IPackerService;
import com.mobiquityinc.service.PackerService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class PackerServiceTest {

    @Test
    public void isLoadingInputFileHandlingFileNotFoundException() {

        IPackerService packerService = new PackerService();
        List<Package> packages = null;
        try {
             packages = packerService.loadInputFile("fileDoNotExits.txt");
        } catch (APIException e) {
            assertNotNull(e);
        }
        assertNull(null);
    }

    @Test
    public void isLoadingInputFileValidatingInputData() {

        IPackerService packerService = new PackerService();
        File file = new File(this.getClass().getResource("/inputTestInvalidData.txt").getFile());
        List<Package> packages = null;
        try {
            packages = packerService.loadInputFile(file.getPath());
        } catch (APIException e) {
            assertNull(e);
        }
        assertNull(packages.get(0));
    }

}