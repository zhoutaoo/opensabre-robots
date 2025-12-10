package io.github.opensabre.robots.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Value("${spring.ai.deepseek.api-key}")
    private String apiKey;

//    @Bean
//    public ChatModel chatModel() {
//        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
//                .apiKey(apiKey)
//                .build();
//        return DeepSeekChatModel.builder()
//                .deepSeekApi(deepSeekApi)
//                .build();
//    }
//
//    @Bean
//    public ChatClient chatClient(ChatModel chatModel) {
//        return ChatClient.create(chatModel);
//    }

}
