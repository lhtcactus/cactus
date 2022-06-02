package com.cactus.core.toolkit;


import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 树工具
 *
 * @author lht
 * @since 2020/10/14 10:03
 */
@SuppressWarnings("unchecked")
public class FormatTree {


    /**
     * @param data              待转树形的元数据
     * @param idSFuncrion       元数据的id lambda
     * @param parentIdSFuncrion 元数据的父级id lambda
     * @param itemsSFuncrion    元数据的子节点 lambda
     * @author lht
     * @date 2021/6/25 10:45 上午
     */
    public static <T> List<T> getTree(List<T> data,
                                      SerializableFuncrion<T, ?> idSFuncrion,
                                      SerializableFuncrion<T, ?> parentIdSFuncrion,
                                      SerializableFuncrion<T, ?> itemsSFuncrion) {
        return getTree(data,
                (parent, cur) -> parent == null,
                idSFuncrion,
                parentIdSFuncrion,
                itemsSFuncrion);
    }



    /**
     * @param data              待转树形的元数据
     * @param rootSelect        根节点选择器<当前节点的父级节点，当前节点>
     * @param idSFuncrion       元数据的id lambda
     * @param parentIdSFuncrion 元数据的父级id lambda
     * @param itemsSFuncrion    元数据的子节点 lambda
     * @author lht
     * @since 2021/7/7 2:11 下午
     */
    public static <T> List<T> getTree(List<T> data,
                                      BiFunction<T, T, Boolean> rootSelect,
                                      SerializableFuncrion<T, ?> idSFuncrion,
                                      SerializableFuncrion<T, ?> parentIdSFuncrion,
                                      SerializableFuncrion<T, ?> itemsSFuncrion) {
        //格式化成为map<id,obj>
        Map<Object, T> map = data.stream().collect(Collectors.toMap(t1 -> PropertyUtils.get(t1, idSFuncrion), t2 -> t2));
        //根节点
        List<T> root = new ArrayList<>();
        //遍历
        data.forEach(t -> {
            T temp = map.get(PropertyUtils.get(t, parentIdSFuncrion));
            if (rootSelect.apply(temp, t)) {
                root.add(t);
            }
            //父级节点不为空，将其加入父级的集合
            if (temp != null) {
                Collection<T> items = (Collection<T>) PropertyUtils.get(temp, itemsSFuncrion);
                if (items == null) {
                    items = new ArrayList<>();
                    PropertyUtils.set(temp, itemsSFuncrion, items);
                }
                items.add(t);
            }
        });
        return root;
    }

    /**
     * 转换树形集合为普通集合，扁平化处理;
     * 同时会清除子节点引用
     *
     * @param treeData     树形集合
     * @param childrenCode 子节点属性
     * @return
     * @author liuhuibin
     * @since 2021/1/14 4:38 下午
     */
    public static <T> List<T> getList(List<T> treeData, String childrenCode) {
        return getList(treeData, childrenCode, true);
    }

    /**
     * 转换树形集合为普通集合，扁平化处理。
     *
     * @param treeData      树形集合
     * @param childrenCode  子节点属性
     * @param clearChildren 是否清除子节点引用,转换后不再使用子节点时建议清除
     * @return
     * @author liuhuibin
     * @since 2021/1/13 5:50 下午
     */
    public static <T> List<T> getList(List<T> treeData, String childrenCode, boolean clearChildren) {
        List<T> result = new ArrayList<>();

        Stack<List<T>> stack = new Stack<>();
        stack.push(treeData);
        while (!stack.empty()) {
            List<T> list = stack.pop();
            if (list != null && list.size() > 0) {
                list.forEach(item -> {
                    result.add(item);
                    List<T> children = (List<T>) PropertyUtils.get(item, childrenCode);
                    stack.push(children);
                });
            }
        }

        // 释放子节点的对象引用
        if (clearChildren) {
            result.forEach(item -> PropertyUtils.set(item, childrenCode, null));
        }

        return result;
    }


}
