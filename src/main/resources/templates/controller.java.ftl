<#--controller生成模板，发布时删除-->
package ${package.Controller};
<#if restControllerStyle>
import org.springframework.web.bind.annotation.*;
<#else>
import org.springframework.stereotype.Controller;
</#if>
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import JSONResult;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import ${cfg.packageConfig}.ServiceConfig;
import ${package.Entity}.${entity};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/data/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final Logger logger = LoggerFactory.getLogger(${table.controllerName}.class);
    private ServiceConfig service = ServiceConfig.serviceConfig;

    /**
     * 分页查询数据
     *
     * @param page  分页信息
     * @param ${table.entityPath} 查询对象
     * @return 查询结果
     */
    <#if !restControllerStyle>
    @ResponseBody
    </#if>
    @RequestMapping("/list")
    public JSONResult list(${entity} ${table.entityPath}, Page<${entity}> page) {
        JSONResult jsonResult = JSONResult.init();
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        //TODO 根据需要决定是否模糊查询，字段值从${table.entityPath}中获取。以下注释部分为模糊查询示例，使用时需要注释或删除queryWrapper.setEntity(${table.entityPath});
        //queryWrapper.like("数据库字段1","字段值");
        //queryWrapper.or();
        //queryWrapper.like("数据库字段2","字段值");
        queryWrapper.setEntity(${table.entityPath});
        service.${table.entityPath}Service.page(page, queryWrapper);
        jsonResult.success().data(page.getRecords()).count(page.getTotal());
        return jsonResult;
    }

    /**
     * 添加数据
     *
     * @param ${table.entityPath} 添加对象
     * @return 添加结果
     */
    <#if !restControllerStyle>
    @ResponseBody
    </#if>
    @RequestMapping("/add")
    public JSONResult add(${entity} ${table.entityPath}) {
        JSONResult jsonResult = JSONResult.init();
        boolean result = service.${table.entityPath}Service.save(${table.entityPath});
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 更新数据
     *
     * @param ${table.entityPath} 更新对象
     * @return 添加结果
     */
    <#if !restControllerStyle>
    @ResponseBody
    </#if>
    @RequestMapping("/update")
    public JSONResult update(${entity} ${table.entityPath}) {
        JSONResult jsonResult = JSONResult.init();
        UpdateWrapper<${entity}> updateWrapper = new UpdateWrapper<>();
        //TODO 根据需要设置需要更新的列，字段值从${table.entityPath}获取。以下注释部分为指定更新列示例，使用时需要注释或删除updateWrapper.setEntity(${table.entityPath});
        //updateWrapper.set("数据库字段1","字段值");
        //updateWrapper.set("数据库字段2","字段值");
        updateWrapper.eq("表示主键的字段","${table.entityPath}中表示主键的值");
        boolean result = service.${table.entityPath}Service.update(${table.entityPath}, updateWrapper);
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }

    /**
     * 删除数据
     *
     * @param ${table.entityPath} 删除对象
     * @param logical 是否逻辑删除。默认false，使用物理删除
     * @return 删除结果
     */
    <#if !restControllerStyle>
    @ResponseBody
    </#if>
    @RequestMapping("/delete")
    public JSONResult delete(${entity} ${table.entityPath}, @RequestParam(required = false, defaultValue = "false") Boolean logical) {
        JSONResult jsonResult = JSONResult.init();
        boolean result;
        if (logical) {
            UpdateWrapper<${entity}> updateWrapper = new UpdateWrapper<>();
            //TODO 根据需要修改表示逻辑删除的列和值。
            updateWrapper.set("表示逻辑删除的字段","表示逻辑删除的值");
            result = service.${table.entityPath}Service.update(${table.entityPath},updateWrapper);
        } else {
            QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
            queryWrapper.setEntity(${table.entityPath});
            result = service.${table.entityPath}Service.remove(queryWrapper);
        }
        if (result)
            jsonResult.success();
        else
            jsonResult.error();
        return jsonResult;
    }
}