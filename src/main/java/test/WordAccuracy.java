package test;

/**
 * @author wangguochao
 * @date 2022/7/27
 */
public class WordAccuracy {



    public static String filterWords(String words) {

        String a = words.split("")[0];
        return a;
    }

    /**
     * 计算编辑距离
     *
     * @param stWords
     * @param clientWords
     * @return
     */
    public static double calcAccuracy(String stWords, String clientWords) {
        String stWordsFilter = filterWords(stWords);
        String clientWordsFilter = filterWords(clientWords);
        int stWordsLength = stWordsFilter.length();
        int clientWordsLength = clientWordsFilter.length();
        if (stWordsFilter.length() <= 0 || clientWordsFilter.length() <= 0) {
            return 0;
        }
        int dp[][] = new int[stWordsLength][clientWordsLength];
        if (stWordsFilter.charAt(0) == clientWordsFilter.charAt(0)) {
            dp[0][0] = 0;
        } else {
            dp[0][0] = 1;
        }
        System.out.println(clientWordsLength);
        for (int i = 1; i < clientWordsLength; i++) {
            dp[0][i] = dp[0][i - 1] + 1;
        }

        for (int j = 1; j < stWordsLength; j++) {
            dp[j][0] = dp[j - 1][0] + 1;
        }

        for (int i = 1; i < stWordsLength; i++) {
            for (int j = 1; j < clientWordsLength; j++) {
//                System.out.println(dp[i][j]);
                int cost;
                if (stWordsFilter.charAt(i) == clientWordsFilter.charAt(j)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                int add = dp[i - 1][j] + 1;
                int sub = dp[i][j - 1] + 1;
                int rep = dp[i - 1][j - 1] + cost;

                if (rep <= add && rep <= sub) {
                    dp[i][j] = rep;
                } else if (sub <= add && sub <= rep) {
                    dp[i][j] = sub;
                } else {
                    dp[i][j] = add;
                }
            }
        }
        double accuracy = 1d - (double) dp[stWordsLength - 1][clientWordsLength - 1] / (double) stWordsLength;
        return accuracy;
    }
}
