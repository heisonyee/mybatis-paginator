package org.mybatis.paginator.model;

import java.io.Serializable;
import java.util.List;

public class Page<E> implements Serializable {

    private static final long serialVersionUID = -2429864663690465105L;

    private static final int DEFAULT_SLIDERS_COUNT = 7;

    /**
     * 分页大小
     */
    private int size;
    /**
     * 当前页数
     */
    private int index;
    /**
     * 总记录数
     */
    private long total;

    /**
     * 结果集
     */
    private List<E> list;

    public Page(int index, int size, long total, List<E> list) {
        super();
        this.size = size;
        this.total = total;
        this.index = compute(index);
        this.list = list;
    }

    private static int computeLastPageNumber(long totalItems, int pageSize) {
        if (pageSize <= 0) return 1;
        int result = (int) (totalItems % pageSize == 0 ?
                totalItems / pageSize
                : totalItems / pageSize + 1);
        if (result <= 1)
            result = 1;
        return result;
    }

    private static int computePageNumber(int page, int pageSize, long totalItems) {
        if (page <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == page
                || page > computeLastPageNumber(totalItems, pageSize)) { //last index
            return computeLastPageNumber(totalItems, pageSize);
        }
        return page;
    }

    private static Integer[] generateLinkPageNumbers(int currentPageNumber, int lastPageNumber, int count) {
        int avg = count / 2;

        int startPageNumber = currentPageNumber - avg;
        if (startPageNumber <= 0) {
            startPageNumber = 1;
        }

        int endPageNumber = startPageNumber + count - 1;
        if (endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }

        if (endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if (startPageNumber <= 0) {
                startPageNumber = 1;
            }
        }

        java.util.List<Integer> result = new java.util.ArrayList<Integer>();
        for (int i = startPageNumber; i <= endPageNumber; i++) {
            result.add(new Integer(i));
        }
        return result.toArray(new Integer[result.size()]);
    }

    public List<E> getList() {
        return list;
    }

    /**
     * 取得当前页。
     */
    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    /**
     * 取得总项数。
     *
     * @return 总项数
     */
    public long getTotal() {
        return total;
    }

    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
    public boolean isFirst() {
        return index <= 1;
    }

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
    public boolean isLast() {
        return index >= getPages();
    }

    public int getPrevious() {
        if (isHasPrevious()) {
            return index - 1;
        } else {
            return index;
        }
    }

    public int getNext() {
        if (isHasNext()) {
            return index + 1;
        } else {
            return index;
        }
    }

    /**
     * 判断指定页码是否被禁止，也就是说指定页码超出了范围或等于当前页码。
     *
     * @param page 页码
     * @return boolean  是否为禁止的页码
     */
    public boolean isDisabled(int page) {
        return ((page < 1) || (page > getPages()) || (page == this.index));
    }

    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
    public boolean isHasPrevious() {
        return (index - 1 >= 1);
    }

    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
    public boolean isHasNext() {
        return (index + 1 <= getPages());
    }

    /**
     * 开始行，可以用于oracle分页使用 (1-based)。
     */
    public int getFirst() {
        if (getSize() <= 0 || total <= 0) return 0;
        return index > 0 ? (index - 1) * getSize() + 1 : 0;
    }

    /**
     * 结束行，可以用于oracle分页使用 (1-based)。
     */
    public long getLast() {
        return index > 0 ? Math.min(size * index, getTotal()) : 0;
    }

    /**
     * offset，计数从0开始，可以用于mysql分页使用(0-based)
     */
    public int getOffset() {
        return index > 0 ? (index - 1) * getSize() : 0;
    }

    /**
     * 得到总页数
     *
     * @return
     */
    public long getPages() {
        if (total <= 0) {
            return 0;
        }
        if (size <= 0) {
            return 0;
        }

        long count = total / size;
        if (total % size > 0) {
            count++;
        }
        return count;
    }

    protected int compute(int page) {
        return computePageNumber(page, size, total);
    }

    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     *
     * @return
     */
    public Integer[] getSlider() {
        return countSlider(DEFAULT_SLIDERS_COUNT);
    }

    /**
     * 页码滑动窗口，并将当前页尽可能地放在滑动窗口的中间部位。
     * 注意:不可以使用 getSlider(1)方法名称，因为在JSP中会与 getSlider()方法冲突，报exception
     *
     * @return
     */
    public Integer[] countSlider(int slidersCount) {
        return generateLinkPageNumbers(getIndex(), (int) getPages(), slidersCount);
    }

    public int getLimit() {
        return getSize();
    }

    public static <E> Page<E> convert(int index, int size, long total, List<E> value) {
        return new Page<E>(index, size, total, value);
    }

    @Override
    public String toString() {
        return "Page{" +
                "index=" + index +
                ", size=" + size +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}