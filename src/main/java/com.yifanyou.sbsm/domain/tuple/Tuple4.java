package com.yifanyou.sbsm.domain.tuple;

/**
 * Created by yifanyou on 16/9/29.
 */
public class Tuple4<T1, T2, T3, T4> {
    public T1 _1;
    public T2 _2;
    public T3 _3;
    public T4 _4;

    static public <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        Tuple4<T1, T2, T3, T4> tuple = new Tuple4<>();
        tuple._1 = t1;
        tuple._2 = t2;
        tuple._3 = t3;
        tuple._4 = t4;
        return tuple;
    }
}
