package com.beatrix.data.link;
/**
 * @author Beatrice V.
 * @created 11.09.2020 - 11:43
 * @project NetworkLab1
 * Keeper for data inside routes
 */

import com.beatrix.data.Data;

import java.util.List;
import java.util.Map;

public class Route {
    private String msg;
    private String data;
    private String mime_type;
    private List<Data> dataList;
    private Map<String, String> link;

    public String getMsg() {
        return msg;
    }
    public String getData() {
        return data;
    }
    public String getMimeType() {
        return mime_type;
    }

    public Map<String, String> getLink() {
        return link;
    }
    public List<Data> getDataList() {
        return dataList;
    }

    public void setMsg(String msg) { this.msg = msg; }
    public void setData(String data) { this.data = data; }

    public void setMime_type(String mime_type) { this.mime_type = mime_type; }
    public void setLink(Map<String, String> link) { this.link = link; }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Route{" +
                "msg='" + msg + '\'' +
                ", link=" + link +
                ", data='" + data + '\'' +
                ", mime_type='" + mime_type + '\'' +
                ", dataList=" + dataList +
                '}';
    }
}
