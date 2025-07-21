package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;

/**
 * ClassName: UserService
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/19 22:24
 */
public interface UserService {
    User wxlogin(UserLoginDTO userLoginDTO);
}
