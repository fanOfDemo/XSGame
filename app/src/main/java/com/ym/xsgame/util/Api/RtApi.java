package com.ym.xsgame.util.Api;


import com.ym.xsgame.po.Bander;
import com.ym.xsgame.po.Result;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 项目名称：xsgame
 * 类描述：
 * 创建人：wengyiming
 * 创建时间：15/11/16 下午10:34
 * 修改人：wengyiming
 * 修改时间：15/11/16 下午10:34
 * 修改备注：
 */
public interface RtApi {
    @GET(Config.USER_LOGIN)
    Observable<Result> getGameList(@Query("op") int op,@Query("style") int style,@Query("ps") int ps,@Query("pi") int pi);
    //op=1&style=0&ps=6&pi=1
    @GET(Config.HOME_INDEX)
    Observable<Bander> getBanner(@Query("Action") String op,@Query("ipage") int ipage,@Query("iplat") int iplat,@Query("pagesize") int pagesize);
    //Action=adlistforindex&ipage=1&iplat=1&pagesize=4




}
