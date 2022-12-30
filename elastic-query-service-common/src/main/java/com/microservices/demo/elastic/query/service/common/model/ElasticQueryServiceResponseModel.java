package com.microservices.demo.elastic.query.service.common.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceResponseModel extends RepresentationModel<ElasticQueryServiceResponseModel> {
    private String id;
    private Long userId;
    private String text;
    private ZonedDateTime createdAt;
}
