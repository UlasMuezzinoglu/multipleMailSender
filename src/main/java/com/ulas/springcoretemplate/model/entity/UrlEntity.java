package com.ulas.springcoretemplate.model.entity;


import com.ulas.springcoretemplate.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urls")
public class UrlEntity {
    @Id
    private String id;
    @Field
    private String loc;
    @Field
    private String changefreq = "always";
    @Field
    private double priority;
    @Field
    private String lastModifiedDate = DateUtil.currentZonedDateTimeToDateWithPattern();

    public UrlEntity(String loc, double priority) {
        this.loc = loc;
        this.priority = priority;
    }
}
