/*
 * cn.aprilsoft.jsapp.common.ExcelUtil.js
 * jsapp, Excel util functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var StringUtil = Using("cn.aprilsoft.jsapp.common.StringUtil");

  Class("cn.aprilsoft.jsapp.common.ExcelUtil", Extend(), Implement(),
  {
    openExcel: Static(function(pFileName)
    {
      if (/.*\.xlsx?/.test(pFileName))
      {
        try
        {
          var oXL = new ActiveXObject("Excel.Application");
        }
        catch(ex)
        {
          throw new Exception(ex, "Excel not installed or ActiveX not supported .");
        }
        try
        {
          var oBook = oXL.Workbooks.Open(pFileName);
        }
        catch(ex)
        {
          oXL.Quit();
          oXL = null;
          throw new Exception(ex, "Open excel:" + pFileName + " failed .");
        }
        var excelObject = new Object();
        excelObject.book = oBook;
        excelObject.activex = oXL;
        return excelObject;
      }
      else
      {
        throw new Exception("File name is not ended with .xls .");
      }
    }),
    
    closeExcel: Static(function(pExcelHandle)
    {
      if (pExcelHandle != null)
      {
        if (pExcelHandle.book != null)
        {
          pExcelHandle.book.Close();
        }
        if (pExcelHandle.activex != null)
        {
          pExcelHandle.activex.Quit();
        }
        pExcelHandle = null;
      }
    }),
    
    getExcelSheets: Static(function(pExcelHandle)
    {
      var sheetNames = new Array();
      var oBook = pExcelHandle.book;
      for (var i = 1; ; i++)
      {
        try
        {
          var oSheet = oBook.Worksheets(i);
        }
        catch(ex)
        {
          break;
        }
        sheetNames.push(oSheet.Name + "");
      }
      return sheetNames;
    }),
    
    getValue: Static(function(pExcelHandle, pExcelSheetName, arg0, arg1)
    {
      var oBook = pExcelHandle.book;
      var oSheet = oBook.Worksheets(pExcelSheetName);
      var cellValue;
      if (arg0 instanceof Array)
      {
        cellValue = oSheet.Cells(arg0[0], arg0[1]).Value;
      }
      else
      {
        cellValue = oSheet.Cells(arg0, arg1).Value;
      }
      return ((cellValue == null) || (cellValue == ""))? null: StringUtil.trim(cellValue.toString());
    }),
    
    getRowsAccordCol: Static(function(pExcelHandle, pExcelSheetName, pColNo, pFromNo)
    {
      if (pColNo == null) (pColNo = 1);
      if (pFromNo == null) (pFromNo = 1);
      try
      {
        var oBook = pExcelHandle.book;
        var oSheet = oBook.Worksheets(pExcelSheetName);
        for (var i = pFromNo; ; i++)
        {
          if (oSheet.Cells(i, pColNo).Value == null)
          {
            return (i == 1)? (-1): (i - 1);
          }
        }
      }
      catch(ex)
      {
        throw new Exception(ex, "read excel sheet error!");
      }
      return -1;
    }),
    
    getColsAccordRow: Static(function(pExcelHandle, pExcelSheetName, pRowNo, pFromNo)
    {
      if (pRowNo == null) (pRowNo = 1);
      if (pFromNo == null) (pFromNo = 1);
      try
      {
        var oBook = pExcelHandle.book;
        var oSheet = oBook.Worksheets(pExcelSheetName);
        for (var i = pFromNo; ; i++)
        {
          if (oSheet.Cells(pRowNo, i).Value == null)
          {
            return (i == 1)? (-1): (i - 1);
          }
        }
      }
      catch(ex)
      {
        throw new Exception(ex, "read excel sheet error!");
      }
      return -1;
    }),
    
    getColsListAccordRow: Static(function(pExcelHandle, pExcelSheetName, pRowList, pFromNo, pToNo)
    {
      if (pRowList == null) pRowList = new Array();
      if (pRowList.length < 1) pRowList.push(1);
      if (pFromNo == null) (pFromNo = 1);
      try
      {
        var oBook = pExcelHandle.book;
        var oSheet = oBook.Worksheets(pExcelSheetName);
        var allDataList = new Array();
        var continueFlag = true;
        for (var i = pFromNo; (pToNo == null)? (continueFlag): (i <= pToNo); i++)
        {
          var oneColList = new Array();
          continueFlag = false;
          for (var j = 0; j < pRowList.length; j++)
          {
            var oneCell = oSheet.Cells(pRowList[j], i).Value;
            if (oneCell != null) (continueFlag = true);
            oneColList.push(oneCell);
          }
          if  ((pToNo == null)? (continueFlag): (i <= pToNo)) (allDataList.push(oneColList));
        }
        return (allDataList.length < 1)? null: allDataList;
      }
      catch(ex)
      {
        throw new Exception(ex, "read excel sheet error!");
      }
      return null;
    }),
    
    getRowsListAccordCol: Static(function(pExcelHandle, pExcelSheetName, pColList, pFromNo, pToNo)
    {
      if (pColList == null) pColList = new Array();
      if (pColList.length < 1) pColList.push(1);
      if (pFromNo == null) (pFromNo = 1);
      try
      {
        var oBook = pExcelHandle.book;
        var oSheet = oBook.Worksheets(pExcelSheetName);
        var allDataList = new Array();
        var continueFlag = true;
        for (var i = pFromNo; (pToNo == null)? (continueFlag): (i <= pToNo); i++)
        {
          var oneRowList = new Array();
          continueFlag = false;
          for (var j = 0; j < pColList.length; j++)
          {
            var oneCell = oSheet.Cells(i, pColList[j]).Value;
            if (oneCell != null) (continueFlag = true);
            oneRowList.push(oneCell);
          }
          if ((pToNo == null)? (continueFlag): (i <= pToNo)) (allDataList.push(oneRowList));
        }
        return (allDataList.length < 1)? null: allDataList;
      }
      catch(ex)
      {
        throw new Exception(ex, "read excel sheet error!");
      }
      return null;
    })
  });
})();

