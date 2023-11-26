package com.mastering.lambdas.misc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.OptionalInt;

class Solution {

    public int minPathSumWrong(Integer[][] grid) {
        int sum = 0;
        int intermediateResult = 0;
        int i, j = 0;
        for (i = intermediateResult; i < grid.length; i++) {
            for (j = i; j < grid.length; j++) {

                if ((i + 1 < grid.length) && (j + 1 < grid.length)) {
                    if (grid[i][j + 1] >= grid[i + 1][j]) {
                        intermediateResult = i + 1;
                        sum += grid[i + 1][j];
                    } else {
                        intermediateResult = j + 1;
                        sum += grid[i][j + 1];
                    }
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(":");
        Integer[][] matrix = {
                {1, 3, 1},
                {1, 5, 1},
                {1, 6, 1}
        };
        Integer[][] matrixExample = {
                {1, 3, 21},
                {1, 5, 6},
                {14, 3, 10}
        };
        Solution s = new Solution();
        int sum = s.minPathSumDirty(matrixExample);
        System.out.println(sum);
    }

    private int minPathSum(Integer[][] matrixExample) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now.format(DateTimeFormatter.ofPattern("MMMM dd")));
        return 0;
    }

    private int minPathSumDirty(Integer[][] matrix) {
        int path1 = matrix[0][0] + matrix[0][1] + matrix[0][2] + matrix[1][2] + matrix[2][2];
        int path2 = matrix[0][0] + matrix[0][1] + matrix[1][1] + matrix[1][2] + matrix[2][2];
        int path3 = matrix[0][0] + matrix[0][1] + matrix[1][1] + matrix[2][1] + matrix[2][2];
        int path4 = matrix[0][0] + matrix[1][0] + matrix[1][1] + matrix[1][2] + matrix[2][2];
        int path5 = matrix[0][0] + matrix[1][0] + matrix[1][1] + matrix[2][1] + matrix[2][2];
        int path6 = matrix[0][0] + matrix[1][0] + matrix[2][0] + matrix[2][1] + matrix[2][2];

        int[] resultArray = {path1, path2, path3, path4, path5, path6};
        OptionalInt result = Arrays.stream(resultArray).min();

        return result.getAsInt();
    }
}
