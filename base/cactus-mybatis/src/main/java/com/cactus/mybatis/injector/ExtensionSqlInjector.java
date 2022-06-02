package com.cactus.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.cactus.core.exception.GlobalException;
import com.cactus.mybatis.injector.neo4j.Neo4jMapper;
import com.cactus.mybatis.injector.neo4j.methods.*;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 增强{@link com.baomidou.mybatisplus.core.injector.DefaultSqlInjector} 增加neo4j数据库相关方法
 * @author lht
 * @since 2021/1/8 10:52
 */
public class ExtensionSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        if(BaseMapper.class.isAssignableFrom(mapperClass)){
            if (tableInfo.havePK()) {
                return Stream.of(
                        new Insert(),
                        new Delete(),
                        new DeleteByMap(),
                        new DeleteById(),
                        new DeleteBatchByIds(),
                        new Update(),
                        new UpdateById(),
                        new SelectById(),
                        new SelectBatchByIds(),
                        new SelectByMap(),
                        new SelectCount(),
                        new SelectMaps(),
                        new SelectMapsPage(),
                        new SelectObjs(),
                        new SelectList(),
                        new SelectPage()
                ).collect(toList());
            } else {
                logger.warn(String.format("%s ,Not found @TableId annotation, Cannot use Mybatis-Plus 'xxById' Method.",
                        tableInfo.getEntityType()));
                return Stream.of(
                        new Insert(),
                        new Delete(),
                        new DeleteByMap(),
                        new Update(),
                        new SelectByMap(),
                        new SelectCount(),
                        new SelectMaps(),
                        new SelectMapsPage(),
                        new SelectObjs(),
                        new SelectList(),
                        new SelectPage()
                ).collect(toList());
            }
        }else if(Neo4jMapper.class.isAssignableFrom(mapperClass)){
            return Stream.of(
                    new Neo4jInsert(),
                    new Neo4jUpdate(),
                    new Neo4jUpdateById(),
                    new Neo4jDelete(),
                    new Neo4jDeleteById()
            ).collect(toList());
        }else{
            throw new GlobalException("mapperClass:"+mapperClass.getName()+"无法找到mybatis-puls增强注入类型");
        }

    }
}
