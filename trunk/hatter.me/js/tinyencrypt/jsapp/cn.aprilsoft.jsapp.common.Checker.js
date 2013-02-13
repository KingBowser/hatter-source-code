/*
 * cn.aprilsoft.jsapp.common.Checker.js
 * jsapp, Common checker functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.common
  Package("cn.aprilsoft.jsapp.common");

  var System = Using("cn.aprilsoft.jsapp.system.System");

  Class("cn.aprilsoft.jsapp.common.Checker", Extend(), Implement(),
  {
    isIPAddressV4: Static(function(ipaddr)
    {
      if (!System.isString(ipaddr))
      {
        return false;
      }
      if (!(/^\d+\.\d+\.\d+\.\d+$/.test(ipaddr)))
      {
        return false;
      }
      var ipaddrarr = ipaddr.split(".");
      for (var i = 0; i < 4; i++)
      {
        if (!((ipaddrarr[i] >= 0) && (ipaddrarr <= 255)))
        {
          return false;
        }
      }
      return true;
    }),
    
    isIPAddressV6: Static(function(ipaddrv6)
    {
      //not valid
      return false;
    }),
    
    isEMailAddress: Static(function(mailaddr)
    {
      return (/^\w+@[\w\.\-_]+\.((com)|(net)|(org)|(biz)|(gov)|(info)|(name)|(mobi)|(\w{2}))$/.test(mailaddr));
    }),
    
    isMobilePhone: Static(function(phonenum)
    {
      return (/^((13\d)|(15\d))\d{9}$/.test(phonenum));
    }),
    
    isZipCode: Static(function(zipcode)
    {
      return (/\d{6}/.test(zipcode));
    }),
    
    getIDCardNumber18Info: Static(function(sId)
    {
      var aCity =
      {
        11: "Beijing",
        12: "Tianjing",
        13: "Hebei",
        14: "Shanxi",
        15: "Neimenggu",
        21: "Liaoning" ,
        22: "Jilin",
        23: "Heilongjiang",
        31: "Shanghai",
        32: "Jiangshu",
        33: "Zhejiang" ,
        34: "Anhui ",
        35: "Fujian",
        36: "Jiangxi",
        37: "Shangdong",
        41: "Henan",
        42: "Hubei",
        43: "Hunan",
        44: "Guangdong",
        45: "Guangxi",
        46: "Hainan",
        50: "Chongqing",
        51: "Shichuang",
        52: "Guizhong",
        53: "Yunnan", 
        54: "Xizhang",
        61: "Shanxi",
        62: "Ganshu",
        63: "Qinhai",
        64: "Ninxia",
        65: "Xinjiang" ,
        71: "Taiwan ",
        81: "Xianggang",
        82: "Aomen",
        91: "Guowai"
      };
      var iSum = 0;
      var info = "";
      if (!/^\d{17}(\d| x)$/i.test(sId))
      {
        throw new Exception("ID card number is not 18 .");
      }
      sId = sId.replace(/ x$/i, "a ");
      if (aCity[parseInt(sId.substr(0, 2))] == null)
      {
        throw new Exception("The provienece error.");
      }
      sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
      var d = new Date(sBirthday.replace(/-/g, "/ "));
      if (sBirthday != (d.getFullYear() + "- " + (d.getMonth() + 1 ) + "- " + d.getDate()))
      {
        throw new Exception("The birthday error");
      }
      for(var i = 17; i >= 0; i--)
      {
        iSum += (Math.pow (2, i) % 11 ) * parseInt(sId.charAt(17  - i), 11);
      }
      if ((iSum % 11) != 1)
      {
        throw new Exception("The ID car number is no valid.");
      }
      var idcardinfo = {};
      idcardinfo.provienece = aCity[parseInt( sId.substr(0, 2))];
      idcardinfo.birthday = sBirthday;
      idcardinfo.agender = sId.substr(16, 1) % 2? "Male": "Female";
      return idcardinfo;
    })
  });
})();

