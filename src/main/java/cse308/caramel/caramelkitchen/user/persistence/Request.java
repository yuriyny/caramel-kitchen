package cse308.caramel.caramelkitchen.user.persistence;

import cse308.caramel.caramelkitchen.user.util.RequestStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "request")
public class Request {
    private String id;
    private String message;
    private String userName;
    private RequestStatus status;
}
