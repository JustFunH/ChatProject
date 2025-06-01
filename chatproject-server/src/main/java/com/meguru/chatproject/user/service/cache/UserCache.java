package com.meguru.chatproject.user.service.cache;


import cn.hutool.core.collection.CollUtil;
import com.meguru.chatproject.common.constant.RedisKey;
import com.meguru.chatproject.user.dao.BlackDao;
import com.meguru.chatproject.user.dao.UserDao;
import com.meguru.chatproject.user.dao.UserRoleDao;
import com.meguru.chatproject.user.domain.entity.Black;
import com.meguru.chatproject.user.domain.entity.User;
import com.meguru.chatproject.user.domain.entity.UserRole;
import com.meguru.chatproject.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 用户相关缓存
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Component
public class UserCache {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private BlackDao blackDao;
    @Autowired
    private UserSummaryCache userSummaryCache;

    public Long getOnlineNum() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return RedisUtils.zCard(onlineKey);
    }

    public Long getOfflineNum() {
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        return RedisUtils.zCard(offlineKey);
    }

    //移除用户
    public void remove(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        RedisUtils.zRemove(onlineKey, uid);
        RedisUtils.zRemove(offlineKey, uid);
    }

    //用户上线
    public void online(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        RedisUtils.zAdd(onlineKey, uid, optTime.getTime());
        RedisUtils.zRemove(offlineKey, uid);
    }

    //用户下线
    public void offline(Long uid, Date optTime) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        String offlineKey = RedisKey.getKey(RedisKey.OFFLINE_UID_ZET);
        RedisUtils.zRemove(onlineKey, uid);
        RedisUtils.zAdd(offlineKey, uid, optTime.getTime());
    }

    public List<Long> getOnlineUidList() {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        Set<String> strings = RedisUtils.zAll(onlineKey);
        return strings.stream().map(Long::parseLong).toList();
    }

    public boolean isOnline(Long uid) {
        String onlineKey = RedisKey.getKey(RedisKey.ONLINE_UID_ZET);
        return RedisUtils.zIsMember(onlineKey, uid);
    }

    public List<Long> getUserModifyTime(List<Long> uidList) {
        List<String> keys = uidList.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).toList();
        return RedisUtils.mget(keys, Long.class);
    }

    public void refreshUserModifyTime(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid);
        RedisUtils.set(key, new Date().getTime());
    }

    public User getUserInfo(Long uid) {
        return getUserInfoBath(Collections.singleton(uid)).get(uid);
    }

    /**
     * 获取用户信息, 盘路缓存模式
     *
     * @param uids
     * @return
     */
    public Map<Long, User> getUserInfoBath(Set<Long> uids) {
        List<String> keys = uids.stream().map(uid -> RedisKey.getKey(RedisKey.USER_INFO_STRING, uid)).toList();
        List<User> megt = RedisUtils.mget(keys, User.class);
        Map<Long, User> map = megt.stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, Function.identity()));
        List<Long> needLoadUidList = uids.stream().filter(a -> !map.containsKey(a)).toList();
        if (CollUtil.isNotEmpty(needLoadUidList)) {
            List<User> needLoadUserList = userDao.listByIds(needLoadUidList);
            Map<String, User> redisMap = needLoadUserList.stream()
                    .collect(Collectors.toMap(a -> RedisKey.getKey(RedisKey.USER_INFO_STRING, a.getId()), Function.identity()));
            RedisUtils.mset(redisMap, 5 * 60);
            map.putAll(needLoadUserList.stream().collect(Collectors.toMap(User::getId, Function.identity())));
        }
        return map;
    }

    public void userInfoChange(Long uid) {
        delUserInfo(uid);
        userSummaryCache.delete(uid);
        refreshUserModifyTime(uid);
    }

    public void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }

    @Cacheable(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> result = new HashMap<>(collect.size());
        for (Map.Entry<Integer, List<Black>> entry : collect.entrySet()) {
            result.put(entry.getKey(), entry.getValue().stream().map(Black::getTarget).collect(Collectors.toSet()));
        }
        return result;
    }

    @CacheEvict(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> evictBlackMap() {
        return null;
    }

    @Cacheable(cacheNames = "user", key = "'roles'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }
}
