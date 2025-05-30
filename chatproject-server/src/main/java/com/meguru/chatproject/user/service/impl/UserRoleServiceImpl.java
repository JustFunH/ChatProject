package com.meguru.chatproject.user.service.impl;

import com.meguru.chatproject.user.domain.entity.UserRole;
import com.meguru.chatproject.user.mapper.UserRoleMapper;
import com.meguru.chatproject.user.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getId, Objects.requireNonNull(uid))
                .list();
    }
}
