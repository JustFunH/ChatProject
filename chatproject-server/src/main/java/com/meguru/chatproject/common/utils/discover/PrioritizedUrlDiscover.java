package com.meguru.chatproject.common.utils.discover;

import org.dromara.hutool.core.text.StrUtil;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 具有优先级的title查询器
 *
 * @author Meguru
 * @since 2025-05-30
 */
public class PrioritizedUrlDiscover extends AbstractUrlDiscover {

    private final List<UrlDiscover> urlDiscovers = new ArrayList<>(2);

    public PrioritizedUrlDiscover() {
        urlDiscovers.add(new CommonUrlDiscover());
    }


    @Nullable
    @Override
    public String getTitle(Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String urlTitle = urlDiscover.getTitle(document);
            if (StrUtil.isNotBlank(urlTitle)) {
                return urlTitle;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getDescription(Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String urlDescription = urlDiscover.getDescription(document);
            if (StrUtil.isNotBlank(urlDescription)) {
                return urlDescription;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public String getImage(String url, Document document) {
        for (UrlDiscover urlDiscover : urlDiscovers) {
            String urlImage = urlDiscover.getImage(url, document);
            if (StrUtil.isNotBlank(urlImage)) {
                return urlImage;
            }
        }
        return null;
    }
}
