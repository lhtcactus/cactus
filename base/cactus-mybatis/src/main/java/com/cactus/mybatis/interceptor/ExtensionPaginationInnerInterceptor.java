package com.cactus.mybatis.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

import java.util.List;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.SPACE;

/**
 * 扩展PaginationInnerInterceptor ，增加neo4j支持
 * @author lht
 * @since 2021/10/15 2:25 下午
 */
public class ExtensionPaginationInnerInterceptor extends PaginationInnerInterceptor {
    private final DbType dbType;

    public ExtensionPaginationInnerInterceptor(DbType dbType) {
        super(dbType);
        this.dbType = dbType;
    }

    @Override
    protected String autoCountSql(IPage<?> page, String sql) {
        //对neo4j数据库 做特殊处理
        if (dbType == DbType.NEO4J) {
            int sqlIndex = sql.toLowerCase().lastIndexOf("return");
            return sql.substring(0,sqlIndex)+" return count(*)";
        }
        return super.autoCountSql(page, sql);
    }
    @Override
    public String concatOrderBy(String originalSql, List<OrderItem> orderList){
        //对neo4j数据库 做特殊处理
        if (dbType == DbType.NEO4J) {
            StringBuilder orderSql = new StringBuilder(" order by ");
            orderList.forEach(orderItem -> {
                orderSql.append(SPACE)
                        .append(orderItem.getColumn())
                        .append(SPACE)
                        .append(orderItem.isAsc()?"asc":"desc")
                        .append(SPACE);
            });

            return originalSql+orderSql;
        }
        return super.concatOrderBy(originalSql, orderList);
    }
}
