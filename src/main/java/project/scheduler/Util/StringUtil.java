package project.scheduler.Util;


public class StringUtil {
    
    public static <T> String toString(Iterable<T> list) {
        StringBuilder string = new StringBuilder().append("[");
        for (T item : list) {
        if(string.length() != 1) {
            string.append(", ");
        }
        string.append(item.toString());
        }
        string.append("]");
        return string.toString();
    }
}
