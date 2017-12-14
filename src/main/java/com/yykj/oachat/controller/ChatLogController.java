package com.yykj.oachat.controller;

import com.yykj.oachat.dto.MessageDTO;
import com.yykj.oachat.service.IChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Lee
 * @date 2017/12/13
 */
@Controller
@RequestMapping("/api/chatLog")
public class ChatLogController {

    @Autowired
    private IChatLogService chatLogService;

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    @ResponseBody
    public MessageDTO readChatLog(String token, Long selfId, Long friendId){
        System.out.println("!!!" + selfId + friendId);
        return chatLogService.updateReadStatus(selfId, friendId);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    @ResponseBody
    public MessageDTO download(String token, Long selfId, Long friendId){
        return chatLogService.updateReceiveStatus(selfId, friendId);
    }
}
