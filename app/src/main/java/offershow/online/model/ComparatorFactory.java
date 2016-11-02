package offershow.online.model;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Kairong on 2016/10/31.
 * mail:wangkrhust@gmail.com
 */
public class ComparatorFactory {
    public static final int COMP_LIULAN  = 1;
    public static final int COMP_KEXIN   = 2;
    public static final int COMP_SHIJIAN = 4;
    private static HashMap<Integer, Comparator<OfferInfo>> comparatorFact = new HashMap<>();
    static {
        comparatorFact.put(COMP_LIULAN, new Comparator1());
        comparatorFact.put(COMP_KEXIN, new Comparator2());
        comparatorFact.put(COMP_SHIJIAN, new Comparator3());
    }
    /**
     * 浏览量比较器(倒序）
     */
    private static class Comparator1 implements Comparator<OfferInfo> {
        @Override
        public int compare(OfferInfo lhs, OfferInfo rhs) {
            return rhs.getNumber() - lhs.getNumber();
        }
    }
    /**
     * 可信度比较器(倒序）
     */
    private static class Comparator2 implements Comparator<OfferInfo> {
        @Override
        public int compare(OfferInfo lhs, OfferInfo rhs) {
            return rhs.getScore() - lhs.getScore();
        }
    }
    /**
     * 时间比较器(倒序）
     */
    private static class Comparator3 implements Comparator<OfferInfo> {
        @Override
        public int compare(OfferInfo lhs, OfferInfo rhs) {
            return rhs.getId() - lhs.getId();
        }
    }
    public static Comparator<OfferInfo> comparator(int type){
        return comparatorFact.get(type);
    }
}
