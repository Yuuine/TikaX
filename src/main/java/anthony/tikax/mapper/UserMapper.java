package anthony.tikax.mapper;

import anthony.tikax.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    Boolean existsByUsername(String username);

    @Select("INSERT INTO users (username, password, role, created_at, updated_at) " +
            "VALUES (#{username}, #{encodedPassword}, #{role}, #{createAt}, #{updateAt})")
    void registerUser(User user);

    @Select("SELECT password FROM users WHERE username = #{username}")
    String getPasswordByUsername(String username);
}
