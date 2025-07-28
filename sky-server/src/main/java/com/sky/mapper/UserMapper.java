package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: UserMapper
 * Description:
 *
 * @Author 乒乓界李大帅
 * @Create 2025/7/20 18:55
 */
@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);
    @Select("select * from user where id = #{id}")
    User getById(Long id);


    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);
}
