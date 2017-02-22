package com.hxnidc.hanxun_vpn.bean;

/**
 * Created by on 2017/1/10 16:19
 * Author：yrg
 * Describe:
 */


public class Ticker {


    /**
     * ticker : {"high":"6427.02","low":"6131.00","buy":"6354.74","sell":"6355.00","last":"6354.99","vol":"1389106.60140000","date":1484036302,"vwap":"6344.14","prev_close":"6274.43","open":"6273.73"}
     */

    private TickerBean ticker;

    public TickerBean getTicker() {
        return ticker;
    }

    public void setTicker(TickerBean ticker) {
        this.ticker = ticker;
    }

    public static class TickerBean {
        /**
         * high : 6427.02
         * low : 6131.00
         * buy : 6354.74
         * sell : 6355.00
         * last : 6354.99
         * vol : 1389106.60140000
         * date : 1484036302
         * vwap : 6344.14
         * prev_close : 6274.43
         * open : 6273.73
         */

        private String high;
        private String low;
        private String buy;
        private String sell;
        private String last;
        private String vol;
        private int date;
        private String vwap;
        private String prev_close;
        private String open;

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getBuy() {
            return buy;
        }

        public void setBuy(String buy) {
            this.buy = buy;
        }

        public String getSell() {
            return sell;
        }

        public void setSell(String sell) {
            this.sell = sell;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getVol() {
            return vol;
        }

        public void setVol(String vol) {
            this.vol = vol;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String getVwap() {
            return vwap;
        }

        public void setVwap(String vwap) {
            this.vwap = vwap;
        }

        public String getPrev_close() {
            return prev_close;
        }

        public void setPrev_close(String prev_close) {
            this.prev_close = prev_close;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }
    }
}
