package com.meguru.chatproject.sensitive.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.sensitive.domain.SensitiveWord;
import com.meguru.chatproject.sensitive.mapper.SensitiveWordMapper;
import org.springframework.stereotype.Service;

/**
 * 敏感词DAO
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Service
public class SensitiveWordDao extends ServiceImpl<SensitiveWordMapper, SensitiveWord> {

}
