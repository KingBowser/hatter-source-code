/*
 * cn.aprilsoft.jsapp.db.oracle.SqlGene.js
 * jsapp, oracle sql gene
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.db.oracle
  Package("cn.aprilsoft.jsapp.db.oracle");

  Class("cn.aprilsoft.jsapp.db.oracle.SqlGene", Extend(), Implement(),
  {
    geneSql: Static(function(tblNameZh, tablNameEn, itemList, isDelOldTbl)
    {
      // itemList 's item format
      // [0] field name (en)
      // [1] field name (zh)
      // [2] field type
      // [3] field length
      // [4] field sub length
      // [5] field key type P or I
      // [6] field not null
      // [7] field memo
    })
  });
})();

