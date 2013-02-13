/*
 * cn.aprilsoft.jsapp.inet.mail.SMTPSendMail.js
 * jsapp, smtp mail send functions
 * 
 * Copyright(C) Hatter Jiang
 */

(function()
{
  // New package: cn.aprilsoft.jsapp.inet.mail
  Package("cn.aprilsoft.jsapp.inet.mail");

  // ATTRIB: FF N, IE Y, OP N
  Class("cn.aprilsoft.jsapp.inet.mail.SMTPSendMail", Extend(), Implement(),
  {
    _smtp_server: null,
    
    _smtp_port: 25,
    
    _smtp_timeout: 10000,
    
    _smtp_authenticate: 1,
    
    _user_name: null,
    
    _password: null,
    
    _use_sll: false,
    
    Constructor: function(smtpServer)
    {
      if (smtpServer == null)
      {
        throw new Exception("SMTP server should not be null!");
      }
      
      this._smtp_server = smtpServer;
    },
    
    getSmtpServer: function()
    {
      return this._smtp_server;
    },
    
    setSmtpServer: function(smtpServer)
    {
      this._smtp_server = smtpServer;
    },
    
    getSmtpPort: function()
    {
      return this._smtp_port;
    },
    
    setSmtpPort: function(smtpPort)
    {
      this._smtp_port = smtpPort;
    },
    
    getSmtpTimeout: function()
    {
      return this._smtp_timeout;
    },
    
    setSmtpTimeout: function(smtpTimeout)
    {
      this._smtp_timeout = smtpTimeout;
    },
    
    getSmtpAuthenticate: function()
    {
      return this._smtp_authenticate;
    },
    
    setSmtpAuthenticate: function(smtpAuthenticate)
    {
      this._smtp_authenticate = smtpAuthenticate;
    },
    
    getUserName: function()
    {
      return this._user_name;
    },
    
    setUserName: function(userName)
    {
      this._user_name = userName;
    },
    
    getPassword: function()
    {
      return this._password;
    },
    
    setPassword: function(password)
    {
      this._password = password;
    },
    
    getUseSSL: function()
    {
      return this._use_sll;
    },
    
    setUseSSL: function(useSSL)
    {
      this._use_sll = useSSL;
    },
    
    send: function(from, to, subject, content)
    {
      var cdo = new ActiveXObject("CDO.Message");
      
      fields = cdo.configuration.fields;
      // 1: Send messages by using the locally installed SMTP service. 
      //    Use this value if the SMTP service is installed on the computer 
      //    where the script is running.
      // 2: Send messages by using the SMTP service on the network.
      //    Use this value if the SMTP service is not installed on the computer 
      //    where the script is running.
      fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusing") = 2;
      fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserver") = this._smtp_server;
      fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpserverport") = this._smtp_port;
      
      var authFlag = false;
      if ((this._smtp_authenticate == "1") && (this._user_name != null) && (this._password != null))
      {
        authFlag = true;
      }
      if (authFlag)
      {
        fields.Item("http://schemas.microsoft.com/cdo/configuration/sendusername") = this._user_name;
        fields.Item("http://schemas.microsoft.com/cdo/configuration/sendpassword") = this._password;
        fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpauthenticate") = this._smtp_authenticate;
      }
      
      fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpconnectiontimeout") = this._smtp_timeout;
      fields.Item("http://schemas.microsoft.com/cdo/configuration/smtpusessl") = this._use_sll;
      fields.Update();
      
      cdo.from = from;
      cdo.to = to;
      cdo.subject = subject;
      cdo.textbody = content;
      try
      {
        cdo.send();
      }
      catch(e)
      {
        throw new Exception(e);
      }
    }
  }
})();

