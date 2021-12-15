package ${package.Parent}.service;

import ${package.Parent}.domain.Page;
import ${package.Parent}.entity${modulePackage}.${entity};
import ${package.Parent}.manager${modulePackage}.${entity}Manager;
import ${package.Parent}.vo${modulePackage}.${entity}VO;
import ${package.Parent}.util.BeanMapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

/**
* <p>
* ${table.comment!} Service
* </p>
*
* @author ${author}
* @since ${date}
*/
@Service
public class ${entity}Service {

private final ${entity}Manager ${entityVarName}Manager;

public ${entity}Service(${entity}Manager ${entityVarName}Manager) {
this.${entityVarName}Manager = ${entityVarName}Manager;
}

public void create(${entity} entity) {
${entityVarName}Manager.insert(entity);
}

public void deleteById(Long id) {
${entityVarName}Manager.deleteById(id);
}

public void updateById(${entity} entity) {
${entityVarName}Manager.updateById(entity);
}

public ${entity} getById(Long id) {
return ${entityVarName}Manager.getById(id);
}

public Page<${entity}> list(int offset, int limit) {
LambdaQueryWrapper<${entity}> queryWrapper = new LambdaQueryWrapper<>();
queryWrapper.orderByDesc(${entity}::getId);
return ${entityVarName}Manager.list(queryWrapper, offset, limit);
}

${toDTOMethod}
${toVOMethod}

}