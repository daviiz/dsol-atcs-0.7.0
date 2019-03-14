package devs.core;

public class ObjectModelBase implements java.io.Serializable {

    /**
     * 消息是否有效,初始时为无效状态，
     */
    protected boolean status = true;

    public void setStatusInvalid(){
        this.status = false;
    }

    public void setStatusValid(){
        this.status = true;
    }
}
