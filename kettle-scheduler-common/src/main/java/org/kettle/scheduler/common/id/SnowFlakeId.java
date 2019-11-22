package org.kettle.scheduler.common.id;

import java.text.MessageFormat;

/**
 * 雪花算法id生成器
 * <pre>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位dataCenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * </pre>
 *
 * @author lyf
 */
public class SnowFlakeId {

    /** 起始的时间戳(2019/6/30) */
    private final static long START_TIMESTAMP = 1561824000L;

    /** 机器id所占的位数 */
    private final static long MACHINE_BITS = 5L;
    /** 数据标识id所占的位数 */
    private final static long DATA_CENTER_BITS = 5L;
    /** 序列在id中占的位数 */
    private final static long SEQUENCE_BITS = 12L;

    /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
    private final static long MAX_MACHINE = ~(-1L << MACHINE_BITS);
    /** 支持的最大数据标识id，结果是31 */
    private final static long MAX_DATA_CENTER = ~(-1L << DATA_CENTER_BITS);
    /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /** 机器ID向左移12位 */
    private final static long MACHINE_LEFT = SEQUENCE_BITS;
    /** 数据标识id向左移17位(12+5) */
    private final static long DATA_CENTER_LEFT = SEQUENCE_BITS + MACHINE_BITS;
    /** 时间截向左移22位(5+5+12) */
    private final static long TIMESTAMP_LEFT = SEQUENCE_BITS + MACHINE_BITS + DATA_CENTER_BITS;

    /** 机器标识ID(0~31) */
    private long machineId;
    /** 数据标识ID(0~31) */
    private long dataCenterId;
    /** 毫秒内序列(0~4095) */
    private long sequence = 0L;
    /** 上次生成ID的时间截 */
    private long lastTimestamp = -1L;

    public SnowFlakeId(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATA_CENTER || dataCenterId < 0) {
            throw new IllegalArgumentException(MessageFormat.format("dataCenterId can't be greater than '{0}' or less than 0", MAX_DATA_CENTER));
        }
        if (machineId > MAX_MACHINE || machineId < 0) {
            throw new IllegalArgumentException(MessageFormat.format("machineId can't be greater than '{0}' or less than 0", MAX_MACHINE));
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     */
    public synchronized long nextId() {
        long currentTimeMillis = System.currentTimeMillis();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (currentTimeMillis < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards Refusing to generate id");
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        if (currentTimeMillis == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 同一毫秒的序列数已经达到最大, 阻塞到下一个毫秒, 获得新的时间戳
            if (sequence == 0L) {
                currentTimeMillis = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        // 存储当前生成的时间戳
        lastTimestamp = currentTimeMillis;

        // 移运算拼到一起组成64位的ID, 组成: 时间戳部分+数据中心部分+机器标识部分+毫秒内序列
        return (currentTimeMillis - START_TIMESTAMP) << TIMESTAMP_LEFT | dataCenterId << DATA_CENTER_LEFT | machineId << MACHINE_LEFT | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     */
    private long getNextMill() {
        long mill = System.currentTimeMillis();
        while (mill <= lastTimestamp) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }
}
