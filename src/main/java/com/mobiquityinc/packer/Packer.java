package com.mobiquityinc.packer;

import com.mobiquityinc.service.IPackerService;
import com.mobiquityinc.service.PackerService;

public class Packer {

    public static String pack(String[] args){

        IPackerService packerService = new PackerService();

        return packerService.pack(args[0]);
    }

}
