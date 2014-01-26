/*
 * cn.aprilsoft.jsapp.orm.ORMObject.js
 * jsapp, orm orm-object functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm
  Package("cn.aprilsoft.jsapp.orm");
  
  Interface("cn.aprilsoft.jsapp.orm.ORMObject", Implement(),
  {
    /**
     * database table name
     */
    orm_table: Abstract() /* : table name */,
    
    /**
     * "jsField[=dbFiled]@dbType[:dbConstraint], and more ..."
     * jsField: field in jsapp application
     * dbField: field in database
     * dbType: type in database (i.e. int, text ...)
     * dbConstraint: constraint in database (i.e. key, unique ...)
     * use sample:
     * ["id@int:key",
     *  "name@text:notnull",
     *  "age@int",
     *  "sex@boolean",
     *  "memo@text"
     * ].join(", ")
     */
    orm_description: Abstract() /* : description string ... */
  });
})();

