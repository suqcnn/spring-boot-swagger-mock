package com.yifanyou.sbsm.domain.tuple;

/**
 * Created by yifanyou on 16/9/29.
 */
public class Tuple2<T1, T2> {
    public T1 _1;
    public T2 _2;

    static public <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        Tuple2<T1, T2> tuple = new Tuple2<>();
        tuple._1 = t1;
        tuple._2 = t2;
        return tuple;
    }
}
