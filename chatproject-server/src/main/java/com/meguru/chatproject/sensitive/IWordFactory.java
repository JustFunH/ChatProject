package com.meguru.chatproject.sensitive;

import java.util.List;

/**
 * 敏感词
 *
 * @author Meguru
 * @since 2025-05-28
 */
public interface IWordFactory {
    /**
     * 返回敏感词数据源
     *
     * @return 结果
     * @since 0.0.13
     */
    List<String> getWordList();
}
