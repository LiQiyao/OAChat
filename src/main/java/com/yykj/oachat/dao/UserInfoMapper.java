package com.yykj.oachat.dao;

import com.yykj.oachat.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    UserInfo getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    List<UserInfo> listFriendByUserId(Long userId);

    List<UserInfo> listFoundUsersByKey(String key);
}