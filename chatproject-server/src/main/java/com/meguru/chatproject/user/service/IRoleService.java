package com.meguru.chatproject.user.service;

import com.meguru.chatproject.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meguru.chatproject.user.domain.entity.UserRole;
import com.meguru.chatproject.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IRoleService {
    /**
     * 权限校验
     *
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);
}
