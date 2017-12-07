package com.yykj.oachat.common;

import com.yykj.oachat.dto.data.ChatLogListDTO;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Lee
 * @date 2017/11/28
 */
public class MapValueComparator implements Comparator<Long> {

    Map<Long, ChatLogListDTO> base;

    public MapValueComparator(Map<Long, ChatLogListDTO> base) {
        this.base = base;
    }

    @Override
    public int compare(Long o1, Long o2) {

        if (o1.equals(o2)){
            return 0;
        }
        if (base.get(o1).getLastChatLogTime() > base.get(o2).getLastChatLogTime()){
            return -1;
        } else {
            return 1;
        }
    }
}
