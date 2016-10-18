package cn.ucai.fulicenter.dao;

        import android.content.Context;

        import cn.ucai.fulicenter.I;
        import cn.ucai.fulicenter.bean.GoodsDetailsBean;
        import cn.ucai.fulicenter.bean.NewGoodsBean;
        import cn.ucai.fulicenter.utils.L;

public class NetDao {
    public static void downloadNewGoods(Context mcontext, int page_id,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(page_id))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
        L.i("xns",utils.mUrl.toString());
    }

    public static void findGoodDetails(Context mcontext, int goodsId
                , OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }
}
