package org.mybatis.paginator.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 包含“分页”信息的List
 */
public class PageList<E> extends ArrayList<E> implements Serializable {

    private static final long serialVersionUID = 1412759446332294208L;

    private Paginator paginator;

    public PageList() {
    }

    public PageList(Collection<? extends E> c) {
        super(c);
    }

    public PageList(Collection<? extends E> c, Paginator paginator) {
        super(c);
        this.paginator = paginator;
    }

    public PageList(Paginator paginator) {
        this.paginator = paginator;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public Page<E> convert() {
        return Page.convert(this.getPaginator().getIndex(),
                this.getPaginator().getSize(),
                this.getPaginator().getTotal(), this);
    }
}
