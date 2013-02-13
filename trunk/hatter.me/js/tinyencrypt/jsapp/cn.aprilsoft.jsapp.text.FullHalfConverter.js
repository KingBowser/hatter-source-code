/*
 * cn.aprilsoft.jsapp.text.FullHalfConverter.js
 * jsapp, full & half conveter functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.text
  Package("cn.aprilsoft.jsapp.text");

  Class("cn.aprilsoft.jsapp.text.FullHalfConverter", Extend(), Implement(),
  {
    convertDigitalToFull: Static(function(str)
    {
      return ThisClass()._commonConvertString(str, 48, 65296, 10);
    }),
    
    convertDigitalToHalf: Static(function(str)
    {
      return ThisClass()._commonConvertString(str, 65296, 48, 10);
    }),
    
    convertAlphabetToFull: Static(function(str)
    {
      var cvtStr = ThisClass()._commonConvertString(str, 97, 65345, 26);
      return ThisClass()._commonConvertString(cvtStr, 65, 65313, 26);
    }),
    
    convertAlphabetToHalf: Static(function(str)
    {
      var cvtStr = ThisClass()._commonConvertString(str, 65345, 97, 26);
      return ThisClass()._commonConvertString(cvtStr, 65313, 65, 26);
    }),
    
    convertToFull: Static(function(str)
    {
      var cvtStr = ThisClass().convertDigitalToFull(str);
      return ThisClass().convertAlphabetToFull(cvtStr);
    }),
    
    convertToHalf: Static(function(str)
    {
      var cvtStr = ThisClass().convertDigitalToHalf(str);
      return ThisClass().convertAlphabetToHalf(cvtStr);
    }),
    
    _commonConvertString: Static(function(str, frConvertFrom, toConvertFrom, convertWidth)
    {
      var cvt = [];
      
      if (str != null)
      {
        for (var i = 0; i < str.length; i++)
        {
          var c = str.charAt(i).charCodeAt(0);
          
          if ((c >= frConvertFrom) && (c <= (frConvertFrom + convertWidth - 1)))
          {
            var cc = String.fromCharCode(c - frConvertFrom + toConvertFrom);
            cvt.push(cc);
          }
          else
          {
            cvt.push(str.charAt(i));
          }
        }
      }
      
      return cvt.join("");
    })
  });
})();

