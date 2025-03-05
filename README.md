Please modify the database configuration in datasource. yml to start the project and create database tables

# 1 insert data
 execute sql script as follows:

INSERT INTO "public"."sys_rule" ("id", "condition_sql", "rule_name") VALUES (1, '1', '1');
INSERT INTO "public"."sys_rule" ("id", "condition_sql", "rule_name") VALUES (2, '2', '2');
INSERT INTO "public"."sys_rule" ("id", "condition_sql", "rule_name") VALUES (3, '3', '3');
INSERT INTO "public"."sys_rule" ("id", "condition_sql", "rule_name") VALUES (4, '4', '4');
INSERT INTO "public"."sys_rule" ("id", "condition_sql", "rule_name") VALUES (5, '5', '5');

# 2 invoke api

After the project is launched, please visit the swagger link to directly perform testing
http://localhost:8080/swagger-ui/index.html#/SysRule/executeSearch-sysrule-get


