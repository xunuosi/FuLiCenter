package cn.ucai.fulicenter.dao;

        import android.content.Context;

        import cn.ucai.fulicenter.I;
        import cn.ucai.fulicenter.bean.BoutiqueBean;
        import cn.ucai.fulicenter.bean.CategoryChildBean;
        import cn.ucai.fulicenter.bean.CategoryGroupBean;
        import cn.ucai.fulicenter.bean.GoodsDetailsBean;
        import cn.ucai.fulicenter.bean.NewGoodsBean;
        import cn.ucai.fulicenter.utils.L;

public class NetDao {
    public static void downloadNewGoods(Context mcontext, int catId, int page_id,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(page_id))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    public static void findGoodDetails(Context mcontext, int goodsId
                , OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener) {

        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.GoodsDetails.KEY_GOODS_ID,String.valueOf(goodsId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }

    public static void findBoutiques(Context mcontext
            , OkHttpUtils.OnCompleteListener<BoutiqueBean[]> listener) {

        OkHttpUtils<BoutiqueBean[]> utils = new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(listener);
    }

    public static void findCategoryGroup(Context mcontext
            , OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener) {

        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    public static void findCategoryChildren(Context mcontext,int parentId
            , OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {

        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }

    /**
     * 请求下载分类二级界面的数据
     * @param mcontext
     * @param catId
     * @param page_id
     * @param listener
     */
    public static void findGoodsDetails(Context mcontext, int catId, int page_id,
                                        OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {

        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.GoodsDetails.KEY_CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(page_id))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
}
