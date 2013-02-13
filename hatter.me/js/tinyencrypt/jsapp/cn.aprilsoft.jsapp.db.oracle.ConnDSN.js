/*
 * cn.aprilsoft.jsapp.db.oracle.ConnDSN.js
 * jsapp, oracle database connction dsn functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db.oracle
  Package("cn.aprilsoft.jsapp.db.oracle");

  Class("cn.aprilsoft.jsapp.db.oracle.ConnDSN", Extend(), Implement(),
  {
    _dsn: Private(null),
    
    Constructor: function(userId, password, sid)
    {
      if ((userId == null) || (password == null) || (sid == null))
      {
        throw new Exception("Userid, password or sid cannot be null.");
      }
      this._dsn = "Provider=MSDAORA.1;Password="
                + password + ";User ID="
                + userId + ";Data Source="
                + sid;
    },
    
    getDsn: function()
    {
      return this._dsn;
    }
  });
})();

