
function application_body_onload()
{
  // body onload code
}

function application_body_onunload()
{
  // body onunload code
}

// coding here

function encrypt()
{
  if ($value("input_password") != $value("input_password2"))
  {
    alert("Two passwords not match!");
    return;
  }
  var aes = new AES();
  $value("text_output", aes.encrypt($value("input_password"), $value("text_input")));
}

function decrypt()
{
  var aes = new AES();
  $value("text_input", aes.decrypt($value("input_password"), $value("text_output")));
}
