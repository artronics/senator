package artronics.senator.helpers;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionHelper
{
    public static <E> Collection<E> makeCollection(Iterable<E> iter)
    {
        Collection<E> list = new ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return list;
    }
}
