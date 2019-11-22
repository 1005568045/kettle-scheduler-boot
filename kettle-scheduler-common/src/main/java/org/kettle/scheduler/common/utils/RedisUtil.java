package org.kettle.scheduler.common.utils;

import lombok.Data;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author WangFan
 * @version 1.1 (GitHub文档: https://github.com/whvcse/RedisUtil )
 * @date 2018-02-24 下午03:09:50
 */
@Data
@Component
public class RedisUtil<k, v> {

    @Resource(type = RedisTemplate.class)
    private RedisTemplate<k, v> redisTemplate;

    /* -------------------key相关操作--------------------- */

    /**
     * 删除key
     *
     * @param key 键值
     */
    public void delete(k key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys 键值集
     */
    public void delete(Collection<k> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 序列化key
     *
     * @param key 键值
     * @return {@code byte[]}
     */
    public byte[] dump(k key) {
        return redisTemplate.dump(key);
    }

    /**
     * 是否存在key
     *
     * @param key 键值
     * @return {@link Boolean}
     */
    public Boolean hasKey(k key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     *
     * @param key     键值
     * @param timeout 过期时间-数字
     * @param unit    时间单位
     * @return {@link Boolean}
     */
    public Boolean expire(k key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间
     *
     * @param key  键值
     * @param date 过期时间-日期
     * @return {@link Boolean}
     */
    public Boolean expireAt(k key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查找匹配的key
     *
     * @param pattern 匹配值
     * @return {@link Set}
     */
    public Set<k> keys(k pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     键值
     * @param dbIndex dbIndex
     * @return {@link Boolean}
     */
    public Boolean move(k key, int dbIndex) {
        return redisTemplate.move(key, dbIndex);
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 键值
     * @return {@link Boolean}
     */
    public Boolean persist(k key) {
        return redisTemplate.persist(key);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  键值
     * @param unit 时间单位
     * @return {@link Long}
     */
    public Long getExpire(k key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long getExpire(k key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return {@link k}
     */
    public k randomKey() {
        return redisTemplate.randomKey();
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 旧的键值
     * @param newKey 新的键值
     */
    public void rename(k oldKey, k newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newkey 不存在时，将 oldKey 改名为 newkey
     *
     * @param oldKey 旧的键值
     * @param newKey 新的键值
     * @return {@link Boolean}
     */
    public Boolean renameIfAbsent(k oldKey, k newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 键值
     * @return {@link DataType}
     */
    public DataType type(k key) {
        return redisTemplate.type(key);
    }

    /* -------------------string相关操作--------------------- */

    /**
     * 设置指定 key 的值
     *
     * @param key   键值
     * @param value 值
     */
    public void set(k key, v value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键值
     * @return {@link v}
     */
    public v get(k key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 返回 key 中字符串值的子字符
     *
     * @param key   键值
     * @param start 截取的开始位置
     * @param end   截取的结束位置
     * @return {@link String}
     */
    public String getRange(k key, long start, long end) {
        return redisTemplate.opsForValue().get(key, start, end);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     *
     * @param key   键值
     * @param value 新值
     * @return {@link v}
     */
    public v getAndSet(k key, v value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)
     *
     * @param key    键值
     * @param offset 偏移量
     * @return {@link Boolean}
     */
    public Boolean getBit(k key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 批量获取
     *
     * @param keys 键值集
     * @return {@link List}
     */
    public List<v> multiGet(Collection<k> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第offset位值变为value
     *
     * @param key    键值
     * @param offset 位置
     * @param value  值,true为1, false为0
     * @return {@link Boolean}
     */
    public Boolean setBit(k key, long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key     键值
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位, 天:TimeUnit.DAYS 小时:TimeUnit.HOURS 分钟:TimeUnit.MINUTES
     *                秒:TimeUnit.SECONDS 毫秒:TimeUnit.MILLISECONDS
     */
    public void setEx(k key, v value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键值
     * @param value 值
     * @return 之前已经存在返回false, 不存在返回true
     */
    public Boolean setIfAbsent(k key, v value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key    键值
     * @param value  值
     * @param offset 从指定位置开始覆写
     */
    public void setRange(k key, v value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long size(k key) {
        return redisTemplate.opsForValue().size(key);
    }

    /**
     * 批量添加
     *
     * @param maps 键值对集
     */
    public void multiSet(Map<k, v> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在
     *
     * @param maps 键值对集
     * @return 之前已经存在返回false, 不存在返回true
     */
    public Boolean multiSetIfAbsent(Map<k, v> maps) {
        return redisTemplate.opsForValue().multiSetIfAbsent(maps);
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key       键值
     * @param increment 增量
     * @return {@link Long}
     */
    public Long incrBy(k key, long increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * @param key       键值
     * @param increment 增量
     * @return {@link Double}
     */
    public Double incrByFloat(k key, double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    /**
     * 追加到末尾
     *
     * @param key   键值
     * @param value 值
     * @return {@link Integer}
     */
    public Integer append(k key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    /* -------------------hash相关操作------------------------- */

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   键值
     * @param field field
     * @return {@link Object}
     */
    public Object hGet(k key, Object field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 键值
     * @return {@link Map}
     */
    public Map<Object, Object> hGetAll(k key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key    键值
     * @param fields field集
     * @return {@link List}
     */
    public List<Object> hMultiGet(k key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    /**
     * @param key     键值
     * @param hashKey hashKey
     * @param value   value
     */
    public void hPut(k key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * @param key  键值
     * @param maps maps
     */
    public void hPutAll(k key, Map<String, Object> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key     键值
     * @param hashKey hashKey
     * @param value   值
     * @return {@link Boolean}
     */
    public Boolean hPutIfAbsent(k key, Object hashKey, Object value) {
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key    键值
     * @param fields field数组
     * @return {@link Long}
     */
    public Long hDelete(k key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 查看哈希表 key 中，指定的字段是否存在
     *
     * @param key   键值
     * @param field field
     * @return {@link Boolean}
     */
    public Boolean hExists(k key, Object field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       键值
     * @param field     field
     * @param increment 增量
     * @return {@link Long}
     */
    public Long hIncrBy(k key, Object field, long increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       键值
     * @param field     field
     * @param increment 增量
     * @return {@link Double}
     */
    public Double hIncrByFloat(k key, Object field, double increment) {
        return redisTemplate.opsForHash().increment(key, field, increment);
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 键值
     * @return {@link Set}
     */
    public Set<Object> hKeys(k key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long hSize(k key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 键值
     * @return {@link List}
     */
    public List<Object> hValues(k key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     键值
     * @param options 选项
     * @return {@link Cursor}
     */
    public Cursor<Entry<Object, Object>> hScan(k key, ScanOptions options) {
        return redisTemplate.opsForHash().scan(key, options);
    }

    /* ------------------------list相关操作---------------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键值
     * @param index 索引
     * @return {@link v}
     */
    public v lIndex(k key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键值
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     * @return {@link List}
     */
    public List<v> lRange(k key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 存储在list头部
     *
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lLeftPush(k key, v value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    @SafeVarargs
    public final Long lLeftPushAll(k key, v... value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lLeftPushAll(k key, Collection<v> value) {
        return redisTemplate.opsForList().leftPushAll(key, value);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lLeftPushIfPresent(k key, v value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    /**
     * 如果pivot存在,再pivot前面添加
     *
     * @param key   键值
     * @param pivot pivot
     * @param value 值
     * @return {@link Long}
     */
    public Long lLeftPush(k key, v pivot, v value) {
        return redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lRightPush(k key, v value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    @SafeVarargs
    public final Long lRightPushAll(k key, v... value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lRightPushAll(k key, Collection<v> value) {
        return redisTemplate.opsForList().rightPushAll(key, value);
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key   键值
     * @param value 值
     * @return {@link Long}
     */
    public Long lRightPushIfPresent(k key, v value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 在pivot元素的右边添加值
     *
     * @param key   键值
     * @param pivot pivot
     * @param value 值
     * @return {@link Long}
     */
    public Long lRightPush(k key, v pivot, v value) {
        return redisTemplate.opsForList().rightPush(key, pivot, value);
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   键值
     * @param index 位置
     * @param value 值
     */
    public void lSet(k key, long index, v value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key 键值
     * @return 删除的元素
     */
    public v lLeftPop(k key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键值
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return {@link v}
     */
    public v lbLeftPop(k key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key 键值
     * @return 删除的元素
     */
    public v lRightPop(k key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     键值
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return {@link v}
     */
    public v lbRightPop(k key, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPop(key, timeout, unit);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      原key
     * @param destinationKey 目标key
     * @return {@link v}
     */
    public v lRightPopAndLeftPush(k sourceKey, k destinationKey) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      原key
     * @param destinationKey 目标key
     * @param timeout        超时时间
     * @param unit           时间单位
     * @return {@link v}
     */
    public v lbRightPopAndLeftPush(k sourceKey, k destinationKey, long timeout, TimeUnit unit) {
        return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key   键值
     * @param index index=0, 删除所有值等于value的元素; index>0, 从头部开始删除第一个值等于value的元素;
     *              index<0, 从尾部开始删除第一个值等于value的元素;
     * @param value 值
     * @return {@link Long}
     */
    public Long lRemove(k key, long index, Object value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * 裁剪list
     *
     * @param key   键值
     * @param start 开始位置
     * @param end   结束位置
     */
    public void lTrim(k key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long lLen(k key) {
        return redisTemplate.opsForList().size(key);
    }

    /* --------------------set相关操作-------------------------- */

    /**
     * set添加元素
     *
     * @param key    键值
     * @param values 值的数组
     * @return {@link Long}
     */
    @SafeVarargs
    public final Long sAdd(k key, v... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key    键值
     * @param values values
     * @return {@link Long}
     */
    public Long sRemove(k key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 键值
     * @return {@link v}
     */
    public v sPop(k key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key     键值
     * @param value   value
     * @param destKey destKey
     * @return {@link Boolean}
     */
    public Boolean sMove(k key, v value, k destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long sSize(k key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key   键值
     * @param value value
     * @return {@link Boolean}
     */
    public Boolean sIsMember(k key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      键值
     * @param otherKey otherKey
     * @return {@link Set}
     */
    public Set<v> sIntersect(k key, k otherKey) {
        return redisTemplate.opsForSet().intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @return {@link Set}
     */
    public Set<v> sIntersect(k key, Collection<k> otherKeys) {
        return redisTemplate.opsForSet().intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key      键值
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return {@link Long}
     */
    public Long sIntersectAndStore(k key, k otherKey, k destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return {@link Long}
     */
    public Long sIntersectAndStore(k key, Collection<k> otherKeys, k destKey) {
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @return {@link Set}
     */
    public Set<v> sUnion(k key, k otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @return {@link Set}
     */
    public Set<v> sUnion(k key, Collection<k> otherKeys) {
        return redisTemplate.opsForSet().union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key      键值
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return {@link Long}
     */
    public Long sUnionAndStore(k key, k otherKey, k destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return {@link Long}
     */
    public Long sUnionAndStore(k key, Collection<k> otherKeys, k destKey) {
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      键值
     * @param otherKey otherKey
     * @return {@link Set}
     */
    public Set<v> sDifference(k key, k otherKey) {
        return redisTemplate.opsForSet().difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @return {@link Set}
     */
    public Set<v> sDifference(k key, Collection<k> otherKeys) {
        return redisTemplate.opsForSet().difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key      键值
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return {@link Long}
     */
    public Long sDifference(k key, k otherKey, k destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return {@link Long}
     */
    public Long sDifference(k key, Collection<k> otherKeys, k destKey) {
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 键值
     * @return {@link Set}
     */
    public Set<v> setMembers(k key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 键值
     * @return {@link v}
     */
    public v sRandomMember(k key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     *
     * @param key   键值
     * @param count count
     * @return {@link List}
     */
    public List<v> sRandomMembers(k key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param key   键值
     * @param count count
     * @return {@link Set}
     */
    public Set<v> sDistinctRandomMembers(k key, long count) {
        return redisTemplate.opsForSet().distinctRandomMembers(key, count);
    }

    /**
     * @param key     键值
     * @param options options
     * @return {@link Cursor}
     */
    public Cursor<v> sScan(k key, ScanOptions options) {
        return redisTemplate.opsForSet().scan(key, options);
    }

    /*------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     *
     * @param key   键值
     * @param value value
     * @param score score
     * @return {@link Boolean}
     */
    public Boolean zAdd(k key, v value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * @param key    键值
     * @param values values
     * @return {@link Long}
     */
    public Long zAdd(k key, Set<TypedTuple<v>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * @param key    键值
     * @param values values
     * @return {@link Long}
     */
    public Long zRemove(k key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 增加元素的score值，并返回增加后的值
     *
     * @param key   键值
     * @param value value
     * @param delta delta
     * @return {@link Double}
     */
    public Double zIncrementScore(k key, v value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
     *
     * @param key   键值
     * @param value value
     * @return {@link Long}0表示第一位
     */
    public Long zRank(k key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 返回元素在集合的排名,按元素的score值由大到小排列
     *
     * @param key   键值
     * @param value value
     * @return {@link Long}
     */
    public Long zReverseRank(k key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   键值
     * @param start 开始位置
     * @param end   结束位置, -1查询所有
     * @return {@link Set}
     */
    public Set<v> zRange(k key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合元素, 并且把score值也获取
     *
     * @param key   键值
     * @param start start
     * @param end   end
     * @return {@link Set}
     */
    public Set<TypedTuple<v>> zRangeWithScores(k key, long start, long end) {
        return redisTemplate.opsForZSet().rangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素
     *
     * @param key 键值
     * @param min 最小值
     * @param max 最大值
     * @return {@link Set}
     */
    public Set<v> zRangeByScore(k key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从小到大排序
     *
     * @param key 键值
     * @param min 最小值
     * @param max 最大值
     * @return {@link Set}
     */
    public Set<TypedTuple<v>> zRangeByScoreWithScores(k key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key   键值
     * @param min   min
     * @param max   max
     * @param start start
     * @param end   end
     * @return {@link Set}
     */
    public Set<TypedTuple<v>> zRangeByScoreWithScores(k key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key   键值
     * @param start start
     * @param end   end
     * @return {@link Set}
     */
    public Set<v> zReverseRange(k key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回score值
     *
     * @param key   键值
     * @param start start
     * @param end   end
     * @return {@link Set}
     */
    public Set<TypedTuple<v>> zReverseRangeWithScores(k key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key 键值
     * @param min min
     * @param max max
     * @return {@link Set}
     */
    public Set<v> zReverseRangeByScore(k key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 根据Score值查询集合元素, 从大到小排序
     *
     * @param key 键值
     * @param min min
     * @param max max
     * @return {@link Set}
     */
    public Set<TypedTuple<v>> zReverseRangeByScoreWithScores(k key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
    }

    /**
     * @param key   键值
     * @param min   min
     * @param max   max
     * @param start start
     * @param end   end
     * @return {@link Set}
     */
    public Set<v> zReverseRangeByScore(k key, double min, double max, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
    }

    /**
     * 根据score值获取集合元素数量
     *
     * @param key 键值
     * @param min min
     * @param max max
     * @return {@link Long}
     */
    public Long zCount(k key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取集合大小
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long zSize(k key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取集合大小
     *
     * @param key 键值
     * @return {@link Long}
     */
    public Long zZCard(k key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 获取集合中value元素的score值
     *
     * @param key   键值
     * @param value value
     * @return {@link Double}
     */
    public Double zScore(k key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   键值
     * @param start start
     * @param end   end
     * @return {@link Long}
     */
    public Long zRemoveRange(k key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 根据指定的score值的范围来移除成员
     *
     * @param key 键值
     * @param min min
     * @param max max
     * @return {@link Long}
     */
    public Long zRemoveRangeByScore(k key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 获取key和otherKey的并集并存储在destKey中
     *
     * @param key      键值
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return {@link Long}
     */
    public Long zUnionAndStore(k key, k otherKey, k destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    /**
     * @param key       键值
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return {@link Long}
     */
    public Long zUnionAndStore(k key, Collection<k> otherKeys, k destKey) {
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 交集
     *
     * @param key      键值
     * @param otherKey otherKey
     * @param destKey  destKey
     * @return {@link Long}
     */
    public Long zIntersectAndStore(k key, k otherKey, k destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    /**
     * 交集
     *
     * @param key       键值
     * @param otherKeys otherKeys
     * @param destKey   destKey
     * @return {@link Long}
     */
    public Long zIntersectAndStore(k key, Collection<k> otherKeys, k destKey) {
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * @param key     键值
     * @param options options
     * @return {@link Cursor}
     */
    public Cursor<TypedTuple<v>> zScan(k key, ScanOptions options) {
        return redisTemplate.opsForZSet().scan(key, options);
    }
}