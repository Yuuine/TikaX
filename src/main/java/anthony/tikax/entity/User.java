package anthony.tikax.entity;

import anthony.tikax.enums.RoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private RoleEnum role;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
