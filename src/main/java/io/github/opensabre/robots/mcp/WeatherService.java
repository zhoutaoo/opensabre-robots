package io.github.opensabre.robots.mcp;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Tool(description = "Get weather information by city name")
    public String getWeather(@ToolParam String cityName) {
        // Implementation
        return cityName + " 今天是下雪";
    }
}