/*
 * cn.aprilsoft.jsapp.encode.URIEncoder.js
 * jsapp, uri encode & decode functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.encode
  Package("cn.aprilsoft.jsapp.encode");

  Static.Class("cn.aprilsoft.jsapp.encode.URIEncoder", Extend(), Implement(),
  {
    getEncode: Static(function(strUri)
    {
      try
      {
        return encodeURI(strUri);
      }
      catch(e)
      {
        throw new Exception("`" + strUri + "' is not right uri stirng.");
      }
    }),
    
    getEncodeAll: Static(function(strUri)
    {
      try
      {
        return encodeURIComponent(strUri);
      }
      catch(e)
      {
        throw new Exception("`" + strUri + "' is not right uri stirng.");
      }
    }),
    
    getDecode: Static(function(strUriEncoded)
    {
      try
      {
        return decodeURI(strUriEncoded);
      }
      catch(e)
      {
        throw new Exception("`" + strUriEncoded + "' is not right encoded uri stirng.");
      }
    }),
    
    getDecodeAll: Static(function(strUriEncoded)
    {
      try
      {
        return decodeURIComponent(strUriEncoded);
      }
      catch(e)
      {
        throw new Exception("`" + strUriEncoded + "' is not right encoded uri stirng.");
      }
    })
  });
})();

