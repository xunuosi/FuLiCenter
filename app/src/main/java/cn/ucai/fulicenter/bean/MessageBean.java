package cn.ucai.fulicenter.bean;

/**
 * Created by Elder on 2016/10/13.
 */

public class MessageBean {


    /**
     * success : true
     * msg : 收藏成功
     */

    private boolean success;
    private String msg;

    public MessageBean() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
