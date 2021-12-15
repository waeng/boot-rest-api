package ${package.Parent}.manager${modulePackage};

import ${package.Entity}.${entity};
import ${package.Mapper}.${entity}Mapper;
import ${package.Parent}.domain.Page;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
* <p>
* ${table.comment!} Manager
* </p>
*
* @author ${author}
* @since ${date}
*/
@Component
public class ${entity}Manager {
@Autowired
private ${entity}Mapper ${entityVarName}Mapper;

public void insert(${entity} entity) {
Date now = new Date();
entity.setCreatedAt(now);
entity.setUpdatedAt(now);
${entityVarName}Mapper.insert(entity);
}

public int deleteById(Long id) {
return ${entityVarName}Mapper.deleteById(id);
}

public int updateById(${entity} update) {
update.setUpdatedAt(new Date());
return ${entityVarName}Mapper.updateById(update);
}

public ${entity} getById(long id) {
return ${entityVarName}Mapper.selectById(id);
}

public int count(Wrapper<${entity}> wrapper) {
return ${entityVarName}Mapper.selectCount(wrapper);
}

public Page<${entity}> list(Wrapper<${entity}> queryWrapper, int offset, int limit) {
return ${entityVarName}Mapper.selectPage(queryWrapper, offset, limit);
}

public List<${entity}> list(Wrapper<${entity}> queryWrapper) {
return ${entityVarName}Mapper.selectList(queryWrapper);
}

}