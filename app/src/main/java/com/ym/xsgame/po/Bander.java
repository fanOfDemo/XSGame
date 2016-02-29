package com.ym.xsgame.po;

import java.util.List;

/**
 * Created by wengyiming on 2016/2/29.
 */
public class Bander {

    /**
     * ReturnCode : 0
     * ReturnMessage : success
     * ReturnData : {"data":[{"iid":61,"iplat":1,"itypeid":1,"sadname":"三国魂","tcss":3,"cposition":"f","surl":"http://xs.qidian.com/api/syxs/sgame/gameService.php?gameid=200241&ty=0","saurl":"","simg":"http://img1.qidian.com/upload/gamesy/2016/02/25/20160225105813du3o7pcsxb.jpg","isort":0,"itag":2,"ipage":1},{"iid":59,"iplat":1,"itypeid":1,"sadname":"平台活动","tcss":3,"cposition":"b","surl":"http://xs.qidian.com/h5v2/activity/activityDetailFrame.html?src=http://xs.qidian.com/h5v2/activity/ploy/lb/index.html","saurl":"","simg":"http://img1.qidian.com/upload/gamesy/2016/02/24/20160224153813j36pcbodvu.jpg","isort":0,"itag":1,"ipage":1},{"iid":57,"iplat":1,"itypeid":1,"sadname":"我欲封天","tcss":3,"cposition":"a","surl":"http://xs.qidian.com/api/syxs/sgame/gameService.php?gameid=200057&ty=0","saurl":"","simg":"http://img.qidian.com/upload/gamesy/2016/02/15/20160215094032xdkmh90qab.jpg","isort":0,"itag":2,"ipage":1}]}
     */

    private int ReturnCode;
    private String ReturnMessage;
    private ReturnDataEntity ReturnData;

    public void setReturnCode(int ReturnCode) {
        this.ReturnCode = ReturnCode;
    }

    public void setReturnMessage(String ReturnMessage) {
        this.ReturnMessage = ReturnMessage;
    }

    public void setReturnData(ReturnDataEntity ReturnData) {
        this.ReturnData = ReturnData;
    }

    public int getReturnCode() {
        return ReturnCode;
    }

    public String getReturnMessage() {
        return ReturnMessage;
    }

    public ReturnDataEntity getReturnData() {
        return ReturnData;
    }

    public static class ReturnDataEntity {
        /**
         * iid : 61
         * iplat : 1
         * itypeid : 1
         * sadname : 三国魂
         * tcss : 3
         * cposition : f
         * surl : http://xs.qidian.com/api/syxs/sgame/gameService.php?gameid=200241&ty=0
         * saurl :
         * simg : http://img1.qidian.com/upload/gamesy/2016/02/25/20160225105813du3o7pcsxb.jpg
         * isort : 0
         * itag : 2
         * ipage : 1
         */

        private List<DataEntity> data;

        public void setData(List<DataEntity> data) {
            this.data = data;
        }

        public List<DataEntity> getData() {
            return data;
        }

        public static class DataEntity {
            private int iid;
            private int iplat;
            private int itypeid;
            private String sadname;
            private int tcss;
            private String cposition;
            private String surl;
            private String saurl;
            private String simg;
            private int isort;
            private int itag;
            private int ipage;

            public void setIid(int iid) {
                this.iid = iid;
            }

            public void setIplat(int iplat) {
                this.iplat = iplat;
            }

            public void setItypeid(int itypeid) {
                this.itypeid = itypeid;
            }

            public void setSadname(String sadname) {
                this.sadname = sadname;
            }

            public void setTcss(int tcss) {
                this.tcss = tcss;
            }

            public void setCposition(String cposition) {
                this.cposition = cposition;
            }

            public void setSurl(String surl) {
                this.surl = surl;
            }

            public void setSaurl(String saurl) {
                this.saurl = saurl;
            }

            public void setSimg(String simg) {
                this.simg = simg;
            }

            public void setIsort(int isort) {
                this.isort = isort;
            }

            public void setItag(int itag) {
                this.itag = itag;
            }

            public void setIpage(int ipage) {
                this.ipage = ipage;
            }

            public int getIid() {
                return iid;
            }

            public int getIplat() {
                return iplat;
            }

            public int getItypeid() {
                return itypeid;
            }

            public String getSadname() {
                return sadname;
            }

            public int getTcss() {
                return tcss;
            }

            public String getCposition() {
                return cposition;
            }

            public String getSurl() {
                return surl;
            }

            public String getSaurl() {
                return saurl;
            }

            public String getSimg() {
                return simg;
            }

            public int getIsort() {
                return isort;
            }

            public int getItag() {
                return itag;
            }

            public int getIpage() {
                return ipage;
            }
        }
    }
}
