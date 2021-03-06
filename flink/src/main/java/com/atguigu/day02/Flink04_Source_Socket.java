package com.atguigu.day02;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Flink04_Source_Socket {
    public static void main(String[] args) throws Exception {

        // 1. 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 2. 从端口获取数据
        DataStreamSource<String> socketTextStream = env.socketTextStream("hadoop106", 9999);

        // 3. 打印数据
        socketTextStream.print();

        // 4. 执行任务
        env.execute();

    }
}
