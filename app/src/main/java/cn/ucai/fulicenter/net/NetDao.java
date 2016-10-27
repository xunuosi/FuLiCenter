package cn.ucai.fulicenter.net;

        import android.content.Context;

        import java.io.File;

        import cn.ucai.fulicenter.I;
        import cn.ucai.fulicenter.bean.BoutiqueBean;
        import cn.ucai.fulicenter.bean.CartBean;
        import cn.ucai.fulicenter.bean.CategoryChildBean;
        import cn.ucai.fulicenter.bean.CategoryGroupBean;
        import cn.ucai.fulicenter.bean.CollectBean;
        import cn.ucai.fulicenter.bean.GoodsDetailsBean;
        import cn.ucai.fulicenter.bean.MessageBean;
        import cn.ucai.fulicenter.bean.NewGoodsBean;
        import cn.ucai.fulicenter.bean.Result;
        import cn.ucai.fulicenter.utils.MD5;

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

    /**
     * 申请注册方法
     * @param mcontext
     * @param username
     * @param nick
     * @param password
     * @param listener
     */
    public static void register(Context mcontext, String username, String nick
            , String password, OkHttpUtils.OnCompleteListener<Result> listener) {

        OkHttpUtils<Result> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, nick)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }

    /**
     * 登录方法的
     * @param mcontext
     * @param username
     * @param password
     * @param listener
     */
    public static void login(Context mcontext,String username, String password
                                ,OkHttpUtils.OnCompleteListener<String> listener) {

        OkHttpUtils<String> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(String.class)
                .execute(listener);
    }

    /**
     * 更新用户头像的方法
     * @param mcontext
     * @param username
     * @param file
     * @param listener
     */
    public static void updateAvatar(Context mcontext,String username,File file
            ,OkHttpUtils.OnCompleteListener<String> listener) {

        OkHttpUtils<String> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID, username)
                .addParam(I.AVATAR_TYPE, I.AVATAR_TYPE_USER_PATH)
                .addFile2(file)
                .targetClass(String.class)
                .post()
                .execute(listener);
    }

    public static void updateNick(Context mcontext,String username,String newNick
            ,OkHttpUtils.OnCompleteListener<String> listener) {

        OkHttpUtils<String> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME, username)
                .addParam(I.User.NICK, newNick)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void findUserByUserName(Context mcontext,String username
            ,OkHttpUtils.OnCompleteListener<String> listener) {

        OkHttpUtils<String> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME, username)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void findCollectCount(Context mcontext, String username
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME, username)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void findCollects(Context mcontext, String username, int page_id
            , OkHttpUtils.OnCompleteListener<CollectBean[]> listener) {

        OkHttpUtils<CollectBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.PAGE_ID, String.valueOf(page_id))
                .addParam(I.PAGE_SIZE, String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(CollectBean[].class)
                .execute(listener);
    }

    public static void deleteCollect(Context mcontext, String username, int goodsId
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.Collect.GOODS_ID, String.valueOf(goodsId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void isCollect(Context mcontext, String username, int goodsId
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_IS_COLLECT)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.Collect.GOODS_ID, String.valueOf(goodsId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void addCollect(Context mcontext, String username, int goodsId
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_ADD_COLLECT)
                .addParam(I.Collect.USER_NAME, username)
                .addParam(I.Collect.GOODS_ID, String.valueOf(goodsId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void findCarts(Context mcontext, String username
            , OkHttpUtils.OnCompleteListener<CartBean[]> listener) {

        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Collect.USER_NAME, username)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    /**
     * 使用自定义的ResultUtils方法时使用
     * @param mcontext
     * @param username
     * @param listener
     */
    public static void findCarts2(Context mcontext, String username
            , OkHttpUtils.OnCompleteListener<String> listener) {

        OkHttpUtils<String> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Collect.USER_NAME, username)
                .targetClass(String.class)
                .execute(listener);
    }

    public static void updateCart(Context mcontext, int cartId, int count
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID, String.valueOf(cartId))
                .addParam(I.Cart.COUNT, String.valueOf(count))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(I.CART_CHECKED_DEFAULT))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    public static void deleteCart(Context mcontext, int cartId
            , OkHttpUtils.OnCompleteListener<MessageBean> listener) {

        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(mcontext);
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID, String.valueOf(cartId))
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
