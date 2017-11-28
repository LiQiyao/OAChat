package com.yykj.oachat.dto.data;

import com.yykj.oachat.entity.UserInfo;

import java.util.List;

/**
 * Created by Lee on 2017/11/26.
 */
public class FoundUsersDTO {

    private Integer count;

    private List<UserDetailDTO> users;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<UserDetailDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetailDTO> users) {
        this.users = users;
    }
}
