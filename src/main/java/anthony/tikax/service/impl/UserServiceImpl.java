package anthony.tikax.service.impl;

import anthony.tikax.mapper.UserMapper;
import anthony.tikax.service.UserService;
import anthony.tikax.utils.BcryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password) {
        usernameAndPasswordNotNull(username, password);
        Boolean result = userMapper.existsByUsername(username);
        if (result) {
            throw new RuntimeException("用户名已存在");
        }
        String encodedPassword = BcryptUtil.encode(password);
        try {
            userMapper.registerUser(username, encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException("注册失败");
        }
    }

    @Override
    public void login(String username, String password) {
        usernameAndPasswordNotNull(username, password);

        String encodedPassword = userMapper.getPasswordByUsername(username);
        boolean result = BcryptUtil.matches(password, encodedPassword);

        if (!result) {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    public void usernameAndPasswordNotNull(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("用户名或密码不能为空");
        }
    }

}
