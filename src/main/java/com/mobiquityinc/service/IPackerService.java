package com.mobiquityinc.service;

import com.mobiquityinc.model.Package;

import java.util.List;

public interface IPackerService {

    String pack(String inputFilePath);

    List<Package> loadInputFile(String path);
}
