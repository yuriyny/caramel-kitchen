package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public abstract class SubprocedureComponent {
    public File imageFile;
    public String name;
}
