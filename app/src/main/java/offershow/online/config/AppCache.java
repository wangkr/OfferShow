package offershow.online.config;

import android.content.Context;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import offershow.online.R;
import offershow.online.model.ComparatorFactory;
import offershow.online.model.OfferInfo;

/**
 * Created by Kairong on 2016/10/30.
 * mail:wangkrhust@gmail.com
 */
public class AppCache {
    private static List<OfferInfo> sort_items;
    private static List<OfferInfo> city_items;
    private static List<OfferInfo> company_items;
    private static HashSet<String> company_src;
    private static TreeMap<String, List<OfferInfo>> cityCategory;
    private static TreeMap<String, List<OfferInfo>> companyCategory;
    private static TreeMap<String, List<OfferInfo>> positionCategory;
    private static int ID_ITEMS = 0, ID_SORT_SJ = 1, ID_SORT_KXD = 2, ID_SORT_LL = 3,ID_CITY_ITEMS = 4, ID_COM_ITEMS = 5, ID_POS_ITEMS = 6;
    private static boolean[] refresh_status = new boolean[7];

    public static class CollatorComparator implements Comparator<String> {
        Collator collator = Collator.getInstance();
        public int compare(String element1, String element2) {
            CollationKey key1 = collator.getCollationKey(element1);
            CollationKey key2 = collator.getCollationKey(element2);
            return key1.compareTo(key2);
        }
    }

    public static void init(Context context){
        sort_items = new ArrayList<>();
        city_items = new ArrayList<>();
        company_items = new ArrayList<>();
        CollatorComparator comparator = new CollatorComparator();
        cityCategory = new TreeMap<>(comparator);
        companyCategory = new TreeMap<>(comparator);
        positionCategory = new TreeMap<>(comparator);
        company_src = new HashSet<>();
        String[] src = context.getResources().getStringArray(R.array.comm_companys);
        for(String s : src) company_src.add(s);
    }

    public synchronized static void clear() {
        sort_items.clear();
        company_items.clear();
        city_items.clear();
        cityCategory.clear();
        companyCategory.clear();
        positionCategory.clear();
    }

    public synchronized static void addAll(List<OfferInfo> _items) {
        Arrays.fill(refresh_status, true);
        sort_items.addAll(_items);
        company_items.addAll(_items);
        city_items.addAll(_items);
        for(OfferInfo item : _items){
            addItem2Category(item);
        }
    }

    public synchronized static void addItem(OfferInfo item){
        Arrays.fill(refresh_status, true);
        sort_items.add(item);
        company_items.add(item);
        city_items.add(item);
        addItem2Category(item);
    }

    private static void addItem2Category(OfferInfo item) {
        if (cityCategory.containsKey(item.getCity())){
            cityCategory.get(item.getCity()).add(item);
        } else {
            cityCategory.put(item.getCity(), new ArrayList<OfferInfo>());
            cityCategory.get(item.getCity()).add(item);
        }

        if (companyCategory.containsKey(item.getCompany())){
            companyCategory.get(item.getCompany()).add(item);
        } else {
            companyCategory.put(item.getCompany(), new ArrayList<OfferInfo>());
            companyCategory.get(item.getCompany()).add(item);
        }

        if (positionCategory.containsKey(item.getPosition())){
            positionCategory.get(item.getPosition()).add(item);
        } else {
            positionCategory.put(item.getPosition(), new ArrayList<OfferInfo>());
            positionCategory.get(item.getPosition()).add(item);
        }
    }

    public static void sortByShijian() {
        int mask = Constants.city_mask | Constants.company_mask | Constants.position_mask | Constants.time_mask;
        Collections.sort(sort_items, ComparatorFactory.comparator(ComparatorFactory.COMP_SHIJIAN));
        for(OfferInfo oi:sort_items) oi.setMask(mask);
        refresh_status[ID_SORT_SJ] = true;
    }

    public static void sortByKexindu() {
        Collections.sort(sort_items, ComparatorFactory.comparator(ComparatorFactory.COMP_KEXIN));
    }

    public static void sortByLiulan() {
        Collections.sort(sort_items, ComparatorFactory.comparator(ComparatorFactory.COMP_LIULAN));
    }

    public static void groupByCity() {
        if (!refresh_status[ID_CITY_ITEMS]) return;
        List<OfferInfo> res = new ArrayList<>();
        Collection<List<OfferInfo>> col = cityCategory.values();
        for(List<OfferInfo> ois : col) {
            OfferInfo tag = new OfferInfo();
            tag.setTag(true);
            tag.setCity(ois.get(0).getCity());
            tag.setMask(Constants.city_mask);
            res.add(tag);
            res.addAll(ois);
        }

        city_items.clear();
        city_items.addAll(res);
        refresh_status[ID_CITY_ITEMS] = false;
    }

    public static void groupByCompany() {
        if (!refresh_status[ID_COM_ITEMS]) return;
        List<OfferInfo> res = new ArrayList<>();
        TreeMap<String, List<OfferInfo>> temp = new TreeMap<>();
        Collection<List<OfferInfo>> col = companyCategory.values();
        // 按公司简称分类
        for(String comp : companyCategory.keySet()){
            if (temp.containsKey(comp)) {
                temp.get(comp).addAll(companyCategory.get(comp));
                continue;
            }
            boolean flag = false;
            for(String src_key : company_src) {
                if (comp.startsWith(src_key)) {
                    if (temp.containsKey(src_key)) {
                        temp.get(src_key).addAll(companyCategory.get(comp));
                    } else {
                        temp.put(src_key, new ArrayList<>(companyCategory.get(comp)));
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag){
                temp.put(comp, companyCategory.get(comp));
            }
        }

        // 按简称分类后集合
        Collection<List<OfferInfo>> col_temp = temp.values();
        for(List<OfferInfo> ois : col_temp) {
            OfferInfo tag = new OfferInfo();
            tag.setTag(true);
            tag.setCompany(ois.get(0).getCompany());
            tag.setMask(Constants.company_mask);
            res.add(tag);
            res.addAll(ois);
        }

        company_items.clear();
        company_items.addAll(res);
        refresh_status[ID_COM_ITEMS] = false;
    }



    public static int getSize() {
        return  sort_items.size();
    }

    public static List<OfferInfo> getCity_items() {
        return city_items;
    }

    public static List<OfferInfo> getCompany_items() {
        return company_items;
    }

    public static List<OfferInfo> getSort_items() {
        return sort_items;
    }

}
