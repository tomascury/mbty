package com.mobiquityinc.service;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;

import java.util.List;

public interface IPackerService {

    List<Package> loadInputFile(String path) throws APIException;
}
