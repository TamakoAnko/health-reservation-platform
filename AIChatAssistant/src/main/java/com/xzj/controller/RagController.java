package com.xzj.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class RagController {

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;
    @Resource
    private VectorStore vectorStore;

    @GetMapping("/rag4aiops")
    public Flux<String> rag(String msg) {
        String systemInfo = """
                你是一个健康管理系统AI助手,请根据用户问题提供详细答案，并使用Markdown格式进行回复。
                
                """;
        RetrievalAugmentationAdvisor advisor =
                RetrievalAugmentationAdvisor.builder()
                        .documentRetriever(
                                VectorStoreDocumentRetriever.builder()
                                        .vectorStore(vectorStore)
                                        .build()
                        )
                        .build();

              return chatClient.prompt()
                      .system(systemInfo)
                      .user(msg)
                      .advisors(advisor)
                      .stream()
                      .content();
    }

}
