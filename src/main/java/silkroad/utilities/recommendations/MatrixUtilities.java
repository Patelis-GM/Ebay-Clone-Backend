package silkroad.utilities.recommendations;

import java.util.Random;

public class MatrixUtilities {

    public static void normalise(double[][] matrix, int rows, int columns) {

        double maxElement = Double.MIN_VALUE;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                if (matrix[i][j] > maxElement)
                    maxElement = matrix[i][j];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                matrix[i][j] /= maxElement;
    }

    public static double[][] multiplyMatrices(double[][] V, double[][] F, int rows, int columns, int latentFeatures) {

        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                result[i][j] = 0.0;
                for (int k = 0; k < latentFeatures; k++)
                    result[i][j] += V[i][k] * F[k][j];
            }

        return result;
    }

    public static double[] getRow(double[][] matrix, int index) {
        return matrix[index];
    }

    public static double[] getColumn(double[][] matrix, int index) {

        double[] column = new double[matrix.length];

        for (int i = 0; i < matrix.length; i++)
            column[i] = matrix[i][index];

        return column;
    }

    public static double makePrediction(double[] row, double[] column) {

        double prediction = 0.0;

        for (int i = 0; i < row.length; i++)
            prediction += (row[i] * column[i]);

        return prediction;
    }

    public static double[][] randomMatrix(int rows, int columns) {

        double[][] matrix = new double[rows][columns];
        Random random = new Random();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                matrix[i][j] = random.nextDouble();

        return matrix;
    }

    public static String toString(double[][] values, int rows, int columns) {

        StringBuilder stringBuilder = new StringBuilder("\n");

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                stringBuilder.append(String.format("%4f", values[i][j]));
                if (j == columns - 1)
                    stringBuilder.append("\n");
                else
                    stringBuilder.append(" ");
            }

        stringBuilder.append("\n");

        return stringBuilder.toString();

    }

}
