package cn.ucai.fulicenter.dao;

        import android.content.Context;

        import cn.ucai.fulicenter.I;
        import cn.ucai.fulicenter.bean.NewGoodsBean;

public class NetDao {
    public static void downloadNewGoods(Context mcontext, int page_id,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(page_id))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_ID_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);

    }
}
