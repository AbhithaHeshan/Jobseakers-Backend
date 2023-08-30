package lk.creativelabs.jobseekers.dto.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Works {

    String title;
    String category;
    String description;
    String docUrl;
    String docUrl2;

    public Works(String title, String category, String description, String docUrl) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.docUrl = docUrl;
    }



}
