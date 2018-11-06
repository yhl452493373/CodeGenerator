package com.h3w.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 此方法中调用CodeGenerator和DataSourceGenerator
 */
public class MybatisPlusCodeGenerate {

    public static void main(String args[]) {
//        singleDataSource();
//        multipleDataSource();
    }

    //单数据源
    private static void singleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setFileOverride(true);
        dsgc.setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);

        CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                "psm", new String[]{"employee"}, "com.yang.demo"
        );
        cgc.setFileOverride(true);
        cgc.setEnableCache(true);
        cgc.setEnableRedis(true);
        CodeGenerator.baseCodeGenerate(cgc);
    }

    //多数据源
    private static void multipleDataSource() {
        DataSourceGeneratorConfig dsgc = new DataSourceGeneratorConfig();
        dsgc.setMultiple(true);
        dsgc.setFileOverride(true);
        dsgc.setCacheEnabled(true);
        CodeGenerator.dataSourceCodeGenerate(dsgc);

        //储存每个数据源需要生成的表名
        Map<String, String[]> dataSourceMap = new HashMap<>();
        dataSourceMap.put("psm", new String[]{
                "employee", "hidden_danger"
        });
        dataSourceMap.put("ime511", new String[]{
                "sys_user", "sys_areas"
        });

        dataSourceMap.keySet().forEach(key -> {
            CodeGeneratorConfig cgc = new CodeGeneratorConfig(
                    key, dataSourceMap.get(key), "com.yang.demo"
            );
            cgc.setFileOverride(true);
            cgc.setEnableCache(true);
            cgc.setEnableRedis(true);

            //psm数据源的包配置
            cgc.setPackageController(cgc.getPackageController() + StringPool.DOT + key);
            cgc.setPackageEntity(cgc.getPackageEntity() + StringPool.DOT + key);
            cgc.setPackageMapper(cgc.getPackageMapper() + StringPool.DOT + key);
            cgc.setPackageService(cgc.getPackageService() + StringPool.DOT + key);
            //默认情况下,mapper.xml文件放在resources目录
            cgc.setMapperPackage(cgc.getMapperPackage() + StringPool.DOT + key);
            //将xml放到mapper.java下的xml目录下的psm文件夹.替换上面一句即可
//        cgc.setMapperInResource(false);
//        cgc.setMapperPackage("mapper.xml." + key);
            CodeGenerator.baseCodeGenerate(cgc);
        });
    }
}
