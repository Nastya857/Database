package org.example;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;
import java.util.Random;

import java.util.concurrent.ThreadLocalRandom;


public class Main {

    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("script_bd.sql");
        FileWriter writerT = new FileWriter("script_bd2.sql");
        ArrayList<Integer> approvedReportsIds = new ArrayList<>();
        ArrayList<Integer> approvedSending = new ArrayList<>();
        ArrayList<Integer> approvedproduct_id = new ArrayList<>();
        ArrayList<Integer> approvedReceptionId = new ArrayList<>();
        ArrayList<Integer> approvedLabReport = new ArrayList<>();
        ArrayList<Integer> approvedSpinId = new ArrayList<>();
        ArrayList<Integer> approvedStoreId = new ArrayList<>();
        ArrayList<Integer> approvedBottilId = new ArrayList<>();
        ArrayList<Integer> approvedStorageId = new ArrayList<>();
        ArrayList<Integer> approvedBlendingId = new ArrayList<>();

        System.out.println("ReportTableSize: " +  10000);
        for (int i = 1; i < 10000; i++) { //
            int report_id = i;
            String ge_repot = RandomStringUtils.randomAlphabetic(25);
            String wc_report = RandomStringUtils.randomAlphabetic(25);
            String m_report = RandomStringUtils.randomAlphabetic(13);
            boolean approve = ThreadLocalRandom.current().nextInt(7) <= 5;
            writer.write(
                    "insert into s312898.reports (ge_repot, approve, wc_report, m_report) values ('%s', '%s', '%s', '%s');"
                            .formatted(ge_repot, approve, wc_report, m_report));
            writer.write('\n');
            if (approve) {
                approvedReportsIds.add(i);
            }
        }
        System.out.println("ProductTableSize: " +  approvedReportsIds.size());
        for (int i = 1; i < approvedReportsIds.size(); i++) { //
            int product_id = ThreadLocalRandom.current().nextInt();
            String product_name = RandomStringUtils.randomAlphabetic(25);
            int minDay = (int) LocalDate.of(1950, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(1980, 1, 1).toEpochDay();
            Random random = new Random();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate product_date = LocalDate.ofEpochDay(randomDay);
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            writer.write(
                    "insert into s312898.product (product_name, product_date, approve, report_id) values ('%s', '%s', '%s', '%s');"
                            .formatted(product_name, product_date, approve, approvedReportsIds.get(i)));
            writer.write('\n');
            if (approve) {
                approvedproduct_id.add(i);
            }
        }
        System.out.println("ReceptionTableSize: 10000");
        for (int i = 1; i < 10000; i++) {
            int reception_id = ThreadLocalRandom.current().nextInt();
            int weight = ThreadLocalRandom.current().nextInt();
            String grade = RandomStringUtils.randomAlphabetic(10);
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 1;
            writer.write("insert into s312898.reception ( weight, grade, approve) values (" + "'" + weight + "', '" + grade + "', '" + approve + "');");
            writer.write('\n');
            if (approve) {
                approvedReceptionId.add(i);
            }
        }
        System.out.println("LabReportTableSize: " +  approvedReceptionId.size());
        for (int i = 1; i <= approvedReceptionId.size()-1; i++) {
            int bottling_id = ThreadLocalRandom.current().nextInt(1000);
            String decision = RandomStringUtils.randomAlphabetic(10);
            writer.write("insert into s312898.lab_report (reception_id, bottling_id, decision) values ('" + approvedReceptionId.get(i) + "','" + bottling_id + "', '" + decision + "');");
            writer.write('\n');
            approvedLabReport.add(i);
        }
        System.out.println("BlendingableSize: " +  Math.min(approvedproduct_id.size(),approvedLabReport.size()));
        for (int i = 1; i < Math.min(approvedproduct_id.size(),approvedLabReport.size()); i++) {
            float weight = ThreadLocalRandom.current().nextFloat(1000);
            int blending_id = ThreadLocalRandom.current().nextInt();
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            //int reception_idd = ThreadLocalRandom.current().nextInt(approvedReceptionId.size());
            //int product_idd = ThreadLocalRandom.current().nextInt();
            //System.out.println(approvedSpinId);
            writer.write("insert into s312898.blending (u) values ('" + approvedproduct_id.get(i) + "','" + weight + "', '"  + approvedReceptionId.get(i)
                    + "', '" + approvedLabReport.get(i) + "', '" + approve + "');");

            writer.write('\n');
            if (approve){
                approvedBlendingId.add(i);
            }
        }
        System.out.println("SpinTableSize: " +  approvedBlendingId.size());
        for (int i = 1; i < approvedBlendingId.size(); i++) {
            int spin_id = i;
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            writerT.write("insert into s312898.spin (blending_id, approve) values ('"+ approvedBlendingId.get(i) + "', '" + approve + "');");
            writerT.write('\n');
            if (approve) {
                approvedSpinId.add(i);
            }
        }

        System.out.println("RecyclingTableSize: " +  approvedBlendingId.size());
        for (int i = 1; i < approvedBlendingId.size(); i++) {
            int recycling = ThreadLocalRandom.current().nextInt(1000);
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            int minDay = (int) LocalDate.of(1980, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2000, 1, 1).toEpochDay();
            Random random = new Random();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate recycling_date = LocalDate.ofEpochDay(randomDay);
            writerT.write("insert into s312898.recycling ( lab_report_id, recycling_date, blending_id, status) values ('" + approvedLabReport.get(i) + "','" + recycling_date + "', '" + approvedBlendingId.get(i) + "', '" + Status.getRandomStatus() + "');");
            writerT.write('\n');
        }
        System.out.println("StorageTableSize: " +  approvedSpinId.size());
        for (int i = 1; i < approvedSpinId.size(); i++) {
            writerT.write("insert into s312898.storage (spin_id, status) values (" + "'"
                    + approvedSpinId.get(i)  + "','" + StorageStatus.getRandomStatus() + "');");
            writerT.write('\n');
            approvedStorageId.add(i);
        }
        System.out.println("BottlingTableSize: " +  approvedStorageId.size());
        for (int i = 1; i < approvedStorageId.size(); i++) {//
            int bottling_count = ThreadLocalRandom.current().nextInt(1000);
            int bottling_id = ThreadLocalRandom.current().nextInt(1000);
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            int minDay = (int) LocalDate.of(2004, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2006, 1, 1).toEpochDay();
            Random random = new Random();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate bottling_date = LocalDate.ofEpochDay(randomDay);
            writerT.write("insert into s312898.bottling (bottling_count, approve,  storage_id, product_id, bottling_date) values (" + "'"
                    + bottling_count + "','" + approve + "', '" + approvedStorageId.get(i) + "', '" + approvedproduct_id.get(i) + "', '" + bottling_date + "');");
            writerT.write('\n');
            if (approve) {
                approvedBottilId.add(i);
            }
        }
        System.out.println("StoreTableSize: 1000");
        for (int i = 1; i <1000; i++) {
            int store_id = ThreadLocalRandom.current().nextInt(1000);
            int bottle_amount = ThreadLocalRandom.current().nextInt(1000);
            int bottling_id = ThreadLocalRandom.current().nextInt(1000);
            int bootled_poroduct = ThreadLocalRandom.current().nextInt(1000);
            int row = ThreadLocalRandom.current().nextInt(approvedBottilId.size());
            writerT.write("insert into s312898.store (bottle_amount, bottling_id, row, product_id) values ('"+ bottle_amount + "','" + bootled_poroduct + "', '" + row + "', '" + approvedproduct_id.get(i) + "');");
            writerT.write('\n');
        }

        System.out.println("SendingTableSize: 3000");
        for (int i = 1; i < 3000; i++) {
            int sending_id = i;
            boolean approve = ThreadLocalRandom.current().nextInt() % 2 == 0;
            String shop_name = RandomStringUtils.randomAlphabetic(13);
            float price = ThreadLocalRandom.current().nextFloat();
            int bottle_amount = ThreadLocalRandom.current().nextInt(1000);
            int store_idd = ThreadLocalRandom.current().nextInt(100);
            writerT.write("insert into s312898.sending (store_id, shop_name, bottle_amount, price, approve) values ('" + store_idd + "','" + shop_name + "', '" + bottle_amount + "', '" + price + "', '" + approve + "');");
            writerT.write('\n');
            if (approve) {
                approvedSending.add(i);
            }
        }
        System.out.println("Stage_infoTableSize: " + approvedproduct_id.size());
        for (int i = 1; i < approvedproduct_id.size(); i++) {//
            String grade = RandomStringUtils.randomAlphabetic(10);
            String stage_name = RandomStringUtils.randomAlphabetic(10);
            int store_id = ThreadLocalRandom.current().nextInt(1000);
            String colour = RandomStringUtils.randomAlphabetic(13);
            float sugar = ThreadLocalRandom.current().nextFloat();
            float acid = ThreadLocalRandom.current().nextFloat();
            float visibility = ThreadLocalRandom.current().nextFloat();
            float weight = ThreadLocalRandom.current().nextFloat();
            float ripeness = ThreadLocalRandom.current().nextFloat();
            int minDay = (int) LocalDate.of(2000, 1, 1).toEpochDay();
            int maxDay = (int) LocalDate.of(2002, 1, 1).toEpochDay();
            Random random = new Random();
            long randomDay = minDay + random.nextInt(maxDay - minDay);
            LocalDate date_start = LocalDate.ofEpochDay(randomDay);
            int miDay = (int) LocalDate.of(2002, 1, 1).toEpochDay();
            int maDay = (int) LocalDate.of(2004, 1, 1).toEpochDay();
            Random rando = new Random();
            long randoDay = minDay + rando.nextInt(maDay - miDay);
            LocalDate date_end = LocalDate.ofEpochDay(randoDay);
            writerT.write("insert into s312898.stage_info (stage_name, stage_id, product, date_start, date_end, sugar, acid, colour, " +
                    "grade, visibility, weight, ripeness) values (" + "'"
                    + stage_name + "', '" + store_id + "','" + approvedproduct_id.get(i) + "', '" + date_start + "', '" + date_end + "', '" +
                    "', '" + sugar + "', '" + acid + "', '" + colour + "', '" + grade + "', '" + visibility + "', '" + weight + "', '" + ripeness + "');");
            writerT.write('\n');
        }

        System.out.println("Product_storeTableSize: " + approvedproduct_id.size());
        for (int i = 1; i < approvedproduct_id.size(); i++) {
            int store_idd = ThreadLocalRandom.current().nextInt(1000);
            writerT.write("insert into s312898.product_store (product_id, store_id) values (" + "'"
                    + approvedproduct_id.get(i) + "', '" + store_idd + "');");
            writerT.write('\n');
        }
        System.out.println("store_sendingTableSize: " + approvedSending.size());
        for (int i = 1; i < approvedSending.size(); i++) {
            int store_idd = ThreadLocalRandom.current().nextInt(100);
            int sending_idd = ThreadLocalRandom.current().nextInt(approvedSending.size());
            writerT.write("insert into s312898.store_sending (sending_id, store_id) values (" + "'"
                    + approvedSending.get(sending_idd) + "', '" + store_idd + "');");
            writerT.write('\n');
        }
        System.out.println("product_sendingTableSize: " + approvedSending.size());
        for (int i = 1; i < approvedSending.size(); i++) {
            int sending_idd =  ThreadLocalRandom.current().nextInt(approvedSending.size());
            int product_idd =  ThreadLocalRandom.current().nextInt(approvedproduct_id.size());
            writerT.write("insert into s312898.product_sending (sending_id, product_id) values (" + "'"
                    + approvedSending.get(sending_idd) + "', '" +approvedproduct_id.get( product_idd) + "');");
            writerT.write('\n');
        }
        System.out.println("bottling_storeTableSize: " + approvedBottilId.size());
        for (int i = 1; i < approvedBottilId.size(); i++) {
            int store_idd = ThreadLocalRandom.current().nextInt(100);
            writerT.write("insert into s312898.bottling_store (bottling_id, store_id) values (" + "'"
                    + approvedBottilId.get(i) + "', '" + store_idd + "');");
            writerT.write('\n');
        }
        System.out.println("bottling_storageTableSize: " + approvedBottilId.size());
        for (int i = 1; i < approvedBottilId.size(); i++) {
            writerT.write("insert into s312898.bottling_storage (bottling_id, storage_id) values (" + "'"
                    + approvedBottilId.get(i) + "', '" + approvedStorageId.get(i) + "');");
            writerT.write('\n');
        }
        writer.close();
        writerT.close();

    }
}