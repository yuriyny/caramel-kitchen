package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;


@Getter
@Setter
public abstract class SubprocedureComponent {
    @Transient
    public String imageFileUrl;
    public String name;
    public String imageName;//apple.png
}
