package anthony.tikax.service.impl;

import anthony.tikax.entity.User;
import anthony.tikax.enums.RoleEnum;
import anthony.tikax.mapper.UserMapper;
import anthony.tikax.service.UserService;
import anthony.tikax.utils.BcryptUtil;
import anthony.tikax.exception.BizException;
import anthony.tikax.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void register(String username, String password) {
        usernameAndPasswordNotNull(username, password);
        Boolean result = userMapper.existsByUsername(username);
        if (result) {
            throw new BizException(ErrorCode.USERNAME_ALREADY_EXIST);
        }
        User user = new User();
        String encodedPassword = BcryptUtil.encode(password);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(RoleEnum.USER);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        try {
            userMapper.registerUser(user);
        } catch (Exception e) {
            throw new BizException(ErrorCode.REGISTER_FAILED, e);
        }
    }

    @Override
    public void login(String username, String password) {
        usernameAndPasswordNotNull(username, password);

        String encodedPassword = userMapper.getPasswordByUsername(username);
        boolean result = BcryptUtil.matches(password, encodedPassword);

        if (!result) {
            throw new BizException(ErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    public void usernameAndPasswordNotNull(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new BizException(ErrorCode.USERNAME_OR_PASSWORD_NOT_NULL);
        }
    }

}
