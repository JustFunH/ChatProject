package com.meguru.chatproject.chat.service.cache;

import com.meguru.chatproject.chat.dao.RoomDao;
import com.meguru.chatproject.chat.dao.RoomFriendDao;
import com.meguru.chatproject.chat.domain.entity.Room;
import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.common.service.cache.AbstractRedisStringCache;
import com.meguru.chatproject.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 房间基本信息的缓存
 *
 * @author Meguru
 * @since 2025-05-31
 */
@Component
public class RoomCache extends AbstractRedisStringCache<Long, Room> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomFriendDao roomFriendDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, Room> load(List<Long> roomIds) {
        List<Room> rooms = roomDao.listByIds(roomIds);
        return rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
    }
}
