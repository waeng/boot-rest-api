package waeng.bootrestapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author waeng
 * @since 2021/12/15
 */
@Getter
@Setter
@Accessors(chain = true)
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 3659950921057860367L;

    private List<T> content;

    private boolean hasMore;

    private Long totalCount;

    private Long moreCount;

    public Page() {
    }

    public Page(List<T> content, boolean hasMore) {
        this.content = content;
        this.hasMore = hasMore;
    }

    public Page(List<T> content, long totalCount, long moreCount) {
        if (moreCount < 0) {
            throw new IllegalArgumentException("moreCount");
        }
        this.content = content;
        this.hasMore = moreCount > 0;
        this.totalCount = totalCount;
        this.moreCount = moreCount;
    }

    public Page(List<T> content, long totalCount, int offset, int limit) {
        this(content, totalCount, Math.max(totalCount - (offset + limit), 0));
    }

}
