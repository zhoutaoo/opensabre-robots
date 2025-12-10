package io.github.opensabre.robots.rest;

import io.github.opensabre.robots.mcp.WeatherService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
class ChatController {

    private final ChatClient chatClient;
    private final ToolCallbackProvider toolCallbackProvider;

    public ChatController(ChatClient.Builder chatClientBuilder, @Qualifier("mcpToolCallbacks") @Autowired ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一个工作小助理")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/ai/text")
    String text(String userInput, String conversationId) {
        PromptTemplate promptTemplate = new PromptTemplate("请给我讲一下 {topic} 原理");
        Prompt prompt = promptTemplate.create(Map.of("topic", "区块链"));

        return this.chatClient.prompt(prompt)
                .user(userInput)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .tools(new WeatherService())
                .call()
                .content();
    }

    @GetMapping(path = "/ai/stream", produces = "text/html;charset=utf-8")
    Flux<String> stream(String userInput, String conversationId) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
        return this.chatClient.prompt()
                .user(userInput)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }

    @GetMapping(path = "/ai/mcp", produces = "text/html;charset=utf-8")
    Flux<String> mcp(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .toolCallbacks(toolCallbackProvider)
                .stream()
                .content();
    }

    @GetMapping("/ai/rag")
    Flux<String> rag(String userInput) {
//        SimpleVectorStore vectorStore = SimpleVectorStore.builder(new PostgresMlEmbeddingModel(jdbcTemplate)).build();
        return this.chatClient.prompt()
                .user(userInput)
//                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .stream()
                .content();
    }
}