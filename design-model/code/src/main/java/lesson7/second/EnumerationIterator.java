package lesson7.second;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * created by ycc at 2018\3\20 0020
 * 迭代器适配器
 * 老版本的枚举器，早期的Collection类型的迭代器
 */
public class EnumerationIterator implements Iterator {
    Enumeration enumeration;

    public EnumerationIterator(Enumeration enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public Object next() {
        return enumeration.nextElement();
    }
}
