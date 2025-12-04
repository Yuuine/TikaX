package anthony.tikax.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT COUNT(*) > 0 FROM users WHERE username = #{username}")
    Boolean existsByUsername(String username);

    @Select("INSERT INTO users (username, password) VALUES (#{username}, #{encodedPassword})")
    void registerUser(String username, String encodedPassword);

    @Select("SELECT password FROM users WHERE username = #{username}")
    String getPasswordByUsername(String username);
}
