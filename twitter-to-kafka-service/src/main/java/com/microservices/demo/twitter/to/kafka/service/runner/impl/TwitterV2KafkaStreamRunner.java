package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.config.TwitterToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 https://github.com/twitterdev/Twitter-API-v2-sample-code/blob/main/Filtered-Stream/FilteredStreamDemo.java
 */

@Component
@ConditionalOnExpression("${twitter-to-kafka-service.enable-v2-tweets} && not ${twitter-to-kafka-service.enable-mock-tweets}")
//@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-v2-tweets", havingValue = "true", matchIfMissing = true)
public class TwitterV2KafkaStreamRunner implements StreamRunner {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterV2KafkaStreamRunner.class);

    private final TwitterToKafkaServiceConfigData twitterToKafkaServiceConfigData;

    private final TwitterV2StreamHelper twitterV2StreamHelper;

    public TwitterV2KafkaStreamRunner(TwitterToKafkaServiceConfigData configData,
                                      TwitterV2StreamHelper twitterV2StreamHelper) {
        this.twitterToKafkaServiceConfigData = configData;
        this.twitterV2StreamHelper = twitterV2StreamHelper;
    }

    @Override
    public void start() {
        String bearerToken = twitterToKafkaServiceConfigData.getTwitterV2BearerToken();
        if (null != bearerToken) {
            try {
                twitterV2StreamHelper.setupRules(bearerToken, getRules());
                twitterV2StreamHelper.connectStream(bearerToken);
            } catch (IOException | URISyntaxException | TwitterException | JSONException e) {
                LOG.error("Error streaming tweets!", e);
                throw new RuntimeException("Error streaming tweets!", e);
            }
        } else {
            LOG.error("There was a problem getting your bearer token. " +
                    "Please make sure you set the TWITTER_BEARER_TOKEN environment variable");
            throw new RuntimeException("There was a problem getting your bearer token. +" +
                    "Please make sure you set the TWITTER_BEARER_TOKEN environment variable");
        }
    }

    private Map<String, String> getRules() {
        List<String> keywords = twitterToKafkaServiceConfigData.getTwitterKeywords();
        Map<String, String> rules = new HashMap<>();
        for (String keyword : keywords) {
            rules.put(keyword, "Keyword: " + keyword);
        }
        LOG.info("Created filter for twitter stream for keywords {}", keywords.toArray());
        return rules;
    }
}
