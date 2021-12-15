package waeng.bootrestapi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.SimpleAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据数据库表结构，实体类、Manager 类、Service 类、VO 类、DTO 类、Form 类、Controller 类、
 * toVO 方法、toDTO 方法、toEntity 方法和简单的 CRUD 代码。
 *
 * @author waeng
 * @since 2021/12/15
 */
public class CodeGenerator {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/waeng?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&autoReconnect=true&autoReconnectForPools=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String PACKAGE = "waeng.bootrestapi";

    //模块prefix
    private static final String MODULE_PACKAGE = "";
    private static final String BASE_MAPPER_CLASS = PACKAGE + ".mapper.BaseMapper";
    private static final String AUTHOR = "waeng";

    private static final String OUTPUT_DIR = "./target/generated-code";
    private static final String MANAGER_TEMPLATE = "/templates/manager.java.ftl";
    private static final String VO_TEMPLATE = "/templates/entity.java.ftl";
    private static final String DTO_TEMPLATE = "/templates/entity.java.ftl";
    private static final String FORM_TEMPLATE = "/templates/entity.java.ftl";
    private static final String SERVICE_TEMPLATE = "/templates/service.java.ftl";
    private static final String CONTROLLER_TEMPLATE = "/templates/controller.java.ftl";

    // column comment e.g. 性别. 0:未知, 1:男, 2:女, 3:其他
    private static final String COLUMN_COMMENT_ENUM_VALUE_REGEX = "\\s*(\\d+)[:：](.+?)([,，]|$)";
    private static final Pattern COLUMN_COMMENT_ENUM_PATTERN = Pattern.compile("^(.+?)(" + COLUMN_COMMENT_ENUM_VALUE_REGEX + ")+$");

    public static void main(String[] args) {
        new MySimpleAutoGenerator().execute();
    }

    public static class MySimpleAutoGenerator extends SimpleAutoGenerator {
        @Override
        public IConfigBuilder<DataSourceConfig> dataSourceConfigBuilder() {
            return new DataSourceConfig.Builder(URL, USERNAME, PASSWORD);
        }

        @Override
        public IConfigBuilder<GlobalConfig> globalConfigBuilder() {
            return new GlobalConfig.Builder()
                    .author(AUTHOR)
                    .dateType(DateType.ONLY_DATE)
                    .outputDir(OUTPUT_DIR)
                    .fileOverride()
                    .openDir(false);
        }

        @Override
        public IConfigBuilder<PackageConfig> packageConfigBuilder() {
            return new PackageConfig.Builder()
                    .parent(PACKAGE)
                    .entity("entity" + MODULE_PACKAGE)
                    .mapper("mapper" + MODULE_PACKAGE)
                    .service("service" + MODULE_PACKAGE)
                    .controller("controller" + MODULE_PACKAGE);
        }

        @Override
        public IConfigBuilder<StrategyConfig> strategyConfigBuilder() {
            return new StrategyConfig.Builder()
                    .entityBuilder()
                    .enableLombok()
                    .enableChainModel()
                    .naming(NamingStrategy.underline_to_camel)
                    .idType(IdType.ASSIGN_ID)
                    .controllerBuilder()
                    .enableRestStyle()
                    .enableHyphenStyle();
        }

        @Override
        protected <T> T configBuilder(IConfigBuilder<T> configBuilder) {
            return super.configBuilder(configBuilder);
        }

        @Override
        public AbstractTemplateEngine templateEngine() {
            return new MyFreemarkerTemplateEngine();
        }
    }

    /**
     * @author waeng
     * @since 2021/12/15
     */
    public static class MyFreemarkerTemplateEngine extends FreemarkerTemplateEngine {
        public AbstractTemplateEngine batchOutput() {
            try {
                ConfigBuilder config = this.getConfigBuilder();
                List<TableInfo> tableInfoList = config.getTableInfoList();
                tableInfoList.forEach(tableInfo -> {
                    Map<String, Object> objectMap = this.getObjectMap(config, tableInfo);
                    Optional.ofNullable(config.getInjectionConfig())
                            .ifPresent(t -> t.beforeOutputFile(tableInfo, objectMap));

                    setBooleanColumnComment(tableInfo, objectMap);
                    outputEntity(tableInfo, objectMap);
                    outputVO(tableInfo, objectMap);
                    outputDTO(tableInfo, objectMap);
                    outputForm(tableInfo, objectMap);
                    outputMapper(tableInfo, objectMap);
                    outputManager(tableInfo, objectMap);
                    outputService(tableInfo, objectMap);
                    outputController(tableInfo, objectMap);
                });
            } catch (Exception e) {
                throw new RuntimeException("无法创建文件，请检查配置信息！", e);
            }
            return this;
        }

        @Override
        public Map<String, Object> getObjectMap(ConfigBuilder config, TableInfo tableInfo) {
            Map<String, Object> map = super.getObjectMap(config, tableInfo);
            map.put("entityVarName", lowerCaseFirstLetter((String) map.get("entity")));
            map.put("baseMapperClass", BASE_MAPPER_CLASS);
            map.put("modulePackage", MODULE_PACKAGE);
            map.put("vo", false);
            map.put("dto", false);
            map.put("form", false);
            return map;
        }

        private void setBooleanColumnComment(TableInfo tableInfo, Map<String, Object> objectMap) {
            for (TableField field : tableInfo.getFields()) {
                if (field.getPropertyType().equals("Boolean")) {
                    Matcher commentMatcher = COLUMN_COMMENT_ENUM_PATTERN.matcher(field.getComment());
                    if (commentMatcher.matches()) {
                        String fieldComment = trim(commentMatcher.group(1));
                        field.setComment(fieldComment);
                    }
                }
            }
        }

        // ---- <生成 Manager> ----
        protected void outputManager(TableInfo tableInfo, Map<String, Object> objectMap) {
            String simpleClassName = tableInfo.getEntityName() + "Manager";
            outputFile("manager" + MODULE_PACKAGE, simpleClassName, objectMap, MANAGER_TEMPLATE);
        }
        // ---- </生成 Manager> ----

        // ---- <生成 VO 类> ----
        protected void outputVO(TableInfo tableInfo, Map<String, Object> objectMap) {
            objectMap.put("swagger", true);
            objectMap.put("vo", true);
            objectMap.put("entity", tableInfo.getEntityName() + "VO");

            String simpleClassName = tableInfo.getEntityName() + "VO";
            outputFile("vo" + MODULE_PACKAGE, simpleClassName, objectMap, VO_TEMPLATE);

            objectMap.put("entity", tableInfo.getEntityName());
            objectMap.put("swagger", false);
            objectMap.put("vo", false);
        }
        // ---- </生成 VO 类> ----

        // ---- <生成 DTO 类> ----
        protected void outputDTO(TableInfo tableInfo, Map<String, Object> objectMap) {
            objectMap.put("dto", true);
            objectMap.put("entity", tableInfo.getEntityName() + "DTO");

            String simpleClassName = tableInfo.getEntityName() + "DTO";
            outputFile("dto" + MODULE_PACKAGE, simpleClassName, objectMap, DTO_TEMPLATE);

            objectMap.put("entity", tableInfo.getEntityName());
            objectMap.put("dto", false);
        }
        // ---- </生成 DTO 类> ----

        // ---- <生成 Form 类> ----
        protected void outputForm(TableInfo tableInfo, Map<String, Object> objectMap) {
            objectMap.put("swagger", true);
            objectMap.put("form", true);
            objectMap.put("entity", tableInfo.getEntityName() + "Form");

            String simpleClassName = tableInfo.getEntityName() + "Form";
            outputFile("form" + MODULE_PACKAGE, simpleClassName, objectMap, FORM_TEMPLATE);

            objectMap.put("entity", tableInfo.getEntityName());
            objectMap.put("form", false);
            objectMap.put("swagger", false);
        }
        // ---- </生成 Form 类> ----

        // ---- <生成 Service 类> ----
        protected void outputService(TableInfo tableInfo, Map<String, Object> objectMap) {
            objectMap.put("toVOMethod", generateEntityToVOCode(tableInfo));
            objectMap.put("toDTOMethod", generateEntityToDTOCode(tableInfo));
            String simpleClassName = tableInfo.getEntityName() + "Service";
            outputFile("service" + MODULE_PACKAGE, simpleClassName, objectMap, SERVICE_TEMPLATE);
        }

        private String generateEntityToVOCode(TableInfo tableInfo) {
            return generateBeanMapCode(
                    tableInfo,
                    "toVO",
                    tableInfo.getEntityName(),
                    tableInfo.getEntityName() + "VO",
                    "entity",
                    "vo",
                    true,
                    null);
        }

        private String generateEntityToDTOCode(TableInfo tableInfo) {
            return generateBeanMapCode(
                    tableInfo,
                    "toDTO",
                    tableInfo.getEntityName(),
                    tableInfo.getEntityName() + "DTO",
                    "entity",
                    "dto",
                    false,
                    null);
        }
        // ---- </生成 Service 类> ----

        // ---- <生成 Controller 类> ----
        protected void outputController(TableInfo tableInfo, Map<String, Object> objectMap) {
            objectMap.put("toEntityMethod", generateFormToEntityCode(tableInfo));
            objectMap.put("requestPath", MODULE_PACKAGE.replace(".", "/") + "/" + tableInfo.getName().replace("_", "/"));
            String simpleClassName = tableInfo.getEntityName() + "Controller";
            outputFile("controller" + MODULE_PACKAGE, simpleClassName, objectMap, CONTROLLER_TEMPLATE);
        }

        private final List<String> formPropertyExclude = Arrays.asList("id", "createdAt", "updatedAt", "createTime", "updateTime");

        private String generateFormToEntityCode(TableInfo tableInfo) {
            return generateBeanMapCode(
                    tableInfo,
                    "toEntity",
                    tableInfo.getEntityName() + "Form",
                    tableInfo.getEntityName(),
                    "form",
                    "entity",
                    false,
                    s -> !formPropertyExclude.contains(s));
        }
        // ---- </生成 Controller 类> ----

        // ---- <bean map> ----
        private String generateBeanMapCode(TableInfo tableInfo,
                                           String methodName,
                                           String fromSimpleClassName,
                                           String toSimpleClassName,
                                           String fromVarName,
                                           String toVarName,
                                           boolean isVO,
                                           Function<String, Boolean> propertyNameFilter) {
            String indentation = "    ";
            StringBuilder builder = new StringBuilder(128);
            builder.append(indentation).append("public " + toSimpleClassName + " " + methodName + "(" + fromSimpleClassName + " " + fromVarName + ") {\n");
            builder.append(indentation).append("    if (" + fromVarName + " == null) {\n");
            builder.append(indentation).append("        return null;\n");
            builder.append(indentation).append("    }\n");
            builder.append(indentation).append("    " + toSimpleClassName + " " + toVarName + " = new " + toSimpleClassName + "();\n");

            for (TableField field : tableInfo.getFields()) {
                if (propertyNameFilter != null && !propertyNameFilter.apply(field.getPropertyName())) {
                    continue;
                }
                String upperName = upperCaseFirstLetter(field.getPropertyName());
                String setValueStatement = toVarName + ".set" + upperName
                        + "(" + getGetterString(field.getPropertyName(), field.getPropertyType(), fromVarName, isVO) + ");";
                builder.append(indentation).append("    " + setValueStatement + "\n");
            }

            builder.append(indentation).append("    return " + toVarName + ";\n");
            builder.append(indentation).append("}\n");
            return builder.toString();
        }

        private String getGetterString(String fieldName, String fieldType, String sourceVar, boolean isVo) {
            String upperName = upperCaseFirstLetter(fieldName);
            String str = sourceVar + ".get" + upperName + "()";
            if (isVo && "Date".equals(fieldType)) {
                return "BeanMapUtil.toLong(" + str + ")";
            } else {
                return str;
            }
        }
        // ---- </bean map> ----


        private void outputFile(String subPackage,
                                String simpleClassName,
                                Map<String, Object> objectMap,
                                String templatePath) {
            String packageName = getConfigBuilder().getPackageConfig().getParent() + "." + subPackage;
            String filePath = getConfigBuilder().getGlobalConfig().getOutputDir()
                    + "/" + packageName.replace(".", "/") + "/" + simpleClassName + ".java";
            outputFile(new File(filePath), objectMap, templatePath);
        }

        private String lowerCaseFirstLetter(String str) {
            if (str.length() == 1) {
                return str.toLowerCase();
            }
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }

        private String upperCaseFirstLetter(String str) {
            if (str.length() == 1) {
                return str.toUpperCase();
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }

        private String trim(String str) {
            return str.trim().replaceAll("[,，.。]+$", "");
        }

        /**
         * 连接字符串，如果连接点两边有字母，则插入一个空格
         */
        private String join(String... strings) {
            StringBuilder builder = new StringBuilder(64);
            for (String str : strings) {
                if (builder.length() > 0 && isLetter(builder.charAt(builder.length() - 1))
                        || str.length() > 0 && isLetter(str.charAt(0))) {
                    builder.append(" ");
                }
                builder.append(str);
            }
            return builder.toString();
        }

        private boolean isLetter(char ch) {
            return ch >= 'A' && ch <= 'Z'
                    || ch >= 'a' && ch <= 'z';
        }
    }

}
