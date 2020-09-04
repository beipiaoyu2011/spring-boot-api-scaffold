package com.scaffold.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaffold.test.entity.FxRate;
import com.scaffold.test.mapper.FxRateMapper;
import com.scaffold.test.service.FxRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-09-04
 */

@Slf4j
@Service
public class FxRateServiceImpl extends ServiceImpl<FxRateMapper, FxRate> implements FxRateService {

    @Autowired
    FxRateMapper fxRateMapper;

    @Override
    public void readExcel() {
        List<FxRate> rateList = new ArrayList<>();
        // 读取文件夹里的文件
        File floder = new File("F:/spark");
        File[] files = floder.listFiles();
        for (File file : files) {
            //数据
            List<String> dataList = new ArrayList<>();
            BufferedReader bufferedReader = null;
            // 解析CSV文件
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    dataList.add(line);
                }
                System.out.println(dataList);
                // 判断CSV数据
                // 获取头部数据
                String[] headerData = dataList.get(0).split(",");
                String sparkTimestamp = "SparkTimestamp";
                String bestBidPrice = "BestBidPrice";
                String bestAskPrice = "BestAskPrice";
                int sparkTimestampIndex = 1;
                int bestBidPriceIndex = 5;
                int bestAskPriceIndex = 6;
                // 需要判断索引是否正确
                for (int i = 0; i < headerData.length; i++) {
                    String name = headerData[i];
                    if (name.equals(sparkTimestamp)) {
                        sparkTimestampIndex = i;
                    } else if (name.equals(bestBidPrice)) {
                        bestAskPriceIndex = i;
                    } else if (name.equals(bestAskPrice)) {
                        bestAskPriceIndex = i;
                    }
                }
                // 遍历数据
                for (int i = 0; i < dataList.size(); i++) {
                    if (i > 0) {
                        String[] currentLineData = dataList.get(i).split(",");
                        FxRate fxRate = new FxRate();
                        String fileName = file.getName();
                        String[] fileNameArr = fileName.split("\\.");
                        fxRate.setSource("spark");
                        // 获取货币类型和买卖货币
                        if (fileNameArr.length > 0) {
                            fxRate.setSellCcy(fileNameArr[0].substring(0, 3));
                            fxRate.setBuyCcy(fileNameArr[0].substring(3, 6));
                            fxRate.setCcyType(fileNameArr[1]);
                            fxRate.setCcyPair(fileNameArr[0]);
                        }
                        if (currentLineData.length > 0) {
                            fxRate.setRateDate(currentLineData[sparkTimestampIndex]);
                            double bidPrice = Double.parseDouble(currentLineData[bestBidPriceIndex]);
                            double askPrice = Double.parseDouble(currentLineData[bestAskPriceIndex]);
                            fxRate.setRate((bidPrice + askPrice) / 2);
                        }
                        rateList.add(fxRate);
                    }
                }

                // 入库
                for(FxRate data: rateList){
                    fxRateMapper.insertRate(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
