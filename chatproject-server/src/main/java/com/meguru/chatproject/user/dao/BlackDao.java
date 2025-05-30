package com.meguru.chatproject.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.user.domain.entity.Black;
import com.meguru.chatproject.user.mapper.BlackMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> {

}
