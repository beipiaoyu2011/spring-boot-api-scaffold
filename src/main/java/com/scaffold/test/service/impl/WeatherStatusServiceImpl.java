package com.scaffold.test.service.impl;

import com.scaffold.test.constants.BaseApplication;
import com.scaffold.test.entity.WeatherStatus;
import com.scaffold.test.mapper.WeatherStatusMapper;
import com.scaffold.test.service.WeatherStatusService;
import com.scaffold.test.utils.DragEasyUtils;
import com.scaffold.test.utils.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author alex wong
 * @since 2020-07-31
 */

@Slf4j
@Service
public class WeatherStatusServiceImpl implements WeatherStatusService {

    @Autowired
    WeatherStatusMapper weatherStatusMapper;

    @Autowired
    private BaseApplication baseApplication;

    @Override
    public void insertStatus() {
        // 天气
//        String[] statusNames = {"阴", "阴转多云", "多云转阴", "多云转小雨", "小雨转阴", "晴", "晴转多云", "多云转晴", "多云", "雾", "小雨", "中雨", "大雨", "雷阵雨", "暴雨", "冰雹", "雨夹雪", "小雪", "中雪", "大雪", "暴雪"};
//        String[] statusValues = {"1", "2", "3", "20", "21", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};


        List<WeatherStatus> weatherStatusList = new ArrayList<>();
        // icon 文件路径
        String iconFolderPath = baseApplication.getIconPath();
        // 判断环境
        if (SystemUtils.isMac()) {
            iconFolderPath = baseApplication.getMacIconPath();
        }

        // 自动读取文件，不再指定名字
        File file = new File(iconFolderPath + "/weather");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName().split("\\.")[0];
            String fileValue = String.valueOf(i);
            WeatherStatus weatherStatus = new WeatherStatus();
            weatherStatus.setName(fileName);
            weatherStatus.setValue(fileValue);
            log.info("第" + (i + 1) + "张---图片开始上传---" + fileName);
            // 上传图床
            String iconUrl = DragEasyUtils.getIconUrl(files[i]);
            weatherStatus.setIcon(iconUrl);
            weatherStatusList.add(weatherStatus);

        }

//        for (int i = 0; i < statusNames.length; i++) {
//            WeatherStatus weatherStatus = new WeatherStatus();
//            weatherStatus.setName(statusNames[i]);
//            weatherStatus.setValue(statusValues[i]);
//            log.info("第" + (i + 1) + "张---图片开始上传---" + statusNames[i]);
//            // 上传图床
//            String iconPath = iconFolderPath + "/weather/" + statusNames[i] + ".png";
//            String iconUrl = DragEasyUtils.getIconUrl(new File(iconPath));
//            weatherStatus.setIcon(iconUrl);
//            weatherStatusList.add(weatherStatus);
//        }

        log.info("-----图片上传完成------");

        for (WeatherStatus weatherStatus : weatherStatusList) {
            weatherStatusMapper.insertStatus(weatherStatus);
        }

    }
}
