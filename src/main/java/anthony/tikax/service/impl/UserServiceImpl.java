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
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        Boolean result = userMapper.existsByUsername(username);
        if (result) {
            throw new RuntimeException("用户已存在");
        }
        String encodedPassword = BcryptUtil.encode(password);
        try {
            userMapper.registerUser(username, encodedPassword);
        } catch (Exception e) {
            throw new RuntimeException("注册失败");
        }
    }



}
