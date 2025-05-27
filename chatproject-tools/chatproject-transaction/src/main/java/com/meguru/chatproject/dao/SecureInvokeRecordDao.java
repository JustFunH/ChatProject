package com.meguru.chatproject.dao;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meguru.chatproject.domain.entity.SecureInvokeRecord;
import com.meguru.chatproject.mapper.SecureInvokeRecordMapper;
import com.meguru.chatproject.service.SecureInvokeService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SecureInvokeRecordDao extends ServiceImpl<SecureInvokeRecordMapper, SecureInvokeRecord> {
    public List<SecureInvokeRecord> getWaitRetryRecords() {
        Date now = new Date();
        DateTime afterTime = DateUtil.offsetMinute(now, -(int) SecureInvokeService.RETRY_INTERVAL_MINUTES);
        return lambdaQuery()
                .eq(SecureInvokeRecord::getStatus, SecureInvokeRecord.STATUS_WAIT)
                .lt(SecureInvokeRecord::getNextRetryTime, new Date())
                .lt(SecureInvokeRecord::getCreateTime, afterTime)
                .list();
    }
}
