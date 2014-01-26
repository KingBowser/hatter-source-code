// application name
APPLICATION_NAME = "Template Merge";

// application library path
APPLICATION_LIBRARY_PATH = "jsapp/";

HTML_APPLICATION_SETTING =
[
  // hta application id
  ["ID", "oHTAEmptyApplication"],
  // hta application name
  ["APPLICATIONNAME", "EmptyApplication"],
  ["BORDER", "normal"],
  ["BORDERSTYLE", "normal"],
  ["CAPTION", "yes"],
  ["ICON", "EmptyApplication.ico"],
  ["MAXIMIZEBUTTON", "yes"],
  ["MINIMIZEBUTTON", "yes"],
  ["SHOWINTASKBAR", "yes"],
  ["SINGLEINSTANCE", "yes"],
  ["SYSMENU", "yes"],
  ["VERSION", "1.0"],
  ["WINDOWSTATE", "normal"]
];

function GET_HTML_APPLICATION_SETTING()
{
  var has = [];
  has.push('<HTA:APPLICATION ID="oHTAEmptyApplication"');
  for (var i = 0; i < HTML_APPLICATION_SETTING.length; i++)
  {
    has.push(' ');
    has.push(HTML_APPLICATION_SETTING[i][0]);
    has.push('=');
    has.push('"');
    has.push(HTML_APPLICATION_SETTING[i][1]);
    has.push('"');
  }
  has.push('/>');
  
  return has.join("");
}

function application_body_onload()
{
  // rewrite his function to set body onload
}

function application_body_onunload()
{
  // rewrite his function to set body onunload
}

