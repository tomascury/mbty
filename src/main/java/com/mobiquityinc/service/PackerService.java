package com.mobiquityinc.service;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.PackageItem;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PackerService implements IPackerService {

    private final static Logger logger = Logger.getLogger(PackerService.class);

    @Override
    public List<Package> loadInputFile(String filePath) throws APIException {

        List<Package> packages;
        try {
            File inputFile = new File(filePath);
            InputStream fileInputStream = new FileInputStream(inputFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            packages = bufferedReader.lines().map(mapToItem).collect(Collectors.toList());
            System.out.println(packages);
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            throw new APIException("File [" + filePath + "] Not Found");
        } catch (IOException e) {
            throw new APIException("Error processing file " + filePath);
        } catch (Exception e){
            throw new APIException("Error loading Input File", e);
        }
        return packages;
    }

    private Function<String, Package> mapToItem = (line) -> {

        String maxWeight = groupMatcher("(^\\d+)\\s*:", line);

        if (maxWeight != null) {

            Package aPackage = new Package();

            aPackage.setMaxWeight(new BigDecimal(maxWeight));
            aPackage.setItems(new ArrayList<PackageItem>());

            List<String> packageItemsStr = groupsMatcher("(\\d{1,10},\\d{1,10}\\.\\d{1,10},.{1}\\d{1,10})", line);

            if (packageItemsStr != null) {

                for (String packageItems : packageItemsStr) {

                    PackageItem packageItem = new PackageItem();
                    String[] packageItemsDetails = packageItems.split(",");

                    if (packageItemsDetails[0] == null || packageItemsDetails[1] == null || packageItemsDetails[2] == null) {
                        logger.error("Invalid item: " + packageItems);
                    } else {
                        packageItem.setIndex(Integer.valueOf(packageItemsDetails[0]));
                        packageItem.setWeight(new BigDecimal(packageItemsDetails[1]));
                        packageItem.setCost(new BigDecimal(packageItemsDetails[2].replaceAll("[^\\d]*", "")));
                        aPackage.getItems().add(packageItem);
                    }
                }
                return aPackage;
            }
        } else {
            logger.error("Invalid Weight Limit: " + line);
        }
        return null;
    };

    private List<String> groupsMatcher(String regex, String value) {

        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    private String groupMatcher(String regex, String value) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.find() ? matcher.group(1) : null;
    }
}
