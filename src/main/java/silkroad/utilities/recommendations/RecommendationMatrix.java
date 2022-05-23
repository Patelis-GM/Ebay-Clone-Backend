package silkroad.utilities.recommendations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import silkroad.entities.SearchHistory;

import java.util.*;

public class RecommendationMatrix {

    private double[][] recommendationMatrix;
    private Map<Long, Integer> auctions;
    private Map<String, Integer> users;
    private final Logger logger = LoggerFactory.getLogger(RecommendationMatrix.class);

    private final double learningRate = 0.001;
    private final double regularizationPenalty = 0.09;
    private final int latentFeatures = 4;
    private final int epochs = 2048;
    private final int patience = 2;

    public RecommendationMatrix() {
        this.recommendationMatrix = null;
        this.auctions = new HashMap<>();
        this.users = new HashMap<>();
    }


    public List<Long> recommend(Authentication authentication, Integer maximumRecommendations) {

        ArrayList<Long> recommendations = new ArrayList<>();

        if (authentication == null)
            return recommendations;

        String username = authentication.getName();

        if (this.recommendationMatrix == null || !this.users.containsKey(username) || this.auctions.isEmpty())
            return recommendations;

        int userIndex = this.users.get(username);
        double[] userRecommendations = MatrixUtilities.getRow(this.recommendationMatrix, userIndex);


        long[] auctionIDS = new long[userRecommendations.length];
        for (Long auctionID : this.auctions.keySet())
            auctionIDS[this.auctions.get(auctionID)] = auctionID;

        List<UserRecommendation> userRecommendationsRecords = new ArrayList<>();
        for (int i = 0; i < auctionIDS.length; i++)
            userRecommendationsRecords.add(new UserRecommendation(auctionIDS[i], userRecommendations[i]));

        Collections.sort(userRecommendationsRecords);

        if (maximumRecommendations > userRecommendationsRecords.size())
            maximumRecommendations = userRecommendationsRecords.size();


        int index = userRecommendationsRecords.size() - 1;
        while (index >= 0 && maximumRecommendations != 0) {
            recommendations.add(userRecommendationsRecords.get(index).getAuctionID());
            maximumRecommendations--;
            index--;
        }

        return recommendations;
    }


    public void factorize(List<String> sortedUsers, List<Long> sortedAuctions, List<SearchHistory> searchHistoryRecords) {

        if (sortedUsers.isEmpty() || sortedAuctions.isEmpty() || searchHistoryRecords.isEmpty())
            return;

        int totalUsers = sortedUsers.size();
        int totalAuctions = sortedAuctions.size();


        int userIndex = 0;
        HashMap<String, Integer> userMap = new HashMap<>();
        for (String user : sortedUsers) {
            userMap.put(user, userIndex);
            userIndex += 1;
        }

        int auctionIndex = 0;
        HashMap<Long, Integer> auctionMap = new HashMap<>();
        for (Long auction : sortedAuctions) {
            auctionMap.put(auction, auctionIndex);
            auctionIndex += 1;
        }

        logger.info("Total Users : " + totalUsers);
        logger.info("Total Auctions : " + totalAuctions);
        logger.info("Users : " + userMap);
        logger.info("Auctions : " + auctionMap);

        double[][] matrix = new double[totalUsers][totalAuctions];
        for (SearchHistory record : searchHistoryRecords) {
            System.out.println(record.getId());
            String username = record.getId().getUserId();
            Long auction = record.getId().getAuctionId();
            double interactions = (double) record.getInteractions();
            matrix[userMap.get(username)][auctionMap.get(auction)] = interactions;
        }


        MatrixUtilities.normalise(matrix, totalUsers, totalAuctions);


        double[][] V = MatrixUtilities.randomMatrix(totalUsers, this.latentFeatures);
        double[][] F = MatrixUtilities.randomMatrix(this.latentFeatures, totalAuctions);
        double previousValidationRMSE = Double.MAX_VALUE;
        int iterations = 0;
        int outOfPatience = 0;

        for (int epoch = 0; epoch < this.epochs; epoch++) {

            iterations++;


            for (int i = 0; i < totalUsers; i++)
                for (int j = 0; j < totalAuctions; j++)
                    if (!isInValidationSet(i, j, totalUsers, totalAuctions) && matrix[i][j] > 0.0) {
                        double[] predictionRow = MatrixUtilities.getRow(V, i);
                        double[] predictionColumn = MatrixUtilities.getColumn(F, j);

                        double prediction = MatrixUtilities.makePrediction(predictionRow, predictionColumn);
                        double eij = matrix[i][j] - prediction;

                        for (int k = 0; k < this.latentFeatures; k++) {

                            double vUpdate = V[i][k] + this.learningRate * (2.0 * eij * F[k][j] - this.regularizationPenalty * V[i][k]);
                            double fUpdate = F[k][j] + this.learningRate * (2.0 * eij * V[i][k] - this.regularizationPenalty * F[k][j]);

                            V[i][k] = vUpdate;
                            F[k][j] = fUpdate;
                        }


                    }


            double validationRMSE = 0.0;
            double validationSetCardinality = 0.0;

            for (int i = 0; i < totalUsers; i++)
                for (int j = 0; j < totalAuctions; j++)
                    if (isInValidationSet(i, j, totalUsers, totalAuctions) && matrix[i][j] > 0.0) {

                        double[] predictionRow = MatrixUtilities.getRow(V, i);
                        double[] predictionColumn = MatrixUtilities.getColumn(F, j);

                        double prediction = MatrixUtilities.makePrediction(predictionRow, predictionColumn);
                        double eij = matrix[i][j] - prediction;

                        validationRMSE += Math.pow(eij, 2.0);
                        validationSetCardinality += 1.0;
                    }


            if (validationSetCardinality > 0.0 && validationRMSE > 0.0) {

                validationRMSE /= validationSetCardinality;
                validationRMSE = Math.sqrt(validationRMSE);

                if (validationRMSE < previousValidationRMSE) {
                    previousValidationRMSE = validationRMSE;
                    outOfPatience = 0;
                } else
                    outOfPatience += 1;

                if (outOfPatience >= patience)
                    break;

            }


        }

        this.recommendationMatrix = MatrixUtilities.multiplyMatrices(V, F, totalUsers, totalAuctions, this.latentFeatures);
        this.auctions = auctionMap;
        this.users = userMap;

        logger.info(MatrixUtilities.toString(matrix, totalUsers, totalAuctions));
        logger.info(MatrixUtilities.toString(recommendationMatrix, totalUsers, totalAuctions));
        logger.info("Total iterations : " + iterations);
    }

    private static boolean isInValidationSet(int i, int j, int rows, int columns) {
        return (i < (rows / 2) && j < (columns / 2));
    }


}
