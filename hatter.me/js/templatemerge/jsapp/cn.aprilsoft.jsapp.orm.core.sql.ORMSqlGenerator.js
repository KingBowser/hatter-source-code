/*
 * cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator.js
 * jsapp, orm orm-sql-generator functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.orm.core.sql
  Package("cn.aprilsoft.jsapp.orm.core.sql");
  
  Class("cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator$Query", Extend(), Implement(),
  {
    query: null,
    keys: null,
    Constructor: function(query, keys)
    {
      this.query = query;
      this.keys = keys;
    },
    
    asString: function()
    {
      return ThisClass().getShortClassName() + " query: " + this.query + ",keys= " + this.keys;
    }
  });
  
  Interface("cn.aprilsoft.jsapp.orm.core.sql.ORMSqlGenerator", Implement(),
  {
    createQuery: Abstract() /* : ORMSqlGenerator$Query */,
    insertQuery: Abstract() /* : ORMSqlGenerator$Query */,
    updateQuery: Abstract() /* : ORMSqlGenerator$Query */,
    deleteQuery: Abstract() /* : ORMSqlGenerator$Query */,
    selectQuery: Abstract() /* : ORMSqlGenerator$Query */
  });
})();

