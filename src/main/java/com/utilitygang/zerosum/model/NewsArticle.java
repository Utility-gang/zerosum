package com.utilitygang.zerosum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

// DTO that the news API response can be marshalled into
// and then added to the view model
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsArticle {
    private String headline;
    private String category;
    private Integer datetime;
    @JsonProperty("image_url")
    private String imageUrl;
    private String source;
    private String summary;
    private String url;
}
