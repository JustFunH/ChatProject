package com.meguru.chatproject.sensitive.algorithm;

import com.meguru.chatproject.sensitive.algorithm.acpro.ACProTrie;
import io.micrometer.core.instrument.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * Description: 基于ACFilter的优化增强版本
 *
 * @author Meguru
 * @since 2025-05-28
 */
public class ACProFilter implements SensitiveWordFilter {

    private ACProTrie acProTrie;

    @Override
    public boolean hasSensitiveWord(String text) {
        if (StringUtils.isBlank(text)) return false;
        return !Objects.equals(filter(text), text);
    }

    @Override
    public String filter(String text) {
        return acProTrie.match(text);
    }

    @Override
    public void loadWord(List<String> words) {
        if (words == null) return;
        acProTrie = new ACProTrie();
        acProTrie.createACTrie(words);
    }
}
