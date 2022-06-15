package utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordAccuracy {

//    public static void main(String[] args) throws IOException {
    public static float  ComparedFile(String standardFile, String clientFile ) {

        try{
            // 读取标准和对比文件内容
            BufferedInputStream  sdWordStream = new BufferedInputStream(new FileInputStream(standardFile));
            byte[] sdWordsBytes = new byte[sdWordStream.available()];
            sdWordStream.read(sdWordsBytes);
            String stWords = new String(sdWordsBytes);

            BufferedInputStream clientWordStream =  new BufferedInputStream(new FileInputStream(clientFile));
            byte[] clientWordsBytes = new byte[clientWordStream.available()];
            clientWordStream.read(clientWordsBytes);
            String clientWords = new String(clientWordsBytes);
            // 计算文件字正确率
            float accuracy = CalcAccuracy(stWords, clientWords);
            sdWordStream.close();

            return accuracy;
        } catch (FileNotFoundException fe){
            fe.printStackTrace();
            return 0.0F;
        } catch (IOException e){
            e.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * 计算编辑距离
     * @param stWords
     * @param clientWords
     * @return
     */
    public static float CalcAccuracy(String stWords, String clientWords){
        String stWordsFilter = FilterWords(stWords);
        String clientWordsFilter = FilterWords(clientWords);
        int stWordsLength = stWordsFilter.length();
        int clientWordsLength = clientWordsFilter.length();
        if(stWordsFilter.length() <= 0 || clientWordsFilter.length() <=0){
            return 0;
        }
        int dp[][] = new int[stWordsLength][clientWordsLength];
        if(stWordsFilter.charAt(0) == clientWordsFilter.charAt(0)){
            dp[0][0] = 0;
        }else {
            dp[0][0] = 1;
        }
        System.out.println(clientWordsLength);
        for(int i=1; i<clientWordsLength; i++){
            dp[0][i] = dp[0][i-1] + 1;
        }

        for(int j=1; j<stWordsLength; j++){
            dp[j][0] = dp[j-1][0] + 1;
        }

        for(int i=1; i<stWordsLength; i++){
            for (int j=1; j<clientWordsLength; j++){
//                System.out.println(dp[i][j]);
                int cost;
                if(stWordsFilter.charAt(i) == clientWordsFilter.charAt(j)){
                    cost = 0;
                } else {
                    cost = 1;
                }
                int add = dp[i-1][j] + 1;
                int sub = dp[i][j-1] + 1;
                int rep = dp[i-1][j-1] + cost;

                if(rep <= add && rep <= sub){
                    dp[i][j] = rep;
                }else if(sub <= add && sub <= rep){
                    dp[i][j] = sub;
                }else {
                    dp[i][j] = add;
                }
            }
        }
        float accuracy = 1f - (float)dp[stWordsLength - 1][clientWordsLength - 1] / (float) stWordsLength;
        return accuracy;
    }

    /**
     * 文件文字过滤标点符号
     * @param words
     * @return
     */
    public static String FilterWords(String words){
        // 过滤标点列表
        List<String> invalidList = Arrays.asList("~", "!", "，", "。", "@", "#", "$", "%", "^", "&", "*", "_", "-", "+", "=", " ", "\t", "\b", "\r", "\n");
//        String[] invalidList = {"~", "!", "，", "。", "@", "#", "$", "%", "^", "&", "*", "_", "-", "+", "=", " ", "\t", "\b", "\r", "\n"};
        System.out.println(words);
        String filterWords = "";
        // 去除文件标点符号
        for(int i=0; i < words.length(); i++) {
            if (!invalidList.contains(words.charAt(i))){
                filterWords += words.charAt(i);
            }
        }
        return filterWords;
    }
}

