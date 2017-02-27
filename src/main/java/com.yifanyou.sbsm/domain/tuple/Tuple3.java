package com.yifanyou.sbsm.domain.tuple;

/**
 * Created by yifanyou on 16/9/29.
 */
public class Tuple3<T1, T2, T3> {
    public T1 _1;
    public T2 _2;
    public T3 _3;

    static public <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        Tuple3<T1, T2, T3> tuple = new Tuple3<>();
        tuple._1 = t1;
        tuple._2 = t2;
        tuple._3 = t3;
        return tuple;
    }
}
