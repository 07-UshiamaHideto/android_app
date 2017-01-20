//1つのセルにあるデータを保存するためのデータクラスです。
package com.example.ushiama.gsapp;

public class MessageRecord {
    //保存するデータ全てを変数で定義します。
    private String imageUrl;
    private String comment;
    private String number;

    //データを１つ作成する関数です。項目が増えたら増やしましょう。
    public MessageRecord(String imageUrl, String comment, String number) {
        this.imageUrl = imageUrl;
        this.comment = comment;
        this.number = number;
    }
    //それぞれの項目を返す関数です。項目が増えたら増やしましょう。
    public String getComment() {
        return comment;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getNumber() {
        return number;
    }
}
