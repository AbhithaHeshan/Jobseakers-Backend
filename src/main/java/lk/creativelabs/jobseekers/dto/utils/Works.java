package lk.creativelabs.jobseekers.dto.utils;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Works {

    String title;
    String category;
    String description;
    String docUrl;

}
