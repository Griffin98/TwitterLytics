package factory;

import model.Tweet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Sentiment analyzer factory.
 *
 * @author Darshan on 07-11-2020
 * @project TweetLytics
 */
public class SentimentAnalyzerFactory {
    private static List<String> happyWords;
    private static List<String> sadWords;

    /**
     * Instantiates a new Sentiment analyzer factory.
     */
    public SentimentAnalyzerFactory() {

        String POSITIVE_FILENAME = "assets/positive-words.txt";
        try (Stream<String> positiveLines = Files.lines(Paths.get(POSITIVE_FILENAME))) {
            happyWords = positiveLines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String NEGATIVE_FILENAME = "assets/negative-words.txt";
        try (Stream<String> negativeLines = Files.lines(Paths.get(NEGATIVE_FILENAME), StandardCharsets.ISO_8859_1)) {
            sadWords = negativeLines.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filter tweets string.
     *
     * @param tweet the tweet
     * @return the string
     */
    public String filterTweets(String tweet){
        var counts =  Arrays.stream(tweet.split(" ")).map(this::classifyWord)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        counts.remove("NA");
      var total = counts.getOrDefault(":-)",0L) + counts.getOrDefault(":-(" , 0L);
      var result = counts.entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (e.getValue() * 100.0f) / total));
      if (result.getOrDefault(":-)", 0f) > 70.0f){
            return ":-)";
        }else if(result.getOrDefault(":-(", 0f) > 70.0f){
            return ":-(";
        }else{
            return ":-|";
        }
    }

    /**
     * Classify word string.
     *
     * @param word the word
     * @return the string
     */
    public String classifyWord(String word){
        if(happyWords.contains(word)){
            return ":-)";
        }else if (sadWords.contains(word)){
            return ":-(";
        }else{
            return ":-|";
        }
    }

    /**
     * Get emotion of tweet list.
     *
     * @param tweets the tweets
     * @return the list
     */
    public List<String> getEmotionOfTweet(List<Tweet> tweets){
        return tweets.stream()
                .map((tweet) -> tweet.getText().toLowerCase().trim())
                .map(this::filterTweets)
                .collect(Collectors.toList());
    }

    /**
     * Gets result of all tweet.
     *
     * @param sentiment the sentiment
     * @return the result of all tweet
     */
    public String getResultOfAllTweet(List<String> sentiment) {
        var happyEmoticon = sentiment.stream().filter(emoticon -> emoticon.equals(":-)")).count();
        var sadEmoticon = sentiment.stream().filter(emoticon -> emoticon.equals(":-(")).count();
        var neutralEmoticon = sentiment.stream().filter(emoticon -> emoticon.equals(":-|")).count();
        System.out.println(happyEmoticon);
        System.out.println(sadEmoticon);
        System.out.println(neutralEmoticon);
        if (happyEmoticon > sadEmoticon && happyEmoticon > neutralEmoticon){
            return ":-)";
        }else if (sadEmoticon > happyEmoticon &&  sadEmoticon > neutralEmoticon){
            return ":-(";
        }else{
            return ":-|";
        }

    }
}
