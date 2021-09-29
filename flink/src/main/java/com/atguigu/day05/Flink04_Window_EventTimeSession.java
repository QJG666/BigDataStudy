package com.atguigu.day05;

import com.atguigu.bean.WaterSensor1;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.time.Duration;

public class Flink04_Window_EventTimeSession {
    public static void main(String[] args) throws Exception {

        // 1. 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 读取端口数据并转换为JavaBean
        SingleOutputStreamOperator<WaterSensor1> waterSensor1DS = env.socketTextStream("hadoop106", 9999)
                .map(data -> {
                    String[] split = data.split(",");
                    return new WaterSensor1(split[0], Long.parseLong(split[1]), Integer.parseInt(split[2]));
                });

        // 3. 提取数据中的时间戳字段
        // Java中泛型方法，泛型写在方法前面
        // WatermarkStrategy.<WaterSensor1>forMonotonousTimestamps()
        // 处理乱序的数据
        WatermarkStrategy<WaterSensor1> waterSensor1WatermarkStrategy = WatermarkStrategy
                .<WaterSensor1>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor1>() {
                    @Override
                    public long extractTimestamp(WaterSensor1 element, long recordTimestamp) {
                        return element.getTs() * 1000L;
                    }
                });

        SingleOutputStreamOperator<WaterSensor1> waterSensor1SingleOutputStreamOperator = waterSensor1DS
                .assignTimestampsAndWatermarks(waterSensor1WatermarkStrategy);

        // 4. 按照id分组
        KeyedStream<WaterSensor1, String> keyedStream = waterSensor1SingleOutputStreamOperator
                .keyBy(WaterSensor1::getId);

        // 5. 开窗
        // WaterMark跟数据比，大于等于5秒
        // 时间间隔：指的是WaterMark跟数据本身的时间差值，包含间隔时间
        WindowedStream<WaterSensor1, String, TimeWindow> window = keyedStream.window(EventTimeSessionWindows.withGap
                (Time.seconds(5)));

        // 6. 计算总和
        SingleOutputStreamOperator<WaterSensor1> result = window.sum("vc");

        // 7. 打印
        result.print();

        // 8. 执行任务
        env.execute();


    }
}
