package com.example.yoshiki.wakeup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by yoshiki on 2015/10/25.
 */
public class Process {


    /*
    評価＆行動のファイル保存
     */
    public static void writeEvAct(int eva,ArrayList<Integer> act) {

        StringBuilder eva_act = new StringBuilder();
        String lineBuffer = null;
        eva_act.append(",").append(String.valueOf(eva)).append(",");
        for (int act_c : act) {
            eva_act.append(String.valueOf(act_c)).append(",");
        }
        eva_act.deleteCharAt(eva_act.length() - 1);
        eva_act.append("\n");
        String result = eva_act.toString();

        OutputStreamWriter writer2 = null;

        //追記する
        //全データから最初一行だけ取る
        String line;
        StringBuilder allDataNo1 = new StringBuilder();
        try {
            FileInputStream in = new FileInputStream(TopActivity.filePath);

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            lineBuffer = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (!line.equals(lineBuffer)) {
                    allDataNo1.append(line).append("\n");
                }
            }
            in.close();

            StringBuilder newLineBuffer = new StringBuilder();
            //一行目作成
            newLineBuffer.append(lineBuffer).append(result);
            String newLine = newLineBuffer.toString();
            //一行目なし作成

            StringBuilder buf = new StringBuilder();

            buf.append(newLine);
            buf.append(allDataNo1);
            String Data = buf.toString();
            File file = new File(TopActivity.filePath);
            writer2 = new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");


            BufferedWriter bw = new BufferedWriter(writer2);
            bw.write(Data);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    ファイル最新日取得
    ファイルなし or 一行目のエラー　＝　戻り値:-1
     */
    public static int readLatestDate() {
        int latestDay = 1;
        String lineBuffer;
        try {
            FileInputStream in = new FileInputStream(TopActivity.filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            lineBuffer = reader.readLine();
            if (lineBuffer != null) {
                String[] splitData = lineBuffer.split(",", 0);
                latestDay = Integer.parseInt(splitData[0]);
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            latestDay = -1;
            return latestDay;
        }
        return latestDay;
    }

    /*
    上書きする際、前回までの同期部分を保持
     */
    public static StringBuilder fileAllDate() {
        StringBuilder allDataBuilder = new StringBuilder("");
        String str;
        try {
            FileInputStream in = new FileInputStream(TopActivity.filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            while ((str = reader.readLine()) != null) {
                allDataBuilder.append(str).append("\n");
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allDataBuilder;
        //String allData = allDataBuilder.toString();
        //return allData;
    }

    /*
    ファイル書き込み
     */
    public static void fileWrite(String result){
                String s1 = fileAllDate().toString();
                File file = new File(TopActivity.filePath);
                if(!file.exists()){
                    file.getParentFile().mkdirs();
                }
                try {
                    OutputStreamWriter writer2 =
                            new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8");
                    //追記する
                    StringBuilder buf = new StringBuilder();
                    buf.append(result);
                    buf.append(s1);
                    String prevData = buf.toString();

                    BufferedWriter bw = new BufferedWriter(writer2);
                    bw.write(prevData);

                    bw.flush();
                    bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
