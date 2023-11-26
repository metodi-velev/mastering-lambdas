package com.mastering.lambdas.misc;

import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class Result {

    /*
     * Complete the 'priceCheck' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. STRING_ARRAY products
     *  2. FLOAT_ARRAY productPrices
     *  3. STRING_ARRAY productSold
     *  4. FLOAT_ARRAY soldPrice
     */

    public static int priceCheck(List<String> products, List<Float> productPrices, List<String> productSold, List<Float> soldPrice) {
        int numberErrors = 0;
        // Write your code here
        for (String productSoldItem : productSold) {
            int indexSold = productSold.indexOf(productSoldItem);
           int index = products.indexOf(productSoldItem);
           float productPrice = productPrices.get(index).floatValue();
           Float soldPriceItem = soldPrice.get(indexSold).floatValue();
           if(productPrice != soldPriceItem) {
               numberErrors++;
           }
        }
        return numberErrors;
    }

}

class Solution1 {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        int productsCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> products = IntStream.range(0, productsCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int productPricesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Float> productPrices = IntStream.range(0, productPricesCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Float::parseFloat)
                .collect(toList());

        int productSoldCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<String> productSold = IntStream.range(0, productSoldCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(toList());

        int soldPriceCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Float> soldPrice = IntStream.range(0, soldPriceCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Float::parseFloat)
                .collect(toList());

        int result = Result.priceCheck(products, productPrices, productSold, soldPrice);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}
