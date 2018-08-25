package top.bootz.security.api.bean;

import java.util.List;

import com.yxt.common.pojo.UserAccount;

/**
 * UserAccountList
 */
public class UserAccountList {
    private List<UserAccount> datas;

    public List<UserAccount> getDatas() {
        return datas;
    }

    public void setDatas(List<UserAccount> datas) {
        this.datas = datas;
    }
}
