package id.hns.restoku;

import java.io.Serializable;

/**
 * Created by HaidarNS on 17/11/2015.
 */
public class MenuModel implements Serializable{
    private String id, name, price, restoId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRestoId() {
        return restoId;
    }

    public void setRestoId(String restoId) {
        this.restoId = restoId;
    }
}
