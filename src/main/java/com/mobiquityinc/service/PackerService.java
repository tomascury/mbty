package com.mobiquityinc.service;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Package;
import com.mobiquityinc.model.PackageItem;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PackerService implements IPackerService {

    private final static Logger logger = Logger.getLogger(PackerService.class);

    /**
     * Filter and validate the list of package items by the package weight limit and cost
     *
     * @param inputFilePath
     * @return @{@link String} The index list of the packages
     */
    @Override
    public String pack(String inputFilePath) {

        if (inputFilePath == null){
            throw new APIException("Input file path is mandatory");
        }

        List<Package> packages = loadInputFile(inputFilePath);

        AtomicReference<String> packageResultIndex = new AtomicReference<>("");

        packages.forEach(_package -> _package.getItems().sort(Comparator.comparing(PackageItem::getCost).reversed().thenComparing(PackageItem::getWeight)));

        logger.debug("packages sorted: " + packages);

        packages.forEach(_package -> {

            BigDecimal maxWeight = _package.getMaxWeight();
            AtomicReference<BigDecimal> packageWeight = new AtomicReference<>(new BigDecimal("0"));
            AtomicReference<String> packageItemsResult = new AtomicReference<>("");
            _package.getItems().forEach(item ->{

                if (item.getWeight().compareTo(maxWeight) == -1 && packageWeight.get().compareTo(maxWeight) == -1){

                    packageWeight.getAndSet(packageWeight.get().add(item.getWeight()));

                    if (packageWeight.get().compareTo(maxWeight) == -1){

                        packageItemsResult.getAndSet(packageItemsResult.get().isEmpty() ? packageItemsResult.get() + item.getIndex()
                                :  packageItemsResult.get() + "," + item.getIndex());

                    } else {
                        packageWeight.getAndSet(packageWeight.get().subtract(item.getWeight()));
                    }
                }
            });
            packageResultIndex.getAndSet(packageResultIndex.get() + (packageItemsResult.get().isEmpty() ? "-" + System.lineSeparator()
                    : packageItemsResult.get() + System.lineSeparator()));
        });

        return packageResultIndex.get();
    }

    @Override
    public List<Package> loadInputFile(String filePath) {

        List<Package> packages;
        try {
            File inputFile = new File(filePath);
            InputStream fileInputStream = new FileInputStream(inputFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            packages = bufferedReader.lines().map(processPackages).collect(Collectors.toList());
            bufferedReader.close();
        } catch (IOException e) {
            throw new APIException("Error processing input file: " + e.getMessage());
        }
        return packages;
    }

    /**
     * Process all lines in the input file, and parses it to the model @{@link Package}
     */
    private Function<String, Package> processPackages = (line) -> {

        //REGEX responsible for retrieve the maxWeight in the line
        String maxWeight = groupMatcher("(^\\d+)\\s*:", line);

        if (maxWeight != null) {

            Package aPackage = new Package();

            aPackage.setMaxWeight(new BigDecimal(maxWeight));
            aPackage.setItems(new ArrayList<PackageItem>());

            //REGEX responsible for retrieve all package items available in the line
            List<String> packageItemsStr = groupsMatcher("(\\d{1,10},\\d{1,10}\\.?\\d{0,10},.{1}\\d{1,10}\\.?\\d{0,10})", line);

            if (packageItemsStr != null) {

                for (String packageItems : packageItemsStr) {

                    String[] packageItemsDetails = packageItems.split(",");

                    Integer itemIndex = Integer.valueOf(packageItemsDetails[0] != null ? packageItemsDetails[0] : "-1");
                    BigDecimal itemWeight = new BigDecimal(packageItemsDetails[1] != null ? packageItemsDetails[1] : "-1");
                    BigDecimal itemCost = new BigDecimal(packageItemsDetails[2] != null ? packageItemsDetails[2].replaceAll("^\\D{1}", "") : "-1");

                    if (itemIndex == -1 || itemWeight.compareTo(new BigDecimal("-1")) == 0 || itemCost.compareTo(new BigDecimal("-1")) == 0) {

                        throw new APIException("Invalid item: " + packageItemsDetails);

                    } if (itemWeight.compareTo(new BigDecimal("100")) == 1) {

                        throw new APIException("Invalid weight item: " + itemWeight + " at line [" + line + "]");

                    } if (itemCost.compareTo(new BigDecimal("100")) == 1) {

                        throw new APIException("Invalid cost item: " + itemCost + " at line [" + line + "]");

                    } else {

                        PackageItem packageItem = new PackageItem();
                        packageItem.setIndex(itemIndex);
                        packageItem.setWeight(itemWeight);
                        packageItem.setCost(itemCost);
                        aPackage.getItems().add(packageItem);
                    }
                }
                return aPackage;
            }
        } else {
            throw new APIException("Invalid weight limit data: " + " at line [" + line + "]");
        }
        return null;
    };

    /**
     * Matches all groups in the given input against a REGEX pattern.
     *
     * @param regex
     * @param value
     * @return @{@link List<String>} REGEX groups matched for this pattern
     */
    private List<String> groupsMatcher(String regex, String value) {

        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        int matchCounter = 0;

        while (matcher.find()) {

            matchCounter++;

            if (matchCounter > 15){
                throw new APIException("Number of package items exceeded: " + matchCounter);
            }
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * Matches the given input against a REGEX pattern.
     *
     * @param regex
     * @param value
     * @return @{@link String} REGEX group matched for this pattern
     */
    private String groupMatcher(String regex, String value) {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.find() ? matcher.group(1) : null;
    }
}
