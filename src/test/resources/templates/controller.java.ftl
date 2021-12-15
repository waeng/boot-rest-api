package ${package.Controller};

import ${package.Parent}.common.ResponseResultWrapper;
import ${package.Parent}.domain.Page;
import ${package.Parent}.entity${modulePackage}.${entity};
import ${package.Parent}.form${modulePackage}.${entity}Form;
import ${package.Parent}.service${modulePackage}.${entity}Service;
import ${package.Parent}.vo${modulePackage}.${entity}VO;
import ${package.Parent}.util.BeanMapUtil;
import ${package.Parent}.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
* <p>
* ${table.comment!} Controller
* </p>
*
* @author ${author}
* @since ${date}
*/
@ResponseResultWrapper
@RestController
@RequestMapping("/api/v1${requestPath}")
@Validated
@Api(tags = "${table.comment!}")
public class ${entity}Controller {

private final ${entity}Service ${entityVarName}Service;

public ${entity}Controller(${entity}Service ${entityVarName}Service) {
this.${entityVarName}Service = ${entityVarName}Service;
}

@ApiOperation("创建")
@PostMapping("/create")
public ${entity}VO create(@RequestBody @Valid ${entity}Form form) {
${entity} entity = toEntity(form);
${entityVarName}Service.create(toEntity(form));
return ${entityVarName}Service.toVO(entity);
}

@ApiOperation("删除")
@PostMapping("/{id}/delete")
public void delete(@PathVariable @Min(1) Long id) {
${entityVarName}Service.deleteById(id);
}

@ApiOperation("修改")
@PostMapping("/{id}/update")
public ${entity}VO update(@PathVariable @Min(1) Long id, @RequestBody @Valid ${entity}Form form) {
${entityVarName}Service.updateById(toEntity(form).setId(id));
return ${entityVarName}Service.toVO(${entityVarName}Service.getById(id));
}

@ApiOperation("根据 ID 查询")
@GetMapping("/{id}")
public ${entity}VO getById(@PathVariable @Min(1) Long id) {
${entity} entity = ${entityVarName}Service.getById(id);
return ${entityVarName}Service.toVO(entity);
}

@ApiOperation("列表")
@GetMapping("/list")
public PageVO<${entity}VO> list(
    @ApiParam(value = "页起始位置", defaultValue = "0") @Min(0) @Max(10000) @RequestParam(defaultValue = "0") int offset,
    @ApiParam(value = "页大小", defaultValue = "20") @Min(1) @Max(100) @RequestParam(defaultValue = "20") int limit) {
    Page<${entity}> page = ${entityVarName}Service.list(offset, limit);
    return BeanMapUtil.toPageVo(page, ${entityVarName}Service::toVO);
    }

    ${toEntityMethod}

    }