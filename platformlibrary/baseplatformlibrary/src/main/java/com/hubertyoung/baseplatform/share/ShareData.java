package com.hubertyoung.baseplatform.share;

import android.text.TextUtils;

import com.hubertyoung.baseplatform.share.image.resource.ImageResource;
import com.hubertyoung.baseplatform.share.media.IMediaObject;

/**
 * <br>
 * function:
 * <p>
 *
 * @author:HubertYoung
 * @date:2018/9/11 15:56
 * @since:V1.0.0
 * @desc:com.hubertyoung.baseplatform.share
 */
public class ShareData {
    public static final int THUMB_LIMIT = 24576;
    public static final int THUMB_LIMIT_WX = 32*1024;
    public static final int IMAGE_LIMIT = 491520;
    public static final String DEFAULT_TITLE = "这里是标题";
    public static final String DEFAULT_DESCRIPTION = "这里是描述";


    public String url = "";
    public String text = "";
    public String title = "";
    public String description = "";
    public ImageResource thumb;
    public IMediaObject media;

    public boolean hasUrl() {
        return !TextUtils.isEmpty(url);
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(text);
    }

    public boolean hasTitle() {
        return !TextUtils.isEmpty(title);
    }

    public boolean hasDescription() {
        return !TextUtils.isEmpty(description);
    }

    public boolean hasThumb() {
        return thumb != null;
    }

    public boolean hasMedia() {
        return media != null;
    }

    public int type() {
        if (media != null) {
            return media.type();
        }
        if (hasText()) {
            return IMediaObject.TYPE_TEXT;
        }
        return IMediaObject.TYPE_INVALID;
    }

    @Override
    public String toString() {
        return "ShareData [type=" + type() + ", title=" + title + ", desc=" + description + ", thumb=" + thumb + "]";
    }
}