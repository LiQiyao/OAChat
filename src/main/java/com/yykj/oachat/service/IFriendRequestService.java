package com.yykj.oachat.service;

import com.yykj.oachat.dto.data.AddFriendRequestDTO;
import com.yykj.oachat.entity.FriendRequest;

import java.util.List;

/**
 * @author Lee
 * @date 2017/11/28
 */
public interface IFriendRequestService {

    boolean saveAddFriendRequest(AddFriendRequestDTO addFriendRequestDTO);

    List<AddFriendRequestDTO> listFriendRequest(Long userId);

    boolean checkExistence(Long fromUserId, Long toUserId);
}
